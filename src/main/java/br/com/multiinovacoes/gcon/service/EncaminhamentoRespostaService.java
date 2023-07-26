package br.com.multiinovacoes.gcon.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.mail.Mailer;
import br.com.multiinovacoes.gcon.model.Anexo;
import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.model.EncaminhamentoResposta;
import br.com.multiinovacoes.gcon.model.Orgao;
import br.com.multiinovacoes.gcon.model.Setor;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoRespostaDto;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoRespostaSetorDto;
import br.com.multiinovacoes.gcon.model.mapper.EncaminhamentoRespostaMapper;
import br.com.multiinovacoes.gcon.model.request.EncaminhamentoRespostaRequest;
import br.com.multiinovacoes.gcon.repository.AnexoRepository;
import br.com.multiinovacoes.gcon.repository.AtendimentoRepository;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRepository;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRespostaRepository;
import br.com.multiinovacoes.gcon.repository.OrgaoRepository;
import br.com.multiinovacoes.gcon.repository.SetorRepository;
import br.com.multiinovacoes.gcon.repository.UsuarioRepository;
import br.com.multiinovacoes.gcon.util.UploadGcon;

@Service
@Transactional
public class EncaminhamentoRespostaService {
	
	
	@Autowired
	EncaminhamentoRespostaMapper encaminhamentoRespostaMapper;
	
	@Autowired
	EncaminhamentoRespostaRepository encaminhamentoRespostaRepository;

	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EncaminhamentoRepository encaminhamentoRepository;
	
	@Autowired
	AtendimentoRepository atendimentoRepository;
	
	@Autowired
	OrgaoRepository orgaoRepositoty;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private AnexoRepository anexoRepository;
	
	
	
	public void salvarEncaminhamentoResposta(EncaminhamentoRespostaRequest request) {
	
		Encaminhamento encaminhamento = encaminhamentoRepository.getById(request.getEncaminhamento());
		Atendimento atendimento = atendimentoRepository.getById(encaminhamento.getAtendimento());
		EncaminhamentoResposta resposta = new EncaminhamentoResposta();
		resposta.setEncaminhamento(request.getEncaminhamento());
		resposta.setDataResposta(LocalDate.now());
		resposta.setDespacho(request.getDespacho());
		resposta.setModeloDocumento(request.getModeloDocumento());
		resposta.setSatisfaz(request.getSatisfaz());
		resposta.setStatus(EncaminhamentoResposta.STATUS_RETORNO_MANUAL);
		resposta.setId(encaminhamentoRespostaRepository.getMaxId()+1);
		encaminhamentoRespostaRepository.save(resposta);

		long dias = ChronoUnit.DAYS.between(encaminhamento.getDataEncaminhamento(), resposta.getDataResposta());
		encaminhamento.setTempoResposta((int)dias);
		encaminhamento.setRecebeu(1);
		encaminhamentoRepository.save(encaminhamento);
		
		atendimento.setStatusAtendimento(1);
		atendimentoRepository.save(atendimento);

		
	}
	
	public void salvarEncaminhamentoResposta(EncaminhamentoRespostaSetorDto encaminhamentoRespostaSetor) throws IOException {
		Encaminhamento encaminhamento = encaminhamentoRepository.findByParametroAndCancelado(encaminhamentoRespostaSetor.getParametro(),0);
		if (encaminhamento != null) {
			Atendimento atendimento = atendimentoRepository.getById(encaminhamento.getAtendimento());
			EncaminhamentoResposta resposta = new EncaminhamentoResposta();
			resposta.setEncaminhamento(encaminhamento.getId());
			resposta.setDataResposta(LocalDate.now());
			resposta.setDespacho(encaminhamentoRespostaSetor.getResposta());
			resposta.setModeloDocumento(0l);
			resposta.setSatisfaz(0);
			resposta.setStatus(EncaminhamentoResposta.STATUS_RETORNO);
			resposta.setId(encaminhamentoRespostaRepository.getMaxId()+1);
			encaminhamentoRespostaRepository.save(resposta);
			
			long dias = ChronoUnit.DAYS.between(encaminhamento.getDataEncaminhamento(), resposta.getDataResposta());
			encaminhamento.setTempoResposta((int)dias);
			
			encaminhamento.setRecebeu(1);
			encaminhamentoRepository.save(encaminhamento);
			
			atendimento.setStatusAtendimento(1);
			atendimentoRepository.save(atendimento);
			
			Setor setor = setorRepository.getById(encaminhamento.getSetorDestino());
			Orgao orgao = orgaoRepositoty.getById(encaminhamento.getOrgaoOrigem());
			
			String[] anexosEmail = null;
		    
		    if (encaminhamentoRespostaSetor.getListaAnexoDto() != null) {
		    	anexosEmail = new String[encaminhamentoRespostaSetor.getListaAnexoDto().size()];
		    	for (int i = 0; i < anexosEmail.length; i++) {
		    		br.com.multiinovacoes.gcon.model.dto.ListaAnexoDto anexos = encaminhamentoRespostaSetor.getListaAnexoDto().get(i); 					
			    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			    	String nomeArq = timeStamp + "_" + anexos.getNomeArquivo();
					UploadGcon.upload(anexos.getStringBase64(), nomeArq);
			    	Anexo anexo = new Anexo();
			    	
					anexo.setAtendimento(atendimento.getId());
					anexo.setDataAnexo(LocalDate.now());
					anexo.setNomeArquivo(nomeArq);
					anexo.setNumeroAtendimento(atendimento.getNumeroAtendimento());
					anexo.setOrgao(atendimento.getOrgao());
					anexo.setOrigem(0);
					anexo.setVisivel(1);
					anexo.setId(anexoRepository.getMaxSequencialId()+1);
			    	anexoRepository.save(anexo);
			    	anexosEmail[i] = "D:\\gcon_arquivos\\arquivos\\" + anexo.getNomeArquivo();
			    }
		    }
			
		    if(setor.getEncaminhamentoAutomatico() == 1){
		    	
		    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		    	 
		    
		        String conteudo = "Prezado Senhor, sua resposta do encaminhamento referente ao atendimento nº " +atendimento.getAnoAtendimento()+""+atendimento.getNumeroAtendimento() +" enviado pela Ouvidoria a seus cuidados em " + encaminhamento.getDataEncaminhamento().format(formatter)+" foi recebida com sucesso pelo sistema em " +  resposta.getDataResposta().format(formatter) +  ".";

		    	Map<String, Object> variaveis = new HashMap<>();
		    	variaveis.put("conteudo", conteudo);
		    	variaveis.put("orgao", orgao.getDescricao());
				variaveis.put("despacho", resposta.getDespacho());
				String template = "mail/emailrespostasetor";
				mailer.enviarEmail(Arrays.asList(encaminhamento.getEmailEnviado()), template, variaveis, "", "Confirmação de Resposta de encaminhamento do atendimento N° " + atendimento.getNumeroProtocolo(), anexosEmail);
		    }
			
			
		}

		
	}
	
	public void salvarResposta(EncaminhamentoRespostaSetorDto encaminhamentoRespostaSetor) throws IOException {
		Encaminhamento encaminhamento = encaminhamentoRepository.findById(Long.parseLong(encaminhamentoRespostaSetor.getCodigoEncaminhamento())).get();
		if (encaminhamento != null) {
			Atendimento atendimento = atendimentoRepository.getById(encaminhamento.getAtendimento());
			EncaminhamentoResposta resposta = new EncaminhamentoResposta();
			resposta.setEncaminhamento(encaminhamento.getId());
			resposta.setDataResposta(LocalDate.now());
			resposta.setDespacho(encaminhamentoRespostaSetor.getResposta());
			resposta.setModeloDocumento(0l);
			resposta.setSatisfaz(0);
			resposta.setStatus(EncaminhamentoResposta.STATUS_RETORNO);
			resposta.setId(encaminhamentoRespostaRepository.getMaxId()+1);
			encaminhamentoRespostaRepository.save(resposta);
			
			long dias = ChronoUnit.DAYS.between(encaminhamento.getDataEncaminhamento(), resposta.getDataResposta());
			encaminhamento.setTempoResposta((int)dias);
			
			encaminhamento.setRecebeu(1);
			encaminhamentoRepository.save(encaminhamento);
			
			atendimento.setStatusAtendimento(1);
			atendimentoRepository.save(atendimento);
			
			Setor setor = setorRepository.getById(encaminhamento.getSetorDestino());
			Orgao orgao = orgaoRepositoty.getById(encaminhamento.getOrgaoOrigem());
			
			String[] anexosEmail = null;
		    
		    if (encaminhamentoRespostaSetor.getListaAnexoDto() != null) {
		    	anexosEmail = new String[encaminhamentoRespostaSetor.getListaAnexoDto().size()];
		    	for (int i = 0; i < anexosEmail.length; i++) {
		    		br.com.multiinovacoes.gcon.model.dto.ListaAnexoDto anexos = encaminhamentoRespostaSetor.getListaAnexoDto().get(i); 					
			    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			    	String nomeArq = timeStamp + "_" + anexos.getNomeArquivo();
					UploadGcon.upload(anexos.getStringBase64(), nomeArq);
			    	Anexo anexo = new Anexo();
			    	
					anexo.setAtendimento(atendimento.getId());
					anexo.setDataAnexo(LocalDate.now());
					anexo.setNomeArquivo(nomeArq);
					anexo.setNumeroAtendimento(atendimento.getNumeroAtendimento());
					anexo.setOrgao(atendimento.getOrgao());
					anexo.setOrigem(0);
					anexo.setVisivel(1);
					anexo.setId(anexoRepository.getMaxSequencialId()+1);
			    	anexoRepository.save(anexo);
			    	anexosEmail[i] = "D:\\gcon_arquivos\\arquivos\\" + anexo.getNomeArquivo();
			    }
		    }
			
		    if(setor.getEncaminhamentoAutomatico() == 1){
		    	
		    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		    	 
		    
		        String conteudo = "Prezado Senhor, sua resposta do encaminhamento referente ao atendimento nº " +atendimento.getAnoAtendimento()+""+atendimento.getNumeroAtendimento() +" enviado pela Ouvidoria a seus cuidados em " + encaminhamento.getDataEncaminhamento().format(formatter)+" foi recebida com sucesso pelo sistema em " +  resposta.getDataResposta().format(formatter) +  ".";

		    	Map<String, Object> variaveis = new HashMap<>();
		    	variaveis.put("conteudo", conteudo);
		    	variaveis.put("orgao", orgao.getDescricao());
				variaveis.put("despacho", resposta.getDespacho());
				String template = "mail/emailrespostasetor";
			    mailer.enviarEmail(Arrays.asList(encaminhamento.getEmailEnviado()), template, variaveis, "", "Confirmação de Resposta de encaminhamento do atendimento N° " + atendimento.getNumeroProtocolo(), anexosEmail);
		    }
			
			
		}

		
	}

	
	
	public EncaminhamentoRespostaDto getEncaminhamentoResposta(Long codigoEncaminhamento) {
		return encaminhamentoRespostaMapper.toDto(encaminhamentoRespostaRepository.consultaEncaminhamentoResposta(codigoEncaminhamento));
	}


	public EncaminhamentoResposta findByCodigoEncaminhamento(Long codigoEncaminhamento) {
		return encaminhamentoRespostaRepository.findByEncaminhamento(codigoEncaminhamento);
	}

	public void atualizar(EncaminhamentoRespostaRequest resposta) {
		EncaminhamentoRespostaDto encaminhamentoRespostaDto = encaminhamentoRespostaMapper.fromEncaminhamentoResposta(resposta); 
		encaminhamentoRespostaRepository.save(encaminhamentoRespostaMapper.toEncaminhamento(encaminhamentoRespostaDto));
	}

}

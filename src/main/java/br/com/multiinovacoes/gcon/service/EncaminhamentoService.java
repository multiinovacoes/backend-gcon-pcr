package br.com.multiinovacoes.gcon.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.enums.FormaEnvioRespostaParcialEnum;
import br.com.multiinovacoes.gcon.enums.PriorizacaoEnum;
import br.com.multiinovacoes.gcon.mail.Mailer;
import br.com.multiinovacoes.gcon.model.Anexo;
import br.com.multiinovacoes.gcon.model.Area;
import br.com.multiinovacoes.gcon.model.Assunto;
import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.Bairro;
import br.com.multiinovacoes.gcon.model.Configuracao;
import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.model.EncaminhamentoResposta;
import br.com.multiinovacoes.gcon.model.Feriado;
import br.com.multiinovacoes.gcon.model.LogAtendimento;
import br.com.multiinovacoes.gcon.model.ModeloDocumento;
import br.com.multiinovacoes.gcon.model.Natureza;
import br.com.multiinovacoes.gcon.model.OrigemManifestacao;
import br.com.multiinovacoes.gcon.model.Setor;
import br.com.multiinovacoes.gcon.model.TipoDocumento;
import br.com.multiinovacoes.gcon.model.Usuario;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoDto;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoRespostaSetorDto;
import br.com.multiinovacoes.gcon.model.mapper.EncaminhamentoMapper;
import br.com.multiinovacoes.gcon.model.mapper.ModeloDocumentoMapper;
import br.com.multiinovacoes.gcon.model.request.EncaminhamentoRequest;
import br.com.multiinovacoes.gcon.model.request.RespostaParcialRequest;
import br.com.multiinovacoes.gcon.model.response.ListaModeloDocumentoResponse;
import br.com.multiinovacoes.gcon.report.DetalheAtendimentoArea;
import br.com.multiinovacoes.gcon.repository.AnexoRepository;
import br.com.multiinovacoes.gcon.repository.AreaRepository;
import br.com.multiinovacoes.gcon.repository.AssuntoRepository;
import br.com.multiinovacoes.gcon.repository.AtendimentoRepository;
import br.com.multiinovacoes.gcon.repository.BairroRepository;
import br.com.multiinovacoes.gcon.repository.ConfiguracaoRepository;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRepository;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRespostaRepository;
import br.com.multiinovacoes.gcon.repository.FeriadoRepository;
import br.com.multiinovacoes.gcon.repository.LogAtendimentoRepository;
import br.com.multiinovacoes.gcon.repository.ModeloDocumentoRepository;
import br.com.multiinovacoes.gcon.repository.NaturezaRepository;
import br.com.multiinovacoes.gcon.repository.OrigemManifestacaoRepository;
import br.com.multiinovacoes.gcon.repository.SetorRepository;
import br.com.multiinovacoes.gcon.repository.TipoDocumentoRepository;
import br.com.multiinovacoes.gcon.repository.UsuarioRepository;
import br.com.multiinovacoes.gcon.security.GconSecurity;
import br.com.multiinovacoes.gcon.util.GeradorString;

@Service
@Transactional
public class EncaminhamentoService {
	
	
	@Autowired
	EncaminhamentoMapper encaminhamentoMapper;
	
	@Autowired
	EncaminhamentoRepository encaminhamentoRepository;

	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EncaminhamentoRespostaRepository respostaRepository;
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private NaturezaRepository naturezaRepository;
	
	@Autowired
	private OrigemManifestacaoRepository origemManifestacaoRepository;
	
	@Autowired
	private AssuntoRepository assuntoRepository;
	
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	private Mailer mailer;
	
	@Autowired
	private GconSecurity gconSecurity;
	
	@Autowired
	private AnexoRepository anexoRepository;
	
	@Autowired
	private RespostaParcialService respostaParcialService;
	
	@Autowired
	private ModeloDocumentoRepository modeloDocumentoRepository;
	
	@Autowired
	private ConfiguracaoRepository configuracaoRepository;
	
	@Autowired
	private ModeloDocumentoService modeloDocumentoService;
	
	@Autowired
	private LogAtendimentoRepository logAtendimentoRepository;
	
	@Autowired
	ModeloDocumentoMapper modeloDocumentoMapper;
	
	@Autowired
	EncaminhamentoRespostaRepository encaminhamentoRespostaRepository;

	@Autowired
	FeriadoRepository feriadoRepository;
	
	public List<EncaminhamentoDto> getEncaminhamentos(Long codigoAtendimento) {
		return encaminhamentoMapper.fromResponseList(encaminhamentoRepository.findByAtendimento(codigoAtendimento));
	}
	
	   public String listaEncaminhamentoAtivosComRespostaSatisfaz(Long codigoAtendimento){
		   List<Encaminhamento> lista = encaminhamentoRepository.consultaAtendimento(codigoAtendimento);
		   
		   if (lista != null) {
			   if (lista.get(lista.size()-1).getCancelado() == 1) {
				return "";   
			   }else {
				   for (Encaminhamento enc : lista) {
					   EncaminhamentoResposta encResp = respostaRepository.findByEncaminhamentoAndSatisfaz(enc.getId(), 1);
					   if (encResp != null) {
						   return encResp.getDespacho();
					   }
				   }
			   }
		   }
		   return null;
	   }
	
	
   public List<EncaminhamentoDto> listaEncaminhamentoComResposta(Long codigoAtendimento){
	   List<Encaminhamento> lista = encaminhamentoRepository.consultaAtendimento(codigoAtendimento);
	   List<EncaminhamentoDto> listaRetorno = null;
	   EncaminhamentoDto encaminhamentoResposta = null;
	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	   int seq = 0;
	   if (lista != null) {
		   listaRetorno = new ArrayList<>();
		   for (Encaminhamento enc : lista) {
			   enc.setSequencial(++seq);
			   enc.setDataEncaminhado(enc.getDataEncaminhamento().format(formatter));
			   enc.setDescricaoSetorOrigem(setorRepository.getById(enc.getSetorOrigem()).getDescricao());
			   enc.setDescricaoSetorDestino(setorRepository.getById(enc.getSetorDestino()).getDescricao());
			   listaRetorno.add(encaminhamentoMapper.toDto(enc));
			   EncaminhamentoResposta encResp = respostaRepository.findByEncaminhamento(enc.getId());
			   if (encResp != null) {
				   encaminhamentoResposta = new EncaminhamentoDto();
				   encaminhamentoResposta.setSequencial(++seq); 
				   encaminhamentoResposta.setDataEncaminhamento(encResp.getDataResposta());
				   encaminhamentoResposta.setId(encResp.getEncaminhamento());
				   encaminhamentoResposta.setIdResposta(encResp.getId());
				   encaminhamentoResposta.setDespacho(encResp.getDespacho().replace("##37;", "%"));
				   encaminhamentoResposta.setStatus(encResp.getStatus());
				   encaminhamentoResposta.setSatisfaz(encResp.getSatisfaz());
				   encaminhamentoResposta.setSetorOrigem(enc.getSetorDestino());
				   encaminhamentoResposta.setSetorDestino(enc.getSetorOrigem());
				   encaminhamentoResposta.setModeloDocumento(enc.getModeloDocumento());
				   encaminhamentoResposta.setTipo("R");
				   encaminhamentoResposta.setRecebeu(1);
				   encaminhamentoResposta.setCancelado(enc.getCancelado());
				   encaminhamentoResposta.setDescricaoSetorOrigem(enc.getDescricaoSetorDestino());
				   encaminhamentoResposta.setDescricaoSetorDestino(enc.getDescricaoSetorOrigem());
				   encaminhamentoResposta.setDataEncaminhado(encResp.getDataResposta().format(formatter));
				   listaRetorno.add(encaminhamentoResposta);
			   }
		   }
	   }
	   return listaRetorno;
   }
	
	public void salvarEncaminhamento(EncaminhamentoRequest request) {
		
		String[] anexos = new String[request.getSelectedAnexos().size()];
		Usuario usuario = usuarioRepository.getById(gconSecurity.getIdUsuario());
		
		Configuracao conf = configuracaoRepository.findByOrgao(usuario.getOrgao());
		
		EncaminhamentoDto encaminhamentoDto = null;
		encaminhamentoDto = encaminhamentoMapper.fromEncaminhamento(request);
		encaminhamentoDto.setSetorDestino(request.getSetorDestino());
		encaminhamentoDto.setSetorOrigem(usuario.getSetor());
		encaminhamentoDto.setDataEncaminhamento(LocalDate.now());
		encaminhamentoDto.setDataPrazo(adicionarDiasUteis(conf.getQtdDiasAlertaEncaminhamento(), usuario.getOrgao(), LocalDate.now()));
		encaminhamentoDto.setUsuario(usuario);
		encaminhamentoDto.setDiasUteisResposta(1);
		encaminhamentoDto.setStatus(Encaminhamento.STATUS_ABERTO);
		encaminhamentoDto.setEmailEnviado(request.getEmailEnviado());
		encaminhamentoDto.setOrgaoOrigem(usuario.getOrgao());
		encaminhamentoDto.setOrgaoDestino(usuario.getOrgao());
		encaminhamentoDto.setCancelado(0);
		encaminhamentoDto.setRecebeu(0);
		encaminhamentoDto.setAnexoEnviado(request.getSelectedAnexos().size() > 0 ? 1 : 0);
		encaminhamentoDto.setParametro(GeradorString.getRandomString());
		encaminhamentoDto.setCopiaDirigente(request.getCopiaEmailDirigente().equals(Boolean.TRUE) ? 1 : 0);  
		encaminhamentoDto.setId(encaminhamentoRepository.getMaxId()+1);
		encaminhamentoRepository.save(encaminhamentoMapper.toEncaminhamento(encaminhamentoDto));
			
		LogAtendimento logAtendimento = 
				  new LogAtendimento(
						  logAtendimentoRepository.getMaxId()+1,
						  encaminhamentoDto.getAtendimento(),
						  LocalDateTime.now(),
						  usuario.getId(),
						  "Encaminhou a manifestação"
						  );
		logAtendimentoRepository.save(logAtendimento);
		
		Optional<Atendimento> atendimento = atendimentoRepository.findById(encaminhamentoDto.getAtendimento());
		
		if (encaminhamentoDto.isEnviarRespostaParcial()) {
			RespostaParcialRequest respostaRequest = new RespostaParcialRequest();
			Configuracao configuracao = configuracaoRepository.findByOrgao(gconSecurity.getOrgao());
            ModeloDocumento modeloDocumento = modeloDocumentoRepository.getById((long)configuracao.getRespostaParcialPadrao());
            String modelo = modeloDocumentoService.getModeloDocumento(modeloDocumento.getId(), encaminhamentoDto.getAtendimento()); 
            respostaRequest.setAtendimento(encaminhamentoDto.getAtendimento());
			respostaRequest.setDescricao(modelo);
			respostaRequest.setEmailEnviado(atendimento.get().getEmail());
			respostaRequest.setFormaEnvio(FormaEnvioRespostaParcialEnum.EMAIL.getCodigo());
			respostaRequest.setModeloDocumento(modeloDocumento.getId());
			respostaParcialService.salvarRespostaParcial(respostaRequest);

		}
		

		
		
		for (int i = 0; i < request.getSelectedAnexos().size(); i++) {
			Optional<Anexo> anexo = anexoRepository.findById(request.getSelectedAnexos().get(i));
			if (anexo.isPresent()) {
				anexos[i] = "D:\\gcon_arquivos\\arquivos\\"+anexo.get().getNomeArquivo();
			}
		}

		Map<String, Object> variaveis = new HashMap<>();

		if (atendimento.isPresent()) {
			
			Optional<Area> area = areaRepository.findById(atendimento.get().getArea());
			Optional<Natureza> natureza = naturezaRepository.findById(atendimento.get().getNatureza());
			Optional<OrigemManifestacao> origemManifestacao = origemManifestacaoRepository.findById(atendimento.get().getOrigemManifestacao());
			Optional<Assunto> assunto =  assuntoRepository.findById(atendimento.get().getAssunto());
			Optional<TipoDocumento> tipoDocumento =  null;
			if (atendimento.get().getTipoDocumento() != null) {
				tipoDocumento =  tipoDocumentoRepository.findById(atendimento.get().getTipoDocumento());
			}
			Optional<Bairro> bairroOcorrencia =  null;

			if (atendimento.get().getCodigoBairroOcorrencia() != null) {
				bairroOcorrencia =  bairroRepository.findById(atendimento.get().getCodigoBairroOcorrencia());
			}
			
			variaveis.put("atendimento", atendimento.get());
			
			if (area.isPresent()) {
				variaveis.put("area", area.get().getDescricao());
			}else {
				variaveis.put("area", "");
			}
			if (natureza.isPresent()) {
				variaveis.put("natureza", natureza.get().getDescricao());	
			}else {
				variaveis.put("natureza", "");
			}
			if (origemManifestacao.isPresent()) {
				variaveis.put("origemManifestacao", origemManifestacao.get().getDescricao());	
			}else {
				variaveis.put("origemManifestacao", "");
			}
			if (assunto.isPresent()) {
				variaveis.put("assunto", assunto.get().getDescricao());
			}else {
				variaveis.put("assunto", "");
			}
			if (tipoDocumento != null) {
				if (tipoDocumento.isPresent()) {
					variaveis.put("tipoDocumento", tipoDocumento.get().getDescricao());
				}else {
					variaveis.put("tipoDocumento", "");	
				}
			}else {
				variaveis.put("tipoDocumento", "");
			}
			if (atendimento.get().getPriorizacao() != null) {
				variaveis.put("priorizacao", PriorizacaoEnum.pegarDescricao(atendimento.get().getPriorizacao()).getDescricao());
			}else {
				variaveis.put("priorizacao", "");
			}
			if (bairroOcorrencia != null) {
				if (bairroOcorrencia.isPresent()) {
					variaveis.put("bairroOcorrencia", bairroOcorrencia.get().getDescricao());	
				}else {
					variaveis.put("bairroOcorrencia", "");
				}
			}
			
			variaveis.put("despacho", encaminhamentoDto.getDespacho());
			variaveis.put("url", "https://ouvidoriagcon8501.recife.pe.gov.br/gconweb/resposta-encaminhamento/"+encaminhamentoDto.getParametro());
			String template = "mail/emailencaminhamento";
			mailer.enviarEmail(Arrays.asList(encaminhamentoDto.getEmailEnviado()), template, variaveis, "", "Encaminhamento do Atendimento Nº " + atendimento.get().getNumeroProtocolo(), anexos);
		}
		
	}
	
	public List<Encaminhamento> getListaEncaminhamentosRecebidos(Long orgao, Pageable pageable, int totalRegistros){
		return encaminhamentoRepository.consultaEncaminhamentosRecebidos(orgao, pageable, totalRegistros);
	}

	public Integer getQtdEncaminhamentosRecebidos(Long orgao){
		return encaminhamentoRepository.getEncaminhamentosRecebidos(orgao);
	}

	public EncaminhamentoDto getEncaminhamento(Long codigoEncaminhamento) {
		EncaminhamentoDto encaminhamentoDto = encaminhamentoMapper.toDto(encaminhamentoRepository.consultaEncaminhamento(codigoEncaminhamento));
		encaminhamentoDto.setDescricaoSetorOrigem(setorRepository.getById(encaminhamentoDto.getSetorOrigem()).getDescricao());
		encaminhamentoDto.setDescricaoSetorDestino(setorRepository.getById(encaminhamentoDto.getSetorDestino()).getDescricao());
		return encaminhamentoDto;
	}

	
	public void excluir(Long codigoEncaminhamento) {
		Encaminhamento enc = encaminhamentoRepository.getById(codigoEncaminhamento);
		EncaminhamentoResposta encResp = respostaRepository.findByEncaminhamento(codigoEncaminhamento);
		if (encResp != null) {
			encResp.setStatus(EncaminhamentoResposta.STATUS_CANCELADO);
			respostaRepository.save(encResp);
		}
		enc.setCancelado(Encaminhamento.CANCELADO_SIM);
		encaminhamentoRepository.save(enc);
	}
	
	public EncaminhamentoRespostaSetorDto getEncaminhamentoRespostaSetor(String parametro) {
		Encaminhamento encaminhamento = encaminhamentoRepository.findByParametroAndCancelado(parametro, 0);
		EncaminhamentoRespostaSetorDto encaminhamentoRespostaSetorDto = null;
		if (encaminhamento != null) {
			encaminhamentoRespostaSetorDto = new EncaminhamentoRespostaSetorDto();
			EncaminhamentoResposta resposta = encaminhamentoRespostaRepository.findByEncaminhamento(encaminhamento.getId());
			if (resposta == null) {
				encaminhamentoRespostaSetorDto.setNomeSetorDestino(setorRepository.getById(encaminhamento.getSetorDestino()).getDescricao());
				Atendimento atendimento = atendimentoRepository.getById(encaminhamento.getAtendimento());
				encaminhamentoRespostaSetorDto.setProtocolo(atendimento.getNumeroProtocolo());
				encaminhamentoRespostaSetorDto.setListaModelo(new ListaModeloDocumentoResponse(Optional
						.ofNullable(modeloDocumentoMapper
								.fromResponseList(modeloDocumentoRepository.findByOrgaoAndTipoOrderByDescricaoAsc(atendimento.getOrgao(), 5)))
						.orElse(Collections.emptyList())));
			}else {
				encaminhamentoRespostaSetorDto.setRespondido(true);
			}
		}
		return encaminhamentoRespostaSetorDto;
	}

	
	public LocalDate adicionarDiasUteis(Integer qtdeDiasAcrescentados, Long codigoOrgao, LocalDate data) {
		List<Feriado> feriadoInfoList = null;
		//data = data.plusDays(1);
		while(qtdeDiasAcrescentados > 0){
			data = data.plusDays(1);
			feriadoInfoList = feriadoRepository.findByOrgaoAndDataFeriado(codigoOrgao, data);
			if (!feriadoInfoList.isEmpty()){
				++qtdeDiasAcrescentados;
			}
			if (data.getDayOfWeek() != DayOfWeek.SATURDAY && data.getDayOfWeek() != DayOfWeek.SUNDAY) {
				--qtdeDiasAcrescentados;
			}
		}
		return data;
	}
	
	
	public List<DetalheAtendimentoArea> relatorioEncaminhamentoSetor(Long orgao, Date dataInicial, Date dataFinal, String area, String status) {
		
		 List<Setor> listaAreas = new ArrayList<>();
		 List<DetalheAtendimentoArea> detalhe = new ArrayList<>();
		
		 if (area.equals("")) {
			 listaAreas = setorRepository.findByOrgaoAndStatusOrderByDescricaoAsc(orgao, 0);
		 }else {
			 listaAreas.add(new Setor(Long.parseLong(area)));
		 }
		 
		 for (Setor area2 : listaAreas) {
			 detalhe.addAll(encaminhamentoRepository.getEncaminhamentoSetor(orgao, dataInicial, dataFinal, area2.getId().toString(), status));
		 }
		
		return detalhe;
	}
	
	
	public void ajustar() {
		List<Encaminhamento> lista = encaminhamentoRepository.getEncaminhamentosRespondido();
	    for (Encaminhamento encaminhamento : lista) {
			EncaminhamentoResposta encResp = encaminhamentoRespostaRepository.findByEncaminhamento(encaminhamento.getId());
			if (encResp != null) {
				long dias = ChronoUnit.DAYS.between(encaminhamento.getDataEncaminhamento(), encResp.getDataResposta());
				encaminhamento.setTempoResposta((int)dias);
				encaminhamentoRepository.save(encaminhamento);
			}
	    }
		
	}

	


}

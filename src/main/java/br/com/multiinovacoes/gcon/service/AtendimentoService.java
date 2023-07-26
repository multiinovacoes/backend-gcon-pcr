package br.com.multiinovacoes.gcon.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.enums.FormaAtendimentoRespostaEnum;
import br.com.multiinovacoes.gcon.enums.OrigemManifestacaoEnum;
import br.com.multiinovacoes.gcon.enums.PesquisaSatisfacaoEnum;
import br.com.multiinovacoes.gcon.enums.PriorizacaoEnum;
import br.com.multiinovacoes.gcon.enums.StatusAtendimentoEnum;
import br.com.multiinovacoes.gcon.enums.TipoManifestanteEnum;
import br.com.multiinovacoes.gcon.exception.NegocioException;
import br.com.multiinovacoes.gcon.mail.Mailer;
import br.com.multiinovacoes.gcon.model.Anexo;
import br.com.multiinovacoes.gcon.model.Area;
import br.com.multiinovacoes.gcon.model.Assunto;
import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.Bairro;
import br.com.multiinovacoes.gcon.model.Configuracao;
import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.model.Feriado;
import br.com.multiinovacoes.gcon.model.LogAtendimento;
import br.com.multiinovacoes.gcon.model.LogoTipo;
import br.com.multiinovacoes.gcon.model.ModeloListaEncaminhamentoRecebidos;
import br.com.multiinovacoes.gcon.model.Natureza;
import br.com.multiinovacoes.gcon.model.Orgao;
import br.com.multiinovacoes.gcon.model.OrigemManifestacao;
import br.com.multiinovacoes.gcon.model.PerguntaSatisfacao;
import br.com.multiinovacoes.gcon.model.PesquisaSatisfacao;
import br.com.multiinovacoes.gcon.model.RespostaParcial;
import br.com.multiinovacoes.gcon.model.RespostaSatisfacao;
import br.com.multiinovacoes.gcon.model.Usuario;
import br.com.multiinovacoes.gcon.model.dto.AtendimentoDto;
import br.com.multiinovacoes.gcon.model.dto.DespachoDto;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoDto;
import br.com.multiinovacoes.gcon.model.dto.HistoricoUsuarioDto;
import br.com.multiinovacoes.gcon.model.dto.ListaAnexoDto;
import br.com.multiinovacoes.gcon.model.dto.PerguntaSatisfacaoDto;
import br.com.multiinovacoes.gcon.model.dto.RespostaParcialDto;
import br.com.multiinovacoes.gcon.model.dto.ResumoAtendimentoDto;
import br.com.multiinovacoes.gcon.model.filter.AtendimentoFilter;
import br.com.multiinovacoes.gcon.model.mapper.AtendimentoMapper;
import br.com.multiinovacoes.gcon.model.mapper.RespostaParcialMapper;
import br.com.multiinovacoes.gcon.model.request.AtendimentoConclusaoRequest;
import br.com.multiinovacoes.gcon.model.request.AtendimentoRequest;
import br.com.multiinovacoes.gcon.model.request.ListaAnexoRequest;
import br.com.multiinovacoes.gcon.model.request.PesquisaSatisfacaoRequest;
import br.com.multiinovacoes.gcon.report.AtendimentoArea;
import br.com.multiinovacoes.gcon.report.AtendimentoNatureza;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.DadosRelatorio;
import br.com.multiinovacoes.gcon.report.DetalheAtendimento;
import br.com.multiinovacoes.gcon.report.DetalheAtendimentoArea;
import br.com.multiinovacoes.gcon.report.EstatisticaPesquisaSatisfacao;
import br.com.multiinovacoes.gcon.report.PainelOuvidoria;
import br.com.multiinovacoes.gcon.report.RelatorioPeriodo;
import br.com.multiinovacoes.gcon.report.RelatorioPesquisaSatisfacao;
import br.com.multiinovacoes.gcon.repository.AnexoRepository;
import br.com.multiinovacoes.gcon.repository.AreaRepository;
import br.com.multiinovacoes.gcon.repository.AssuntoRepository;
import br.com.multiinovacoes.gcon.repository.AtendimentoRepository;
import br.com.multiinovacoes.gcon.repository.BairroRepository;
import br.com.multiinovacoes.gcon.repository.ConfiguracaoRepository;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRepository;
import br.com.multiinovacoes.gcon.repository.FeriadoRepository;
import br.com.multiinovacoes.gcon.repository.LogAtendimentoRepository;
import br.com.multiinovacoes.gcon.repository.LogoTipoRepository;
import br.com.multiinovacoes.gcon.repository.NaturezaRepository;
import br.com.multiinovacoes.gcon.repository.OrgaoRepository;
import br.com.multiinovacoes.gcon.repository.OrigemManifestacaoRepository;
import br.com.multiinovacoes.gcon.repository.PerguntaSatisfacaoRepository;
import br.com.multiinovacoes.gcon.repository.PesquisaSatisfacaoRepository;
import br.com.multiinovacoes.gcon.repository.RespostaParcialRepository;
import br.com.multiinovacoes.gcon.repository.RespostaSatisfacaoRepository;
import br.com.multiinovacoes.gcon.repository.SetorRepository;
import br.com.multiinovacoes.gcon.repository.UsuarioRepository;
import br.com.multiinovacoes.gcon.security.GconSecurity;
import br.com.multiinovacoes.gcon.util.GeradorString;
import br.com.multiinovacoes.gcon.util.UploadGcon;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Transactional
public class AtendimentoService {
	
	
	@Autowired
	private AtendimentoMapper atendimentoMapper;
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ConfiguracaoRepository configuracaoRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private RespostaParcialRepository respostaParcialRepository;
	
	@Autowired
	private PerguntaSatisfacaoRepository perguntaSatisfacaoRepository;
	
	@Autowired
	private RespostaSatisfacaoRepository respostaSatisfacaoRepository;
	
	@Autowired
	private RespostaParcialMapper respostaParcialMapper;
	
	@Autowired
	private OrgaoRepository orgaoRepository;
	
	@Autowired
	private AnexoRepository anexoRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private GconSecurity gconSecurity;
	
	@Autowired
	private EncaminhamentoService encaminhamentoService;
	
	@Autowired
	private DespachoService despachoService;
	
	@Autowired
	private NaturezaRepository naturezaRepository;
	
	@Autowired
	private OrigemManifestacaoRepository origemManifestacaoRepository;
	
	@Autowired
	private BairroRepository bairroRepository;
	
	@Autowired
	private AssuntoRepository assuntoRepository;
	
	@Autowired
	private LogAtendimentoRepository logAtendimentoRepository;
	
	@Autowired
	private SetorRepository setorRepository;
	
	@Autowired
	private RespostaParcialService respostaParcialService;
	
	@Autowired
	private PesquisaSatisfacaoRepository pesquisaSatisfacaoRepository;
	
	@Autowired
	private EncaminhamentoRepository encaminhamentoRepository;
	
	@Autowired
	private AnexoService anexoService;
	
	@Autowired
	private LogoTipoRepository logotipoRepository;
	
	private static final String IMAGE_CABECALHO = "\\img\\ouvidoria_recife.png";

	private static final String NOME_SOLICITANTE_ANONIMO = "Anônimo";
	
	
	@Transactional
	public Boolean getSalvarPesquisaSatisfacao(PesquisaSatisfacaoRequest pesquisaSatisfacao) {
		try {
			for (PerguntaSatisfacaoDto pergunta : pesquisaSatisfacao.getListaPergunta()) {
				PesquisaSatisfacao pesquisa = new PesquisaSatisfacao();
				pesquisa.setIdAtendimento(pesquisaSatisfacao.getIdAtendimento());
				pesquisa.setIdPergunta(pergunta.getId());
				pesquisa.setIdResposta(pergunta.getIdResposta());
				pesquisa.setId(pesquisaSatisfacaoRepository.getMaxId()+1);
				pesquisaSatisfacaoRepository.save(pesquisa);
			}
			Atendimento atendimento = atendimentoRepository.getById(pesquisaSatisfacao.getIdAtendimento());
			atendimento.setSatisfaz(1);
			atendimento.setMeioComunicacaoRespPesquisa(pesquisaSatisfacao.getMeioComunicacaoRespPesquisa());
			atendimentoRepository.save(atendimento);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	
	public AtendimentoDto getBuscarAtendimento(Long codigoEncaminhamento) {
		Encaminhamento encaminhamento = encaminhamentoRepository.findById(codigoEncaminhamento).get();
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimentoRepository.consultaAtendimento(encaminhamento.getAtendimento()));
		atendimentoDto.setDescricaoArea(areaRepository.getById(atendimentoDto.getArea()).getDescricao());
		atendimentoDto.setDescricaoAssunto(assuntoRepository.getById(atendimentoDto.getAssunto()).getDescricao());
		atendimentoDto.setDescricaoOrigem(origemManifestacaoRepository.getById(atendimentoDto.getOrigemManifestacao()).getDescricao());
		atendimentoDto.setDescricaoNatureza(naturezaRepository.getById(atendimentoDto.getNatureza()).getDescricao());
		atendimentoDto.setDescricaoPrioridade(PriorizacaoEnum.pegarDescricao(atendimentoDto.getPriorizacao()).getDescricao());
		atendimentoDto.setTextoEncaminhamento(encaminhamento.getDespacho());
		atendimentoDto.setAnexos(anexoService.getListaAnexos(atendimentoDto.getId()));
        return atendimentoDto;
	}
	
	
	@Transactional
	public AtendimentoDto getAtendimento(Long codigoAtendimento) {
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimentoRepository.consultaAtendimento(codigoAtendimento));
		this.regrasTela(atendimentoDto);
		atendimentoDto.setQtdAnexos(anexoRepository.findByAtendimento(codigoAtendimento).size());
		if (atendimentoDto.getTipoUsuario() > 0) {
			atendimentoDto.setQtdHistoricoUsuario(atendimentoRepository.qtdHistoricoUsuario(atendimentoDto.getOrgao(), atendimentoDto.getTipoDocumento(), 
				atendimentoDto.getNumeroDocumento(), atendimentoDto.getEmail(), atendimentoDto.getId()));
		}
        return atendimentoDto;
	}

	@Transactional
	public AtendimentoDto getPesquisaProtocolo(String numeroProtocolo) {
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimentoRepository.pesquisarNumeroProtocolo(numeroProtocolo));
        return atendimentoDto;
	}

	@Transactional
	public List<HistoricoUsuarioDto> getHistoricoUsuario(Long codigoAtendimento) {
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimentoRepository.consultaAtendimento(codigoAtendimento));
		List<HistoricoUsuarioDto>  lista = atendimentoRepository.historicoUsuario(atendimentoDto.getOrgao(), atendimentoDto.getTipoDocumento(), 
				atendimentoDto.getNumeroDocumento(), atendimentoDto.getEmail(), atendimentoDto.getId());
        return lista;
	}

	public AtendimentoDto novoAtendimento() {
		Atendimento atendimento = new Atendimento();
		Usuario usuario = usuarioRepository.findById(gconSecurity.getIdUsuario()).orElse(null);
		atendimento.setUsuarioCriacao(usuario.getId());
		atendimento.setDataCriacao(LocalDateTime.now());
		atendimento.setDataAlteracao(LocalDateTime.now());
		atendimento.setDataEntrada(LocalDate.now());
		atendimento.setOrigemManifestacao((long)OrigemManifestacaoEnum.O800.getCodigo());
        return atendimentoMapper.toDto(atendimento);
	}

	@Transactional
	public void cancelarAtendimento(Long idAtendimento) {
		Atendimento atendimento = atendimentoRepository.getById(idAtendimento);
		atendimento.setStatus(2);
		atendimentoRepository.save(atendimento);
	}

	private void regrasTela(AtendimentoDto atendimentoDto) {
		atendimentoDto.setManifestacaoClassificada(true);
    	atendimentoDto.setCamposDesabilitados(false);
    	atendimentoDto.setManifestacaoConcluida(false);
    	atendimentoDto.setHabilitaBotaoConcluir(true);
    	
		atendimentoDto.setDescricaoUsuario(usuarioRepository.getById(atendimentoDto.getUsuarioCriacao()).getNome());
		atendimentoDto.setDescricaoUsuarioAlteracao(usuarioRepository.getById(atendimentoDto.getUsuarioAlteracao()).getNome());
    	
		if (atendimentoDto.getArea() == 0 || atendimentoDto.getAssunto() == 0) {
			atendimentoDto.setManifestacaoClassificada(false);
		}
        
        if (atendimentoDto.getTipoUsuario().equals(TipoManifestanteEnum.ANONIMO.getCodigo())) {
        	atendimentoDto.setCamposDesabilitados(true);
        }
        
        if (atendimentoDto.getStatus().equals(StatusAtendimentoEnum.CONCLUIDO.getCodigo())){
          atendimentoDto.setManifestacaoConcluida(true);
        }
        
        if (atendimentoDto.getSatisfaz().equals(PesquisaSatisfacaoEnum.NAO.getCodigo())){
       	 atendimentoDto.setHabilitaBotaoConcluir(false);
        }
        
	}
	
	@Transactional
	public AtendimentoDto salvarAtendimentoSite(AtendimentoRequest atendimentoRequest) {
		
		try {
		    Atendimento atendimento = atendimentoMapper.fromAtendimento(atendimentoRequest);
		    if (atendimentoRequest.getSigilo() != null)
		    	atendimento.setManterSigilo(Boolean.TRUE.equals(atendimentoRequest.getSigilo()) ? 1 : 0);
		    else
		    	atendimento.setManterSigilo(0);		
		    atendimento.setTipoUsuario(atendimentoRequest.getIdentificado().equals("0") ? 0l : 1l);
		    Orgao orgao = orgaoRepository.getById(atendimento.getOrgao());
		    this.carregaAtendimentoSite(atendimento);

		    atendimentoRepository.save(atendimento);
		    
		    
			LogAtendimento logAtendimento = 
					  new LogAtendimento(
							  logAtendimentoRepository.getMaxId()+1,
							  atendimento.getId(),
							  LocalDateTime.now(),
							  atendimento.getUsuarioCriacao(),
							  "Incluiu a manifestação"
							  );
			logAtendimentoRepository.save(logAtendimento);
		    
		    // verifica se tem anexos na requisição
		    if (atendimentoRequest.getListaAnexoDto() != null) {
			    for (ListaAnexoDto anexos : atendimentoRequest.getListaAnexoDto()) {
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
			    }
		    }
		    
		    // se for uma manifestação identificada, envia email
			if (atendimentoRequest.getIdentificado().equals("1")) {
			    Map<String, Object> variaveis = new HashMap<>();
			    variaveis.put("sigla", orgao.getSigla());
				variaveis.put("atendimento", atendimento);
				variaveis.put("url", atendimento.getOrgao());
				String template = null;
				template = "mail/emailcadastromanifestacao";
				mailer.enviarEmail(Arrays.asList(atendimento.getEmail()), template, variaveis, orgao.getSigla(), "Cadastro de Atendimento", null);
			}
			return atendimentoMapper.toDto(atendimento);
		}catch (IOException e) {
			e.getMessage();
		}
		return null;
	}
	
	
	private void carregaAtendimentoSite(Atendimento atendimento) {
		Configuracao conf = configuracaoRepository.findByOrgao(atendimento.getOrgao());
	    if (atendimento.getTipoUsuario() == 0) {
	    	atendimento.setNomeSolicitante(NOME_SOLICITANTE_ANONIMO);
	    	atendimento.setEndereco(NOME_SOLICITANTE_ANONIMO);
	    	atendimento.setBairro(NOME_SOLICITANTE_ANONIMO);
	    	atendimento.setMunicipio(NOME_SOLICITANTE_ANONIMO);
	    	atendimento.setComplemento(NOME_SOLICITANTE_ANONIMO);
	    	atendimento.setEmail("");
	    	atendimento.setCep("");
	    	atendimento.setNumeroDocumento("");
	    	atendimento.setUf("");
	    	atendimento.setDddCelular("");
	    	atendimento.setFoneCelular("");
	    	atendimento.setProtocoloOrigem("");
	    	atendimento.setTipoDocumento(0l);
	    	atendimento.setDataNascimento(new Timestamp(0));
	    }else {
	    	atendimento.setBairro(atendimento.getBairro() == null ? "" :atendimento.getBairro());
	    	atendimento.setMunicipio(atendimento.getMunicipio() == null ? "" : atendimento.getMunicipio());
	    	atendimento.setComplemento(atendimento.getComplemento() == null ? "" : atendimento.getComplemento());
	    	atendimento.setCep(atendimento.getCep() == null ? "" : atendimento.getCep());
	    	atendimento.setNumeroDocumento(atendimento.getNumeroDocumento() == null ? "" : atendimento.getNumeroDocumento());
	    	atendimento.setDddCelular(atendimento.getDddCelular() == null ? "" : atendimento.getDddCelular());
	    	atendimento.setFoneCelular(atendimento.getFoneCelular() == null ? "" : atendimento.getFoneCelular());
	    	atendimento.setProtocoloOrigem(atendimento.getProtocoloOrigem() == null ? "" : atendimento.getProtocoloOrigem());
		    atendimento.setEndereco(atendimento.getEndereco() == null ? "" : atendimento.getEndereco());
		    atendimento.setUf(atendimento.getUf() == null ? "" : atendimento.getUf());
		    atendimento.setCodigoBairroOcorrencia(atendimento.getCodigoBairroOcorrencia() == null ? 0 : atendimento.getCodigoBairroOcorrencia());
	    }

    	atendimento.setArea(51l);
	    atendimento.setStatus(StatusAtendimentoEnum.ABERTO.getCodigo());
	    atendimento.setDescricaoSolucao("");
	    atendimento.setStatusAtendimento(StatusAtendimentoEnum.ABERTO.getCodigo());
	    atendimento.setPriorizacao(conf.getPrioridadePadrao());
	    atendimento.setSetor(conf.getSetor());
	    atendimento.setDataPrazo(adicionarDiasUteis(conf.getQtdDiasResposta(), atendimento.getOrgao()));
	    atendimento.setDataPrazoPrevisto(adicionarDiasUteis(conf.getQtdDiasResposta(), atendimento.getOrgao()));
	    atendimento.setDataQuando("");
	    atendimento.setDescricao("");
	    atendimento.setDescricaoComo("");
	    atendimento.setDescricaoFatos(atendimento.getDescricaoOque());
	    atendimento.setDescricaoOnde("");
	    atendimento.setDescricaoQuando("");
	    atendimento.setDescricaoQuem("");
	    atendimento.setTempoDuracaoAtendimento("");
	    atendimento.setEstadoCivil(0);
	    atendimento.setDddFone("");
	    atendimento.setFone("");
	    atendimento.setObservacao("");
	    atendimento.setSatisfaz(PesquisaSatisfacaoEnum.NAO.getCodigo());
	    atendimento.setResposta("");
	    atendimento.setOrigemManifestacao((long)OrigemManifestacaoEnum.SITE.getCodigo());
	    atendimento.setCodigoAtendimentoResposta(FormaAtendimentoRespostaEnum.NAO_INFORMADO.getCodigo());
	    atendimento.setJustificativaProrrogacao("");
	    atendimento.setCodigoMunicipio(0);
	    atendimento.setOrgao(atendimento.getOrgao()); 
	    atendimento.setAnoAtendimento(LocalDate.now().getYear());
	    atendimento.setUsuarioCriacao(3l);
	    atendimento.setDataCriacao(LocalDateTime.now()); 
	    atendimento.setUsuarioAlteracao(3l);
	    atendimento.setDataAlteracao(LocalDateTime.now());
	    atendimento.setProvidencia("");
	    atendimento.setCodigoModelo(0l);
	    atendimento.setTotalDias(0);
	    atendimento.setDataConclusao(new Timestamp(0));
	    if (atendimento.getAssunto() == null)
	    	atendimento.setAssunto(0l);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("ssmm");
	    Date date = new Date();
	    atendimento.setSenhaManifestante(sdf.format(date));
	    atendimento.setPendente(0);
	
	    atendimento.setDataEntrada(LocalDate.now());
	    atendimento.setNumeroAtendimento(atendimentoRepository.getMaxNumeroAtendimento(LocalDate.now().getYear())+1);
	    atendimento.setNumeroProtocolo(atendimento.getAnoAtendimento() +""+atendimento.getNumeroAtendimento());
	    atendimento.setSequencialOrgao(atendimentoRepository.getMaxSequencialOrgao(LocalDate.now().getYear(), atendimento.getOrgao())+1);
	}

	
	@Transactional
	public AtendimentoDto atualizarAtendimento(AtendimentoRequest request) {
		Atendimento atendimento = atendimentoMapper.fromAtendimento(request);
		Usuario usuario = usuarioRepository.findById(gconSecurity.getIdUsuario()).orElse(null);
		
		atendimento.setUsuarioAlteracao(usuario.getId());
		atendimento.setDataAlteracao(LocalDateTime.now());
		
		atendimentoRepository.save(atendimento);
		
		LogAtendimento logAtendimento = 
				  new LogAtendimento(
						  logAtendimentoRepository.getMaxId()+1,
						  atendimento.getId(),
						  LocalDateTime.now(),
						  usuario != null ? usuario.getId() : 0l,
						  "Alterou a manifestação"
						  );
		logAtendimentoRepository.save(logAtendimento);
		
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimento);

		this.regrasTela(atendimentoDto);
		
		return atendimentoDto;
	}

	@Transactional
	public synchronized AtendimentoDto salvarAtendimento(AtendimentoRequest request) {
		Atendimento atendimento = atendimentoMapper.fromAtendimento(request);
		Configuracao conf = null;
		Usuario usuario = usuarioRepository.findById(gconSecurity.getIdUsuario()).orElse(null);
		conf = configuracaoRepository.findByOrgao(usuario.getOrgao());
		this.carregaAtendimentoInicial(atendimento, conf, usuario);
		
		atendimento.setUsuarioAlteracao(usuario.getId());
		atendimento.setDataAlteracao(LocalDateTime.now());
		
		atendimentoRepository.save(atendimento);
		
		LogAtendimento logAtendimento = 
				  new LogAtendimento(
						  logAtendimentoRepository.getMaxId()+1,
						  atendimento.getId(),
						  LocalDateTime.now(),
						  usuario != null ? usuario.getId() : 0l,
						  "Incluiu a manifestação"
						  );
		logAtendimentoRepository.save(logAtendimento);
		
		if (request.getListaAnexoDto() != null) {
			
			ListaAnexoRequest listaAnexoRequest = new ListaAnexoRequest();
			listaAnexoRequest.setListaAnexoDto(request.getListaAnexoDto());
			listaAnexoRequest.setCodigoAtendimento(atendimento.getId());
			anexoService.salvarArquivo(listaAnexoRequest);
			
		}
		
		
		
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimento);

		this.regrasTela(atendimentoDto);
		
		return atendimentoDto;
	}
	
	
	private void carregaAtendimentoInicial(Atendimento atendimentoDto, Configuracao conf, Usuario usuario) {
		atendimentoDto.setDescricaoOque(atendimentoDto.getDescricaoFatos());
		atendimentoDto.setNatureza(atendimentoDto.getNatureza() == null ? 0l : atendimentoDto.getNatureza());
		atendimentoDto.setCodigoAtendimentoResposta(0);
		atendimentoDto.setPriorizacao(atendimentoDto.getPriorizacao() == null ? 1 : atendimentoDto.getPriorizacao());
		atendimentoDto.setAssunto(atendimentoDto.getAssunto() == null ? 0l : atendimentoDto.getAssunto());
		atendimentoDto.setDescricao("");
		atendimentoDto.setOutroLocal("");
		atendimentoDto.setCodigoModelo(0l);
		atendimentoDto.setDataQuando(atendimentoDto.getDataQuando() == null ? "" : atendimentoDto.getDataQuando());
		atendimentoDto.setDescricaoComo(atendimentoDto.getDescricaoComo() == null ? "" : atendimentoDto.getDescricaoComo());
		atendimentoDto.setDescricaoQuem(atendimentoDto.getDescricaoQuem() == null ? "" : atendimentoDto.getDescricaoQuem());
		atendimentoDto.setDescricaoOnde(atendimentoDto.getDescricaoOnde() == null ? "" : atendimentoDto.getDescricaoOnde());
		atendimentoDto.setBairro(atendimentoDto.getBairro() == null ? "" : atendimentoDto.getBairro());
		atendimentoDto.setArea(atendimentoDto.getArea() == null ? 0l : atendimentoDto.getArea());
		atendimentoDto.setCep(atendimentoDto.getCep() == null ? "" : atendimentoDto.getCep());
		atendimentoDto.setCodigoBairroOcorrencia(atendimentoDto.getCodigoBairroOcorrencia() == null ? 0l : atendimentoDto.getCodigoBairroOcorrencia());
		atendimentoDto.setComplemento(atendimentoDto.getComplemento() == null ? "" : atendimentoDto.getComplemento());
		atendimentoDto.setDddCelular(atendimentoDto.getDddCelular() == null ? "" : atendimentoDto.getDddCelular());
		atendimentoDto.setDddFone(atendimentoDto.getDddFone() == null ? "" : atendimentoDto.getDddFone());
		atendimentoDto.setFone(atendimentoDto.getFone() == null ? "" : atendimentoDto.getFone());
		atendimentoDto.setFoneCelular(atendimentoDto.getFoneCelular() == null ? "" : atendimentoDto.getFoneCelular());
		atendimentoDto.setEmail(atendimentoDto.getEmail() == null ? "" : atendimentoDto.getEmail());
		atendimentoDto.setEndereco(atendimentoDto.getEndereco() == null ? "" : atendimentoDto.getEndereco());
		atendimentoDto.setEstadoCivil(atendimentoDto.getEstadoCivil() == null ? 0 : atendimentoDto.getEstadoCivil());
		atendimentoDto.setManterSigilo(atendimentoDto.getManterSigilo() == null ? 0 : atendimentoDto.getManterSigilo());
		atendimentoDto.setMunicipio(atendimentoDto.getMunicipio() == null ? "" : atendimentoDto.getMunicipio());
		atendimentoDto.setNumeroDocumento(atendimentoDto.getNumeroDocumento() == null ? "" : atendimentoDto.getNumeroDocumento());
		atendimentoDto.setObservacao("");
		atendimentoDto.setProtocoloOrigem(atendimentoDto.getProtocoloOrigem() == null ? "" : atendimentoDto.getProtocoloOrigem());
		atendimentoDto.setResposta("");
		atendimentoDto.setSatisfaz(PesquisaSatisfacaoEnum.NAO.getCodigo());
	    SimpleDateFormat sdf = new SimpleDateFormat("ssmm");
	    Date date = new Date();
	    atendimentoDto.setSenhaManifestante(sdf.format(date));
		atendimentoDto.setPendente(0);
		atendimentoDto.setDataConclusao(new Timestamp(0));
		atendimentoDto.setCodigoMunicipio(0);
		atendimentoDto.setTempoDuracaoAtendimento("");
		atendimentoDto.setTipoDocumento(atendimentoDto.getTipoDocumento() == null ? 0 : atendimentoDto.getTipoDocumento());
		atendimentoDto.setUf(atendimentoDto.getUf() == null ? "" : atendimentoDto.getUf());
		atendimentoDto.setStatus(StatusAtendimentoEnum.ABERTO.getCodigo());
		atendimentoDto.setSetor(usuario.getSetor());
		atendimentoDto.setStatusAtendimento(StatusAtendimentoEnum.ABERTO.getCodigo());
		atendimentoDto.setOrgao(usuario.getOrgao()); 
		atendimentoDto.setAnoAtendimento(LocalDate.now().getYear());
		atendimentoDto.setUsuarioCriacao(usuario.getId());
		atendimentoDto.setUsuarioAlteracao(usuario.getId());
		atendimentoDto.setDataCriacao(LocalDateTime.now()); 
		atendimentoDto.setDataAlteracao(LocalDateTime.now());
		atendimentoDto.setDataEntrada(LocalDate.now());
		atendimentoDto.setNumeroAtendimento(atendimentoRepository.getMaxNumeroAtendimento(LocalDate.now().getYear())+1);
		atendimentoDto.setNumeroProtocolo(atendimentoDto.getAnoAtendimento() +""+atendimentoDto.getNumeroAtendimento());
		atendimentoDto.setDataPrazo(adicionarDiasUteis(conf.getQtdDiasResposta(), atendimentoDto.getOrgao()));
		atendimentoDto.setDataPrazoPrevisto(adicionarDiasUteis(conf.getQtdDiasResposta(), atendimentoDto.getOrgao()));
		atendimentoDto.setSequencialOrgao(atendimentoRepository.getMaxAnoAtendimento(LocalDate.now().getYear(), usuario.getOrgao())+1);
	}
	
	@Transactional
	public Page<Atendimento> getPesquisaAtendimentoDescricao(Long orgao, AtendimentoFilter filtro, Pageable page){
		return atendimentoRepository.filtrar(orgao, filtro, page);
	}
	

    public Page<Atendimento> getListaNovosAtendimentos(Long orgao, Pageable page)
    {
        return atendimentoRepository.consultaNovasManifestacoes(orgao, page, atendimentoRepository.getNovosAtendimentos(orgao));
    }

    public Page<Atendimento> getListaAtendimentosClassfNaoEnc(Long orgao, Pageable page)
    {
        return atendimentoRepository.consultaAtendimentosClassifNaoEnc(orgao, page, atendimentoRepository.getAtendimentosClassifNaoEnc(orgao));
    }

    @Transactional
    public Page<ModeloListaEncaminhamentoRecebidos> getListaAtendimentosRecebidos(Long orgao, Pageable page)
    {
        List<ModeloListaEncaminhamentoRecebidos> lista = new ArrayList<>();	
        Integer totalReg = encaminhamentoService.getQtdEncaminhamentosRecebidos(orgao);
    	List<Encaminhamento> enc = encaminhamentoService.getListaEncaminhamentosRecebidos(orgao, page, totalReg);
    	for (Encaminhamento encaminhamento : enc) {
    		Atendimento atendimento = atendimentoRepository.getById(encaminhamento.getAtendimento());
    		ModeloListaEncaminhamentoRecebidos modelo = new ModeloListaEncaminhamentoRecebidos();
    		modelo.setSetorDestino(setorRepository.getById(encaminhamento.getSetorDestino()).getDescricao());
    		modelo.setNumeroProtocolo(atendimento.getNumeroProtocolo());
    		modelo.setData(encaminhamento.getDataEncaminhamento());
    		modelo.setIdAtendimento(encaminhamento.getAtendimento());
    		modelo.setData(encaminhamento.getDataEncaminhamento());
    		modelo.setAssunto(assuntoRepository.getById(atendimento.getAssunto()).getDescricao());
    		modelo.setPrioridade(PriorizacaoEnum.pegarDescricao(atendimento.getPriorizacao()).getDescricao());
    		lista.add(modelo);
		}
        return new PageImpl<>(lista, page, totalReg);
    }

	@Transactional
	public void concluirAtendimento(AtendimentoConclusaoRequest atendimentoConclusao, Principal auth) {
		Usuario usuario = usuarioRepository.findByCpf(auth.getName()).orElse(null);
		Atendimento atendimento = atendimentoRepository.getById(atendimentoConclusao.getAtendimento());
		atendimento.setCodigoAtendimentoResposta(atendimentoConclusao.getFormaResposta());
		atendimento.setCodigoModelo(atendimentoConclusao.getModeloDocumento());
		atendimento.setResposta(atendimentoConclusao.getTextoProvidencia());
		atendimento.setStatus(StatusAtendimentoEnum.CONCLUIDO.getCodigo());
		atendimento.setUsuarioAlteracao(usuario.getId());
		atendimento.setDataAlteracao(LocalDateTime.now());
		atendimento.setDataConclusao(Timestamp.valueOf(LocalDateTime.now()));
		atendimento.setPendente(0);
		atendimento.setTotalDias(0);
		atendimento.setParametroHash(GeradorString.getRandomString());
		atendimento.setStatusAtendimento(StatusAtendimentoEnum.CONCLUIDO.getCodigo());
		atendimentoRepository.save(atendimento);
		
		LogoTipo logo = logotipoRepository.findByOrgao(atendimento.getOrgao());
		
		
		LogAtendimento logAtendimento = 
				  new LogAtendimento(
						  logAtendimentoRepository.getMaxId()+1, 
						  atendimento.getId(),
						  LocalDateTime.now(),
						  usuario != null ? usuario.getId() : 0l,
						  "Concluiu a manifestação"
						  );
		logAtendimentoRepository.save(logAtendimento);
		
		
		String[] anexos = new String[atendimentoConclusao.getSelectedAnexos().size()];
	    
		for (int i = 0; i < atendimentoConclusao.getSelectedAnexos().size(); i++) {
			Optional<Anexo> anexo = anexoRepository.findById(atendimentoConclusao.getSelectedAnexos().get(i));
			if (anexo.isPresent()) {
				anexos[i] = "D:\\gcon_arquivos\\arquivos\\"+anexo.get().getNomeArquivo();
			}
		}


	    if (atendimentoConclusao.getFormaResposta().equals(FormaAtendimentoRespostaEnum.EMAIL.getCodigo())) {
			Orgao orgao = orgaoRepository.getById(atendimento.getOrgao());
			String strResposta = "<table><tr><td>Nº do Protocolo: "+ atendimento.getAnoAtendimento()+ "" + atendimento.getNumeroAtendimento() +"<b><font style='font-family:tahoma, verdana, arial, helvetica, sans-serif; font-size:10pt;text-align:justify;'>"+atendimentoConclusao.getTextoProvidencia()+"</font></td></tr>";
		    Map<String, Object> variaveis = new HashMap<>();
		    variaveis.put("sigla", orgao.getSigla());
			variaveis.put("texto", strResposta);
			variaveis.put("url", atendimento.getOrgao());
			variaveis.put("logo", "https://ouvidoriagcon8501.recife.pe.gov.br/assets/image/"+logo.getNome());
			String template = "mail/emailconclusao";
			mailer.enviarEmail(Arrays.asList(atendimento.getEmail()), template, variaveis, orgao.getSigla(), "Resposta do Atendimento", anexos);
			
			
			String posAtendimento = "<table><tr><td>Nº do Protocolo: <b>"+ atendimento.getAnoAtendimento()+ "" + atendimento.getNumeroAtendimento() +"</b></td></tr><tr><td>Prezado (a) "+atendimento.getNomeSolicitante()+",</td></tr><tr><td>Colabore com o aprimoramento dos serviços da Ouvidoria. Responda nossa pesquisa de satisfação.<br><br></td></tr>";
		    variaveis = new HashMap<>();
		    variaveis.put("sigla", orgao.getSigla());
			variaveis.put("texto", posAtendimento);
			variaveis.put("url", "https://ouvidoriagcon8501.recife.pe.gov.br/gconweb/pesquisa-satisfacao/"+atendimento.getParametroHash());
			variaveis.put("logo", "https://ouvidoriagcon8501.recife.pe.gov.br/assets/image/"+logo.getNome());
			template = "mail/emailpesquisasatisfacao";
			mailer.enviarEmail(Arrays.asList(atendimento.getEmail()), template, variaveis, "Ouvidoria - Prefeitura do Recife", "Pesquisa de Satisfação", null);
			
			
			
	    }
	}


	@Transactional
	public AtendimentoDto reabrirAtendimento(Long codigoAtendimento, Principal auth) {
		Usuario usuario = usuarioRepository.findByCpf(auth.getName()).orElse(null);
		Atendimento atendimento = atendimentoRepository.getById(codigoAtendimento);
		atendimento.setStatus(StatusAtendimentoEnum.ABERTO.getCodigo());
		atendimento.setUsuarioAlteracao(usuario.getId());
		atendimento.setDataAlteracao(LocalDateTime.now());
		atendimento.setDataConclusao(new Timestamp(0));
		atendimento.setStatusAtendimento(StatusAtendimentoEnum.ABERTO.getCodigo());
		atendimentoRepository.save(atendimento);
		
		LogAtendimento logAtendimento = 
				  new LogAtendimento(
						  logAtendimentoRepository.getMaxId()+1,
						  atendimento.getId(),
						  LocalDateTime.now(),
						  usuario != null ? usuario.getId() : 0l,
						  "Reabriu o atendimento da manifestação"
						  );
		logAtendimentoRepository.save(logAtendimento);
		
		AtendimentoDto atendimentoDto = atendimentoMapper.toDto(atendimento);
		regrasTela(atendimentoDto);
        return atendimentoDto;
	}

	
	public List<AtendimentoArea> relatorioAtendimentoArea(Long orgao, Date dataInicial, Date dataFinal, String area, String status) {
		
		 List<AtendimentoArea> lista = new ArrayList<>();
		 List<Area> listaAreas = new ArrayList<>();
		 List<DetalheAtendimentoArea> detalhe = new ArrayList<>();
		
		 if (area.equals("")) {
			 listaAreas = areaRepository.findByOrgaoAndStatusOrderByDescricaoAsc(orgao, 0);
		 }else {
			 listaAreas.add(new Area(Long.parseLong(area)));
		 }
		 
		 for (Area area2 : listaAreas) {
			 detalhe = atendimentoRepository.getAtendimentoArea(orgao, dataInicial, dataFinal, area2.getId().toString(), status);
			 lista.add(new AtendimentoArea(area2.getDescricao(), detalhe));
		 }
		
		return lista;
	}

	public List<DetalheAtendimentoArea> relatorioAtendimentoAreaPDF(Long orgao, Date dataInicial, Date dataFinal, String area, String status) {
		
		 List<Area> listaAreas = new ArrayList<>();
		 List<DetalheAtendimentoArea> detalhe = new ArrayList<>();
		
		 if (area.equals("")) {
			 listaAreas = areaRepository.findByOrgaoAndStatusOrderByDescricaoAsc(orgao, 0);
		 }else {
			 listaAreas.add(new Area(Long.parseLong(area)));
		 }
		 
		 for (Area area2 : listaAreas) {
			 detalhe.addAll(atendimentoRepository.getAtendimentoArea(orgao, dataInicial, dataFinal, area2.getId().toString(), status));
		 }
		
		return detalhe;
	}

	public List<RelatorioPeriodo> relatorioAtendimentoPeriodo(Long orgao, Date dataInicial, Date dataFinal) {
		
		if (dataFinal.before(dataInicial)) {
			throw new  NegocioException("Data final menor que data inicial");
		}
		
	   List<RelatorioPeriodo> detalhe = new ArrayList<>();
	   detalhe.addAll(atendimentoRepository.getAtendimentoPeriodo(orgao, dataInicial, dataFinal));
	   return detalhe;
	}

	public List<DetalheAtendimentoArea> relatorioAtendimentoAreaPDF(Long orgao, Date dataInicial, Date dataFinal, String area) {
		
		 List<DetalheAtendimentoArea> detalhe = new ArrayList<>();
		 
		 detalhe.addAll(atendimentoRepository.getAtendimentoArea(orgao, dataInicial, dataFinal, area));
		
		return detalhe;
	}


	public List<DetalheAtendimento> relatorioAtendimentoAreaPDF(Long idAtendimento) {
		 List<DetalheAtendimento> detalhe = new ArrayList<>();
		 Optional<Atendimento> atendimento = atendimentoRepository.findById(idAtendimento);
		 if (atendimento.isPresent()) {
			 
			 
			Optional<Area> area = areaRepository.findById(atendimento.get().getArea());
			Optional<Natureza> natureza = naturezaRepository.findById(atendimento.get().getNatureza());
			Optional<OrigemManifestacao> origemManifestacao = origemManifestacaoRepository.findById(atendimento.get().getOrigemManifestacao());
			Optional<Bairro> bairroOcorrencia =  null;
			if (atendimento.get().getCodigoBairroOcorrencia() != null) {
				bairroOcorrencia =  bairroRepository.findById(atendimento.get().getCodigoBairroOcorrencia());
			}
			Optional<Assunto> assunto =  assuntoRepository.findById(atendimento.get().getAssunto());
			 
			 List<EncaminhamentoDto> listaEnc = encaminhamentoService.listaEncaminhamentoComResposta(atendimento.get().getId());
			 List<DespachoDto> listDespacho = despachoService.getDespachos(atendimento.get().getId());
			 List<RespostaParcialDto> listaRespostaParcial = respostaParcialService.getRespostaParcials(atendimento.get().getId());
			 
			 if (atendimento.get().getManterSigilo() == 1) {
				 atendimento.get().setNomeSolicitante("Sigilo");
				 atendimento.get().setEndereco("Sigilo");
				 atendimento.get().setComplemento("Sigilo");
				 atendimento.get().setBairro("Sigilo");
				 atendimento.get().setUf("");
				 atendimento.get().setMunicipio("Sigilo");
				 atendimento.get().setNumeroDocumento("Sigilo");
				 atendimento.get().setCep("Sigilo");
				 atendimento.get().setFoneCelular("Sigilo");
				 atendimento.get().setFone("Sigilo");
				 atendimento.get().setEmail("Sigilo");
			 }
			 
			 if (area.isPresent() && natureza.isPresent() && origemManifestacao.isPresent() && assunto.isPresent()) {
				 detalhe.add(new DetalheAtendimento(atendimento.get(), area.get().getDescricao(), natureza.get().getDescricao(), origemManifestacao.get().getDescricao(), assunto.get().getDescricao(), 
						 PriorizacaoEnum.pegarDescricao(atendimento.get().getPriorizacao()).getDescricao(), bairroOcorrencia == null ? "" : bairroOcorrencia.get().getDescricao(), listaEnc, listDespacho, listaRespostaParcial));
			 }
		 }
		return detalhe;
	}
	
	public byte[] impressaoRelatorio(List<DetalheAtendimento> lista){
		 try {
			 Map<String, Object> parametros = new HashMap<>();
			 parametros.put("IMAGE", IMAGE_CABECALHO);
			 parametros.put("COMO", lista.get(0).getAtendimento().getDescricaoComo());
			 Resource resource = new ClassPathResource("\\rel");
			 File file = resource.getFile();		 
			 parametros.put("SUBREPORT_DIR", file.getAbsolutePath()+"\\");
			 parametros.put("DOC", lista.get(0).getAtendimento().getTipoDocumento() == 1 ? "CPF:" : "RG:");
			 parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			 parametros.put("PROTOCOLO", lista.get(0).getAtendimento().getNumeroProtocolo());
				InputStream inputStream = this.getClass().getResourceAsStream(
						"/rel/relatorio_atend.jasper"); 
			 JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
					new JRBeanCollectionDataSource(lista));
			 return JasperExportManager.exportReportToPdf(jasperPrint);
		 }catch (Exception e) {
			 e.getMessage();
		}
		 return new byte[0];
	}


	public byte[] impressaoRelatorio(List<?> lista, Date dataInicial, Date dataFinal, String arquivoJasper ) {
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		 Map<String, Object> parametros = new HashMap<>();
		 parametros.put("IMAGE", IMAGE_CABECALHO);
		 parametros.put("PERIODO", dateFormat.format(dataInicial) + " a " + dateFormat.format(dataFinal));
		 parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		 try {
			InputStream inputStream = this.getClass().getResourceAsStream(arquivoJasper);
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
					new JRBeanCollectionDataSource(lista));
			return JasperExportManager.exportReportToPdf(jasperPrint);
		 }catch (Exception e) {
			e.getMessage();
		}
		 return new byte[0];
	}

	public byte[] impressaoRelatorioEncaminhamentoSetor(List<?> lista, Date dataInicial, Date dataFinal, String arquivoJasper, String status) {
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		 Map<String, Object> parametros = new HashMap<>();
		 parametros.put("IMAGE", IMAGE_CABECALHO);
		 parametros.put("PERIODO", dateFormat.format(dataInicial) + " a " + dateFormat.format(dataFinal));
		 parametros.put("STATUS", status.equals("1") ? "ENCAMINHAMENTOS VENCIDOS" : "ENCAMINHAMENTOS NO PRAZO");
		 parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		 try {
			InputStream inputStream = this.getClass().getResourceAsStream(arquivoJasper);
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
					new JRBeanCollectionDataSource(lista));
			return JasperExportManager.exportReportToPdf(jasperPrint);
		 }catch (Exception e) {
			e.getMessage();
		}
		 return new byte[0];
	}

	public LocalDate adicionarDiasUteis(Integer qtdeDiasAcrescentados, Long codigoOrgao) {
		List<Feriado> feriadoInfoList = null;
		LocalDate data = LocalDate.now();
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



	
	
    
	public List<ResumoAtendimentoDto> getResumo(Long orgao, String cpf, String numero, String senha) {

		List<Atendimento> listaAtendimento = new ArrayList<>();
		List<ResumoAtendimentoDto> listaResumo = new ArrayList<>();
		
		if (cpf != null && !cpf.equals("")) {
			listaAtendimento = atendimentoRepository.findByOrgaoAndNumeroDocumento(orgao, cpf);
		}else {
			listaAtendimento = atendimentoRepository.findByOrgaoAndNumeroProtocoloAndSenhaManifestante(orgao, numero, senha);
		}
		
		ResumoAtendimentoDto resumo = null;

		for (Atendimento atendimento : listaAtendimento) {
			resumo = new ResumoAtendimentoDto();
			List<RespostaParcial> listRespostaParcial = respostaParcialRepository.consultaAtendimento(atendimento.getId());
			resumo = new ResumoAtendimentoDto();
			if (atendimento.getSatisfaz().equals(PesquisaSatisfacaoEnum.NAO.getCodigo())) {
				List<PerguntaSatisfacao> listaPerguntas = perguntaSatisfacaoRepository.findByOrgaoAndStatus(atendimento.getOrgao(), 0);
				List<RespostaSatisfacao> listaRespostasSatisfacao = new ArrayList<>();
				
				for (int i = 0; i < listaPerguntas.size(); i++) {
					listaRespostasSatisfacao = respostaSatisfacaoRepository.findByPerguntaSatisfacaoAndStatus(listaPerguntas.get(i).getId(), 0);
					listaPerguntas.get(i).setListaRespostaSatisfacao(listaRespostasSatisfacao);
				}
				resumo.setListaPergunta(listaPerguntas);
			}
			
    	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
    	    resumo.setNumeroProtocolo(atendimento.getNumeroProtocolo());
    	    if (atendimento.getAssunto() != null && atendimento.getAssunto() > 0)
    	    	resumo.setAssunto(assuntoRepository.getById(atendimento.getAssunto()).getDescricao());
    	    else
    	    	resumo.setAssunto("Assunto não encontrado");
    	    if (atendimento.getStatus() == 1) {
    	    	resumo.setSituacao("Concluída");
    	    }else {
    	    	resumo.setSituacao("Em atendimento");
    	    }
	    	resumo.setNatureza(naturezaRepository.getById(atendimento.getNatureza()).getDescricao());
	    	
	    	if (atendimento.getCodigoBairroOcorrencia() != null && atendimento.getCodigoBairroOcorrencia() > 0) {
	    		resumo.setBairroOcorrencia(bairroRepository.getById(atendimento.getCodigoBairroOcorrencia()).getDescricao());
	    	}else {
	    		resumo.setBairroOcorrencia("");
	    	}

			resumo.setNomeSolicitante(atendimento.getNomeSolicitante());
			resumo.setDescricaoFatos(atendimento.getDescricaoFatos());
			resumo.setDataCriacao(atendimento.getDataCriacao().format(formatter));
			resumo.setRespostaConclusao(atendimento.getResposta());
			resumo.setRespondeuPesquisa(atendimento.getSatisfaz());
			resumo.setIdAtendimento(atendimento.getId());
			resumo.setManifestacaoConcluida(atendimento.getDataConclusao().toString().contains("1969-12-31") ? "Não" : "Sim");
			resumo.setListaResposta(respostaParcialMapper.fromResponseList(listRespostaParcial));
			listaResumo.add(resumo);
			
		}
		
		return listaResumo;

	}
	
	
	public List<ResumoAtendimentoDto> getResumo(String parametroHash) {

		List<Atendimento> listaAtendimento = new ArrayList<>();
		List<ResumoAtendimentoDto> listaResumo = new ArrayList<>();
		
		listaAtendimento = atendimentoRepository.findByParametroHash(parametroHash);
		
		ResumoAtendimentoDto resumo = null;

		for (Atendimento atendimento : listaAtendimento) {
			resumo = new ResumoAtendimentoDto();
			List<RespostaParcial> listRespostaParcial = respostaParcialRepository.consultaAtendimento(atendimento.getId());
			resumo = new ResumoAtendimentoDto();
			if (atendimento.getSatisfaz().equals(PesquisaSatisfacaoEnum.NAO.getCodigo())) {
				List<PerguntaSatisfacao> listaPerguntas = perguntaSatisfacaoRepository.findByOrgaoAndStatus(atendimento.getOrgao(), 0);
				List<RespostaSatisfacao> listaRespostasSatisfacao = new ArrayList<>();
				
				for (int i = 0; i < listaPerguntas.size(); i++) {
					listaRespostasSatisfacao = respostaSatisfacaoRepository.findByPerguntaSatisfacaoAndStatus(listaPerguntas.get(i).getId(), 0);
					listaPerguntas.get(i).setListaRespostaSatisfacao(listaRespostasSatisfacao);
				}
				resumo.setListaPergunta(listaPerguntas);
			}
			
    	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
    	    resumo.setNumeroProtocolo(atendimento.getNumeroProtocolo());
    	    if (atendimento.getAssunto() != null && atendimento.getAssunto() > 0)
    	    	resumo.setAssunto(assuntoRepository.getById(atendimento.getAssunto()).getDescricao());
    	    else
    	    	resumo.setAssunto("Assunto não encontrado");
    	    if (atendimento.getStatus() == 1) {
    	    	resumo.setSituacao("Concluída");
    	    }else {
    	    	resumo.setSituacao("Em atendimento");
    	    }
	    	resumo.setNatureza(naturezaRepository.getById(atendimento.getNatureza()).getDescricao());
	    	
	    	if (atendimento.getCodigoBairroOcorrencia() != null && atendimento.getCodigoBairroOcorrencia() > 0) {
	    		resumo.setBairroOcorrencia(bairroRepository.getById(atendimento.getCodigoBairroOcorrencia()).getDescricao());
	    	}else {
	    		resumo.setBairroOcorrencia("");
	    	}

			resumo.setNomeSolicitante(atendimento.getNomeSolicitante());
			resumo.setDescricaoFatos(atendimento.getDescricaoFatos());
			resumo.setDataCriacao(atendimento.getDataCriacao().format(formatter));
			resumo.setRespostaConclusao(atendimento.getResposta());
			resumo.setRespondeuPesquisa(atendimento.getSatisfaz());
			resumo.setIdAtendimento(atendimento.getId());
			resumo.setManifestacaoConcluida(atendimento.getDataConclusao().toString().contains("1969-12-31") ? "Não" : "Sim");
			resumo.setListaResposta(respostaParcialMapper.fromResponseList(listRespostaParcial));
			listaResumo.add(resumo);
			
		}
		
		return listaResumo;

	}
	
	public PainelOuvidoria getPainel(Long orgao, Integer ano) {
		PainelOuvidoria painel = new PainelOuvidoria();
		painel.setTotalAtendimentos(atendimentoRepository.getQtdAtendimentosAno(orgao, ano));
		painel.setTotalAtendimentosResolvidos(atendimentoRepository.getQtdAtendimentosConcluidos(orgao, ano));
		painel.setTotalAtendimentosTramitacao(atendimentoRepository.getQtdAtendimentosAnoNaoConcluidos(orgao, ano));
		painel.setTotalAtendimentosTramitacaoNaoVencidos(atendimentoRepository.getQtdAtendimentosAnoNaoConcluidosNaoVencidos(orgao, ano));
		painel.setTotalAtendimentosTramitacaoVencidos(atendimentoRepository.getQtdAtendimentosAnoNaoConcluidosVencidos(orgao, ano));
		painel.setTotalAtendimentosNovos(atendimentoRepository.getNovosAtendimentos(orgao, ano));

		BigDecimal percentual = null;
		
		if (painel.getTotalAtendimentosResolvidos() > 0) {
			percentual = new BigDecimal((painel.getTotalAtendimentosResolvidos()*100f)/painel.getTotalAtendimentos()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			painel.setPercentualResolvido(percentual.doubleValue());
		}
		
		if (painel.getTotalAtendimentosTramitacao() > 0) {
			percentual = new BigDecimal((painel.getTotalAtendimentosTramitacao()*100f)/painel.getTotalAtendimentos()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			painel.setPercentualTramitacao(percentual.doubleValue());
		}
		return painel;
	}
	
	public List<AtendimentoNatureza> getPainelNatureza(Long orgao, Integer ano) {
        Integer listaAtendimentos = atendimentoRepository.getQtdAtendimentosAno(orgao, ano);
		return atendimentoRepository.getQtdAtendimentosNatureza(orgao, ano, listaAtendimentos);
	}
	
	
	public RelatorioPesquisaSatisfacao  getEstatisticaPesquisa(Long orgao, Date dataInicial, Date dataFinal, Long area) {
		List<PerguntaSatisfacao> listaPerguntas = perguntaSatisfacaoRepository.findByOrgaoAndStatus(orgao, 0);
		
		List<EstatisticaPesquisaSatisfacao> listaEstatistica = new ArrayList<>();
		
		RelatorioPesquisaSatisfacao relatorioPesquisaSatisfacao = atendimentoRepository.getEstatisticaPesqSatisfacao(orgao, dataInicial, dataFinal, area);
		
		for (PerguntaSatisfacao perguntaSatisfacao : listaPerguntas) {
			
			listaEstatistica.add(atendimentoRepository.getListaEstatisticaPerguntas(orgao, dataInicial, dataFinal, perguntaSatisfacao.getId(), area));
			
		}
		
		relatorioPesquisaSatisfacao.setListaEstatistica(listaEstatistica);
		
		return relatorioPesquisaSatisfacao;
		
	}
	
	
	
	public DadosRelatorio getListaEncaminhamentos(Long orgao, String dataInicial, String dataFinal, Long area){
		Integer total = encaminhamentoRepository.getEncaminhamentosEnviados(orgao, area, dataInicial, dataFinal);
		return atendimentoRepository.getRelatorioSecretaria(orgao, dataInicial, dataFinal, area, total);
	}

	

	


}

package br.com.multiinovacoes.gcon.controller;

import static br.com.multiinovacoes.gcon.controller.AtendimentoController.ATENDIMENTO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.multiinovacoes.gcon.graphics.StackedColumn;
import br.com.multiinovacoes.gcon.model.dto.ResumoAtendimentoDto;
import br.com.multiinovacoes.gcon.model.filter.AtendimentoFilter;
import br.com.multiinovacoes.gcon.model.mapper.AtendimentoMapper;
import br.com.multiinovacoes.gcon.model.request.AtendimentoConclusaoRequest;
import br.com.multiinovacoes.gcon.model.request.AtendimentoRequest;
import br.com.multiinovacoes.gcon.model.request.PesquisaSatisfacaoRequest;
import br.com.multiinovacoes.gcon.model.response.AtendimentoResponse;
import br.com.multiinovacoes.gcon.model.response.ListaAtendimentoResponse;
import br.com.multiinovacoes.gcon.model.response.ListaAtendimentosRecebidosResponse;
import br.com.multiinovacoes.gcon.model.response.ListaHistoricoUsuarioResponse;
import br.com.multiinovacoes.gcon.model.response.ResumoAtendimentoResponse;
import br.com.multiinovacoes.gcon.report.AtendimentoNatureza;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.PainelOuvidoria;
import br.com.multiinovacoes.gcon.repository.AtendimentoRepository;
import br.com.multiinovacoes.gcon.security.GconSecurity;
import br.com.multiinovacoes.gcon.service.AtendimentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Api(value = "Atendimento", produces = APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(path = ATENDIMENTO	)
public class AtendimentoController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AtendimentoController.class);
	
	public static final String ATENDIMENTO = "/atendimentos";
	public static final String CONCLUIR_ATENDIMENTO = "/concluir-atendimento";
	public static final String SITE = "/site";
	public static final String LISTAR = "/listar";
	public static final String LISTAR_NOVOS = "/listarNovos";
	public static final String QTD_NOVOS_ATENDIMENTOS = "/novos-atendimentos";
	public static final String LISTA_ATENDIMENTOS_CLASSIF_NAO_ENC = "/atendimentos-classificados-nao-enc";
	
	public static final String QTD_ATENDIMENTO_RECEBIDOS = "/atendimentos-recebidos";
	public static final String QTD_ATENDIMENTOS_MES = "/totalAtendimentosMes";
	public static final String QTD_ATENDIMENTOS_MES_CONCLUIDOS = "/totalAtendimentosMesConcluidos";
	public static final String QTD_ATENDIMENTOS_NATUREZA = "/totalAtendimentosNatureza";
	public static final String QTD_ATENDIMENTOS_NATUREZA_DASHBOARD = "/totalAtendimentosNaturezaDashboard";
	public static final String QTD_ATENDIMENTOS_SECRETARIA = "/totalAtendimentosSecretaria";
	public static final String PARAMETRO = "/{codigo}";
	public static final String PARAMETRO_ENCAMINHAMENTO = "/enc/{codigoEncaminhamento}";
	public static final String CANCELAR_MANIFESTACAO = "/cancelar/{codigo}";
	public static final String CONSULTA = "/consulta";
	public static final String CONSULTA_ATENDIMENTO = "/consultaAtendimento";
	public static final String NOVO_ATENDIMENTO = "/novo-atendimento";
	public static final String LISTAR_HISTORICO = "/historico-usuario";
	public static final String PESQUISAR_PROTOCOLO = "/pesquisar-protocolo";
	public static final String CONECTA_RECIFE = "/conecta-recife";
	public static final String REABRIR_ATENDIMENTO = "/reabrir-atendimento";
	public static final String PESQUISA_SATISFACAO = "/pesquisaSatisfacao";
	public static final String PESQUISA_SATISFACAO_EMAIL = "/pesquisa-satisfacao/{parametro}";
	public static final String QTD_ATENDIMENTOS_PAINEL_OUV = "/totalAtendimentosPainelOuv";
	public static final String QTD_ATENDIMENTOS_ASSUNTO = "/totalAtendimentosAssunto";
	public static final String QTD_ATENDIMENTOS_RESOLUTIVIDADE = "/totalAtendimentosResolutividade";
	public static final String QTD_ATENDIMENTOS = "/totalAtendimentos";
	public static final String AVALIACAO_PESQUISA_SATISFACAO = "/avaliacao-pesquisa-satisfacao";
	public static final String QTD_ATENDIMENTOS_ORIGEM_MANIFESTACAO = "/totalAtendimentosOrigemManifestacao";
	public static final String QTD_ATENDIMENTOS_SETOR_NATUREZA = "/totalAtendimentosSetorNatureza";
	public static final String QTD_ATENDIMENTOS_CLASSIF_NAO_ENCAMINHADOS = "/atendimentos-status";
	public static final String QTD_ATENDIMENTOS_NAO_VENCIDOS = "/atendimentos-nao-vencidos";
	public static final String QTD_ATENDIMENTOS_VENCIDOS = "/atendimentos-vencidos";
	
	
	
	@Autowired 
	AtendimentoRepository atendimentoRepository;
	
	@Autowired
	AtendimentoService atendimentoService;
	
	@Autowired
	AtendimentoMapper atendimentoMapper;
	
	@Autowired
	GconSecurity gconSecurity;
	
	@ApiOperation(value = "Quantidade de atendimentos do mês por natureza", nickname = QTD_ATENDIMENTOS_NATUREZA_DASHBOARD)
	@GetMapping(path = QTD_ATENDIMENTOS_NATUREZA_DASHBOARD, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getQtdTotalAtendimentosNaturezaMes(Long orgao, Integer mes, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosNaturezaDashboard(orgao, mes, ano);
	}
	
	@ApiOperation(value = "Quantidade de atendimentos por assunto", nickname = QTD_ATENDIMENTOS_ASSUNTO)
	@GetMapping(path = QTD_ATENDIMENTOS_ASSUNTO, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getQtdTotalAtendimentosAssunto(Long orgao, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosAssunto(orgao, 2, ano);
	}

	@ApiOperation(value = "Quantidade de atendimentos por resolutividade", nickname = QTD_ATENDIMENTOS_RESOLUTIVIDADE)
	@GetMapping(path = QTD_ATENDIMENTOS_RESOLUTIVIDADE, produces = APPLICATION_JSON_VALUE)
	public List<DadosGrafico> getQtdTotalAtendimentosResolutividade(Long orgao, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosResolutividade(orgao, ano);
	}
	
	@ApiOperation(value = "Avaliação da pesquisa de satisfação", nickname = AVALIACAO_PESQUISA_SATISFACAO)
	@GetMapping(path = AVALIACAO_PESQUISA_SATISFACAO, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getAvaliacaoPesquisaSatisfacao(Long orgao, Integer ano) {
		return atendimentoRepository.getQtdPainelPesquisaSatisfacao(orgao, ano);
	}
	
	@ApiOperation(value = "Quantidade de atendimentos do mês por setor e natureza", nickname = QTD_ATENDIMENTOS_SETOR_NATUREZA)
	@GetMapping(path = QTD_ATENDIMENTOS_SETOR_NATUREZA, produces = APPLICATION_JSON_VALUE)
	public StackedColumn getQtdTotalAtendimentosSetorNatureza(Long orgao) {
		return atendimentoRepository.getQtdAtendimentosSecretariaNatureza(orgao, 2, 0, LocalDate.now().getYear());
	}
	
	@ApiOperation(value = "Quantidade de atendimentos anual", nickname = QTD_ATENDIMENTOS)
	@GetMapping(path = QTD_ATENDIMENTOS, produces = APPLICATION_JSON_VALUE)
	public List<DadosGrafico> getQtdTotalAtendimentosAnual(Long orgao, Integer ano) {
		return atendimentoRepository.getQtdAtendimentos(orgao, ano);
	}
	
	@ApiOperation(value = "Quantidade de atendimentos por origem de manifestação", nickname = QTD_ATENDIMENTOS_ORIGEM_MANIFESTACAO)
	@GetMapping(path = QTD_ATENDIMENTOS_ORIGEM_MANIFESTACAO, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getQtdAtendimentosOrigemManifestacao(Long orgao) {
		return atendimentoRepository.getQtdPainelOrigemManifestacao(orgao, LocalDate.now().getYear());
	}
	
	@ApiOperation(value = "Quantidade de atendimentos concluidos anual", nickname = QTD_ATENDIMENTOS_PAINEL_OUV)
	@GetMapping(path = QTD_ATENDIMENTOS_PAINEL_OUV, produces = APPLICATION_JSON_VALUE)
	public PainelOuvidoria getQtdTotalAtendimentosMesConcluidos(Long orgao, Integer ano) {
		return atendimentoService.getPainel(orgao, ano);
	}

	
	@ApiOperation(value = "Enviar uma atendimento", nickname = PESQUISA_SATISFACAO)
	@PostMapping(path = PESQUISA_SATISFACAO, produces = APPLICATION_JSON_VALUE)
	public Boolean salvarPesquisaSatisfacao(@Valid @RequestBody PesquisaSatisfacaoRequest request) {
		return atendimentoService.getSalvarPesquisaSatisfacao(request);
	}

	@ApiOperation(value = "Lista de novos atendimentos", nickname = NOVO_ATENDIMENTO)
	@GetMapping(path = NOVO_ATENDIMENTO, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse novoAtendimento() {
		LOGGER.info("Novo atendimento");
		return new AtendimentoResponse(atendimentoService.novoAtendimento());
	}

	@ApiOperation(value = "Quantidade de novos atendimentos", nickname = LISTAR_NOVOS)
	@GetMapping(path = LISTAR_NOVOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdNovosAtendimentos(Long orgao) {
		return atendimentoRepository.getNovosAtendimentos(orgao);
	}

	@ApiOperation(value = "Quantidade de atendimentos classificados e não encaminhados", nickname = QTD_ATENDIMENTOS_CLASSIF_NAO_ENCAMINHADOS)
	@GetMapping(path = QTD_ATENDIMENTOS_CLASSIF_NAO_ENCAMINHADOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdAtendimentosClassifNaoEnc(Long orgao) {
		return atendimentoRepository.getAtendimentosClassifNaoEnc(orgao);
	}

	@ApiOperation(value = "Lista de novos atendimentos", nickname = QTD_NOVOS_ATENDIMENTOS)
	@GetMapping(path = QTD_NOVOS_ATENDIMENTOS, produces = APPLICATION_JSON_VALUE)
	public ListaAtendimentoResponse getListaNovosAtendimentos(Long orgao, Pageable page) {
		return new ListaAtendimentoResponse(atendimentoService.getListaNovosAtendimentos(orgao, page));
	}

	@ApiOperation(value = "Lista de atendimentos classificados e não encaminhados", nickname = LISTA_ATENDIMENTOS_CLASSIF_NAO_ENC)
	@GetMapping(path = LISTA_ATENDIMENTOS_CLASSIF_NAO_ENC, produces = APPLICATION_JSON_VALUE)
	public ListaAtendimentoResponse getListaAtendimentosClassifNaoEnc(Long orgao, Pageable page) {
		return new ListaAtendimentoResponse(atendimentoService.getListaAtendimentosClassfNaoEnc(orgao, page));
	}

	@ApiOperation(value = "Lista de novos atendimentos", nickname = QTD_ATENDIMENTO_RECEBIDOS)
	@GetMapping(path = QTD_ATENDIMENTO_RECEBIDOS, produces = APPLICATION_JSON_VALUE)
	public ListaAtendimentosRecebidosResponse getListaAtendimentosRecebidos(Long orgao, Pageable page) {
		return new ListaAtendimentosRecebidosResponse(atendimentoService.getListaAtendimentosRecebidos(orgao, page));
	}

	@ApiOperation(value = "Quantidade de atendimentos do mês atual", nickname = QTD_ATENDIMENTOS_MES)
	@GetMapping(path = QTD_ATENDIMENTOS_MES, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdTotalAtendimentosMes(Long orgao, Integer mes, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosMes(orgao, 2, mes, ano);
	}

	@ApiOperation(value = "Quantidade de atendimentos não vencidos do mês", nickname = QTD_ATENDIMENTOS_NAO_VENCIDOS)
	@GetMapping(path = QTD_ATENDIMENTOS_NAO_VENCIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdTotalAtendimentosNaoVencidos(Long orgao, Integer mes, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosNaoVencidos(orgao, 2, mes, ano, 0);
	}

	@ApiOperation(value = "Quantidade de atendimentos do mês vencidos", nickname = QTD_ATENDIMENTOS_VENCIDOS)
	@GetMapping(path = QTD_ATENDIMENTOS_VENCIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdTotalAtendimentosVencidos(Long orgao, Integer mes, Integer ano) {
		return atendimentoRepository.getQtdAtendimentosVencidos(orgao, 2, mes, ano, 0);
	}

	@ApiOperation(value = "Quantidade de atendimentos concluídos no mês atual", nickname = QTD_ATENDIMENTOS_MES_CONCLUIDOS)
	@GetMapping(path = QTD_ATENDIMENTOS_MES_CONCLUIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdTotalAtendimentosMesConcluidos(Long orgao) {
		return atendimentoRepository.getQtdAtendimentosMesConcluidos(orgao, 2, LocalDate.now().getMonthValue(), LocalDate.now().getYear(), 1);
	}

	@ApiOperation(value = "Quantidade de atendimentos do mês por natureza", nickname = QTD_ATENDIMENTOS_NATUREZA)
	@GetMapping(path = QTD_ATENDIMENTOS_NATUREZA, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getQtdTotalAtendimentosNatureza(Long orgao, Integer ano) {
		return atendimentoService.getPainelNatureza(orgao, ano);
	}


	@ApiOperation(value = "Quantidade de atendimentos do mês por secretaria", nickname = QTD_ATENDIMENTOS_SECRETARIA)
	@GetMapping(path = QTD_ATENDIMENTOS_SECRETARIA, produces = APPLICATION_JSON_VALUE)
	public List<AtendimentoNatureza> getQtdTotalAtendimentosSecretaria(Long orgao, Integer mes, Integer ano) {
		if (mes == 0) {
			return atendimentoRepository.getQtdAtendimentosSecretaria(orgao, 2, 0, ano);
		}
		return atendimentoRepository.getQtdAtendimentosSecretaria(orgao, 2, mes, ano);
	}
	

	@ApiOperation(value = "Obter uma atendimento para edição", nickname = PARAMETRO)
	@GetMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_ATENDIMENTO') and #oauth2.hasScope('write')")
	public AtendimentoResponse getEditarAtendimento(@PathVariable Long codigo) {
		return new AtendimentoResponse(atendimentoService.getAtendimento(codigo));
	}

	@ApiOperation(value = "Obter uma atendimento para tratar o encaminhamento", nickname = PARAMETRO_ENCAMINHAMENTO)
	@GetMapping(path = PARAMETRO_ENCAMINHAMENTO, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse buscarAtendimento(@PathVariable Long codigoEncaminhamento) {
		return new AtendimentoResponse(atendimentoService.getBuscarAtendimento(codigoEncaminhamento));
	}

	@ApiOperation(value = "Enviar uma atendimento", nickname = ATENDIMENTO)
	@PostMapping(produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse salvarAtendimento(@Valid @RequestBody AtendimentoRequest request) {
		return new AtendimentoResponse(atendimentoService.salvarAtendimento(request));
	}

	@ApiOperation(value = "Enviar uma atendimento", nickname = SITE)
	@PostMapping(path = SITE, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse salvarAtendimentoSite(@Valid @RequestBody AtendimentoRequest request) {
		return new AtendimentoResponse(atendimentoService.salvarAtendimentoSite(request));
	}

	@ApiOperation(value = "Enviar uma atendimento", nickname = CONECTA_RECIFE)
	@PostMapping(path = CONECTA_RECIFE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> salvarAtendimentoConecta(@Valid @RequestBody AtendimentoRequest request) {
	       return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoMapper.fromResponse(atendimentoService.salvarAtendimentoSite(request)));
	}

	@ApiOperation(value = "Concluir um atendimento", nickname = CONCLUIR_ATENDIMENTO)
	@PutMapping(path = CONCLUIR_ATENDIMENTO, produces = APPLICATION_JSON_VALUE)
	public void concluirAtendimento(@Valid @RequestBody AtendimentoConclusaoRequest request, Principal principal) {
		atendimentoService.concluirAtendimento(request, principal);
	}

	@ApiOperation(value = "Reabrir um atendimento", nickname = REABRIR_ATENDIMENTO)
	@PutMapping(path = REABRIR_ATENDIMENTO, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse reabrirAtendimento(@Valid @RequestBody AtendimentoRequest atendimento, Principal principal) {
		return new AtendimentoResponse(atendimentoService.reabrirAtendimento(atendimento.getId(), principal));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Excluir uma atendimento", nickname = PARAMETRO)
	@DeleteMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public void excluirAtendimento(@PathVariable Long codigo) {
		atendimentoRepository.deleteById(codigo);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Cancelar uma manifestação", nickname = CANCELAR_MANIFESTACAO)
	@DeleteMapping(path = CANCELAR_MANIFESTACAO, produces = APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_CANCELAR_ATENDIMENTO') and #oauth2.hasScope('write')")
	public void cancelarAtendimento(@PathVariable Long codigo) {
		atendimentoService.cancelarAtendimento(codigo);
	}

	@ApiOperation(value = "Atualizar uma atendimento", nickname = PARAMETRO)
	@PutMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse atualizarAtendimento(@PathVariable Long codigo, 
			@Valid @RequestBody AtendimentoRequest request) {
		return new AtendimentoResponse(atendimentoService.atualizarAtendimento(request));
	}
	

	@ApiOperation(value = "Obter uma atendimento pelo filtro", nickname = ATENDIMENTO)
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ListaAtendimentoResponse getPesquisaAtendimento(AtendimentoFilter filtro, Pageable page) {
		return new ListaAtendimentoResponse(atendimentoService.getPesquisaAtendimentoDescricao(gconSecurity.getOrgao(), filtro, page));
	}
	
	@ApiOperation(value = "Obter o atendimento", nickname = CONSULTA_ATENDIMENTO)
	@GetMapping(path = CONSULTA_ATENDIMENTO, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResumoAtendimentoResponse> getConsultaAtendimento(Long orgao, String cpf, String numero, String senha) {
	 List<ResumoAtendimentoDto> resumo = atendimentoService.getResumo(orgao, cpf, numero, senha);
	 if (!resumo.isEmpty()) {
		 ResumoAtendimentoResponse resumoAtendimento = new ResumoAtendimentoResponse(resumo.get(0));
		 return ResponseEntity.ok(resumoAtendimento); 
	 }else {
		 return ResponseEntity.notFound().build();
	 }
	}
	
	@ApiOperation(value = "Responder a pesquisa de satisfação enviada por email", nickname = PESQUISA_SATISFACAO_EMAIL)
	@GetMapping(path = PESQUISA_SATISFACAO_EMAIL, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<ResumoAtendimentoResponse> getPesquisaSatisfacaoEmail(@PathVariable("parametro") String parametro) {
		 List<ResumoAtendimentoDto> resumo = atendimentoService.getResumo(parametro);
		 if (!resumo.isEmpty()) {
			 ResumoAtendimentoResponse resumoAtendimento = new ResumoAtendimentoResponse(resumo.get(0));
			 return ResponseEntity.ok(resumoAtendimento); 
		 }else {
			 return ResponseEntity.notFound().build();
		 }
	}
	
	
	
	@ApiOperation(value = "Obter lista de historico do usuario", nickname = LISTAR)
	@GetMapping(path = LISTAR_HISTORICO, produces = APPLICATION_JSON_VALUE)
	public ListaHistoricoUsuarioResponse getListaHistoricoUsuario(Long codigoAtendimento) {
		return new ListaHistoricoUsuarioResponse(atendimentoService.getHistoricoUsuario(codigoAtendimento));
	}	
	
	
	@ApiOperation(value = "Obter uma atendimento para edição", nickname = PARAMETRO)
	@GetMapping(path = PESQUISAR_PROTOCOLO, produces = APPLICATION_JSON_VALUE)
	public AtendimentoResponse getPesquisarProtocolo(String numeroProtocolo) {
		return new AtendimentoResponse(atendimentoService.getPesquisaProtocolo(numeroProtocolo));
	}
	

}

package br.com.multiinovacoes.gcon.controller;

import static br.com.multiinovacoes.gcon.controller.EncaminhamentoController.ENCAMINHAMENTO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.model.dto.EncaminhamentoRespostaSetorDto;
import br.com.multiinovacoes.gcon.model.mapper.EncaminhamentoMapper;
import br.com.multiinovacoes.gcon.model.request.EncaminhamentoRequest;
import br.com.multiinovacoes.gcon.model.response.EncaminhamentoResponse;
import br.com.multiinovacoes.gcon.model.response.ListaEncaminhamentoResponse;
import br.com.multiinovacoes.gcon.model.response.ModeloDocumentoResponse;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamento;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamentoTratar;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoRepository;
import br.com.multiinovacoes.gcon.service.EncaminhamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Api(value = "Encaminhamento", produces = APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(path = ENCAMINHAMENTO	)
public class EncaminhamentoController {
	
	public static final String ENCAMINHAMENTO = "/encaminhamentos";
	public static final String ENCAMINHAMENTOS_RECEBIDOS = "/encaminhamentosRecebidos";
	public static final String ENCAMINHAMENTO_ABERTO = "/encaminhamento-aberto";
	public static final String ENCAMINHAMENTO_TRATAR = "/encaminhamento-tratar";
	public static final String LISTAR = "/listar/atendimento/{codigo}";
	public static final String LISTAR_ENC_ABERTOS = "/listarAbertos/atendimento/{codigo}";
	public static final String PARAMETRO = "/{codigo}";
	public static final String RESPOSTA_SATISFAZ = "/listar/encaminhamento/resposta/{codigo}";
	public static final String PARAMETRO_ENVIADO = "/resposta-setor/{parametro}";
	public static final String QTD_ENCAMINHAMENTOS_ENVIADOS_VENCIDOS = "/listar-enc-vencidos";
	public static final String QTD_ENCAMINHAMENTOS_ENVIADOS_NAO_VENCIDOS = "/listar-enc-nao-vencidos";
	public static final String LISTA_SETOR_ENCAMINHAMENTOS_VENCIDOS = "/listar-setor-enc-vencidos";
	public static final String LISTA_SETOR_ENCAMINHAMENTOS_ENVIADOS = "/listar-setor-enc-enviados";
	

	
	@Autowired
	EncaminhamentoRepository encaminhamentoRepository;
	
	@Autowired
	EncaminhamentoService encaminhamentoService;
	
	@Autowired
	EncaminhamentoMapper encaminhamentoMapper;
	
	@ApiOperation(value = "Quantidade de encaminhamentos recebidos", nickname = PARAMETRO)
	@GetMapping(path = ENCAMINHAMENTOS_RECEBIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdEncaminhamentosRecebidos(Long orgao) {
		return encaminhamentoRepository.getEncaminhamentosRecebidos(orgao);
	}
	
	@ApiOperation(value = "Obter lista de encaminhamentos", nickname = LISTAR)
	@GetMapping(path = LISTAR, produces = APPLICATION_JSON_VALUE)
	public ListaEncaminhamentoResponse getListaEncaminhamentos(@PathVariable Long codigo) {
		return new ListaEncaminhamentoResponse(encaminhamentoService.listaEncaminhamentoComResposta(codigo));
	}

	@ApiOperation(value = "Obter lista de encaminhamentos abertos por setor", nickname = ENCAMINHAMENTO_ABERTO)
	@GetMapping(path = ENCAMINHAMENTO_ABERTO, produces = APPLICATION_JSON_VALUE)
	public  List<DadosEncaminhamento> getListaEncaminhamentosAbertosSetor(String orgao, String setor, Date dataInicial, Date dataFinal, String prazo, String despacho15diasatras) {
		return encaminhamentoRepository.encaminhamentosNaoRespondidos(Long.parseLong(orgao), Long.parseLong(setor), dataInicial, dataFinal, prazo, despacho15diasatras);
	}
	
//	@ApiOperation(value = "Obter lista de encaminhamentos abertos por setor", nickname = ENCAMINHAMENTO_ABERTO)
//	@GetMapping(path = ENCAMINHAMENTO_ABERTO, produces = APPLICATION_JSON_VALUE)
//	public  Page<DadosEncaminhamento> getListaEncaminhamentosAbertosSetor(String orgao, String setor, Date dataInicial, Date dataFinal, String prazo, String despacho15diasatras, Pageable page) {
//		return encaminhamentoRepository.encaminhamentosNaoRespondidos(Long.parseLong(orgao), Long.parseLong(setor), dataInicial, dataFinal, prazo, despacho15diasatras, page);
//	}


	@ApiOperation(value = "Obter lista de encaminhamentos do setor para tratar", nickname = ENCAMINHAMENTO_TRATAR)
	@GetMapping(path = ENCAMINHAMENTO_TRATAR, produces = APPLICATION_JSON_VALUE)
	public  List<DadosEncaminhamentoTratar> getListaEncaminhamentosTratar(String orgao, String setor, Integer ano) {
		return encaminhamentoRepository.tratarEncaminhamentos(Long.parseLong(orgao), Long.parseLong(setor), ano);
	}

	@ApiOperation(value = "Obter resposta de conclusão do executor", nickname = LISTAR)
	@GetMapping(path = RESPOSTA_SATISFAZ, produces = APPLICATION_JSON_VALUE)
	public ModeloDocumentoResponse getRespostaSatisfaz(@PathVariable Long codigo) {
		return new ModeloDocumentoResponse(encaminhamentoService.listaEncaminhamentoAtivosComRespostaSatisfaz(codigo)); 
	}

	@ApiOperation(value = "Obter lista de encaminhamentos abertos", nickname = LISTAR_ENC_ABERTOS)
	@GetMapping(path = LISTAR_ENC_ABERTOS, produces = APPLICATION_JSON_VALUE)
	public ListaEncaminhamentoResponse getListaEncaminhamentosAbertos(@PathVariable Long codigo) {
		return new ListaEncaminhamentoResponse(encaminhamentoMapper.fromResponseList(encaminhamentoRepository.findByAtendimentoAndStatusAndCancelado(codigo, Encaminhamento.STATUS_ABERTO, 0)));
	}

	@ApiOperation(value = "Obter uma encaminhamento para edição", nickname = PARAMETRO)
	@GetMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public EncaminhamentoResponse getEditarEncaminhamento(@PathVariable Long codigo) {
		return new EncaminhamentoResponse(encaminhamentoService.getEncaminhamento(codigo));
	}
	
	@ApiOperation(value = "Enviar uma encaminhamento", nickname = ENCAMINHAMENTO) 
	@PostMapping(produces = APPLICATION_JSON_VALUE)
	public void salvarEncaminhamento(@Valid @RequestBody EncaminhamentoRequest request) {
		encaminhamentoService.salvarEncaminhamento(request);
	}


	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Excluir uma encaminhamento", nickname = PARAMETRO)
	@DeleteMapping(path = PARAMETRO, produces = APPLICATION_JSON_VALUE)
	public void excluirEncaminhamento(@PathVariable Long codigo) {
		encaminhamentoService.excluir(codigo);
	}
	
	@ApiOperation(value = "Obter lista de encaminhamentos", nickname = PARAMETRO_ENVIADO)
	@GetMapping(path = PARAMETRO_ENVIADO, produces = APPLICATION_JSON_VALUE)
	public EncaminhamentoRespostaSetorDto getEncaminhamentoEnviado(@PathVariable("parametro") String parametro) {
		return encaminhamentoService.getEncaminhamentoRespostaSetor(parametro);
	}
	
	@ApiOperation(value = "Quantidade de atendimentos enviados vencidos", nickname = QTD_ENCAMINHAMENTOS_ENVIADOS_VENCIDOS)
	@GetMapping(path = QTD_ENCAMINHAMENTOS_ENVIADOS_VENCIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdEncaminhamentosEnviadosVencidos(Long orgao, Integer mes, Integer ano) {
		return encaminhamentoRepository.getEncaminhamentosVencidos(orgao, ano, mes);
	}
	
	@ApiOperation(value = "Quantidade de atendimentos enviados não vencidos", nickname = QTD_ENCAMINHAMENTOS_ENVIADOS_NAO_VENCIDOS)
	@GetMapping(path = QTD_ENCAMINHAMENTOS_ENVIADOS_NAO_VENCIDOS, produces = APPLICATION_JSON_VALUE)
	public Integer getQtdEncaminhamentosEnviadosNaoVencidos(String orgao, Integer mes, Integer ano) {
		return encaminhamentoRepository.getEncaminhamentosNaoVencidos(Long.parseLong(orgao), ano, mes);
	}	
	
	@ApiOperation(value = "Grafico de encaminhados vencidos", nickname = LISTA_SETOR_ENCAMINHAMENTOS_VENCIDOS)
	@GetMapping(path = LISTA_SETOR_ENCAMINHAMENTOS_VENCIDOS, produces = APPLICATION_JSON_VALUE)
	public List<DadosGrafico> getEstatisticaEncaminhamentoVencido(Long orgao, Integer mes, Integer ano) {
		return encaminhamentoRepository.getEstatisticaEncaminhamentoVencido(orgao, mes, ano);
	}

	@ApiOperation(value = "Grafico de encaminhados enviados", nickname = LISTA_SETOR_ENCAMINHAMENTOS_ENVIADOS)
	@GetMapping(path = LISTA_SETOR_ENCAMINHAMENTOS_ENVIADOS, produces = APPLICATION_JSON_VALUE)
	public List<DadosGrafico> getEstatisticaEncaminhamentoEnviados(Long orgao, Integer mes, Integer ano) {
		return encaminhamentoRepository.getEstatisticaEncaminhamentoEnviado(orgao, mes, ano);
	}
	
	
	@GetMapping(path = "/ajustar-tempo", produces = APPLICATION_JSON_VALUE)
	public void ajustarTempo() {
		encaminhamentoService.ajustar();
	}

	
}

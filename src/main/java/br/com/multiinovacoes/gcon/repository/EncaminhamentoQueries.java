package br.com.multiinovacoes.gcon.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamento;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamentoTratar;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.DetalheAtendimentoArea;

public interface EncaminhamentoQueries {
	
	public List<Encaminhamento> consultaAtendimento(Long codigoAtendimento);
	
	public Encaminhamento consultaEncaminhamento(Long codigoEncaminhamento);
	
	public String consultar(String campo, Long id);
	
	public List<Encaminhamento> consultaEncaminhamentosRecebidos(Long orgao, Pageable pageable, Integer totalRegistros);
	
	public List<DadosEncaminhamento> encaminhamentosNaoRespondidos(Long orgao, Long setor, Date dataInicial, Date dataFinal, String prazo, String despacho15diasatras);
	
	public List<DadosEncaminhamentoTratar> tratarEncaminhamentos(Long orgao, Long setor, Integer ano);
	
	public List<DadosGrafico> getEstatisticaEncaminhamentoVencido(Long orgao, Integer mes, Integer ano);
	
	public List<DadosGrafico> getEstatisticaEncaminhamentoEnviado(Long orgao, Integer mes, Integer ano);
	
	public List<DetalheAtendimentoArea> getEncaminhamentoSetor(Long orgao, Date dataInicial, Date dataFinal, String area, String status);
	
	public Integer getEncaminhamentosEnviados(Long orgao, Long area, String dataInicial, String dataFinal);


}

package br.com.multiinovacoes.gcon.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.multiinovacoes.gcon.graphics.StackedColumn;
import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.dto.HistoricoUsuarioDto;
import br.com.multiinovacoes.gcon.model.filter.AtendimentoFilter;
import br.com.multiinovacoes.gcon.report.AtendimentoNatureza;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.DadosRelatorio;
import br.com.multiinovacoes.gcon.report.DetalheAtendimentoArea;
import br.com.multiinovacoes.gcon.report.RelatorioPeriodo;

public interface AtendimentoQueries {
	
	public Page<Atendimento> filtrar(Long orgao, AtendimentoFilter filtro, Pageable page);
	
	public String consultar(String campo, Long id, Long orgao);
	
	public Atendimento consultaAtendimento(Long codigoAtendimento);
	
	public Atendimento pesquisarNumeroProtocolo(String numeroProtocolo);
	
//	@Query("SELECT n.descricao, count(a.id) as total FROM Atendimento a inner join Natureza n on a.natureza = n.id where a.orgao = ?1 and a.status <> ?2 "
//			+ "and month(a.dataCriacao) = ?3 and year(a.dataCriacao) = ?4 group by n.descricao order by total desc")
	public List<AtendimentoNatureza> getQtdAtendimentosNatureza(Long orgao, Integer status, Integer mes, Integer ano);
	
	public List<AtendimentoNatureza> getQtdAtendimentosSecretaria(Long orgao, Integer status, Integer mes, Integer ano);
	
	
	public List<DetalheAtendimentoArea> getAtendimentoArea(Long orgao, Date dataInicial, Date dataFinal, String area, String status);
	
	public List<DetalheAtendimentoArea> getAtendimentoArea(Long orgao, Date dataInicial, Date dataFinal, String area);
	
	public List<RelatorioPeriodo> getAtendimentoPeriodo(Long orgao, Date dataInicial, Date dataFinal);
	
	public Page<Atendimento> consultaNovasManifestacoes(Long orgao, Pageable page, Integer totalRegistros);
	
	public Page<Atendimento> consultaAtendimentosClassifNaoEnc(Long orgao, Pageable page, Integer totalRegistros);
	
	//public Page<Atendimento> encaminhamentosRecebidos(Long orgao, Pageable page, Integer totalRegistros);
	
	public Integer qtdHistoricoUsuario(long codigoOrgao, Long tipoDocumento, String documento, String email, long numero);
	
	public List<HistoricoUsuarioDto> historicoUsuario(long codigoOrgao, Long tipoDocumento, String documento, String email, long numero);
	
	public List<AtendimentoNatureza> getQtdAtendimentosNatureza(Long orgao, Integer ano, Integer totalAtendimentos);
	
	public List<AtendimentoNatureza> getQtdAtendimentosNaturezaDashboard(Long orgao, Integer mes, Integer ano);
	
	public List<AtendimentoNatureza> getQtdAtendimentosAssunto(Long orgao, Integer status, Integer ano);
	
	public List<DadosGrafico> getQtdAtendimentosResolutividade(Long orgao, Integer ano);
	
	public List<DadosGrafico> getQtdAtendimentos(Long orgao, Integer ano);
	
	public List<AtendimentoNatureza> getQtdPainelPesquisaSatisfacao(Long orgao, Integer ano);
	
	public List<AtendimentoNatureza> getQtdPainelOrigemManifestacao(Long orgao, Integer ano);
	
	public StackedColumn getQtdAtendimentosSecretariaNatureza(Long orgao, Integer status, Integer mes, Integer ano);
	
	public List<AtendimentoNatureza> getQtdAtendimentosNatureza(Long orgao, Integer status, Integer mes, Integer ano, Long area, Long natureza);
	
	public List<DetalheAtendimentoArea> getDetalheProdutividade(Long orgao, Date dataInicial, Date dataFinal, Long area, Long codigoUsuario);
	
	public DadosRelatorio getRelatorioAvaliacaoOuvidoria(Long orgao, Date dataInicial, Date dataFinal);



}

package br.com.multiinovacoes.gcon.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.request.FiltroRelatorioRequest;
import br.com.multiinovacoes.gcon.model.request.RelatorioGeralRequest;
import br.com.multiinovacoes.gcon.report.DadosComparativoPeriodo;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.DadosRelatorio;
import br.com.multiinovacoes.gcon.report.DadosRelatorioUsuario;
import br.com.multiinovacoes.gcon.report.EstatisticaPesquisaSatisfacao;
import br.com.multiinovacoes.gcon.report.RelatorioDetalhe;
import br.com.multiinovacoes.gcon.report.RelatorioPesquisaSatisfacao;
import br.com.multiinovacoes.gcon.report.RetornoRelatorioGeral;

@Repository
public interface RelatorioQueries {
	
	public List<DadosGrafico> getRelatorioNatureza(Long orgao, Date dataInicial, Date dataFinal);
	
	public Page<Atendimento>  getListaDetalhe(Long orgao, LocalDate dataInicial, LocalDate dataFinal, Long natureza, Pageable page, Integer totalRegistros);
	
	public List<DadosGrafico> getRelatorioAssunto(Long orgao, Date dataInicial, Date dataFinal, String assunto);
	
	public List<DadosGrafico> getRelatorioPriorizacao(Long orgao, Date dataInicial, Date dataFinal);
	
	public List<DadosGrafico> getRelatorioOrigem(Long orgao, Date dataInicial, Date dataFinal);
	
	public List<DadosGrafico> getRelatorioAreaAssunto(Long orgao, Date dataInicial, Date dataFinal);
	
	public List<DadosGrafico> getRelatorioTipoManifestante(Long orgao, Date dataInicial, Date dataFinal);
	
	public DadosRelatorio getRelatorioSecretaria(Long orgao, String dataInicial, String dataFinal, Long area, Integer totalEncaminhamentosEnviados);
	
	public DadosRelatorioUsuario getRelatorioUsuario(FiltroRelatorioRequest filtroRelatorioRequest);
	
	public List<DadosGrafico> getRelatorioMediaResposta(Long orgao, Date dataInicial, Date dataFinal);
	
	public DadosRelatorio getRelatorioEficienciaOuvidoria(Long orgao, Date dataInicial, Date dataFinal);
	
	public DadosRelatorio getRelatorioProdutividadeCallCenter(Long orgao, Date dataInicial, Date dataFinal, Long area);
	
	public DadosComparativoPeriodo getRelatorioComparativo(Long orgao, Date dataInicial, Date dataFinal, Date dataInicialAnterior, Date dataFinalAnterior);
	
	public List<RelatorioDetalhe>  getListaDetalheAssunto(Long orgao, Date dataInicial, Date dataFinal, Long area, Long assunto);
	
	public List<RelatorioDetalhe>  getListaDetalheAssuntoAgrupado(Long orgao, Date dataInicial, Date dataFinal, String assunto);
	
	public List<RelatorioDetalhe>  getListaDetalheNatureza(Long orgao, Date dataInicial, Date dataFinal, Long area, Long natureza);
	
	public List<RelatorioDetalhe>  getListaDetalheResolutividade(Long orgao, Date dataInicial, Date dataFinal, Long area, Integer statusResolutividade);
	
	public RetornoRelatorioGeral getListaGeral(RelatorioGeralRequest request);
	
	public RelatorioPesquisaSatisfacao  getEstatisticaPesqSatisfacao(Long orgao, Date dataInicial, Date dataFinal, Long area);
	
	public EstatisticaPesquisaSatisfacao getListaEstatisticaPerguntas(Long orgao, Date dataInicial, Date dataFinal, Long pergunta, Long area);
	
	

}

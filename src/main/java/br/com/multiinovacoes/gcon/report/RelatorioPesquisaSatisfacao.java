package br.com.multiinovacoes.gcon.report;

import java.util.List;

public class RelatorioPesquisaSatisfacao {
	
	private String area;
	
	private Integer qtdManifestacoes;
	
	private Integer qtdManifestacoesConcluidas;
	
	private Integer qtdPesquisaSatisfacaoRespondida;
	
	private List<DadosGrafico> listaDadosTipoManifestante;
	
	private List<DadosGrafico> listaTiposRespostasPesquisa;
	
	private List<EstatisticaPesquisaSatisfacao> listaEstatistica;
	
	
	
	
	public RelatorioPesquisaSatisfacao() {
	}

	public RelatorioPesquisaSatisfacao(Integer qtdManifestacoes, Integer qtdManifestacoesConcluidas,
			Integer qtdPesquisaSatisfacaoRespondida, List<DadosGrafico> listaDadosTipoManifestante, 
			List<DadosGrafico> listaTiposRespostasPesquisa,
			List<EstatisticaPesquisaSatisfacao> listaEstatistica, String area) {
		super();
		this.qtdManifestacoes = qtdManifestacoes;
		this.qtdManifestacoesConcluidas = qtdManifestacoesConcluidas;
		this.qtdPesquisaSatisfacaoRespondida = qtdPesquisaSatisfacaoRespondida;
		this.listaDadosTipoManifestante = listaDadosTipoManifestante;
		this.listaTiposRespostasPesquisa = listaTiposRespostasPesquisa;
		this.listaEstatistica = listaEstatistica;
		this.area = area;
	}

	public Integer getQtdManifestacoes() {
		return qtdManifestacoes;
	}

	public void setQtdManifestacoes(Integer qtdManifestacoes) {
		this.qtdManifestacoes = qtdManifestacoes;
	}

	public Integer getQtdManifestacoesConcluidas() {
		return qtdManifestacoesConcluidas;
	}

	public void setQtdManifestacoesConcluidas(Integer qtdManifestacoesConcluidas) {
		this.qtdManifestacoesConcluidas = qtdManifestacoesConcluidas;
	}

	public Integer getQtdPesquisaSatisfacaoRespondida() {
		return qtdPesquisaSatisfacaoRespondida;
	}

	public void setQtdPesquisaSatisfacaoRespondida(Integer qtdPesquisaSatisfacaoRespondida) {
		this.qtdPesquisaSatisfacaoRespondida = qtdPesquisaSatisfacaoRespondida;
	}

	public List<DadosGrafico> getListaDadosTipoManifestante() {
		return listaDadosTipoManifestante;
	}

	public void setListaDadosTipoManifestante(List<DadosGrafico> listaDadosTipoManifestante) {
		this.listaDadosTipoManifestante = listaDadosTipoManifestante;
	}

	public List<DadosGrafico> getListaTiposRespostasPesquisa() {
		return listaTiposRespostasPesquisa;
	}

	public void setListaTiposRespostasPesquisa(List<DadosGrafico> listaTiposRespostasPesquisa) {
		this.listaTiposRespostasPesquisa = listaTiposRespostasPesquisa;
	}

	public List<EstatisticaPesquisaSatisfacao> getListaEstatistica() {
		return listaEstatistica;
	}

	public void setListaEstatistica(List<EstatisticaPesquisaSatisfacao> listaEstatistica) {
		this.listaEstatistica = listaEstatistica;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
	
	

}

package br.com.multiinovacoes.gcon.report;

import java.util.List;

public class EstatisticaPesquisaSatisfacao {
	
	private String nome;
	
	private String dataSource;
	
	private List<DadosGrafico> dados;

	public EstatisticaPesquisaSatisfacao(String nome, List<DadosGrafico> dados) {
		super();
		this.nome = nome;
		this.dados = dados;
	}
	
	

	public String getDataSource() {
		return dataSource;
	}



	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}



	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<DadosGrafico> getDados() {
		return dados;
	}

	public void setDados(List<DadosGrafico> dados) {
		this.dados = dados;
	}
	
	

}

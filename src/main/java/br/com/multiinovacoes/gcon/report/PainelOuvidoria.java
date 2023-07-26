package br.com.multiinovacoes.gcon.report;

public class PainelOuvidoria {
	
	private Integer totalAtendimentos;
	
	private Integer totalAtendimentosResolvidos;
	
	private Integer totalAtendimentosTramitacao;
	
	private Integer totalAtendimentosNovos;
	
	private Integer totalAtendimentosTramitacaoNaoVencidos;
	
	private Integer totalAtendimentosTramitacaoVencidos;
	
	private Double percentualResolvido;
	
	private Double percentualTramitacao;
	
	public PainelOuvidoria() {
	}

	public PainelOuvidoria(Integer totalAtendimentos, Integer totalAtendimentosResolvidos, Integer totalAtendimentosTramitacao, Double percentualResolvido,
			Double percentualTramitacao, Integer totalAtendimentosTramitacaoNaoVencidos, Integer totalAtendimentosTramitacaoVencidos, Integer totalAtendimentosNovos) {
		super();
		this.totalAtendimentos = totalAtendimentos;
		this.totalAtendimentosResolvidos = totalAtendimentosResolvidos;
		this.totalAtendimentosTramitacao = totalAtendimentosTramitacao;
		this.percentualResolvido = percentualResolvido;
		this.percentualTramitacao = percentualTramitacao;
		this.totalAtendimentosTramitacaoNaoVencidos = totalAtendimentosTramitacaoNaoVencidos;
		this.totalAtendimentosTramitacaoVencidos =totalAtendimentosTramitacaoVencidos;
		this.totalAtendimentosNovos =totalAtendimentosNovos;
		
	}

	public Integer getTotalAtendimentos() {
		return totalAtendimentos;
	}

	public void setTotalAtendimentos(Integer totalAtendimentos) {
		this.totalAtendimentos = totalAtendimentos;
	}

	public Integer getTotalAtendimentosResolvidos() {
		return totalAtendimentosResolvidos;
	}

	public void setTotalAtendimentosResolvidos(Integer totalAtendimentosResolvidos) {
		this.totalAtendimentosResolvidos = totalAtendimentosResolvidos;
	}

	public Integer getTotalAtendimentosTramitacao() {
		return totalAtendimentosTramitacao;
	}

	public void setTotalAtendimentosTramitacao(Integer totalAtendimentosTramitacao) {
		this.totalAtendimentosTramitacao = totalAtendimentosTramitacao;
	}

	public Double getPercentualResolvido() {
		return percentualResolvido;
	}

	public void setPercentualResolvido(Double percentualResolvido) {
		this.percentualResolvido = percentualResolvido;
	}

	public Double getPercentualTramitacao() {
		return percentualTramitacao;
	}

	public void setPercentualTramitacao(Double percentualTramitacao) {
		this.percentualTramitacao = percentualTramitacao;
	}

	public Integer getTotalAtendimentosTramitacaoNaoVencidos() {
		return totalAtendimentosTramitacaoNaoVencidos;
	}

	public void setTotalAtendimentosTramitacaoNaoVencidos(Integer totalAtendimentosTramitacaoNaoVencidos) {
		this.totalAtendimentosTramitacaoNaoVencidos = totalAtendimentosTramitacaoNaoVencidos;
	}

	public Integer getTotalAtendimentosTramitacaoVencidos() {
		return totalAtendimentosTramitacaoVencidos;
	}

	public void setTotalAtendimentosTramitacaoVencidos(Integer totalAtendimentosTramitacaoVencidos) {
		this.totalAtendimentosTramitacaoVencidos = totalAtendimentosTramitacaoVencidos;
	}

	public Integer getTotalAtendimentosNovos() {
		return totalAtendimentosNovos;
	}

	public void setTotalAtendimentosNovos(Integer totalAtendimentosNovos) {
		this.totalAtendimentosNovos = totalAtendimentosNovos;
	}
	
	
	

}

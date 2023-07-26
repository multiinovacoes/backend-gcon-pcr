package br.com.multiinovacoes.gcon.report;

public class RelatorioDetalhe {
	
	
	private String protocolo;

	private String area;

	private String assunto;

	private String natureza;
	
	private String descricaoFatos;

	private String dataCriacao;
	
	private String concluido;
	
	private String dataPrazo;
	
	private String dataEncaminhado;
	
	private String dataResposta;
	
	private String status;
	
	
public RelatorioDetalhe(
	String protocolo,
	String area,
	String assunto,
	String natureza,
	String descricaoFatos,
	String dataCriacao,
	String concluido
	) {
	this.protocolo = protocolo;
	this.area = area;
	this.assunto = assunto;
	this.natureza = natureza;
	this.descricaoFatos = descricaoFatos;
	this.dataCriacao = dataCriacao;
	this.concluido = concluido;
}

public RelatorioDetalhe(
		String protocolo,
		String area,
		String assunto,
		String natureza,
		String descricaoFatos,
		String dataEncaminhado,
		String dataPrazo,
		String dataResposta,
		String status
		) {
		this.protocolo = protocolo;
		this.area = area;
		this.assunto = assunto;
		this.natureza = natureza;
		this.descricaoFatos = descricaoFatos;
		this.dataEncaminhado = dataEncaminhado;
		this.dataPrazo = dataPrazo;
		this.dataResposta = dataResposta;
		this.status = status;
	}

public String getProtocolo() {
	return protocolo;
}


public void setProtocolo(String protocolo) {
	this.protocolo = protocolo;
}


public String getArea() {
	return area;
}


public void setArea(String area) {
	this.area = area;
}


public String getAssunto() {
	return assunto;
}


public void setAssunto(String assunto) {
	this.assunto = assunto;
}


public String getNatureza() {
	return natureza;
}


public void setNatureza(String natureza) {
	this.natureza = natureza;
}

public String getDescricaoFatos() {
	return descricaoFatos;
}


public void setDescricaoFatos(String descricaoFatos) {
	this.descricaoFatos = descricaoFatos;
}


public String getDataCriacao() {
	return dataCriacao;
}


public void setDataCriacao(String dataCriacao) {
	this.dataCriacao = dataCriacao;
}


public String getConcluido() {
	return concluido;
}


public void setConcluido(String concluido) {
	this.concluido = concluido;
}


public String getDataPrazo() {
	return dataPrazo;
}


public void setDataPrazo(String dataPrazo) {
	this.dataPrazo = dataPrazo;
}

public String getDataEncaminhado() {
	return dataEncaminhado;
}

public void setDataEncaminhado(String dataEncaminhado) {
	this.dataEncaminhado = dataEncaminhado;
}

public String getDataResposta() {
	return dataResposta;
}

public void setDataResposta(String dataResposta) {
	this.dataResposta = dataResposta;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}





	

}

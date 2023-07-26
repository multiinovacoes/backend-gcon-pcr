package br.com.multiinovacoes.gcon.model.request;

import java.util.Date;

public class FiltroRelatorioRequest {
	
	private Long orgao;
	
	private Date dataInicial; 
	
	private Date dataFinal;
	
	private Long usuario;
	
	private Boolean concluidas;
	private Boolean encaminhadas;
	private Boolean respostasParcial;
	private Boolean despacho;
	private Boolean alteradas;
	
	private Boolean prazoVencido;
	private Boolean prazoVencer;
	private Boolean despacho15diasatras;
	
	private Integer pagina;
	private Integer itensPorPagina;
	

	public Long getOrgao() {
		return orgao;
	}

	public void setOrgao(Long orgao) {
		this.orgao = orgao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public Boolean getConcluidas() {
		return concluidas;
	}

	public void setConcluidas(Boolean concluidas) {
		this.concluidas = concluidas;
	}

	public Boolean getEncaminhadas() {
		return encaminhadas;
	}

	public void setEncaminhadas(Boolean encaminhadas) {
		this.encaminhadas = encaminhadas;
	}

	public Boolean getRespostasParcial() {
		return respostasParcial;
	}

	public void setRespostasParcial(Boolean respostasParcial) {
		this.respostasParcial = respostasParcial;
	}

	public Boolean getDespacho() {
		return despacho;
	}

	public void setDespacho(Boolean despacho) {
		this.despacho = despacho;
	}

	public Boolean getAlteradas() {
		return alteradas;
	}

	public void setAlteradas(Boolean alteradas) {
		this.alteradas = alteradas;
	}

	public Boolean getPrazoVencido() {
		return prazoVencido;
	}

	public void setPrazoVencido(Boolean prazoVencido) {
		this.prazoVencido = prazoVencido;
	}

	public Boolean getPrazoVencer() {
		return prazoVencer;
	}

	public void setPrazoVencer(Boolean prazoVencer) {
		this.prazoVencer = prazoVencer;
	}

	public Boolean getDespacho15diasatras() {
		return despacho15diasatras;
	}

	public void setDespacho15diasatras(Boolean despacho15diasatras) {
		this.despacho15diasatras = despacho15diasatras;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public Integer getItensPorPagina() {
		return itensPorPagina;
	}

	public void setItensPorPagina(Integer itensPorPagina) {
		this.itensPorPagina = itensPorPagina;
	}




	

}

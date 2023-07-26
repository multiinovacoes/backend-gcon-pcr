package br.com.multiinovacoes.gcon.report;

import java.util.List;

public class DadosRelatorio {
	
	
	String area;
	
	List<DadosGrafico> listaResolutividade;
	
	List<DadosGrafico> listaNatureza;
	
	List<DadosGrafico> listaAssunto;
	
	List<DadosGrafico> lista;
	
	List<DadosGrafico> listaArea;
	
	List<DadosGrafico> listaPriorizacao;
	
	public DadosRelatorio() {
	}
	
	
	public DadosRelatorio(List<DadosGrafico> listaResolutividade, List<DadosGrafico> listaNatureza,
			List<DadosGrafico> listaArea, List<DadosGrafico> listaPriorizacao) {
		super();
		this.listaResolutividade = listaResolutividade;
		this.listaNatureza = listaNatureza;
		this.listaArea = listaArea;
		
	}

	public DadosRelatorio(String area, List<DadosGrafico> listaResolutividade, List<DadosGrafico> listaNatureza,
			List<DadosGrafico> listaAssunto) {
		super();
		this.area = area;
		this.listaResolutividade = listaResolutividade;
		this.listaNatureza = listaNatureza;
		this.listaAssunto = listaAssunto;
	}

	public DadosRelatorio(String area, List<DadosGrafico> lista) {
		super();
		this.area = area;
		this.lista = lista;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<DadosGrafico> getListaResolutividade() {
		return listaResolutividade;
	}

	public void setListaResolutividade(List<DadosGrafico> listaResolutividade) {
		this.listaResolutividade = listaResolutividade;
	}

	public List<DadosGrafico> getListaNatureza() {
		return listaNatureza;
	}

	public void setListaNatureza(List<DadosGrafico> listaNatureza) {
		this.listaNatureza = listaNatureza;
	}

	public List<DadosGrafico> getListaAssunto() {
		return listaAssunto;
	}

	public void setListaAssunto(List<DadosGrafico> listaAssunto) {
		this.listaAssunto = listaAssunto;
	}

	public List<DadosGrafico> getLista() {
		return lista;
	}

	public void setLista(List<DadosGrafico> lista) {
		this.lista = lista;
	}

	public List<DadosGrafico> getListaArea() {
		return listaArea;
	}

	public void setListaArea(List<DadosGrafico> listaArea) {
		this.listaArea = listaArea;
	}

	public List<DadosGrafico> getListaPriorizacao() {
		return listaPriorizacao;
	}

	public void setListaPriorizacao(List<DadosGrafico> listaPriorizacao) {
		this.listaPriorizacao = listaPriorizacao;
	}


	

}

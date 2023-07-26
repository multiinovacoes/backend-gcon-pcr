package br.com.multiinovacoes.gcon.model.dto;

import java.util.List;

import br.com.multiinovacoes.gcon.model.PerguntaSatisfacao;

public class PesquisaSatisfacaoTela {
	
	List<PerguntaSatisfacao> listaPergunta;
	
	public PesquisaSatisfacaoTela() {
	}

	public PesquisaSatisfacaoTela(List<PerguntaSatisfacao> listaPergunta) {
		super();
		this.listaPergunta = listaPergunta;
	}


	public List<PerguntaSatisfacao> getListaPergunta() {
		return listaPergunta;
	}

	public void setListaPergunta(List<PerguntaSatisfacao> listaPergunta) {
		this.listaPergunta = listaPergunta;
	}





	
}

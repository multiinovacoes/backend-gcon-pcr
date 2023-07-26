package br.com.multiinovacoes.gcon.model.request;

import java.util.List;

import br.com.multiinovacoes.gcon.model.dto.PerguntaSatisfacaoDto;
import io.swagger.annotations.ApiModelProperty;

public class PesquisaSatisfacaoRequest {
	
	@ApiModelProperty(value = "Id do atendimento")
	private Long idAtendimento;
	
	private Long orgao;
	
	private Integer meioComunicacaoRespPesquisa;

	private List<PerguntaSatisfacaoDto> listaPergunta;

	public List<PerguntaSatisfacaoDto> getListaPergunta() {
		return listaPergunta;
	}

	public Long getOrgao() {
		return orgao;
	}

	public void setOrgao(Long orgao) {
		this.orgao = orgao;
	}

	public void setListaPergunta(List<PerguntaSatisfacaoDto> listaPergunta) {
		this.listaPergunta = listaPergunta;
	}

	public Long getIdAtendimento() {
		return idAtendimento;
	}

	public void setIdAtendimento(Long idAtendimento) {
		this.idAtendimento = idAtendimento;
	}

	public Integer getMeioComunicacaoRespPesquisa() {
		return meioComunicacaoRespPesquisa;
	}

	public void setMeioComunicacaoRespPesquisa(Integer meioComunicacaoRespPesquisa) {
		this.meioComunicacaoRespPesquisa = meioComunicacaoRespPesquisa;
	}
	
	

	

}

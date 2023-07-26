package br.com.multiinovacoes.gcon.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.multiinovacoes.gcon.model.dto.PesquisaSatisfacaoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude
@ApiModel("Lista Bairro Response")
public class ListaPesquisaSatisfacaoResponse {
	
	@ApiModelProperty(value = "Lista de de Pesquisa de Satisfação")
	private List<PesquisaSatisfacaoDto> pesquisaSatisfacaoDtoList;
	
	public ListaPesquisaSatisfacaoResponse(List<PesquisaSatisfacaoDto> pesquisaSatisfacaoDtoList) {
		this.pesquisaSatisfacaoDtoList = pesquisaSatisfacaoDtoList;
	}

	public List<PesquisaSatisfacaoDto> getPesquisaSatisfacaoDtoList() {
		return pesquisaSatisfacaoDtoList;
	}

	public void setPesquisaSatisfacaoDtoList(List<PesquisaSatisfacaoDto> pesquisaSatisfacaoDtoList) {
		this.pesquisaSatisfacaoDtoList = pesquisaSatisfacaoDtoList;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	


}

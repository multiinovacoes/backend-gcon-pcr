package br.com.multiinovacoes.gcon.model.response;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.multiinovacoes.gcon.model.ModeloListaEncaminhamentoRecebidos;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude
@ApiModel("Lista Atendimento Recebidos Response")
public class ListaAtendimentosRecebidosResponse {
	
	@ApiModelProperty(value = "Lista de Atendimentos")
	private Page<ModeloListaEncaminhamentoRecebidos> modeloListaAtendimentosRecebidos;
	
	public ListaAtendimentosRecebidosResponse(Page<ModeloListaEncaminhamentoRecebidos> modeloListaAtendimentosRecebidos) {
		this.modeloListaAtendimentosRecebidos = modeloListaAtendimentosRecebidos;
	}


	public Page<ModeloListaEncaminhamentoRecebidos> getModeloListaAtendimentosRecebidos() {
		return modeloListaAtendimentosRecebidos;
	}


	public void setModeloListaAtendimentosRecebidos(
			Page<ModeloListaEncaminhamentoRecebidos> modeloListaAtendimentosRecebidos) {
		this.modeloListaAtendimentosRecebidos = modeloListaAtendimentosRecebidos;
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

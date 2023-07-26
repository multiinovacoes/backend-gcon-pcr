package br.com.multiinovacoes.gcon.model.dto;

import io.swagger.annotations.ApiModelProperty;

public class OrigemManifestacaoDto {
	
	
	@ApiModelProperty(value = "Id da origem manifestação")	
	private Long id;

	@ApiModelProperty(value = "Descrição da origem manifestação")	
	private String descricao;
	
	@ApiModelProperty(value = "Status da origem manifestação")	
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

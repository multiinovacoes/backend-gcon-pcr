package br.com.multiinovacoes.gcon.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoManifestanteEnum {
	
	ANONIMO(0,"Anônimo"),
	CIDADAO(1, "Cidadão"),
	SERVIDOR(2, "Servidor");
	
	private Integer codigo;
	
	private String descricao;
	
	private static final Map<Integer, TipoManifestanteEnum> funcaoPegaDescricao = new HashMap<>();
	
	static {
		for (TipoManifestanteEnum tipoManifestanteEnum : TipoManifestanteEnum.values()) {
			funcaoPegaDescricao.put(tipoManifestanteEnum.getCodigo(), tipoManifestanteEnum);
		}
	}
	
	TipoManifestanteEnum(Integer codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoManifestanteEnum pegarDescricao(Integer codigo) {
		return funcaoPegaDescricao.get(codigo);
	}
	

}

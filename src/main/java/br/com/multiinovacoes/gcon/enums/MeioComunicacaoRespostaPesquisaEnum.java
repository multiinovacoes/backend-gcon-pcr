package br.com.multiinovacoes.gcon.enums;

import java.util.HashMap;
import java.util.Map;

public enum MeioComunicacaoRespostaPesquisaEnum {
	
	SITE(1,"Site"),
	FONE_0800(2, "0800"),
	EMAIL(3, "Email");
	
	private Integer codigo;
	
	private String descricao;
	
	private static final Map<Integer, MeioComunicacaoRespostaPesquisaEnum> funcaoPegaDescricao = new HashMap<>();
	
	static {
		for (MeioComunicacaoRespostaPesquisaEnum tipoManifestanteEnum : MeioComunicacaoRespostaPesquisaEnum.values()) {
			funcaoPegaDescricao.put(tipoManifestanteEnum.getCodigo(), tipoManifestanteEnum);
		}
	}
	
	MeioComunicacaoRespostaPesquisaEnum(Integer codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static MeioComunicacaoRespostaPesquisaEnum pegarDescricao(Integer codigo) {
		return funcaoPegaDescricao.get(codigo);
	}
	

}

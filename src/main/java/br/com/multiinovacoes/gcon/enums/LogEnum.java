package br.com.multiinovacoes.gcon.enums;

import java.util.HashMap;
import java.util.Map;

public enum LogEnum {
	
	ALTERADAS(1, "Alterou a manifestação"),
	CONCLUIDO(2, "Concluiu a manifestação"),
	ENCAMINHADO(3,"Encaminhou a manifestação"),
	DESPACHO(4, "Enviou despacho"),
	RESPOSTA_PARCIAL(5, "Enviou resposta parcial");
	
	private Integer codigo;
	
	private String descricao;
	
	private static final Map<Integer, LogEnum> funcaoPegaDescricao = new HashMap<>();
	
	static {
		for (LogEnum statusEnum : LogEnum.values()) {
			funcaoPegaDescricao.put(statusEnum.getCodigo(), statusEnum);
		}
	}
	
	LogEnum(Integer codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static LogEnum pegarDescricao(Integer codigo) {
		return funcaoPegaDescricao.get(codigo);
	}
	

}

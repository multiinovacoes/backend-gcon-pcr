package br.com.multiinovacoes.gcon.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.multiinovacoes.gcon.model.Atendimento;
import br.com.multiinovacoes.gcon.model.dto.AtendimentoDto;
import br.com.multiinovacoes.gcon.model.request.AtendimentoRequest;
import br.com.multiinovacoes.gcon.model.response.AtendimentoConectaResponse;

@Mapper(componentModel = "spring")
public interface AtendimentoMapper {

	 
	@Mapping(target = "sigilo", ignore = true)
	@Mapping(target = "camposDesabilitados", ignore = true)
	@Mapping(target = "habilitaBotaoConcluir", ignore = true)
	@Mapping(target = "manifestacaoClassificada", ignore = true)
	@Mapping(target = "manifestacaoConcluida", ignore = true)
	@Mapping(target = "identificado", ignore = true)
	@Mapping(target = "listaAnexoDto", ignore = true)
	@Mapping(target = "modeloResposta", ignore = true)
	@Mapping(target = "complementoDescricao", ignore = true)
	@Mapping(target = "descricaoQuando", ignore = true)
	@Mapping(target = "justificativaProrrogacao", ignore = true) 
	@Mapping(target = "manifestante", ignore = true)
	@Mapping(target = "modoResposta", ignore = true)
	@Mapping(target = "numero", ignore = true)
	@Mapping(target = "situacao", ignore = true)
	AtendimentoDto toDto(Atendimento area);   
	 
	AtendimentoDto fromAtendimentoDto(AtendimentoRequest request);
	
	@Mapping(target = "codigoModelo", ignore = true)
	@Mapping(target = "descricaoSolucao", ignore = true)
	@Mapping(target = "descricaoUsuario", ignore = true)
	@Mapping(target = "outroLocal", ignore = true)
	@Mapping(target = "providencia", ignore = true)
	@Mapping(target = "totalDias", ignore = true)
	Atendimento fromAtendimento(AtendimentoRequest request); 
	
	
	@Mapping(target = "codigoModelo", ignore = true)
	@Mapping(target = "outroLocal", ignore = true)
	@Mapping(target = "totalDias", ignore = true)
	@Mapping(target = "descricaoSolucao", ignore = true)
	@Mapping(target = "providencia", ignore = true)
	@Mapping(target = "descricaoUsuario", ignore = true)
	Atendimento toAtendimento(AtendimentoDto dto);   

	AtendimentoConectaResponse fromResponse(AtendimentoDto atendimentodto);

}

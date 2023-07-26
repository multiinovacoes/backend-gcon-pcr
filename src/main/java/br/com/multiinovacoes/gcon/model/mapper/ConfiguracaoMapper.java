package br.com.multiinovacoes.gcon.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.multiinovacoes.gcon.model.Configuracao;
import br.com.multiinovacoes.gcon.model.dto.ConfiguracaoDto;
import br.com.multiinovacoes.gcon.model.request.ConfiguracaoRequest;

@Mapper(componentModel = "spring")
public interface ConfiguracaoMapper {
	
	@Mapping(target = "numeroDiasProrrogarAtendimento", ignore = true)
	@Mapping(target = "respostaParcialPadraoProrrogacao", ignore = true)
	ConfiguracaoDto toDto(Configuracao configuracao);
	
	@Mapping(target = "orgao", ignore = true)
	@Mapping(target = "id", ignore = true)
	ConfiguracaoDto fromConfiguracao(ConfiguracaoRequest request);
	
	@Mapping(target = "setor", ignore = true)
	Configuracao toConfiguracao(ConfiguracaoDto dto);


}

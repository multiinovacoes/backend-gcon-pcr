package br.com.multiinovacoes.gcon.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.multiinovacoes.gcon.model.Feriado;
import br.com.multiinovacoes.gcon.model.dto.FeriadoDto;
import br.com.multiinovacoes.gcon.model.request.FeriadoRequest;

@Mapper(componentModel = "spring")
public interface FeriadoMapper {
	
	
	List<FeriadoDto> fromResponseList(List<Feriado> list);
	
	FeriadoDto toDto(Feriado feriado);
	
	@Mapping(target = "id", ignore = true)
	FeriadoDto fromFeriado(FeriadoRequest request);
	
	Feriado toFeriado(FeriadoDto dto);


}

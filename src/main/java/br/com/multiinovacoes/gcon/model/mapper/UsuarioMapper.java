package br.com.multiinovacoes.gcon.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.multiinovacoes.gcon.model.Usuario;
import br.com.multiinovacoes.gcon.model.dto.UsuarioDto;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
	
	List<UsuarioDto> fromResponseList(List<Usuario> list);
	
	UsuarioDto toDto(Usuario usuario);
	
	Usuario toUsuario(UsuarioDto dto);


}

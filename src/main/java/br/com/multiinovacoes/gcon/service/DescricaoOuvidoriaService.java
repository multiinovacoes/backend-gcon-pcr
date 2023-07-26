package br.com.multiinovacoes.gcon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.model.dto.DescricaoOuvidoriaDto;
import br.com.multiinovacoes.gcon.model.mapper.DescricaoOuvidoriaMapper;
import br.com.multiinovacoes.gcon.repository.DescricaoOuvidoriaRepository;

@Service
@Transactional
public class DescricaoOuvidoriaService {
	
	
	@Autowired
	DescricaoOuvidoriaMapper descricaoOuvidoriaMapper;
	
	@Autowired
	DescricaoOuvidoriaRepository descricaoOuvidoriaRepository;
	
	public DescricaoOuvidoriaDto getDescricaoOuvidoria(Long codigoDescricaoOuvidoria) {
		return descricaoOuvidoriaMapper.toDto(descricaoOuvidoriaRepository.getById(codigoDescricaoOuvidoria));
	}
	
	public DescricaoOuvidoriaDto getDescricaoOuvidoriaOrgao(Long orgao) {
		return descricaoOuvidoriaMapper.toDto(descricaoOuvidoriaRepository.findByOrgao(orgao));
	}

	
	public DescricaoOuvidoriaDto salvarDescricaoOuvidoria(DescricaoOuvidoriaDto descricaoOuvidoriaDto) {
		return descricaoOuvidoriaMapper.toDto(descricaoOuvidoriaRepository.save(descricaoOuvidoriaMapper.toDescricaoOuvidoria(descricaoOuvidoriaDto)));
	}
	


}

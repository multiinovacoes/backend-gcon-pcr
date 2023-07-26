package br.com.multiinovacoes.gcon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.model.dto.DescricaoEmailDto;
import br.com.multiinovacoes.gcon.model.mapper.DescricaoEmailMapper;
import br.com.multiinovacoes.gcon.repository.DescricaoEmailRepository;

@Service
@Transactional
public class DescricaoEmailService {
	
	
	@Autowired
	DescricaoEmailMapper descricaoEmailMapper;
	
	@Autowired
	DescricaoEmailRepository descricaoEmailRepository;
	
	public DescricaoEmailDto getDescricaoEmail(Long codigoDescricaoEmail) {
		return descricaoEmailMapper.toDto(descricaoEmailRepository.getById(codigoDescricaoEmail));
	}

	public DescricaoEmailDto getDescricaoEmailOrgao(Long orgao) {
		return descricaoEmailMapper.toDto(descricaoEmailRepository.findByOrgao(orgao));
	}

	
	public DescricaoEmailDto salvarDescricaoEmail(DescricaoEmailDto descricaoEmailDto) {
		return descricaoEmailMapper.toDto(descricaoEmailRepository.save(descricaoEmailMapper.toDescricaoEmail(descricaoEmailDto)));
	}
	


}

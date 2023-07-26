package br.com.multiinovacoes.gcon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.model.dto.ConfiguracaoDto;
import br.com.multiinovacoes.gcon.model.mapper.ConfiguracaoMapper;
import br.com.multiinovacoes.gcon.repository.ConfiguracaoRepository;

@Service
@Transactional
public class ConfiguracaoService {
	
	
	@Autowired
	ConfiguracaoMapper configuracaoMapper;
	
	@Autowired
	ConfiguracaoRepository configuracaoRepository;
	
	public ConfiguracaoDto getConfiguracao(Long codigoConfiguracao) {
		return configuracaoMapper.toDto(configuracaoRepository.getById(codigoConfiguracao));
	}

	public ConfiguracaoDto getConfiguracaoOrgao(Long orgao) {
		return configuracaoMapper.toDto(configuracaoRepository.findByOrgao(orgao));
	}

	
	public ConfiguracaoDto salvarConfiguracao(ConfiguracaoDto configuracaoDto) {
		return configuracaoMapper.toDto(configuracaoRepository.save(configuracaoMapper.toConfiguracao(configuracaoDto)));
	}
	


}

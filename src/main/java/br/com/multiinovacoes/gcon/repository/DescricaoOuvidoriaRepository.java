package br.com.multiinovacoes.gcon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.DescricaoOuvidoria;


@Repository
public interface DescricaoOuvidoriaRepository extends JpaRepository<DescricaoOuvidoria, Long> {
	
	public DescricaoOuvidoria findByOrgao(Long orgao);


}

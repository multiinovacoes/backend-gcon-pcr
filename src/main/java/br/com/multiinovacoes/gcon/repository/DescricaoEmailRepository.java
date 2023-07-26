package br.com.multiinovacoes.gcon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.DescricaoEmail;


@Repository
public interface DescricaoEmailRepository extends JpaRepository<DescricaoEmail, Long> {


	public DescricaoEmail findByOrgao(Long orgao);

}

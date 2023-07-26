package br.com.multiinovacoes.gcon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.TipoExpressoes;


@Repository
public interface TipoExpressoesRepository extends JpaRepository<TipoExpressoes, Long> {
	
	public List<TipoExpressoes> findByOrgaoAndDescricaoContainingIgnoreCaseOrderByDescricaoAsc(Long orgao, String descricao);
	
	public List<TipoExpressoes> findByOrgaoAndRelatorioGeralOrderByDescricaoAsc(Long orgao, Integer relatorioGeral);
	
	public Optional<TipoExpressoes> findByOrgaoAndExpressao(Long orgao, String expressao);


}

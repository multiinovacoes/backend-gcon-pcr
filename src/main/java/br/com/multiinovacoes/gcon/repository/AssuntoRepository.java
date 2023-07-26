package br.com.multiinovacoes.gcon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.Assunto;


@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {
	
	public List<Assunto> findByOrgaoAndDescricaoContainingIgnoreCase(Long orgao, String descricao);
	
	public List<Assunto> findByAreaOrderByDescricaoAsc(Long area);
	
	@Query("SELECT a.descricao FROM Assunto a where a.orgao = ?1 GROUP BY a.descricao")
	public List<String> getListaAssuntos(Long orgao);
	
	
	public List<Assunto> findByOrgaoAndAreaAndStatusOrderByDescricaoAsc(Long orgao, Long area, Integer status);
	
	@Query("SELECT coalesce(max(a.id), 0) FROM Assunto a")
	public Long getMaxId();


}

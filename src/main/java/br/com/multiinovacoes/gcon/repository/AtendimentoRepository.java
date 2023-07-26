package br.com.multiinovacoes.gcon.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.Atendimento;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, AtendimentoQueries, RelatorioQueries{
		
	public Atendimento findByNumeroProtocolo(String numeroProtocolo);
	
	public List<Atendimento> findByOrgao(Long orgao);
	
	public List<Atendimento> findByOrgaoAndDescricaoContainingIgnoreCase(Long orgao, String descricao);
	
	public List<Atendimento> findByOrgaoAndNumeroDocumento(Long orgao, String numeroDocumento);
	
	public List<Atendimento> findByParametroHash(String parametroHash);
	
	public List<Atendimento> findByOrgaoAndNumeroProtocoloAndSenhaManifestante(Long orgao, String numeroProtocolo, String senhaManifestante);
	
	public List<Atendimento> findByOrgaoAndAnoAtendimentoAndNumeroAtendimentoAndSenhaManifestante(Long orgao, Integer ano, Long numeroAtendimento, String senhaManifestante);

	@Query("SELECT coalesce(max(a.sequencialOrgao), 0) FROM Atendimento a where a.anoAtendimento = ?1 and a.orgao = ?2")
	public Long getMaxSequencialOrgao(Integer ano, Long orgao);
	
	@Query("SELECT coalesce(max(a.numeroAtendimento), 0) FROM Atendimento a where a.anoAtendimento = ?1")
	public Long getMaxNumeroAtendimento(Integer ano);

	@Query("SELECT coalesce(max(a.sequencialOrgao), 0) FROM Atendimento a where a.anoAtendimento = ?1 and a.orgao = ?2")
	public Long getMaxAnoAtendimento(Integer ano, Long orgao);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and status <> 2 "
			+ "and statusAtendimento = 0 and (dataConclusao = '1969-12-31 21:00:00.000' or dataConclusao is null) and (a.area = 0 or a.area = 51) "
			+ "and (select count(e.id) from Encaminhamento e where e.atendimento = a.id and e.status = 0) = 0")
	public Integer getNovosAtendimentos(Long orgao);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and status <> 2 "
			+ "and statusAtendimento = 0 and (dataConclusao = '1969-12-31 21:00:00.000' or dataConclusao is null) and a.area > 0 and  a.area != 51 "
			+ "and (select count(e.id) from Encaminhamento e where e.atendimento = a.id and e.status = 0) = 0")
	public Integer getAtendimentosClassifNaoEnc(Long orgao);

	@Query("SELECT count(a.id) FROM Atendimento a, Natureza n where a.natureza = n.id and a.orgao = ?1 and a.status <> 2 " + 
		" and a.dataEntrada >= ?2 and a.dataEntrada <= ?3 and a.natureza = ?4")
	public Integer getTotalRegistrosNatureza(Long orgao, LocalDate dataInicial, LocalDate dataFinal, Long natureza);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status <> ?2 "
			+ "and month(a.dataCriacao) = ?3 and year(a.dataCriacao) = ?4")
	public Integer getQtdAtendimentosMes(Long orgao, Integer status, Integer mes, Integer ano);
	
	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status <> ?2 "
			+ "and month(a.dataCriacao) = ?3 and year(a.dataCriacao) = ?4 and a.statusAtendimento = ?5")
	public Integer getQtdAtendimentosMesConcluidos(Long orgao, Integer status, Integer mes, Integer ano, Integer statusAtendimento);
	
	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status <> 2 "
			+ " and year(a.dataCriacao) = ?2 ")
	public Integer getQtdAtendimentosAno(Long orgao, Integer ano);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status = 1 "
			+ " and year(a.dataCriacao) = ?2 and a.statusAtendimento = 1")
	public Integer getQtdAtendimentosConcluidos(Long orgao, Integer ano);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status = 0 "
			+ " and year(a.dataCriacao) = ?2 ")
	public Integer getQtdAtendimentosAnoNaoConcluidos(Long orgao, Integer ano);
	
	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and status = 0 "
			+ "and statusAtendimento = 0 and (dataConclusao = '1969-12-31 21:00:00.000' or dataConclusao is null) and (a.area = 0 or a.area = 51) and year(a.dataCriacao) = ?2 "
			+ "and (select count(e.id) from Encaminhamento e where e.atendimento = a.id and e.status = 0) = 0")
	public Integer getNovosAtendimentos(Long orgao, Integer ano);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status = 0 "
			+ " and year(a.dataCriacao) = ?2 AND a.dataPrazo >= DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)")
	public Integer getQtdAtendimentosAnoNaoConcluidosNaoVencidos(Long orgao, Integer ano);
	
	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status = 0 "
			+ " and year(a.dataCriacao) = ?2 AND a.dataPrazo < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)")
	public Integer getQtdAtendimentosAnoNaoConcluidosVencidos(Long orgao, Integer ano);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status <> ?2 "
			+ "and month(a.dataCriacao) = ?3 and year(a.dataCriacao) = ?4 and a.status = ?5 AND a.dataPrazo >= DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)")
	public Integer getQtdAtendimentosNaoVencidos(Long orgao, Integer status, Integer mes, Integer ano, Integer statusAtendimento);

	@Query("SELECT count(a.id) FROM Atendimento a where a.orgao = ?1 and a.status <> ?2 "
			+ "and month(a.dataCriacao) = ?3 and year(a.dataCriacao) = ?4 and a.status = ?5 AND a.dataPrazo < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)")
	public Integer getQtdAtendimentosVencidos(Long orgao, Integer status, Integer mes, Integer ano, Integer statusAtendimento);
	

}

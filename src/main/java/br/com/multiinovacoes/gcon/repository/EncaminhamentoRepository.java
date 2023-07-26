package br.com.multiinovacoes.gcon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.multiinovacoes.gcon.model.Encaminhamento;


@Repository
public interface EncaminhamentoRepository extends JpaRepository<Encaminhamento, Long>, EncaminhamentoQueries {
	
	
	public List<Encaminhamento> findByAtendimento(Long atendimento);
	
	public List<Encaminhamento> findByAtendimentoAndStatusAndCancelado(Long atendimento, Integer status, Integer cancelado);
	
	@Query("SELECT coalesce(max(e.id), 0) FROM Encaminhamento e")
	public Long getMaxId();
	
	public Encaminhamento findByAtendimentoAndSetorDestinoAndStatusAndCancelado(Long atendimento, Long setorDestino, Integer status, Integer cancelado);
	
	@Query("SELECT count(e.id) FROM Encaminhamento e inner join Atendimento a on a.id = e.atendimento left join EncaminhamentoResposta r "
			+ "on (e.id = r.encaminhamento and r.status = 0) where a.orgao = ?1 and a.status = 0 "
			+ "and (a.dataConclusao = '1969-12-31 21:00:00.000' or a.dataConclusao = '1969-12-31 00:00:00.000' or a.dataConclusao is null)"
			+ "and e.status = 0 and e.cancelado = 0 and r.id is not null ")
	public Integer getEncaminhamentosRecebidos(Long orgao);
	
	@Query("SELECT count(e.id) FROM Encaminhamento e inner join Atendimento a on a.id = e.atendimento "
			+ " left join EncaminhamentoResposta r on e.id = r.encaminhamento "
			+ " where a.orgao = ?1 and a.status = 0 and e.status = 0 and year(e.dataPrazo) = ?2" 
			+ " and month(e.dataPrazo) = ?3 and e.dataPrazo < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)"
			+ " and r.id is null ")
	public Integer getEncaminhamentosVencidos(Long orgao, Integer ano, Integer mes);

	@Query("SELECT count(e.id) FROM Encaminhamento e inner join Atendimento a on a.id = e.atendimento "
			+ " left join EncaminhamentoResposta r on e.id = r.encaminhamento "
			+ " where a.orgao = ?1 and a.status = 0 and e.status = 0 and year(e.dataPrazo) = ?2" 
			+ " and month(e.dataPrazo) = ?3 and e.dataPrazo >= DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)"
			+ " and r.id is null ")
	public Integer getEncaminhamentosNaoVencidos(Long orgao, Integer ano, Integer mes);

	public Encaminhamento findByParametroAndCancelado(String parametro, Integer cancelado);
	
	
	@Query("SELECT e FROM Encaminhamento e "
			+ " where year(e.dataEncaminhamento) = 2023 and e.tempoResposta is null and e.cancelado = 0")
	public List<Encaminhamento> getEncaminhamentosRespondido();
	
	
	



}

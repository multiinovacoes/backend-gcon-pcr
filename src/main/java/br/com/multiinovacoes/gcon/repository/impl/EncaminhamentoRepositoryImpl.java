package br.com.multiinovacoes.gcon.repository.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.StyledEditorKit.ItalicAction;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.multiinovacoes.gcon.model.Encaminhamento;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamento;
import br.com.multiinovacoes.gcon.report.DadosEncaminhamentoTratar;
import br.com.multiinovacoes.gcon.report.DadosGrafico;
import br.com.multiinovacoes.gcon.report.DetalheAtendimentoArea;
import br.com.multiinovacoes.gcon.repository.EncaminhamentoQueries;
import br.com.multiinovacoes.gcon.util.DateTools;

public class EncaminhamentoRepositoryImpl implements EncaminhamentoQueries{
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional
	public List<Encaminhamento> consultaAtendimento(Long codigoAtendimento) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Encaminhamento> criteria = builder.createQuery(Encaminhamento.class);
		Root<Encaminhamento> root = criteria.from(Encaminhamento.class);
		root.fetch("usuario", JoinType.LEFT);
		Predicate predicates = builder.equal(root.get("atendimento"), codigoAtendimento);
		criteria.where(predicates);
		TypedQuery<Encaminhamento> query = manager.createQuery(criteria);
		
		return query.getResultList().isEmpty() ? null : query.getResultList();
	}

	@Override
	public Encaminhamento consultaEncaminhamento(Long codigoEncaminhamento) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Encaminhamento> criteria = builder.createQuery(Encaminhamento.class);
		Root<Encaminhamento> root = criteria.from(Encaminhamento.class);
		root.fetch("usuario", JoinType.LEFT);
		Predicate predicates = builder.equal(root.get("id"), codigoEncaminhamento);
		criteria.where(predicates);
		TypedQuery<Encaminhamento> query = manager.createQuery(criteria);
		return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
	}

	@Override
	public String consultar(String campo, Long id) {
		Query query =  manager.createNativeQuery("SELECT CONVERT(nvarchar(30), " + campo + ", 103) FROM OUVIDORIA_ENCAMINHAMENTO_ENVIO WHERE INATENDIMENTOID = ? ORDER BY INCODIGOENCAMINHAMENTOENVIO DESC");
		query.setParameter(1, id);
	    @SuppressWarnings("unchecked")
		List<Object> lista = query.getResultList();
        if (!lista.isEmpty()) {
        	return (String )lista.get(0);
        }
        else
        	return "Não encontrado";
	}

	@Override
	public List<Encaminhamento> consultaEncaminhamentosRecebidos(Long orgao, Pageable pageable, Integer totalRegistros) {
		
		String q = " SELECT e FROM Encaminhamento e inner join Atendimento a on a.id = e.atendimento left join EncaminhamentoResposta r " + 
				" on (e.id = r.encaminhamento and r.status = 0) where a.orgao = ?1 and a.status = 0 " + 
				" and (a.dataConclusao = '1969-12-31 21:00:00.000' or a.dataConclusao = '1969-12-31 00:00:00.000' or a.dataConclusao is null) " + 
				" and e.status = 0 and e.cancelado = 0 and r.id is not null ";
		
		TypedQuery<Encaminhamento> query = manager.createQuery(q, Encaminhamento.class);
		query.setParameter(1, orgao);
		adicionarRestricoesDePaginacao(query, pageable);
		return  query.getResultList();
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<Encaminhamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	
	private void adicionarRestricoesDePaginacaoNativa(Query query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}


	@Override
	public List<DadosEncaminhamento> encaminhamentosNaoRespondidos(Long orgao, Long setor, Date dataInicial,Date dataFinal, String prazo, String despacho15diasatras) {
		String q = " SELECT OE.INATENDIMENTOID as IdAtend, OA.VANUMPROTOCOLO as Prot, OA.INNUMEROATENDIMENTO as NumeroAtend , OA.SMANOATENDIMENTO as Ano, S.DESCRICAO as Setor1, OA.INCODIGOASSUNTO as CodAssunto, A.VADESCRICAO as Assunto11, OA.INCODIGONATUREZA as codNatu, OA.INCODIGOPRIORIZACAO as codPrio, " + 
				   " CONVERT(varchar, OE.DAENCAMINHAMENTO, 103) as DATA_ENCAMINHADO, OE.INCODIGOENCAMINHAMENTOENVIO as CodEnc, CONVERT(varchar, OE.DAPRAZOENCAMINHAMENTO, 103) as DATA_PRAZO, " +
				   " (select top 1 CONVERT(varchar, DADESPACHO, 103) as DataDespacho2 from OUVIDORIA_DESPACHO where INATENDIMENTOID = OA.INATENDIMENTOID and INCODIGO_SETOR_DESTINO = ? order by DADESPACHO desc) AS DATA_DESPACHO, " +
				   " (select count(*) from OUVIDORIA_DESPACHO where INATENDIMENTOID = OA.INATENDIMENTOID and INCODIGO_SETOR_DESTINO = ?) AS QTD " + 
				   " FROM OUVIDORIA_ATENDIMENTO OA (NOLOCK) " + 
				   " INNER JOIN OUVIDORIA_ASSUNTO A ON OA.INCODIGOASSUNTO = A.INCODIGOASSUNTO " +
				   " INNER JOIN OUVIDORIA_ENCAMINHAMENTO_ENVIO OE (NOLOCK) ON (OA.INATENDIMENTOID = OE.INATENDIMENTOID)  LEFT JOIN OUVIDORIA_ENCAMINHAMENTO_RESPOSTA R (NOLOCK) " + 
				   " ON (OE.INCODIGOENCAMINHAMENTOENVIO = R.INCODIGOENCAMINHAMENTO) " + 
				   " INNER JOIN GLOB_SETOR S (NOLOCK) ON (S.CODIGO_SETOR = OE.INCODIGOSETORDESTINO)  WHERE (OA.INCODIGOORGAO = ?) AND OE.INCODIGOSETORDESTINO = ? AND (OE.DAENCAMINHAMENTO >= ?) " + 
				   " AND (OE.DAENCAMINHAMENTO <= ?)  AND (OA.DACONCLUSAO = ?) AND (OA.SMSTATUS <> ?) AND OE.SMCANCELADO = 0 AND R.INCODIGORESPOSTA IS NULL "; 

            		   
		   if (prazo.equals("1")) {
			   q = q + "AND OE.DAPRAZOENCAMINHAMENTO < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)";
		   }else if (prazo.equals("2")) {
			   q = q + "AND OE.DAPRAZOENCAMINHAMENTO >= DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0)";
		   }
		   
		   q = q + " ORDER BY OA.VANUMPROTOCOLO";
		
	       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		   Query query =  manager.createNativeQuery(q);
		   query.setParameter(1, setor);
		   query.setParameter(2, setor);
		   query.setParameter(3, orgao);
		   query.setParameter(4, setor);
		   query.setParameter(5, dateFormat.format(dataInicial));
		   query.setParameter(6, dateFormat.format(dataFinal));
		   query.setParameter(7, "1969/12/31 21:00:00.000");
		   query.setParameter(8, 2);
		   

//		   @SuppressWarnings("unchecked")
//		   List<Object> listaCount = query.getResultList();
//
//		   Query qResult = manager.createNativeQuery(q);
//		   
//		   qResult.setParameter(1, setor);
//		   qResult.setParameter(2, setor);
//		   qResult.setParameter(3, orgao);
//		   qResult.setParameter(4, setor);
//		   qResult.setParameter(5, dateFormat.format(dataInicial));
//		   qResult.setParameter(6, dateFormat.format(dataFinal));
//		   qResult.setParameter(7, "1969/12/31 21:00:00.000");
//		   qResult.setParameter(8, 2);
//		   
//		   adicionarRestricoesDePaginacaoNativa(qResult, page);
//		   @SuppressWarnings("unchecked")
		   List<Object> lista = query.getResultList();
		   
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		   List<DadosEncaminhamento> dados = new ArrayList<>(lista.size());
		   LocalDate date = null;
		   for (int i = 0; i < lista.size(); i++) {
	    	  Object obj = lista.get(i);
			  Object[] itensObj = (Object[])obj;
			  if (despacho15diasatras != null && despacho15diasatras.equals("3")) {
				  if (itensObj[12] != null) {
					  date = LocalDate.parse(itensObj[12].toString(),formatter).plusDays(15);
					  if (date.isAfter(LocalDate.now())) {
						  continue;
					  }
			  	  }
			  }
			  dados.add(new DadosEncaminhamento(Long.parseLong(itensObj[0].toString()), 
					  itensObj[1].toString(), itensObj[9].toString(), itensObj[6].toString(), Long.parseLong(itensObj[10].toString()), 
					  itensObj[11].toString(), Integer.parseInt(itensObj[13].toString()) == 0 ? "" : itensObj[13].toString(), 
							  itensObj[12] == null ? "" : itensObj[12].toString(), 
									  LocalDate.parse(itensObj[11].toString(), formatter).isBefore(LocalDate.now()) == true ? "S" : "N"));
		   }
		   
			//return new PageImpl<>(dados, page, listaCount.size());
		   return dados;
		
	}

	@Override
	public List<DadosEncaminhamentoTratar> tratarEncaminhamentos(Long orgao, Long setor, Integer ano) {
		String q = " SELECT OA.INATENDIMENTOID, OA.VANUMPROTOCOLO, OA.SMANOATENDIMENTO, OA.INNUMEROATENDIMENTO, ASS.VADESCRICAO as 'ASSUNTO', OP.VADESCRICAO as 'PRIORIDADE', CONVERT(varchar, EE.DAENCAMINHAMENTO, 103) AS 'DATA_ENC', " + 
				"  CONVERT(varchar, EE.DAPRAZOENCAMINHAMENTO, 103) AS 'DATA_PRAZO', EE.INCODIGOENCAMINHAMENTOENVIO, DATEDIFF(day,EE.DAPRAZOENCAMINHAMENTO, GETDATE()) AS 'DIAS_ATRASADO' " + 
				"  FROM OUVIDORIA_ENCAMINHAMENTO_ENVIO EE (NOLOCK) " + 
				"  INNER JOIN OUVIDORIA_ATENDIMENTO OA (NOLOCK) ON (EE.INATENDIMENTOID = OA.INATENDIMENTOID) " + 
				"  LEFT JOIN OUVIDORIA_ENCAMINHAMENTO_INTERNO_ENVIO IE (NOLOCK) ON (IE.INCODIGOENCAMINHAMENTOENVIO = EE.INCODIGOENCAMINHAMENTOENVIO) " + 
				"  LEFT JOIN OUVIDORIA_ENCAMINHAMENTO_INTERNO_RESPOSTA IR (NOLOCK) ON (IR.INCODIGOENCAMINHAMENTOINTERNOENVIO = IE.INCODIGOENCAMINHAMENTOINTERNOENVIO) " + 
				"  LEFT JOIN OUVIDORIA_ENCAMINHAMENTO_RESPOSTA ER (NOLOCK) ON (EE.INCODIGOENCAMINHAMENTOENVIO = ER.INCODIGOENCAMINHAMENTO) " + 
				"  INNER JOIN OUVIDORIA_ASSUNTO ASS (NOLOCK) ON (OA.INCODIGOASSUNTO = ASS.INCODIGOASSUNTO AND OA.INCODIGOORGAO = ASS.INCODIGOORGAO) " + 
				"  INNER JOIN OUVIDORIA_PRIORIZACAO OP (NOLOCK) ON (OA.INCODIGOPRIORIZACAO = OP.INCODIGOPRIORIZACAO) ";
		
		if (setor != 55) {
			q = q + "  WHERE OA.INCODIGOORGAO = ?  AND OA.SMSTATUS <> ? AND OA.DACONCLUSAO = ? AND OA.SMANOATENDIMENTO = ? AND EE.INCODIGOSETORDESTINO = ? " + 
			"  AND ER.INCODIGORESPOSTA IS NULL AND IE.INCODIGOENCAMINHAMENTOINTERNOENVIO IS NULL  ORDER BY DIAS_ATRASADO "; 
		}else {
			q = q + "  WHERE OA.INCODIGOORGAO = ? AND OA.SMSTATUS <> ? AND OA.DACONCLUSAO = ? AND OA.SMANOATENDIMENTO = ? AND (EE.INCODIGOSETORDESTINO = ? OR EE.INCODIGOSETORDESTINO = ?)  " + 
			"  AND ER.INCODIGORESPOSTA IS NULL AND IE.INCODIGOENCAMINHAMENTOINTERNOENVIO IS NULL  ORDER BY DIAS_ATRASADO "; 
		}
				
		
		
		   Query query =  manager.createNativeQuery(q);
		   query.setParameter(1, orgao);
		   query.setParameter(2, 2);
		   query.setParameter(3, "1969/12/31 21:00:00.000");
		   query.setParameter(4, ano);
			if (setor != 55) {
			   query.setParameter(5, setor);
			}else{
			   query.setParameter(5, setor);
			   query.setParameter(6, 17);
			}

		   @SuppressWarnings("unchecked")
		   List<Object> lista = query.getResultList();
		   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		   List<DadosEncaminhamentoTratar> dados = new ArrayList<>(lista.size());
		   for (int i = 0; i < lista.size(); i++) {
	    	  Object obj = lista.get(i);
			  Object[] itensObj = (Object[])obj;
			  dados.add(new DadosEncaminhamentoTratar(Long.parseLong(itensObj[0].toString()), 
					  itensObj[1].toString(), itensObj[6].toString(), itensObj[4].toString(), Long.parseLong(itensObj[8].toString()), 
					  itensObj[7].toString(), 
									  LocalDate.parse(itensObj[7].toString(), formatter).isBefore(LocalDate.now()) == true ? "S" : "N", itensObj[5].toString(), Integer.parseInt(itensObj[9].toString()) <= 0 ? "" : itensObj[9].toString()));
		   }
		   
		   return dados;
	}
	
	
	
	@Override
	public List<DadosGrafico> getEstatisticaEncaminhamentoVencido(Long orgao, Integer mes, Integer ano) {
		
		String q = " SELECT TOP 5 S.DESCRICAO, COUNT(e.INATENDIMENTOID) AS TOTAL FROM OUVIDORIA_ENCAMINHAMENTO_ENVIO e inner join ouvidoria_atendimento a on a.INATENDIMENTOID = e.INATENDIMENTOID " + 
				" left join OUVIDORIA_ENCAMINHAMENTO_RESPOSTA r on e.INCODIGOENCAMINHAMENTOENVIO = r.INCODIGOENCAMINHAMENTO " + 
				" inner join GLOB_SETOR S ON S.CODIGO_SETOR = E.INCODIGOSETORDESTINO " + 
				" where a.incodigoorgao = ? and a.smstatus = 0 and e.INSTATUS = 0 " + 
				" and year(e.DAPRAZOENCAMINHAMENTO) = ? " + 
				" and month(e.DAPRAZOENCAMINHAMENTO) = ? " + 
				" and e.DAPRAZOENCAMINHAMENTO < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0) " + 
				" and r.INCODIGOENCAMINHAMENTO is null " + 
				" GROUP BY S.DESCRICAO " + 
				" ORDER BY TOTAL DESC ";
		
		Query query =  manager.createNativeQuery(q);
		
		query.setParameter(1, orgao);
		query.setParameter(2, ano);
		query.setParameter(3, mes);
	
		@SuppressWarnings("unchecked")
		List<Object> lista = query.getResultList();
		List<DadosGrafico> listaGrafico = new ArrayList<>(lista.size());
		for (int i = 0; i < lista.size(); i++) {
    		Object obj = lista.get(i);
			Object[] itensObj = (Object[])obj;
			listaGrafico.add(new DadosGrafico(itensObj[0].toString(), Double.parseDouble(itensObj[1].toString()), null));
		}
        return listaGrafico;        
	}

	

	@Override
	public List<DadosGrafico> getEstatisticaEncaminhamentoEnviado(Long orgao, Integer mes, Integer ano) {
		
		String q = " SELECT TOP 5 S.DESCRICAO, COUNT(e.INATENDIMENTOID) AS TOTAL FROM OUVIDORIA_ENCAMINHAMENTO_ENVIO e inner join ouvidoria_atendimento a on a.INATENDIMENTOID = e.INATENDIMENTOID " + 
				" left join OUVIDORIA_ENCAMINHAMENTO_RESPOSTA r on e.INCODIGOENCAMINHAMENTOENVIO = r.INCODIGOENCAMINHAMENTO " + 
				" inner join GLOB_SETOR S ON S.CODIGO_SETOR = E.INCODIGOSETORDESTINO " + 
				" where a.incodigoorgao = ? and a.smstatus <> 2 and e.INSTATUS = 0 " + 
				" and year(e.DAPRAZOENCAMINHAMENTO) = ? " + 
				" and month(e.DAPRAZOENCAMINHAMENTO) = ? " + 
				" and r.INCODIGOENCAMINHAMENTO is null " + 
				" GROUP BY S.DESCRICAO " + 
				" ORDER BY TOTAL DESC ";
		
		Query query =  manager.createNativeQuery(q);
		
		query.setParameter(1, orgao);
		query.setParameter(2, ano);
		query.setParameter(3, mes);
	
		@SuppressWarnings("unchecked")
		List<Object> lista = query.getResultList();
		List<DadosGrafico> listaGrafico = new ArrayList<>(lista.size());
		for (int i = 0; i < lista.size(); i++) {
    		Object obj = lista.get(i);
			Object[] itensObj = (Object[])obj;
			listaGrafico.add(new DadosGrafico(itensObj[0].toString(), Double.parseDouble(itensObj[1].toString()), null));
		}
        return listaGrafico;        
	}
	
	
	
	@Override
	public List<DetalheAtendimentoArea> getEncaminhamentoSetor(Long orgao, Date dataInicial, Date dataFinal, String area, String status) {
		
	        
		   String q = " SELECT DISTINCT(A.INATENDIMENTOID), A.INNUMEROATENDIMENTO , A.SMANOATENDIMENTO,  S.DESCRICAO as 'area', A.SMSTATUS as 'status', ASS.VADESCRICAO as 'assunto',  " + 
		   		" N.VADESCRICAO as 'natureza', A.INCODIGOORGAO, CONVERT(varchar, E.DAENCAMINHAMENTO, 103) as 'data', CONVERT(varchar, E.DAPRAZOENCAMINHAMENTO , 103) as 'DATAPRAZO' " + 
		   		" from  ouvidoria_atendimento A " + 
		   		" INNER join OUVIDORIA_ENCAMINHAMENTO_ENVIO E on a.INATENDIMENTOID = e.INATENDIMENTOID " + 
		   		" INNER JOIN OUVIDORIA_ASSUNTO ASS ON ASS.INCODIGOASSUNTO = A.INCODIGOASSUNTO " + 
		   		" INNER JOIN OUVIDORIA_AREAASSUNTO AR ON ASS.INCODIGOAREAASSUNTO = AR.INCODIGOAREAASSUNTO " + 
		   		" INNER JOIN OUVIDORIA_NATUREZA N ON N.INCODIGONATUREZA = A.INCODIGONATUREZA " + 
		   		" LEFT JOIN OUVIDORIA_ENCAMINHAMENTO_RESPOSTA r on e.INCODIGOENCAMINHAMENTOENVIO = r.INCODIGOENCAMINHAMENTO " + 
		   		" INNER JOIN GLOB_SETOR S ON S.CODIGO_SETOR = E.INCODIGOSETORDESTINO  where A.incodigoorgao = ? " + 
		   		" and a.smstatus = 0 and e.INSTATUS = 0 AND E.SMCANCELADO = 0 " + 
		   		" and (e.DAPRAZOENCAMINHAMENTO >= ? and e.DAPRAZOENCAMINHAMENTO <= ? )" + 
		   		" AND ASS.INCODIGOORGAO = A.INCODIGOORGAO " + 
		   		" AND AR.INCODIGOORGAO = A.INCODIGOORGAO " + 
		   		" and r.INCODIGOENCAMINHAMENTO is null ";
		   
		   if(status.equals("1")){
                q = (new StringBuilder()).append(q).append(" and e.DAPRAZOENCAMINHAMENTO < DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0) ").toString();
           }else{
                q = (new StringBuilder()).append(q).append(" and e.DAPRAZOENCAMINHAMENTO >= DATEADD(day, DATEDIFF(day, 0, GETDATE()), 0) ").toString();
           }
		   
		   if(!area.equals("")){
	          q = (new StringBuilder()).append(q).append(" AND E.INCODIGOSETORDESTINO = ? ").toString();
		   }
		   
	       q = (new StringBuilder()).append(q).append(" GROUP BY A.INATENDIMENTOID, A.INNUMEROATENDIMENTO , A.SMANOATENDIMENTO, " + 
							" S.DESCRICAO, A.SMSTATUS, A.SMSTATUSATENDIMENTO, " +
							" ASS.VADESCRICAO, N.VADESCRICAO, A.INCODIGOORGAO, " + 
							" E.DAENCAMINHAMENTO, E.DAPRAZOENCAMINHAMENTO   " +
							" ORDER BY S.DESCRICAO, CONVERT(varchar, E.DAENCAMINHAMENTO, 103) ").toString();
	       
	       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   
		   
		   Query query =  manager.createNativeQuery(q);
		   query.setParameter(1, orgao);
		   query.setParameter(2, dateFormat.format(dataInicial));
		   query.setParameter(3, dateFormat.format(dataFinal));
		   
		   if(!area.equals("")){
			   query.setParameter(4, area);
		   }
		   
		   

		   @SuppressWarnings("unchecked")
		   List<Object> lista = query.getResultList();
		   List<DetalheAtendimentoArea> detalhe = new ArrayList<>(lista.size());
		   
		   for (int i = 0; i < lista.size(); i++) {
	    		Object obj = lista.get(i);
				Object[] itensObj = (Object[])obj;
				String prot = itensObj[2].toString() + itensObj[1].toString();
				detalhe.add(new DetalheAtendimentoArea(Long.parseLong(itensObj[0].toString()), prot,  itensObj[1].toString(), itensObj[2].toString(), itensObj[3].toString(), itensObj[4].toString().equals("0") ? "Aberto" : "Concluído",
						itensObj[5].toString(), itensObj[6].toString(), itensObj[8].toString(), itensObj[9].toString().equals("31/12/1969") ? "" : itensObj[9].toString(), "", ""));
			}
		   
		return detalhe;
	}

	@Override
	public Integer getEncaminhamentosEnviados(Long orgao, Long area, String dataInicial, String dataFinal) {
		String q = " select COUNT(A.INATENDIMENTOID) " + 
				" from OUVIDORIA_ATENDIMENTO A inner join OUVIDORIA_ENCAMINHAMENTO_ENVIO E on A.INATENDIMENTOID = E.INATENDIMENTOID " + 
				"WHERE A.INCODIGOORGAO = ? " + 
				"AND E.INCODIGOSETORDESTINO = ? " +
				"AND (E.DAENCAMINHAMENTO >= ? AND E.DAENCAMINHAMENTO <= ? )" +
				"AND E.SMCANCELADO = 0 " + 
				"AND A.SMSTATUS <> 2";
		
		Query query =  manager.createNativeQuery(q);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		query.setParameter(1, orgao);
		query.setParameter(2, area);
		query.setParameter(3, dateFormat.format(DateTools.stringDateToTimestamp(dataInicial)));
		query.setParameter(4, dateFormat.format(DateTools.stringDateToTimestamp(dataFinal)));
	
		@SuppressWarnings("unchecked")
		List<Object> lista = query.getResultList();
		if (!lista.isEmpty()) {
			return Integer.parseInt(lista.get(0).toString());
		}
        return 0;        
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	

}

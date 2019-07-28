package com.algaworks.algamoneyapi.repository.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.algaworks.algamoneyapi.model.Categoria_;
import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.model.Lancamento_;
import com.algaworks.algamoneyapi.model.Pessoa_;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projecao.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtar(LancamentoFilter lancamentoFilter,Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarWhere(lancamentoFilter,builder,root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		criarPaginacao(query,pageable);
		
		return new PageImpl<>(query.getResultList(),pageable,gettotal(lancamentoFilter));
	}

	private Long gettotal(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarWhere(lancamentoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class 
				,root.get(Lancamento_.CODIGO),root.get(Lancamento_.DESCRICAO)
				,root.get(Lancamento_.DATA_VENCIMENTO),root.get(Lancamento_.DATA_PAGAMENTO)
				,root.get(Lancamento_.VALOR),root.get(Lancamento_.TIPO)
				,root.get(Lancamento_.CATEGORIA).get(Categoria_.NOME)
				,root.get(Lancamento_.PESSOA).get(Pessoa_.NOME)
				));

		Predicate[] predicates = criarWhere(lancamentoFilter,builder,root);
		criteria.where(predicates);

		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		
		criarPaginacao(query,pageable);
		
		return new PageImpl<>(query.getResultList(),pageable,gettotal(lancamentoFilter));
	}
	
	private void criarPaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistroPorPagina;
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistroPorPagina);
		
	}

	
	private Predicate[] criarWhere(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(
						builder.lower(root.get(Lancamento_.DESCRICAO)), 
						"%"+ lancamentoFilter.getDescricao().toLowerCase() +"%"));
		}
		if (!StringUtils.isEmpty(lancamentoFilter.getDataVencimentoDe())) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.get(Lancamento_.DATA_VENCIMENTO),lancamentoFilter.getDataVencimentoDe()));
		}
		if (!StringUtils.isEmpty(lancamentoFilter.getDataVencimentoAte())) {
			predicates.add(builder.lessThanOrEqualTo(
					root.get(Lancamento_.DATA_VENCIMENTO),lancamentoFilter.getDataVencimentoAte()));
			
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}


}

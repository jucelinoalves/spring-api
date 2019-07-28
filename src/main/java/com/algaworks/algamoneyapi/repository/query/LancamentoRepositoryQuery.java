package com.algaworks.algamoneyapi.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projecao.ResumoLancamento;

public interface LancamentoRepositoryQuery {

		public Page<Lancamento> filtar(LancamentoFilter lancamentoFilter, Pageable pageable);
		public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
		
}

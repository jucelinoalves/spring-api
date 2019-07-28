package com.algaworks.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoneyapi.model.Categoria;

public interface CategoriaRespository extends JpaRepository<Categoria,Long> {

}

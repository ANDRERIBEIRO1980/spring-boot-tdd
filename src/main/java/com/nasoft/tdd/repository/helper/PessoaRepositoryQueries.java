package com.nasoft.tdd.repository.helper;

import java.util.List;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.repository.filtro.PessoaFiltro;

public interface PessoaRepositoryQueries {

    List<Pessoa> filtrar(PessoaFiltro filtro);
}

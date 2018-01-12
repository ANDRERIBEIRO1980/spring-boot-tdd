package com.nasoft.tdd.servico;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.modelo.Telefone;
import com.nasoft.tdd.servico.exception.TelefoneNaoEncontradoException;
import com.nasoft.tdd.servico.exception.UnicidadeCpfException;
import com.nasoft.tdd.servico.exception.UnicidadeTelefoneException;

public interface PessoaService {

    Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfException, UnicidadeTelefoneException;

    Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException;

}

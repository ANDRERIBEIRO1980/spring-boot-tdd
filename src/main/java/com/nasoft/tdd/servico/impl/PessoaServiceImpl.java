package com.nasoft.tdd.servico.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.modelo.Telefone;
import com.nasoft.tdd.repository.PessoaRepository;
import com.nasoft.tdd.servico.PessoaService;
import com.nasoft.tdd.servico.exception.TelefoneNaoEncontradoException;
import com.nasoft.tdd.servico.exception.UnicidadeCpfException;
import com.nasoft.tdd.servico.exception.UnicidadeTelefoneException;

@Service
public class PessoaServiceImpl implements PessoaService {

	private PessoaRepository pessoaRepository;

	public PessoaServiceImpl(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	@Override
	public Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfException, UnicidadeTelefoneException  {
		
		Optional<Pessoa> optional = pessoaRepository.findByCpf(pessoa.getCpf());		
		if (optional.isPresent()){
			 throw new UnicidadeCpfException("Já existe pessoa cadastrada com o CPF '"+pessoa.getCpf()+"'");
		}
		
		optional = pessoaRepository.findByTelefoneDddAndTelefoneNumero(pessoa.getTelefones().get(0).getDdd(), pessoa.getTelefones().get(0).getNumero());
		if (optional.isPresent()){
			throw new UnicidadeTelefoneException();
		}
		
		return pessoaRepository.save(pessoa);
	}

	@Override
	public Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException {
		
		Optional<Pessoa> optional = pessoaRepository.findByTelefoneDddAndTelefoneNumero(telefone.getDdd(), telefone.getNumero());
		//optional.get(); retorna diretamente a instancia
		//optional.orElseThrow((); diz para retornar a instancia diretamente, caso nao tenha, lance a excecao
		return optional.orElseThrow(() -> new TelefoneNaoEncontradoException("Não existe pessoa com o telefone (" + telefone.getDdd() + ")" + telefone.getNumero()));
	}

 
}

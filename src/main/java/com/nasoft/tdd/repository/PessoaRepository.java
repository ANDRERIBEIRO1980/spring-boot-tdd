package com.nasoft.tdd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.repository.helper.PessoaRepositoryQueries;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQueries {
	
	Optional<Pessoa> findByCpf(String cpf);

    @Query("SELECT bean FROM Pessoa bean JOIN bean.telefones tele WHERE tele.ddd = :ddd AND tele.numero = :numero")
	Optional<Pessoa> findByTelefoneDddAndTelefoneNumero(@Param("ddd") String ddd, @Param("numero") String numero);
	
}

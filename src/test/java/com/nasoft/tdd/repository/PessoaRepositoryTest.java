package com.nasoft.tdd.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.repository.filtro.PessoaFiltro;

@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // executa scrip de carregamento de dados antes da execucao de cada metodo de test
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // executa scrip de clean apos execucao de cada metodo de test
@RunWith(SpringRunner.class) // executa teste com spring
@DataJpaTest // executa no contexto do spring data fazendo conexao com o banco de dados em memoria h2sql
@TestPropertySource("classpath:application-test.properties") // especifica as propriedados do banco de dados em memoria, pegando do arquivo application-test.properties
public class PessoaRepositoryTest {

	@Autowired
	private PessoaRepository sut;

	@Test
	public void deve_procurar_pessoa_pelo_cpf() throws Exception {
		
		Optional<Pessoa> optional = sut.findByCpf("38767897100");
		assertThat(optional.isPresent()).isTrue();
		Pessoa pessoa = optional.get();
		assertThat(pessoa.getCodigo()).isEqualTo(3L);
		assertThat(pessoa.getNome()).isEqualTo("Caue");
		assertThat(pessoa.getCpf()).isEqualTo("38767897100");		
	}

	@Test
	public void nao_deve_encontrar_pessoa_de_cpf_inexistente() throws Exception {
		
		Optional<Pessoa> optional = sut.findByCpf("999999999");
		assertThat(optional.isPresent()).isFalse();
	}

	@Test
	public void deve_encontrar_pessoa_pelo_ddd_e_numero_de_telefone() throws Exception {
		
		Optional<Pessoa> optional = sut.findByTelefoneDddAndTelefoneNumero("86", "35006330");
		assertThat(optional.isPresent()).isTrue();
		Pessoa pessoa = optional.get();
		assertThat(pessoa.getCodigo()).isEqualTo(3L);
		assertThat(pessoa.getNome()).isEqualTo("Caue");
		assertThat(pessoa.getCpf()).isEqualTo("38767897100");
	}

	@Test
	public void nao_deve_encontrar_pessoa_cujo_ddd_e_telefone_nao_estejam_cadastrados() throws Exception {
		
		Optional<Pessoa> optional = sut.findByTelefoneDddAndTelefoneNumero("11", "123456789");
		assertThat(optional.isPresent()).isFalse();
	}

	@Test
	public void deve_filtrar_pessoas_por_parte_do_nome() throws Exception {
		
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setNome("a");
		List<Pessoa> pessoas = sut.filtrar(filtro);
		assertThat(pessoas.size()).isEqualTo(3);
	}
	
	@Test
	public void deve_filtrar_pessoas_por_filtro_composto() throws Exception {
		
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setNome("a");
		filtro.setCpf("78");
		List<Pessoa> pessoas = sut.filtrar(filtro);
		assertThat(pessoas.size()).isEqualTo(2);
	}
	
	@Test
	public void deve_filtrar_pessoas_pelo_ddd_do_telefone() throws Exception {
		
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setDdd("21");
		List<Pessoa> pessoas = sut.filtrar(filtro);
		assertThat(pessoas.size()).isEqualTo(1);	
	}
	
	@Test
	public void deve_filtrar_pessoas_pelo_numero_do_telefone() throws Exception {
		PessoaFiltro filtro = new PessoaFiltro();
		filtro.setTelefone("997504");
		List<Pessoa> pessoas = sut.filtrar(filtro);
		assertThat(pessoas.size()).isEqualTo(0);	
	}
}

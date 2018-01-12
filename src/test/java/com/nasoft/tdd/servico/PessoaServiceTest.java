package com.nasoft.tdd.servico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.modelo.Telefone;
import com.nasoft.tdd.repository.PessoaRepository;
import com.nasoft.tdd.servico.exception.TelefoneNaoEncontradoException;
import com.nasoft.tdd.servico.exception.UnicidadeCpfException;
import com.nasoft.tdd.servico.exception.UnicidadeTelefoneException;
import com.nasoft.tdd.servico.impl.PessoaServiceImpl;

@RunWith(SpringRunner.class) // executa testes pelo spring
public class PessoaServiceTest {

	private static final String NUMERO = "123456";
	private static final String DDD = "11";
	private static final String NOME = "Andre";
	private static final String CPF = "123456789";
	
	@MockBean
	private PessoaRepository pessoaRepository;
	
	//caso queira especificar a exception e colocar a msg
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

	private PessoaService sut;
	
	private Pessoa pessoa;
	
	private Telefone telefone;

	@Before
	public void setUp() throws Exception {
		
		sut = new PessoaServiceImpl(pessoaRepository);
		
		pessoa = new Pessoa();		
		pessoa.setNome(NOME);
		pessoa.setCpf(CPF);
		
		telefone = new Telefone();
		telefone.setDdd(DDD);
		telefone.setNumero(NUMERO);
		
		pessoa.setTelefones(Arrays.asList(telefone));
		
		when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.empty());
		when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD,NUMERO)).thenReturn(Optional.empty());
		
	}

	@Test
	public void deve_salvar_pessoa_no_repositorio() throws Exception {
		
		sut.salvar(pessoa); //chama o metodo salvar
		verify(pessoaRepository).save(pessoa); //verifica se metodo save foi executado
		
	}
	
	@Test
	public void nao_deve_salvar_duas_pessoas_com_o_mesmo_cpf() throws Exception {
		

        expectedException.expect(UnicidadeCpfException.class);
        expectedException.expectMessage("Já existe pessoa cadastrada com o CPF '"+ CPF +"'");
        
		when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.of(pessoa)); //executa o metodo findByCpf e retorna pessoa
		sut.salvar(pessoa);
	}
	
	@Test(expected = UnicidadeTelefoneException.class)
	public void nao_deve_salvar_duas_pessoas_com_o_mesmo_telefone() throws Exception {
	
		when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD,NUMERO)).thenReturn(Optional.of(pessoa));
		sut.salvar(pessoa);
	}
	
	@Test
	public void deve_procurar_pessoa_pelo_ddd_e_numero_do_telefone() throws Exception {
		
		when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD, NUMERO)).thenReturn(Optional.of(pessoa)); //executa o metodo findByCpf e retorna pessoa
		
		Pessoa pessoaTeste = sut.buscarPorTelefone(telefone);		
		verify(pessoaRepository).findByTelefoneDddAndTelefoneNumero(DDD, NUMERO);
		assertThat(pessoaTeste).isNotNull();
		assertThat(pessoaTeste.getNome()).isEqualTo(NOME);
		assertThat(pessoaTeste.getCpf()).isEqualTo(CPF);
		
	}
	
	@Test(expected = TelefoneNaoEncontradoException.class)
	public void deve_retornar_excecao_de_nao_encontrado_quando_nao_existir_pessoa_com_o_ddd_e_numero_de_telefone() throws Exception {
		sut.buscarPorTelefone(telefone);	
	
	}
	
    @Test
    public void deve_retornar_dados_do_telefone_dentro_da_excecao_de_telefone_nao_encontrado_exception() throws Exception {
        expectedException.expect(TelefoneNaoEncontradoException.class);
        expectedException.expectMessage("Não existe pessoa com o telefone (" + DDD +")" + NUMERO);

        sut.buscarPorTelefone(telefone);
    }

}

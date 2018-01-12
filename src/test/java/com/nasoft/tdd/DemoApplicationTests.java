package com.nasoft.tdd;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;


@Sql(value = "/load-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)// executa scrip de carregamento de dados antes da execucao de cada metodo de test
@Sql(value = "/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)// executa scrip de clean apos execucao de cada metodo de test
@RunWith(SpringRunner.class)// executa teste com spring
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//sobe a aplicacao principal para os endpoints estarem disponiveis
@TestPropertySource("classpath:application-test.properties")// especifica as propriedados do banco de dados em memoria, pegando do arquivo application-test.properties                               
public abstract class DemoApplicationTests {
	//classe abstract porque já contem todas as configuracoes e servirá de base, 
	//deve ser extendida em todas as classes de testes de resource
	
	@Value("${local.server.port}")
	protected int porta;

	@Before
	public void setUp() throws Exception {
		//adiciona a porta para que o restassured possa saber a porta dos endpoints
		//verificar no pom a dependencia
		RestAssured.port = porta;
	}

	
}


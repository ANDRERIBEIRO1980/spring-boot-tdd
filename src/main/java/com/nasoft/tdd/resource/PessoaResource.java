package com.nasoft.tdd.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nasoft.tdd.modelo.Pessoa;
import com.nasoft.tdd.modelo.Telefone;
import com.nasoft.tdd.repository.PessoaRepository;
import com.nasoft.tdd.repository.filtro.PessoaFiltro;
import com.nasoft.tdd.servico.PessoaService;
import com.nasoft.tdd.servico.exception.TelefoneNaoEncontradoException;
import com.nasoft.tdd.servico.exception.UnicidadeCpfException;
import com.nasoft.tdd.servico.exception.UnicidadeTelefoneException;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaRepository repository;

    @GetMapping("/{ddd}/{numero}")
    public ResponseEntity<Pessoa> buscarPorDddENumeroDoTelefone(@PathVariable("ddd") String ddd,
                                                                @PathVariable("numero") String numero) throws TelefoneNaoEncontradoException {
        final Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(numero);

        final Pessoa pessoa = pessoaService.buscarPorTelefone(telefone);

        return new ResponseEntity<>(pessoa, HttpStatus.OK);
    }


  @PostMapping
    public ResponseEntity<Pessoa> salvarNova(@RequestBody Pessoa pessoa, HttpServletResponse response) throws UnicidadeCpfException, UnicidadeTelefoneException {
        final Pessoa pessoaSalva = pessoaService.salvar(pessoa);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{ddd}/{numero}")
                .buildAndExpand(pessoa.getTelefones().get(0).getDdd(), pessoa.getTelefones().get(0).getNumero()).toUri();
        response.setHeader("Location", uri.toASCIIString());

        return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
    }
  
    @PostMapping("/filtrar")
    public ResponseEntity<List<Pessoa>> filtrar(@RequestBody PessoaFiltro filtro) {
        final List<Pessoa> pessoas = repository.filtrar(filtro);
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }

    @ExceptionHandler({UnicidadeCpfException.class})
    public ResponseEntity<Erro> handleUnicidadeCpfException(UnicidadeCpfException e) {
        return new ResponseEntity<>(new Erro(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler({TelefoneNaoEncontradoException.class})
    public ResponseEntity<Erro> handleTelefoneNaoEncontradoException(TelefoneNaoEncontradoException e) {
        return new ResponseEntity<>(new Erro(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    class Erro {
        private final String erro;

        public Erro(String erro) {
            this.erro = erro;
        }

        public String getErro() {
            return erro;
        }
    }

}

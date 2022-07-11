package br.com.icarros.icontas.service;

import br.com.icarros.icontas.dto.request.CorrentistaRequest;
import br.com.icarros.icontas.dto.request.GerenteCorrentistaRequest;
import br.com.icarros.icontas.dto.response.CorrentistaResponse;
import br.com.icarros.icontas.entity.Correntista;
import br.com.icarros.icontas.entity.Gerente;
import br.com.icarros.icontas.entity.enums.UF;
import br.com.icarros.icontas.exception.CorrentistaNaoEcontradoException;
import br.com.icarros.icontas.exception.RegraDeNegocioException;
import br.com.icarros.icontas.repository.CorrentistaRepository;
import br.com.icarros.icontas.repository.GerenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CorrentistaServiceTest {

    @InjectMocks
    private CorrentistaService correntistaService;

    @Mock
    private CorrentistaRepository correntistaRepository;

    @Mock
    private GerenteRepository gerenteRepository;

    Gerente gerente;

    Correntista correntista;

    CorrentistaRequest correntistaRequest;

    @BeforeEach
    public void setup() {
        correntista = stubCorrentista();
        correntistaRequest = stubCorrentistaRequest();
        gerente = stubGerente();
    }

    @Test
    public void testCadastraCorrentista_Sucesso() throws RegraDeNegocioException {
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.empty());
        when(gerenteRepository.findByCpf(anyString())).thenReturn(Optional.ofNullable(stubGerente()));

        CorrentistaResponse correntistaResponse = correntistaService.create(stubCorrentistaRequest());

        assertNotNull(correntistaResponse);
    }

    @Test
    public void testCadastroCorrentista_CorrentistaJaAtivoException(){
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.ofNullable(stubCorrentista()));
        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.create(stubCorrentistaRequest());
                }
        );
    }

    @Test
    public void testCadastroCorrentista_RegraDeNegocioException(){
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.ofNullable(stubCorrentista()));
        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.create(stubCorrentistaRequest());
                }
        );
    }

    private Correntista stubCorrentista(){
        return Correntista.builder()
                .cpf("73602050858")
                .agencia("001")
                .conta("12345")
                .nome("PESSOA_1")
                .email("pessoa1@icarros.com")
                .telefone("11940074048")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .usuario(null)
                .gerente(null)
                .transacoes(null)
                .situacao(true)
                .build();
    }

    private CorrentistaRequest stubCorrentistaRequest(){
        GerenteCorrentistaRequest gerenteCorrentistaRequest = new GerenteCorrentistaRequest();
        gerenteCorrentistaRequest.setCpf("40710878893");
        return CorrentistaRequest.builder()
                .cpf("73602050858")
                .agencia("001")
                .conta("12345")
                .nome("PESSOA_1")
                .email("pessoa1@icarros.com")
                .telefone("11940074048")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .gerente(gerenteCorrentistaRequest)
                .build();
    }

    private Gerente stubGerente(){
        return Gerente.builder()
                .nome("PESSOA_2")
                .email("pessoa2@icarros.com")
                .cpf("04027512057")
                .senha("1234")
                .correntistas(null)
                .build();
    }
}
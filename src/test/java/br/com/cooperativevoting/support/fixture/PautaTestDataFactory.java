package br.com.cooperativevoting.support.fixture;

import br.com.cooperativevoting.domain.model.Pauta;
import br.com.cooperativevoting.domain.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Cria e persiste {@link Pauta}s de teste com valores padrão, permitindo customização pontual.
 * Centraliza a inicialização de dados para ser reaproveitada por qualquer teste de integração,
 * não só o de pauta.
 */
@TestComponent
@RequiredArgsConstructor
public class PautaTestDataFactory {

    private final PautaRepository pautaRepository;

    /** Builder de uma pauta válida com valores padrão, pronta para customização ou persistência. */
    public Pauta.PautaBuilder umaPauta() {
        return Pauta.builder()
                .titulo("Pauta de teste")
                .descricao("Descrição de pauta de teste");
    }

    /** Persiste uma pauta com os valores padrão de {@link #umaPauta()}. */
    public Pauta persistirPauta() {
        Pauta pauta = umaPauta().build();
        return pautaRepository.save(pauta);
    }

    /** Persiste uma pauta customizada a partir dos valores padrão de {@link #umaPauta()}. */
    public Pauta persistirPauta(UnaryOperator<Pauta.PautaBuilder> customizacao) {
        Pauta.PautaBuilder builder = umaPauta();
        Pauta pauta = customizacao.apply(builder).build();
        return pautaRepository.save(pauta);
    }

    /** Persiste {@code quantidade} pautas com títulos distintos. */
    public List<Pauta> persistirPautas(int quantidade) {
        List<Pauta> pautas = new ArrayList<>();
        for (int indice = 1; indice <= quantidade; indice++) {
            int tituloDaPauta = indice;
            Pauta pauta = persistirPauta(builder -> builder.titulo("Pauta de teste " + tituloDaPauta));
            pautas.add(pauta);
        }
        return pautas;
    }
}

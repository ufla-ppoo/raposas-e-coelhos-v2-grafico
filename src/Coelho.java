import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 * 
 * @author David J. Barnes e Michael Kölling
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class Coelho extends Animal
{
    // Características compartilhadas por todos os coelhos (atributos estáticos, da classe).

    // A idade em que um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 5;
    // A idade máxima que um coelho pode atingir.
    private static final int IDADE_MAXIMA = 40;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.12;
    // O número máximo de filhotes que podem nascer de cada vez.
    private static final int TAMANHO_MAXIMO_NINHADA = 4;
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = Randomizador.obterRandom();
    
    // Características individuais (atributos comuns, de instância).
    
    // A idade do coelho.
    private int idade;

    /**
     * Cria um novo coelho. Um coelho pode ser criado com idade
     * zero (recém-nascido) ou com uma idade aleatória.
     * 
     * @param idadeAleatoria Se verdadeiro, o coelho terá uma idade aleatória.
     * @param campo O campo atualmente ocupado.
     * @param localizacao A localização dentro do campo.
     */
    public Coelho(boolean idadeAleatoria, Campo campo, Localizacao localizacao)
    {
        super(campo, localizacao);
        idade = 0;
        if(idadeAleatoria) {
            idade = rand.nextInt(IDADE_MAXIMA);
        }
    }
    
    /**
     * Isto é o que o coelho faz na maior parte do tempo: ele corre por aí.
     * Às vezes, ele se reproduz ou morre de velhice.
     * @param novosCoelhos Uma lista para retornar os coelhos recém-nascidos.
     */
    @Override
    public void agir(List<Animal> novosCoelhos)
    {
        incrementarIdade();
        if(estaVivo()) {
            reproduzir(novosCoelhos);            
            // Tenta se mover para uma localização livre.
            Localizacao novaLocalizacao = obterCampo().localizacaoVizinhaLivre(obterLocalizacao());
            if(novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
            }
            else {
                // Superlotação.
                morrer();
            }
        }
    }

    /**
     * Aumenta a idade.
     * Isso pode resultar na morte do coelho.
     */
    private void incrementarIdade()
    {
        idade++;
        if(idade > IDADE_MAXIMA) {
            morrer();
        }
    }
    
    /**
     * Verifica se este coelho deve dar à luz neste passo.
     * Novos nascimentos serão feitos em locais vizinhos livres.
     * @param novosCoelhos Uma lista para retornar os coelhos recém-nascidos.
     */
    private void reproduzir(List<Animal> novosCoelhos)
    {
        // Novos coelhos nascem em locais vizinhos.
        // Obter uma lista de locais vizinhos livres.
        List<Localizacao> locaisLivres = obterCampo().localizacoesVizinhasLivres(obterLocalizacao());
        int nascimentos = procriar();
        for(int b = 0; b < nascimentos && locaisLivres.size() > 0; b++) {
            Localizacao loc = locaisLivres.remove(0);
            Coelho filhote = new Coelho(false, obterCampo(), loc);
            novosCoelhos.add(filhote);
        }
    }
        
    /**
     * Gera um número representando o número de nascimentos,
     * se puder procriar.
     * @return O número de nascimentos (pode ser zero).
     */
    private int procriar()
    {
        int nascimentos = 0;
        if(podeProcriar() && rand.nextDouble() <= PROBABILIDADE_REPRODUCAO) {
            nascimentos = rand.nextInt(TAMANHO_MAXIMO_NINHADA) + 1;
        }
        return nascimentos;
    }

    /**
     * Um coelho pode procriar se tiver atingido a idade de reprodução.
     * @return verdadeiro se o coelho puder procriar, falso caso contrário.
     */
    private boolean podeProcriar()
    {
        return idade >= IDADE_REPRODUCAO;
    }
}
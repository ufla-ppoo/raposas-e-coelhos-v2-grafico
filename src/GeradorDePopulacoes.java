import java.util.List;
import java.util.Random;
import java.awt.Color;


public class GeradorDePopulacoes {
    // A probabilidade de uma raposa ser criada em qualquer posição da grade.
    private static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;
    // A probabilidade de um coelho ser criado em qualquer posição.
    private static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;

    /**
     * Define as cores de cada espécie de animal nas visões do simulador
     * @param visao
     */
    public static void definirCores(VisaoSimulador visao) {
        visao.definirCor(Coelho.class, Color.ORANGE);
        visao.definirCor(Raposa.class, Color.BLUE);
    }    

    /**
     * Povoa aleatoriamente o campo com animais.
     */
    public static void povoar(Campo campo, List<Animal> animais)
    {
        Random rand = Randomizador.obterRandom();
        campo.limpar();
        for(int linha = 0; linha < campo.obterComprimento(); linha++) {
            for(int coluna = 0; coluna < campo.obterLargura(); coluna++) {
                if(rand.nextDouble() <= PROBABILIDADE_CRIACAO_RAPOSA) {
                    Localizacao localizacao = new Localizacao(linha, coluna);
                    Raposa raposa = new Raposa(true, campo, localizacao);
                    animais.add(raposa);
                }
                else if(rand.nextDouble() <= PROBABILIDADE_CRIACAO_COELHO) {
                    Localizacao localizacao = new Localizacao(linha, coluna);
                    Coelho coelho = new Coelho(true, campo, localizacao);
                    animais.add(coelho);
                }
                // caso contrário, deixa a localização vazia.
            }
        }
    }
}

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Um simulador simples de predador-presa, baseado em um campo retangular contendo 
 * coelhos e raposas.
 * 
 * @author David J. Barnes e Michael Kölling
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class Simulador
{
    // Constantes que representam informações de configuração para a simulação.
    // A largura padrão da grade.
    private static final int LARGURA_PADRAO = 120;
    // O comprimento padrão da grade.
    private static final int COMPRIMENTO_PADRAO = 80;

    // Lista de animais no campo.
    private List<Animal> animais;
    // O estado atual do campo.
    private Campo campo;
    // O passo atual da simulação.
    private int passo;
    // Visões gráficas da simulação.
    private List<VisaoSimulador> visoes;
    
    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulador()
    {
        this(COMPRIMENTO_PADRAO, LARGURA_PADRAO);
    }
    
    /**
     * Cria um campo de simulação com o tamanho fornecido.
     * @param comprimento O comprimento do campo. Deve ser maior que zero.
     * @param largura A largura do campo. Deve ser maior que zero.
     */
    public Simulador(int comprimento, int largura)
    {
        if(largura <= 0 || comprimento <= 0) {
            System.out.println("As dimensões devem ser >= zero.");
            System.out.println("Usando valores padrões.");
            comprimento = COMPRIMENTO_PADRAO;
            largura = LARGURA_PADRAO;
        }
        
        animais = new ArrayList<>();
        campo = new Campo(comprimento, largura);

        visoes = new ArrayList<>();
        
        VisaoSimulador visao = new VisaoDeGrade(comprimento, largura, this);
        GeradorDePopulacoes.definirCores(visao);
        visoes.add(visao);

        visao = new VisaoDeGrafico(800, 400, 500);
        GeradorDePopulacoes.definirCores(visao);
        visoes.add(visao);
        
        // Configura um ponto de partida válido.
        reiniciar();
    }
    
    /**
     * Executa a simulação a partir de seu estado atual por um período razoavelmente longo 
     * (4000 passos).
     */
    public void executarSimulacaoLonga()
    {
        // altere o parâmetro de atraso se quiser executar mais lentamente
        simular(4000, 0);
    }
    
    /**
     * Executa a simulação pelo número fornecido de passos.
     * Para a simulação antes do número fornecido de passos se ela se tornar inviável.
     * @param numPassos O número de passos a executar.
     */
    public void simular(int numPassos, int atraso)
    {
        for(int passo = 1; passo <= numPassos && visoes.get(0).ehViavel(campo); passo++) {
            simularUmPasso();
            if (atraso > 0) {
                pausar(atraso);   
            }
        }
        reabilitarOpcoesVisoes();
    }
    
    /**
     * Executa a simulação a partir de seu estado atual por um único passo. 
     * Itera por todo o campo atualizando o estado de cada raposa e coelho.
     */
    public void simularUmPasso()
    {
        passo++;

        // Fornece espaço para os animais recém-nascidos.
        List<Animal> novosAnimais = new ArrayList<>(); 
        // Permite que todos os ns ajam.
        for(Iterator<Animal> it = animais.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.agir(novosAnimais);
            if(!animal.estaVivo()) {
                it.remove();
            }
        }
        
        // Adiciona os animais recém-nascidos às listas principais.
        animais.addAll(novosAnimais);

        atualizarVisoes();
    }
        
    /**
     * Reinicia a simulação para uma posição inicial.
     */
    public void reiniciar()
    {
        passo = 0;
        animais.clear();
        for (VisaoSimulador visao : visoes) {
            visao.reiniciar();
        }

        GeradorDePopulacoes.povoar(campo, animais);
        
        atualizarVisoes();
        reabilitarOpcoesVisoes();
    }

    /**
     * Atualiza todas as visões existentes.
     */
    private void atualizarVisoes()
    {
        for (VisaoSimulador visao : visoes) {
            visao.mostrarStatus(passo, campo);
        }
    }

    /**
     * Reabilita as opções de todas as visões existentes.
     */
    private void reabilitarOpcoesVisoes()
    {
        for (VisaoSimulador visao : visoes) {
            visao.reabilitarOpcoes();
        }
    }

    /**
     * Pausa por um tempo fornecido.
     * @param milissegundos O tempo para pausar, em milissegundos.
     */
    private void pausar(int milissegundos)
    {
        try {
            Thread.sleep(milissegundos);
        }
        catch (InterruptedException ie) {
            // acorda
        }
    }
}
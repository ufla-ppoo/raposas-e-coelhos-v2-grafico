import java.awt.Color;

/**
 * Uma visão gráfica da grade de simulação. Esta interface define todos os diferentes
 * tipos de visões.
 * 
 * @author Michael Kölling and David J. Barnes
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public interface VisaoSimulador
{
    /**
     * Define uma cor a ser usada para uma classe específica de animal.
     * @param classeAnimal A classe do animal.
     * @param cor A cor a ser usada para a classe fornecida.
     */
    public void definirCor(Class<?> classeAnimal, Color cor);

    /**
     * Determina se a simulação deve continuar a ser executada.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean ehViavel(Campo campo);

    /**
     * Mostra o estado atual do campo.
     * @param passo Qual iteração do passo está sendo exibida.
     * @param campo O campo cujo estado será exibido.
     */
    public void mostrarStatus(int passo, Campo campo);
    
    /**
     * Prepara para uma nova execução
     */
    void reiniciar();

    /**
     * Reabilita as opçẽos da visão
     */
    void reabilitarOpcoes();
}
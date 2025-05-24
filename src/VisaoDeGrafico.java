import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

/**
 * VisaoGrafico fornece uma visão de duas popluações de atores no campo como um gráfico de
 * linha no tempo. Na sua versão atual, ele consegue plotar exatamente duas classes diferentes
 * de animais. Se mais classes de animais forem acrescentadas, elas não serão exibidas com
 * essa versão do código.
 * 
 * @author Michael Kölling and David J. Barnes
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class VisaoDeGrafico implements VisaoSimulador
{
    private static final Color CINZA_CLARO = new Color(0, 0, 0, 40);

    private static JFrame frame;
    private static PainelDeGrafico grafico;
    private static JLabel rotuloPasso;
    private static JLabel rotuloContador;

    // As classes que estão sendo tratadas por essa visão
    private Set<Class<?>> classes;
    // Um mapa para armazenar cores para participantes na simulação.
    private Map<Class<?>, Color> cores;
    // Um objeto de estatísticas que calcula e armazena informações da simulação.
    private EstatisticasCampo estatisticas;

    /**
     * Construtor.
     * 
     * @param largura A largura da janela do gráfico (in pixels).
     * @param altura A largura da janela do gráfico (in pixels).
     * @param yMaximoInicial O valor máximo inicial para o eixo y.
     */
    public VisaoDeGrafico(int largura, int altura, int yMaximoInicial)
    {
        estatisticas = new EstatisticasCampo();
        classes = new HashSet<>();
        cores = new HashMap<>();

        if (frame == null) {
            frame = construirFrame(largura, altura, yMaximoInicial);
        }
        else {
            grafico.novaExecucao();
        }

        //showStatus(0, null);
    }

    /**
     * Define uma cor a ser usada para uma classe específica de animal.
     * @param classeAnimal A classe do animal.
     * @param cor A cor a ser usada para a classe fornecida.
     */
    @Override
    public void definirCor(Class<?> classeAnimal, Color cor)
    {
        cores.put(classeAnimal, cor);
        classes = cores.keySet();
    }


    /**
     * Mostra o estado atual do campo. O estado é mostrado através de um gráfico de linha para as
     * duas classes no campo. Essa visão atualmente não funciona para mais (ou menos) do que
     * exatamente duas classes. Se o campo tiver mais de dois tipos diferentes de aniimais,
     * somente duas das classes são exibidas
     * 
     * @param passo Qual iteração do passo está sendo exibida.
     * @param campo O campo cujo estado será exibido.
     */
    @Override
    public void mostrarStatus(int passo, Campo campo)
    {
        grafico.atualizar(passo, campo, estatisticas);
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     * @return true Se houver mais de uma espécie viva.
     */
    @Override
    public boolean ehViavel(Campo campo)
    {
        return estatisticas.ehViavel(campo);
    }

    /**
     * Prepara para uma nova execução
     */
    @Override
    public void reiniciar() {
        estatisticas.reiniciar();
        grafico.novaExecucao();
    }

    /** 
     * Reabilita as opções da visão 
      */
    @Override
    public void reabilitarOpcoes() {
        // nada a fazer nesta classe
    }
    
    /**
     * Prepara o frame para a exibição do gráfico.
     */
    private JFrame construirFrame(int largura, int altura, int yMaximoInicial)
    {
        JFrame frame = new JFrame("Visão de Gráfico");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Container painelDeConteudo = frame.getContentPane();

        grafico = new PainelDeGrafico(largura, altura, yMaximoInicial);
        painelDeConteudo.add(grafico, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel();
        painelInferior.add(new JLabel("Passo:"));
        rotuloPasso = new JLabel("");
        painelInferior.add(rotuloPasso);
        rotuloContador = new JLabel(" ");
        painelInferior.add(rotuloContador);
        painelDeConteudo.add(painelInferior, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocation(20, 600);

        frame.setVisible(true);

        return frame;
    }

    // ============================================================================
    /**
     * Classe aninhada interna: um componente para exibir o gráfico.
     */
    class PainelDeGrafico extends JComponent
    {
        private static final double FATOR_DE_ESCALA = 0.8;

        // Uma imagem interna usada com buffer para o desenho.
        // Essa imagem é copiada para a tela na hora que o deseno precisa
        // ser realmente exibido
        private BufferedImage imagemGrafico;
        private int ultimoValor1, ultimoValor2;
        private int yMaximo;

        /**
         * Cria um PainelDeGrafico novo, vazio.
         */
        public PainelDeGrafico(int largura, int altura, int yMaximoInicial)
        {
            imagemGrafico = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
            limparImagem();
            ultimoValor1 = altura;
            ultimoValor2 = altura;
            yMaximo = yMaximoInicial;
        }

        /**
         * Indica uma nova execução da simulação no painel.
         */
        public void novaExecucao()
        {
            int altura = imagemGrafico.getHeight();
            int largura = imagemGrafico.getWidth();

            Graphics g = imagemGrafico.getGraphics();
            g.copyArea(4, 0, largura-4, altura, -4, 0);            
            g.setColor(Color.BLACK);
            g.drawLine(largura-4, 0, largura-4, altura);
            g.drawLine(largura-2, 0, largura-2, altura);
            ultimoValor1 = altura;
            ultimoValor2 = altura;
            repaint();
        }

        /**
         * Exibe um novo ponto de dados.
         */
        public void atualizar(int passo, Campo campo, EstatisticasCampo estatisticas)
        {
            if (classes.size() >= 2) {
                Iterator<Class<?>> it = classes.iterator();
                Class<?> classe1 = it.next();
                Class<?> classe2 = it.next();

                estatisticas.reiniciar();
                int contagem1 = estatisticas.obterContagemPopulacao(campo, classe1);
                int contagem2 = estatisticas.obterContagemPopulacao(campo, classe2);

                Graphics g = imagemGrafico.getGraphics();

                int altura = imagemGrafico.getHeight();
                int largura = imagemGrafico.getWidth();

                // move o gráfico um pixel para a esquerda
                g.copyArea(1, 0, largura-1, altura, -1, 0);

                // calula y, verifica se está fora da tela e diminui a escala do gráfico
                // se necessário
                int y = altura - ((altura * contagem1) / yMaximo) - 1;
                while (y<0) {
                    reduzirEscala();
                    y = altura - ((altura * contagem1) / yMaximo) - 1;
                }
                g.setColor(CINZA_CLARO);
                g.drawLine(largura-2, y, largura-2, altura);
                g.setColor(cores.get(classe1));
                g.drawLine(largura-3, ultimoValor1, largura-2, y);
                ultimoValor1 = y;

                y = altura - ((altura * contagem2) / yMaximo) - 1;
                while (y<0) {
                    reduzirEscala();
                    y = altura - ((altura * contagem2) / yMaximo) - 1;
                }
                g.setColor(CINZA_CLARO);
                g.drawLine(largura-2, y, largura-2, altura);
                g.setColor(cores.get(classe2));
                g.drawLine(largura-3, ultimoValor2, largura-2, y);
                ultimoValor2 = y;

                repaint();

                rotuloPasso.setText("" + passo);
                rotuloContador.setText(estatisticas.obterDetalhesPopulacao(campo));
            }
        }

        /**
         * Diminui a escala do gráfico verticalmente para liberar espaço no topo.
         */
        public void reduzirEscala()
        {
            Graphics g = imagemGrafico.getGraphics();
            int altura = imagemGrafico.getHeight();
            int largura = imagemGrafico.getWidth();

            BufferedImage imagemTemp = new BufferedImage(largura, (int)(altura*FATOR_DE_ESCALA), 
                                                         BufferedImage.TYPE_INT_RGB);
            Graphics2D gTemp = (Graphics2D) imagemTemp.getGraphics();

            gTemp.scale(1.0, FATOR_DE_ESCALA);
            gTemp.drawImage(imagemGrafico, 0, 0, null);

            int topoAntigo = (int) (altura * (1.0-FATOR_DE_ESCALA));

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, largura, topoAntigo);
            g.drawImage(imagemTemp, 0, topoAntigo, null);

            yMaximo = (int) (yMaximo / FATOR_DE_ESCALA);
            ultimoValor1 = topoAntigo + (int) (ultimoValor1 * FATOR_DE_ESCALA);
            ultimoValor2 = topoAntigo + (int) (ultimoValor2 * FATOR_DE_ESCALA);

            repaint();
        }

        /**
         * Limpa a imagem do painel.
         */
        final public void limparImagem()
        {
            Graphics g = imagemGrafico.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, imagemGrafico.getWidth(), imagemGrafico.getHeight());
            repaint();
        }

        // Os métodos abaixo são sobrescritas dos métodos definidos na superclasse

        /**
         * Indica para o layout manager quão o compoente gostaria de ser.
         * (Este método é chamado pelos gestores de layout para posicionar os componentes)
         * 
         * @return A dimensão preferida para esse componente.
         */
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(imagemGrafico.getWidth(), imagemGrafico.getHeight());
        }

        /**
         * Este componente é opaco.
         */
        @Override
        public boolean isOpaque()
        {
            return true;
        }

        /**
         * Chamado quando esse componente precisa ser reexibido. Copia a 
         * imagem interna para a tela. (Este método é internamente pelo
         * Swing sempre que o componente precisa ser exibido)
         * 
         * @param g O contexto gráfico que pode ser usado para desenhar neste componente.
         */
        @Override
        public void paintComponent(Graphics g)
        {
            if(imagemGrafico != null) {
                g.drawImage(imagemGrafico, 0, 0, null);
            }
        }
    }
}
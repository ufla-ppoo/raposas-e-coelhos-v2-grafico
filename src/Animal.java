import java.util.List;

public abstract class Animal {

    // Características individuais (atributos comuns, de instância).
    
    // Indica se o animal está vivo ou não.
    private boolean vivo;
    // A localização do animal.
    private Localizacao localizacao;
    // O campo ocupado.
    private Campo campo;

    public Animal(Campo campo, Localizacao localizacao)
    {
        vivo = true;
        this.campo = campo;
        definirLocalizacao(localizacao);
    }
    
    /**
     * Verifica se o animal está vivo ou não.
     * @return verdadeiro se o animal ainda estiver vivo.
     */
    public boolean estaVivo()
    {
        return vivo;
    }
    
    /**
     * Define que o animal não está mais vivo.
     * Ele é removido do campo.
     */
    protected void morrer()
    {
        vivo = false;
        if(localizacao != null) {
            campo.limpar(localizacao);
            localizacao = null;
            campo = null;
        }
    }
    
    /**
     * Retorna a localização do animal.
     * @return A localização do animal.
     */
    public Localizacao obterLocalizacao()
    {
        return localizacao;
    }
    
    /**
     * Coloca o animal na nova localização no campo fornecido.
     * @param novaLocalizacao A nova localização do animal.
     */
    protected void definirLocalizacao(Localizacao novaLocalizacao)
    {
        if(localizacao != null) {
            campo.limpar(localizacao);
        }
        localizacao = novaLocalizacao;
        campo.colocar(this, novaLocalizacao);
    }

    public Campo obterCampo() {
        return campo;
    }

    public abstract void agir(List<Animal> novosAnimais);
}

import net.jini.core.entry.Entry;
public class CountToAll implements Entry {
    public String palavraCount="Count";
    public String tipoCount;//pode ser Ambiente, dispositivos e usuário
    public Integer quantidade;
    public Integer auxParaNome;
    public CountToAll() {
    }
}
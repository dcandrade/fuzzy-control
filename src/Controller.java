import net.sourceforge.jFuzzyLogic.FIS;

public class Controller {
    private final FIS load;

    public Controller() {
        load = FIS.load('fuzzy.fcl');
    }

    public void run(){
        int t = 0;
        var tanque = new Tanque();


        while (!tanque.isFull(t)) {
            System.out.println("Altura: " + tanque.getAlturaAtual(t));
            System.out.println("Temperatura da saída: " + tanque.temperaturaSaida(t));
            System.out.println("Vazão na saída: " + tanque.vazaoSaida(t));
            t++;
        }

        System.out.println("Tanque cheio");
    }
}

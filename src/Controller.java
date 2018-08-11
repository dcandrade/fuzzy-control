import net.sourceforge.jFuzzyLogic.FIS;

public class Controller {
    private final FIS fis;

    public Controller() {
        fis = FIS.load("libs/tanque.fcl");
    }

    public void run() {
        var t = 0;
        var temperaturaDesejada = 37;
        var temp = -10.0;
        var tanque = new Tanque(1, temp ,temperaturaDesejada, 5);
        double h, vaz, vf, vq;


        while (Math.abs(temp-temperaturaDesejada) > 1) {
            t += Tanque.DELTA_T;

            h = tanque.getAlturaAtual(t);
            temp = tanque.temperaturaSaida(t);
            vaz = tanque.vazaoSaida(t);

            fis.setVariable("dv", tanque.offsetVazaoSaida(t));
            fis.setVariable("dt", tanque.offsetTemperatura(t));
            fis.evaluate();
            vf = fis.getVariable("vf").getValue();
            vq = fis.getVariable("vq").getValue();
            System.out.println("vq: " + vq + " vf: " + vf);

            tanque.setVazaoEntradaFria(vf);
            tanque.setVazaoEntradaQuente(vq);
            System.out.println("h: " + h + " temp: " + temp);
        }

        System.out.println("Temperatura atingida");
    }

    public static void main(String[] args) {
        var controller = new Controller();
        controller.run();
    }
}

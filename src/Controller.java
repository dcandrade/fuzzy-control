import net.sourceforge.jFuzzyLogic.FIS;

public class Controller {
    private final FIS fis;

    public Controller() {
        fis = FIS.load("libs/tanque.fcl");
    }

    public void run() {
        var vazaoMaxima = Tanque.getVazaoMaxima();
        System.out.println(vazaoMaxima);
        var t = 0;
        var temperaturaDesejada = 17.75;
        var vazaoDesejada = vazaoMaxima/2;
        var temp = 15.0;
        var tanque = new Tanque(1, temp, temperaturaDesejada, vazaoDesejada);
        double h, vaz = 0, vf, vq;


        while (Math.abs(temp - temperaturaDesejada) > 1 || (Math.abs(vazaoDesejada-vaz)/vazaoDesejada) < 0.1) {
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
            System.out.println("--> h: " + h + " temp: " + temp + " vazao: " + vaz);
        }

        System.out.println("Temperatura atingida");
    }

    public static void main(String[] args) {
        var controller = new Controller();
        controller.run();
    }
}

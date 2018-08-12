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
        var temperaturaDesejada = 30.75;
        var vazaoDesejada = vazaoMaxima/2;
        var temp = 25.0;
        var tanque = new Tanque(0, temp, temperaturaDesejada, vazaoDesejada);
        double h, vaz = 0, vf, vq;
        var erroVazao = 100.0;
        var erroTemp = 100.0;
        System.out.println("Temperatura Desejada: " + temperaturaDesejada + ", Vazão Desejada: " + vazaoDesejada);

        while (Math.abs(erroTemp) > 0.05 || Math.abs(erroVazao) > 0.2) {
            t += Tanque.DELTA_T;

            h = tanque.getAlturaAtual(t);
            temp = tanque.temperaturaSaida(t);
            vaz = tanque.vazaoSaida(t);

            erroVazao = (vaz-vazaoDesejada) / vazaoDesejada; // se positivo vazao tem q aumentar, mais baixa q o esperado

            erroTemp = (temp - temperaturaDesejada) / temperaturaDesejada; // se positivo temperatura tá mais alta q o esperado

            fis.setVariable("dv", erroVazao);
            fis.setVariable("dt", erroTemp);
            fis.evaluate();
            vf = fis.getVariable("vf").getValue();
            vq = fis.getVariable("vq").getValue();
            System.out.println("vq: " + vq + " vf: " + vf);

            tanque.setVazaoEntradaFria(vf);
            tanque.setVazaoEntradaQuente(vq);
            System.out.println("--> h: " + h + " temp: " + temp + " vazao: " + vaz + ", erroVazao: " + erroVazao + ", erroTemp: " + erroTemp);
        }

        System.out.println("Temperatura atingida");
    }

    public static void main(String[] args) {
        var controller = new Controller();
        controller.run();
    }
}

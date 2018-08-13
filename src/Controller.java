import net.sourceforge.jFuzzyLogic.FIS;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {
    private final FIS fis;

    public Controller() {
        fis = FIS.load("libs/tanque_regras.fcl");
    }

    public void run() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("notebooks/var_2_regras.txt"));
        writer.write("tempo,temp,h,vazao,vazaoideal,tempideal+\n");
        var vazaoMaxima = Tanque.getVazaoMaxima();
        System.out.println(vazaoMaxima);
        var t = 0;
        var temperaturaDesejada = 57.0;
        var vazaoDesejada = 3 * vazaoMaxima; //testing
        var temp = -10.0;
        var tanque = new Tanque(2, temp, temperaturaDesejada, vazaoDesejada);
        double h, vaz = 0, vf, vq;
        var erroVazao = 100.0;
        var erroTemp = 100.0;
        System.out.println("Temperatura Desejada: " + temperaturaDesejada + ", Vazão Desejada: " + vazaoDesejada);


        while (t < 1000) {
            t += Tanque.DELTA_T;

            h = tanque.getAlturaAtual(t);
            temp = tanque.temperaturaSaida(t);
            vaz = tanque.vazaoSaida(t);

            erroVazao = (vaz - vazaoDesejada) / vazaoDesejada; // se positivo vazao tem q aumentar, mais baixa q o esperado

            erroTemp = (temp - temperaturaDesejada) / temperaturaDesejada; // se positivo temperatura tá mais alta q o esperado

            fis.setVariable("dv", erroVazao * 100); // entrada do controlador é em %
            fis.setVariable("dt", erroTemp * 100);
            fis.evaluate();
            vf = fis.getVariable("vf").getValue();
            vq = fis.getVariable("vq").getValue();
            System.out.println("vq: " + vq + " vf: " + vf);

            tanque.setVazaoEntradaFria(vf);
            tanque.setVazaoEntradaQuente(vq);
            System.out.println("--> h: " + h + " temp: " + temp + " vazao: " + vaz + ", erroVazao: " + erroVazao + ", erroTemp: " + erroTemp);
            writer.write(t + "," + temp + "," + h + "," + vaz + "," + vazaoDesejada + ", " + temperaturaDesejada + '\n');

        }
        writer.close();
        System.out.println("Temperatura atingida");
    }

    public static void main(String[] args) throws IOException {
        var controller = new Controller();
        controller.run();
    }
}

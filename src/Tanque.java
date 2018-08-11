import java.util.ArrayList;
import java.util.List;

public class Tanque {
    private final double G = 9.8;
    private double alturaMaximaTanque = 5;

    private double vazaoMaximaEntradaQuente = 1;
    private double vazaoMaximaEntradaFria = 1;
    private double vazaoEntradaQuente = 0.5;
    private double vazaoEntradaFria = 0.5;
    //private double alturaInicialAgua = 0;
    private double raioTanque = 2;
    private double vazaoSaidaAguaQuente = 10;
    private double vazaoSaidaAguaFria = 10;
    private double temperaturaEntradaAguaFria = 10;
    private double temperaturaEntradaAguaQuente = 35;
    //private double temperaturaInicialAguaTanque = 22.5;
    private double temperaturaDeseja = 30;

    int deltaT = 10;

    private List<Double> alturaAgua;
    private List<Double> temperaturaAgua;


    public Tanque(double alturaInicialAgua, double temperaturaInicialAgua) {
        alturaAgua = new ArrayList<>();
        temperaturaAgua = new ArrayList<>();
        temperaturaAgua.add(temperaturaInicialAgua);
        alturaAgua.add(alturaInicialAgua);
    }


    public double vazaoSaida(int t) {
        if (t == 0) {
            return 0;
        }
        return 0;
        //return 0.002 * Math.sqrt(2 * G * this.alturaAgua.get(t - 1));
    }


    public boolean isFull(int t) {
        return this.getAlturaAtual(t) > alturaMaximaTanque;
    }


    public double getAlturaAtual(int t) {

        double novaAltura =
                this.alturaAgua.get(t - 1) +
                        (vazaoEntradaQuente + vazaoEntradaFria - this.vazaoSaida(t)) *
                                (deltaT / (Math.PI * raioTanque * raioTanque));

        this.alturaAgua.add(novaAltura);
        return novaAltura;
    }

    public double temperaturaSaida(int t) throws Exception {

        double temp = (this.temperaturaAgua.get(t - 1) * this.alturaAgua.get(t - 1) * (Math.PI * raioTanque * raioTanque))
                + (temperaturaEntradaAguaQuente * vazaoEntradaQuente * deltaT)
                + (temperaturaEntradaAguaFria * vazaoEntradaQuente * deltaT);
        temp /= (this.alturaAgua.get(t - 1) * (Math.PI * Math.pow(raioTanque, 2))) + (vazaoEntradaQuente * deltaT + vazaoEntradaQuente * deltaT);

        this.temperaturaAgua.add(temp);
        return temp;
    }

    public void setVazaoEntradaFria(double vazaoEntradaFria) {
        this.vazaoEntradaFria = vazaoEntradaFria;
    }

    public void setVazaoEntradaQuente(double vazaoEntradaQuente) {
        this.vazaoEntradaQuente = vazaoEntradaQuente;
    }
}

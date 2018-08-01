import java.util.ArrayList;
import java.util.List;

public class Tanque {
    private final double G = 9.8;
    private double alturaMaximaTanque = 10;

    private double vazaoMaximaEntradaQuente = 50;
    private double vazaoMaximaEntradaFria = 50;
    private double vazaoEntradaQuente = 25;
    private double vazaoEntradaFria = 25;
    //private double alturaInicialAgua = 0;
    private double raioTanque = 100;
    private double vazaoSaidaAguaQuente = 10;
    private double vazaoSaidaAguaFria = 10;
    private double temperaturaEntradaAguaFria = 10;
    private double temperaturaEntradaAguaQuente = 35;
    //private double temperaturaInicialAguaTanque = 22.5;
    private double temperaturaDeseja = 30;

    int deltaT = 1;

    private List<Double> alturaAgua;
    private List<Double> temperaturaAgua;


    public Tanque() {
        alturaAgua = new ArrayList<>();
        temperaturaAgua = new ArrayList<>();
        alturaAgua.add(0.0); //inicia vazio
        temperaturaAgua.add(0.0);
    }

    public double getVazaoMaximaEntradaQuente() {
        return vazaoMaximaEntradaQuente;
    }

    public double getVazaoMaximaEntradaFria() {
        return vazaoMaximaEntradaFria;
    }

    public double getVazaoSaidaAguaQuente() {
        return vazaoSaidaAguaQuente;
    }

    public double getVazaoSaidaAguaFria() {
        return vazaoSaidaAguaFria;
    }

    public void setVazaoMaximaEntradaQuente(double vazaoMaximaEntradaQuente) {

        this.vazaoMaximaEntradaQuente = vazaoMaximaEntradaQuente;
    }

    public void setVazaoMaximaEntradaFria(double vazaoMaximaEntradaFria) {
        this.vazaoMaximaEntradaFria = vazaoMaximaEntradaFria;
    }

    public void setVazaoSaidaAguaQuente(double vazaoSaidaAguaQuente) {
        this.vazaoSaidaAguaQuente = vazaoSaidaAguaQuente;
    }

    public void setVazaoSaidaAguaFria(double vazaoSaidaAguaFria) {
        this.vazaoSaidaAguaFria = vazaoSaidaAguaFria;
    }

    public void setTemperaturaEntradaAguaFria(double temperaturaEntradaAguaFria) {
        this.temperaturaEntradaAguaFria = temperaturaEntradaAguaFria;
    }

    public void setTemperaturaDeseja(double temperaturaDeseja) {
        this.temperaturaDeseja = temperaturaDeseja;
    }

    public void setTemperaturaEntradaAguaQuente(double temperaturaEntradaAguaQuente) {
        this.temperaturaEntradaAguaQuente = temperaturaEntradaAguaQuente;
    }

    public void setVazaoEntradaQuente(double vazaoEntradaQuente) {
        this.vazaoEntradaQuente = vazaoEntradaQuente;
    }

    public void setVazaoEntradaFria(double vazaoEntradaFria) {
        this.vazaoEntradaFria = vazaoEntradaFria;
    }

    public double getAlturaMaximaTanque() {
        return alturaMaximaTanque;
    }

    public double getVazaoEntradaQuente() {
        return vazaoEntradaQuente;
    }

    public double getVazaoEntradaFria() {
        return vazaoEntradaFria;
    }

    public double getTemperaturaEntradaAguaFria() {
        return temperaturaEntradaAguaFria;
    }

    public double getTemperaturaEntradaAguaQuente() {
        return temperaturaEntradaAguaQuente;
    }

    public double getTemperaturaDeseja() {
        return temperaturaDeseja;
    }

    public double getTemperaturaAgua(int t) {
        return temperaturaAgua.get(t);
    }

    public double vazaoSaida(int t) {
        if (t == 0) {
            return 0;
        }

        return 0.002 * Math.sqrt(2 * G * this.alturaAgua.get(t - 1));
    }


    public boolean isFull(int t) {
        return this.getAlturaAtual(t) > alturaMaximaTanque;
    }


    public double getAlturaAtual(int t) {
        if (t == 0) {
            return 0;
        }

        double novaAltura =
                this.alturaAgua.get(t - 1) + ((vazaoEntradaQuente + vazaoEntradaFria - this.vazaoSaida(t)) * deltaT)
                        / ((Math.PI) * Math.pow(raioTanque, 2));


        this.alturaAgua.add(novaAltura);
        return novaAltura;
    }

    public double getAlturaNoTempo(int t) {
        return this.alturaAgua.get(t);
    }

    public double temperaturaSaida(int t) {
        if (t == 0) {
            return 0;
        }

        double temp = (this.temperaturaAgua.get(t - 1) * this.alturaAgua.get(t - 1) * (Math.PI * Math.pow(raioTanque, 2)))
                + (temperaturaEntradaAguaQuente * vazaoEntradaQuente * deltaT)
                + (temperaturaEntradaAguaFria * vazaoEntradaFria * deltaT);
        temp /= (this.alturaAgua.get(t - 1) * (Math.PI * Math.pow(raioTanque, 2))) + (vazaoEntradaQuente * deltaT + vazaoEntradaFria * deltaT);

        this.temperaturaAgua.add(temp);
        return temp;
    }

    private double getTemperaturaNoTempo(int t) {
        return this.temperaturaAgua.get(t);
    }


}

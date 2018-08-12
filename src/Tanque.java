import java.util.ArrayList;
import java.util.List;

public class Tanque {
    private final static double G = 9.8;
    public final static int DELTA_T = 1;
    private final static double VAZAO_MAXIMA_ENTRADA_QUENTE = 100;
    private final static double VAZAO_MAXIMA_ENTRADA_FRIA = 100;

    public static final double ALTURA_MAXIMA_TANQUE = 10;
    private static final double VAZAO_TORNEIRA = 0.002;

    private double raioTanque = 2;


    private double vazaoEntradaQuente = 1;
    private double vazaoEntradaFria = 1;
    private double vazaoDesejadaTanque;

    private double temperaturaEntradaAguaFria = 10;
    private double temperaturaEntradaAguaQuente = 100;
    private boolean friaLigada;
    private boolean quenteLigada;
    private double temperaturaDesejada;


    private List<Double> alturaAgua;
    private List<Double> temperaturaAgua;


    public Tanque(double alturaInicialAgua, double temperaturaInicialAgua, double temperaturaDesejada, double vazaoDesejadaTanque) {
        alturaAgua = new ArrayList<>();
        temperaturaAgua = new ArrayList<>();
        temperaturaAgua.add(temperaturaInicialAgua);
        alturaAgua.add(alturaInicialAgua);

        this.temperaturaDesejada = temperaturaDesejada;
        this.vazaoDesejadaTanque = vazaoDesejadaTanque;
        quenteLigada = true;
        friaLigada = true;
    }

    public Tanque(double temperaturaDesejada, double vazaoDesejadaTanque) {
        this(0.0, 0.0, temperaturaDesejada, vazaoDesejadaTanque);
    }

    private double getTemperaturaAgua(int t) {
        return this.temperaturaAgua.get(t / DELTA_T);
    }

    private double getAlturaAgua(int t) {
        return this.alturaAgua.get(t / DELTA_T);

    }

    public double vazaoSaida(int t) {
        t = t / DELTA_T;
        if (t == 0) {
            return 0;
        }
        //return 0; //vazão = 0
        return VAZAO_TORNEIRA * Math.sqrt(2 * G * this.alturaAgua.get(t - 1));
    }

    public static double getVazaoMaxima() {
        return VAZAO_TORNEIRA * Math.sqrt(2 * G * ALTURA_MAXIMA_TANQUE);
    }

    public boolean isFull(int t) {
        return this.getAlturaAgua(t) > ALTURA_MAXIMA_TANQUE;
    }


    public double getAlturaAtual(int t) {
        var vazaoQuente = vazaoEntradaQuente;
        var vazaoFria = vazaoEntradaFria;

        if (!this.friaLigada) {
            vazaoFria = 0;
        }
        if (!this.quenteLigada) {
            vazaoQuente = 0;
        }

        double novaAltura =
                this.getAlturaAgua(t - 1) +
                        (vazaoQuente + vazaoFria - this.vazaoSaida(t)) *
                                (DELTA_T / (Math.PI * raioTanque * raioTanque));

        this.alturaAgua.add(novaAltura);
        return novaAltura;
    }

    public double temperaturaSaida(int t) {
        var vazaoQuente = vazaoEntradaQuente;
        var vazaoFria = vazaoEntradaFria;

        if (!this.friaLigada) {
            vazaoFria = 0;
        }
        if (!this.quenteLigada) {
            vazaoQuente = 0;
        }


        double temp = (this.getTemperaturaAgua(t - 1) * this.getAlturaAgua(t - 1) * (Math.PI * raioTanque * raioTanque))
                + (temperaturaEntradaAguaQuente * vazaoQuente * DELTA_T)
                + (temperaturaEntradaAguaFria * vazaoFria * DELTA_T);
        temp /= (this.getAlturaAgua(t - 1) * (Math.PI * raioTanque * raioTanque)) + (vazaoQuente * DELTA_T + vazaoFria * DELTA_T);

        this.temperaturaAgua.add(temp);
        return temp;
    }

    public void setVazaoEntradaFria(double razao) { // razao [-100%, 100%]
        //var novaVazaoFria = vazaoEntradaFria + vazaoEntradaFria * razao / 100;
       vazaoEntradaFria = VAZAO_MAXIMA_ENTRADA_FRIA * razao / 100;
    }

    public void desligarEntradaFria() {
        this.friaLigada = false;
    }

    public void desligarEntradaQuente() {
        this.quenteLigada = false;
    }

    public void ligarEntradaFria() {
        this.friaLigada = true;
    }

    public void ligarEntradaQuente() {
        this.quenteLigada = true;
    }

    public void setVazaoEntradaQuente(double razao) {

        //var novaVazaoQuente = vazaoEntradaQuente + ((vazaoEntradaQuente * razao) / 100);
        vazaoEntradaQuente = VAZAO_MAXIMA_ENTRADA_QUENTE * razao / 100;
    }


    // retorna o erro percentual entre a temperatura desejada e a temp atual.
    public double offsetTemperatura(int t) {
        var temp = this.getTemperaturaAgua(t);
        return (this.temperaturaDesejada - temp) * 100 / temperaturaDesejada;
    }

    public double offsetVazaoSaida(int t) {
        var vazao = this.vazaoSaida(t);
        return (this.vazaoDesejadaTanque - vazao) * 100 / vazaoDesejadaTanque;
    }
}

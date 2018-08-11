import java.util.ArrayList;
import java.util.List;

public class Tanque {
    private final static double G = 9.8;
    public final static int DELTA_T = 1;
    private final static double VAZAO_MAXIMA_ENTRADA_QUENTE = 100;
    private final static double VAZAO_MAXIMA_ENTRADA_FRIA = 100;

    private double alturaMaximaTanque = 10;
    private double raioTanque = 10;


    private double vazaoEntradaQuente = 1;
    private double vazaoEntradaFria = 1;
    private double vazaoDesejadaTanque;

    private double temperaturaEntradaAguaFria = 10;
    private double temperaturaEntradaAguaQuente = 100;
    private double vazaoTorneira = 0.02;

    private double temperaturaDesejada = 30;


    private List<Double> alturaAgua;
    private List<Double> temperaturaAgua;


    public Tanque(double alturaInicialAgua, double temperaturaInicialAgua, double temperaturaDesejada, double vazaoDesejadaTanque) {
        alturaAgua = new ArrayList<>();
        temperaturaAgua = new ArrayList<>();
        temperaturaAgua.add(temperaturaInicialAgua);
        alturaAgua.add(alturaInicialAgua);

        this.temperaturaDesejada = temperaturaDesejada;
        this.vazaoDesejadaTanque = vazaoDesejadaTanque;
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
        return vazaoTorneira * Math.sqrt(2 * G * this.alturaAgua.get(t - 1));
    }


    public boolean isFull(int t) {
        return this.getAlturaAgua(t) > alturaMaximaTanque;
    }


    public double getAlturaAtual(int t) {

        double novaAltura =
                this.getAlturaAgua(t - 1) +
                        (vazaoEntradaQuente + vazaoEntradaFria - this.vazaoSaida(t)) *
                                (DELTA_T / (Math.PI * raioTanque * raioTanque));

        this.alturaAgua.add(novaAltura);
        return novaAltura;
    }

    public double temperaturaSaida(int t) {

        double temp = (this.getTemperaturaAgua(t - 1) * this.getAlturaAgua(t - 1) * (Math.PI * raioTanque * raioTanque))
                + (temperaturaEntradaAguaQuente * vazaoEntradaQuente * DELTA_T)
                + (temperaturaEntradaAguaFria * vazaoEntradaQuente * DELTA_T);
        temp /= (this.getAlturaAgua(t - 1) * (Math.PI * Math.pow(raioTanque, 2))) + (vazaoEntradaQuente * DELTA_T + vazaoEntradaQuente * DELTA_T);

        this.temperaturaAgua.add(temp);
        return temp;
    }

    public void setVazaoEntradaFria(double razao) { // razao [-100%, 100%]
        var novaVazaoFria = vazaoEntradaFria + vazaoEntradaFria * razao / 100;
        if (novaVazaoFria < VAZAO_MAXIMA_ENTRADA_FRIA)
            this.vazaoEntradaFria += novaVazaoFria;
    }

    public void setVazaoEntradaQuente(double razao) {

        var novaVazaoQuente = vazaoEntradaQuente + ((vazaoEntradaQuente * razao) / 100);
        if (novaVazaoQuente < VAZAO_MAXIMA_ENTRADA_QUENTE)
            this.vazaoEntradaQuente += novaVazaoQuente;
    }


    // retorna a diferença percentual entre a temperatura desejada e a temp atual.
    //ex: se a temperatura atual for 50 e a desejada for 100, retornará 100 pois a
    //temperatura atual tem que aumentar 100%.
    public double offsetTemperatura(int t) {
        var temp = this.getTemperaturaAgua(t);
        return (this.temperaturaDesejada - temp) * 100 / temp;
    }

    public double offsetVazaoSaida(int t) {
        var vazao = this.vazaoSaida(t);
        return (this.vazaoDesejadaTanque - vazao) * 100 / vazao;
    }
}

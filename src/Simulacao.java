import java.util.Scanner;

public class Simulacao {
    public static void main(String[] args) throws Exception {
        int t = 0;
        var tanque = new Tanque(1, 20);

        while (!tanque.isFull(t)) {
            System.out.println("Altura: " + tanque.getAlturaAtual(t));
            System.out.println("Temperatura da saída: " + tanque.temperaturaSaida(t));
            System.out.println("Vazão na saída: " + tanque.vazaoSaida(t));
            t++;
        }

        System.out.println("Tanque cheio");
    }
}

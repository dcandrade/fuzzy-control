import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Simulacao {
    public static void main(String[] args) throws Exception {
        int t = 0;
        var tanque = new Tanque(1, 25 ,37, 5);

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("notebooks/altura_com_vazao__.txt"));
        writer.write("tempo,altura\n");
        while (!tanque.isFull(t)) {
            t += Tanque.DELTA_T;

            double h = tanque.getAlturaAtual(t);
            double temp = tanque.temperaturaSaida(t);
            double v = tanque.vazaoSaida(t);

            System.out.println(t+", "+h);
            writer.write(t+","+h+'\n');

        }
        writer.close();
        System.out.println("Tanque cheio");
    }
}
//System.out.println("Altura: " + tanque.getAlturaAtual(t));
//System.out.println("Temperatura da saída: " + tanque.temperaturaSaida(t));
//System.out.println("Vazão na saída: " + tanque.vazaoSaida(t));
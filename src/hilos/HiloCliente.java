package hilos;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HiloCliente implements Runnable{
    private Socket sCliente;
    public HiloCliente(Socket sCliente){
        this.sCliente = sCliente;
    }
    @Override
    public void run() {
        try {
            InputStream in = sCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(in);
            while (true) {
                if (in.available() > 0) {
                    System.out.println(flujo_entrada.readUTF());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

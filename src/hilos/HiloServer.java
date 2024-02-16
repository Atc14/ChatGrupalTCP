package hilos;

import servidor.Servidor;

import java.io.*;
import java.net.Socket;

public class HiloServer implements Runnable {
    @Override
    public void run() {

        System.out.println(Servidor.getClientes());
        while (true) {
            for (Socket cliente : Servidor.getClientes()) {
                try {
                    if (!cliente.isClosed()) {
                        InputStream in = cliente.getInputStream();
                        if (in.available() > 0) {
                            DataInputStream flujo_entrada = new DataInputStream(in);
                            String mensaje = flujo_entrada.readUTF();
                            System.out.println(mensaje);
                            for (Socket otroCliente : Servidor.getClientes()) {
                                if (!otroCliente.isClosed()) {
                                    OutputStream out = otroCliente.getOutputStream();
                                    DataOutputStream flujo_salida = new DataOutputStream(out);
                                    flujo_salida.writeUTF(mensaje);
                                    flujo_salida.flush();
                                }
                            }

                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}

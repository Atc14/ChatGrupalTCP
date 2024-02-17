package hilos;

import datos.Usuario;
import servidor.Servidor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HiloServer implements Runnable {
    private final Socket sCliente;
    private boolean parar;
    public HiloServer(Socket sCliente){
        this.sCliente = sCliente;
        this.parar = false;
    }
    @Override
    public void run() {
        synchronized (Servidor.getUsuarios()) {
            try {
                OutputStream outaux = sCliente.getOutputStream();
                ObjectOutputStream flujo_salida = new ObjectOutputStream(outaux);
                flujo_salida.writeObject(Servidor.getUsuarios());
                flujo_salida.flush();
                InputStream inaux = sCliente.getInputStream();

                ObjectInputStream flujo_entrada = new ObjectInputStream(inaux);

                Servidor.agregarUsuario((Usuario) flujo_entrada.readObject());
                System.out.println(Servidor.getUsuarios());

            } catch (IOException | ClassNotFoundException e) {
                try {
                    sCliente.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        System.out.println(Servidor.getClientes());
        synchronized (Servidor.getClientes()) {
            while (true) {
                List<Socket> clientes = new ArrayList<>(Servidor.getClientes()); // Copiar la lista de clientes
                for (Socket cliente : clientes) {
                    try {
                        if (!cliente.isClosed()) {
                            InputStream in = cliente.getInputStream();
                            if (in.available() > 0) {
                                DataInputStream flujo_entrada = new DataInputStream(in);
                                String mensaje = flujo_entrada.readUTF();
                                System.out.println(mensaje);
                                enviarMensajeAClientes(mensaje);
                            }
                        } else {
                            Servidor.eliminarCliente(cliente);
                            parar = true;
                        }
                    } catch (IOException e) {
                        //Si ocurre una excepción, eliminamos el cliente de la lista
                        Servidor.eliminarCliente(cliente);
                        parar = true;
                    }
                }
            }
        }
    }

    private synchronized void enviarMensajeAClientes(String mensaje) {
        List<Socket> clientes = new ArrayList<>(Servidor.getClientes());
        for (Socket otroCliente : clientes) {
            try {
                if (!otroCliente.isClosed()) {
                    OutputStream out = otroCliente.getOutputStream();
                    DataOutputStream flujo_salida = new DataOutputStream(out);
                    flujo_salida.writeUTF(mensaje);
                    flujo_salida.flush();


                } else {
                    Servidor.eliminarCliente(otroCliente);

                }
            } catch (IOException e) {
                //Si ocurre una excepción, eliminamos el cliente de la lista
                Servidor.eliminarCliente(otroCliente);
            }
        }
    }
}

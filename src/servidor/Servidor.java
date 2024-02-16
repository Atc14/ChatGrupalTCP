package servidor;

import hilos.HiloServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static final List<Socket> clientes = new ArrayList<>();

    public synchronized static List<Socket> getClientes() {
        return clientes;
    }

    public synchronized static void agregarCliente(Socket cliente) {
        clientes.add(cliente);
    }

    public synchronized static void eliminarCliente(Socket cliente) {
        getClientes().remove(cliente);
    }

    public static void main(String[] args) {
        int puerto = 6004;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("Escucho en el puerto " + puerto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                Socket cliente = servidor.accept();
                agregarCliente(cliente);
                Thread t = new Thread(new HiloServer());
                t.start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}

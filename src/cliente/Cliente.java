package cliente;

import hilos.HiloCliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket sCliente = new Socket("localhost", 6004);
            System.out.println("Introduce el nombre de tu Usuario: ");
            String userName = sc.next();
            Thread t = new Thread(new HiloCliente(sCliente,userName));
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

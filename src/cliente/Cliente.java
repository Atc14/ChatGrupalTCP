package cliente;

import datos.Mensaje;
import hilos.HiloCliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket sCliente = new Socket("localhost", 6004);
            System.out.println("Introduce el nombre de tu Usuario: ");
            String nombre = sc.next();
            OutputStream out = sCliente.getOutputStream();
            DataOutputStream flujo_salida = new DataOutputStream(out);
            Thread t = new Thread(new HiloCliente(sCliente));
            t.start();
            while (true){
                if(!sCliente.isClosed()){
                    Mensaje m = new Mensaje(nombre, sc.next());
                    flujo_salida.writeUTF(m.getMensaje());
                    flujo_salida.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package cliente;

import hilos.HiloCliente;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket sCliente = new Socket("localhost", 6004);

            String userName = JOptionPane.showInputDialog("Debes introducir un nombre");
            if (userName == null || userName.isBlank()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre. La aplicación se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            Thread t = new Thread(new HiloCliente(sCliente,userName));
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

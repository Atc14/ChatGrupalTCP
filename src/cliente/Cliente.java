package cliente;

import datos.Usuario;
import hilos.HiloCliente;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket sCliente = new Socket("localhost", 6004);
            List<Usuario> usuarios = null;
            Usuario usuario = null;
            try {
                InputStream inaux = sCliente.getInputStream();
                ObjectInputStream flujo_entrada = new ObjectInputStream(inaux);
                usuarios = (List<Usuario>) flujo_entrada.readObject();

            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            String userName = JOptionPane.showInputDialog("Debes introducir un nombre");
            if (userName == null || userName.isBlank()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre. La aplicación se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            for (Usuario u : usuarios) {
                if (u.getUserName().equals(userName)) {
                    JOptionPane.showMessageDialog(null, "Ese nombre está repetido. La aplicación se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
            usuario = new Usuario(userName);
            try {
                OutputStream outaux = sCliente.getOutputStream();
                ObjectOutputStream flujo_entrada = new ObjectOutputStream(outaux);
                flujo_entrada.writeObject(usuario);
                flujo_entrada.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Thread t = new Thread(new HiloCliente(sCliente,userName));
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

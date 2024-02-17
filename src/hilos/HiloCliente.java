package hilos;

import datos.Chat;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HiloCliente implements Runnable {
    private final Socket sCliente;
    private final Chat chat;
    private final String userName;
    public HiloCliente(Socket sCliente, String userName) {
        this.sCliente = sCliente;
        this.userName = userName;
        this.chat = new Chat(sCliente, userName);
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            chat.setVisible(true);
            chat.setSize(800, 600);
            chat.setTitle("Chat Web : " + userName);
            chat.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            chat.setLocationRelativeTo(null);
            chat.setContentPane(chat.getTexto());
            chat.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        sCliente.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    chat.dispose();
                }

            });
        });
        try {
            InputStream in = sCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(in);
            while (!sCliente.isClosed()) {
                if (in.available() > 0) {
                    String recibido = flujo_entrada.readUTF();
                    if(recibido.contains("\\c")){
                        recibido = recibido.replace("\\c","");
                        String [] nombres =recibido.split(",");
                        for (String s : nombres) {
                            this.chat.agregarUsuarioLista(s);
                        }
                    }
                    else {
                        this.chat.agregarMensaje(recibido);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Socket cerrado");
        } finally {
            try {
                sCliente.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

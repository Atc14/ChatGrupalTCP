package hilos;

import datos.Chat;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HiloCliente implements Runnable{
    private final Socket sCliente;
    private final Chat chat;
    public HiloCliente(Socket sCliente, String userName){
        this.sCliente = sCliente;
        this.chat = new Chat(sCliente, userName);
        chat.setVisible(true);
        chat.setSize(800, 600);
        chat.setTitle("Chat Web : " + userName);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setLocationRelativeTo(null);
        chat.setContentPane(chat.getTexto());
    }
    @Override
    public void run() {
        try {
            InputStream in = sCliente.getInputStream();
            DataInputStream flujo_entrada = new DataInputStream(in);
            while (true) {
                if (in.available() > 0) {
                    this.chat.agregarMensaje(flujo_entrada.readUTF());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

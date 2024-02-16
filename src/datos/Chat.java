package datos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class Chat extends JFrame {
    private JPanel Texto;
    private JList listaUsuarios;
    private JTextArea mensajesChat;
    private JPanel panelInferior;
    private JTextField textField_endrada;
    private JButton button_enviar;
    private JScrollPane panelMensajes;
    private JScrollPane panelListaUsuarios;
    private Mensaje mensaje;


    public Chat(Socket cliente, String userName) {
        Texto.setBorder(new EmptyBorder(30, 10, 30, 10));
        mensajesChat.setEditable(false);
        mensajesChat.setMinimumSize(new Dimension(100, 0));
        panelMensajes.setBorder(new EmptyBorder(0, 0, 0, 10));
        panelListaUsuarios.setPreferredSize(new Dimension(175, 0));
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));

        try {
            if (!cliente.isClosed()) {
                OutputStream outaux = cliente.getOutputStream();
                DataOutputStream flujo_salida = new DataOutputStream(outaux);

                button_enviar.addActionListener(e -> {
                    if (!textField_endrada.getText().isBlank()) {
                        mensaje = new Mensaje(userName, textField_endrada.getText());
                        try {
                            flujo_salida.writeUTF(mensaje.getMensaje());
                            flujo_salida.flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        textField_endrada.setText("");
                    }
                });
            }
            textField_endrada.addActionListener(e -> button_enviar.doClick());
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public synchronized void agregarMensaje(String mensaje) {
        mensajesChat.append(mensaje);
    }

    public synchronized JPanel getTexto() {
        return Texto;
    }
}
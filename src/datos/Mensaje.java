package datos;

public class Mensaje {
    private final String nombre;
    private final String texto;

    public Mensaje(String nombre, String texto){
        this.nombre = nombre;
        this.texto = texto;
    }

    public synchronized String getMensaje(){
        return nombre + ": " + texto + "\n";
    }
}

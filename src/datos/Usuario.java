package datos;

public class Usuario {
    private String userName;

    public Usuario(String userName){
        this.userName = userName;
    }

    public synchronized String getUserName() {
        return userName;
    }
}

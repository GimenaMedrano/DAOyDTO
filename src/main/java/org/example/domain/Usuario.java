package org.example.domain;

public class Usuario {
    private int id_usuario;
    private String username;
    private String password;
    private byte[] salt; // Añade este atributo para almacenar el salt

    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.id_usuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

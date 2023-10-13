package org.example.datos;

import org.example.domain.Usuario;

import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UsuarioJDBD {
    private Connection conexionTransaccional;

    private static final String SQL_SELECT = "SELECT * FROM usuario";
    private static final String SQL_INSERT = "INSERT INTO usuario(username, salt, password) VALUES(?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario=?";
    private static final String SQL_UPDATE = "UPDATE usuario SET username=?, salt=?, password=? WHERE id_usuario=?";

    public UsuarioJDBD() {
    }

    public UsuarioJDBD(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    public List<Usuario> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            // Obtiene una conexión transaccional si está disponible, de lo contrario, obtiene una nueva conexión.
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            // Recorre los resultados y crea objetos Usuario a partir de los datos recuperados de la base de datos.
            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String username = rs.getString("username");
                byte[] salt = rs.getBytes("salt");
                String password = rs.getString("password");

                usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setUsername(username);
                usuario.setSalt(salt);
                usuario.setPassword(password);

                usuarios.add(usuario);
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);

            // Cierra la conexión si no se trata de una conexión transaccional.
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return usuarios;
    }

    public int insert(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            // Obtiene una conexión transaccional si está disponible, de lo contrario, obtiene una nueva conexión.
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();

            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getUsername());

            // Genera un salt aleatorio para cada usuario.
            byte[] salt = generateSalt();

            // Aplica un algoritmo de hash (SHA-256) a la contraseña y el salt y almacena los datos en la base de datos.
            stmt.setBytes(2, salt);
            stmt.setString(3, hashPassword(usuario.getPassword(), salt));

            // Ejecuta la inserción y registra el número de filas afectadas.
            rows = stmt.executeUpdate();
        } finally {
            Conexion.close(stmt);

            // Cierra la conexión si no es una conexión transaccional.
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return rows;
    }

    public int update(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            // Obtiene una conexión transaccional si está disponible, de lo contrario, obtiene una nueva conexión.
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, usuario.getUsername());
            stmt.setBytes(2, usuario.getSalt());

            // Aplica un algoritmo de hash (SHA-256) a la contraseña y el salt y actualiza los datos en la base de datos.
            stmt.setString(3, hashPassword(usuario.getPassword(), usuario.getSalt()));
            stmt.setInt(4, usuario.getIdUsuario());

            // Ejecuta la actualización y registra el número de filas actualizadas.
            rows = stmt.executeUpdate();
        } finally {
            Conexion.close(stmt);

            // Cierra la conexión si no es una conexión transaccional.
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return rows;
    }

    public int delete(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            // Obtiene una conexión transaccional si está disponible, de lo contrario, obtiene una nueva conexión.
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, usuario.getIdUsuario());

            // Ejecuta la eliminación y registra el número de filas eliminadas.
            rows = stmt.executeUpdate();
        } finally {
            Conexion.close(stmt);

            // Cierra la conexión si no es una conexión transaccional.
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return rows;
    }

    // Genera un salt aleatorio para cada usuario.
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Aplica un algoritmo de hash (SHA-256) a la contraseña y el salt.
    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña.", e);
        }
    }
}


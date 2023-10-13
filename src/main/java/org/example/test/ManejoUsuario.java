package org.example.test;

import org.example.datos.Conexion;
import org.example.datos.UsuarioJDBD;
import org.example.domain.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

public class ManejoUsuario {
    public static void main(String[] args) {
        // Definimos la variable conexión
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection(); // Obtiene una conexión a la base de datos.

            // El autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false); // Desactiva el modo autocommit para administrar la transacción manualmente.
            }

            UsuarioJDBD usuarioJdbc = new UsuarioJDBD(conexion); // Crea una instancia de la clase UsuarioJDBD.

            // Crear un nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername("Regina");
            nuevoUsuario.setPassword("hola");

            try {
                usuarioJdbc.insert(nuevoUsuario); // Inserta un nuevo usuario en la base de datos.
            } catch (SQLException e) {
                throw new RuntimeException(e); // Maneja las excepciones de SQL.
            }

            // Hacer commit de la transacción
            conexion.commit(); // Confirma la transacción en la base de datos.
            System.out.println("Se ha hecho commit de la transacción");

            // Vamos a actualizar sus datos
            Usuario cambioUsuario = new Usuario();
            cambioUsuario.setIdUsuario(1);
            cambioUsuario.setUsername("Gimena");
            cambioUsuario.setPassword("223");

            try {
                usuarioJdbc.update(cambioUsuario); // Actualiza los datos del usuario en la base de datos.
            } catch (SQLException e) {
                throw new RuntimeException(e); // Maneja las excepciones de SQL.
            }

            // Hacer commit de la transacción
            conexion.commit(); // Confirma la transacción nuevamente.
            System.out.println("Se ha hecho commit de la transacción");

        } catch (SQLException ex) {
            ex.printStackTrace(System.out); // Maneja las excepciones de SQL e imprime información de diagnóstico.
            System.out.println("Entramos al rollback");

            try {
                if (conexion != null) {
                    conexion.rollback(); // Entra en la cláusula de rollback para deshacer la transacción en caso de error.
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out); // Maneja las excepciones de SQL durante el rollback.
            }
        }
    }
}


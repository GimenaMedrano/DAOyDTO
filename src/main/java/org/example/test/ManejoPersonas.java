package org.example.test;

import org.example.datos.Conexion;
import org.example.datos.PersonaJDBD;
import org.example.domain.Persona;

import java.sql.*;

public class ManejoPersonas {

    public static void main(String[] args) {
        //hacer pruebas de los metodos de persona
        Connection conexion = null;
        PersonaJDBD personaJdbc = new PersonaJDBD();
        try {
            conexion = Conexion.getConnection();
            //el autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            Persona nuevaPersona = new Persona();
            nuevaPersona.setNombre("melvin");
            nuevaPersona.setApellido("tzun");
            nuevaPersona.setEmail("mtzune@gmail.com");
            personaJdbc.insert(nuevaPersona);

            if (nuevaPersona.getNombre().equals("melvin")){
                conexion.rollback();
                throw new SQLException("No se puede insertar la persona, se hizo rollback");

            } else {
                System.out.println("se ha insertado con exito y se hará commit");
                conexion.commit();

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }

    }
}

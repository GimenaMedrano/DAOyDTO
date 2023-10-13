package org.example.datos;
import org.example.domain.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// TRANSACCIONES DE LA BASE DE DATOS DAO
public class PersonaJDBD {
     //en esta capa de datos manejamos la manipulacion de datos del objeto persona
     //la variable conexionTransaccional es la que apunta a la bse de datos para sus transacciones
     private Connection conexionTransaccional;

     //constantes para la manipulacion de informacion
     private static final String SQL_SELECT = "SELECT id_persona, nombre, apellido, email, telefono FROM persona";
     private static final String SQL_INSERT = "INSERT INTO persona(nombre, apellido, email, telefono) VALUES(?, ?, ?, ?)";
     private static final String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona = ?";
     private static final String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";

     //CONSTRUCTOR EN BLANCO
     public PersonaJDBD() {

     }

     //constructor, recibe parametro del manejador de transacciones
     public PersonaJDBD(Connection conexionTransaccional) {
          this.conexionTransaccional = conexionTransaccional;
     }

     //hace la consulta a la base de datos, retorna un objeto de tipo persona en una lista
     public List<Persona> select() throws SQLException {
          Connection conn = null;
          PreparedStatement stmt = null;
          ResultSet rs = null;
          Persona persona = null;
          List<Persona> personas = new ArrayList<Persona>();

          try{
               //ternario para comparar si la conexion esta activa.
               conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
               stmt = conn.prepareStatement(SQL_SELECT);
               rs = stmt.executeQuery();
               while(rs.next()){
                    int id_persona = rs.getInt("id_persona");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String email = rs.getString("email");
                    String telefono = rs.getString("telefono");

                    //agrego informacion de una fila a un objeto de tipo persona (daoPersona)
                    //y lo agrego a la lista de personas
                    persona = new Persona();
                    persona.setId_persona(id_persona);
                    persona.setNombre(nombre);
                    persona.setApellido(apellido);
                    persona.setEmail(email);
                    persona.setTelefono(telefono);

                    personas.add(persona); //se agrega a la lista de personas
               }

          } finally {
               Conexion.close(rs);
               Conexion.close(stmt);
               if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
               }
          }
          return personas;
     }


     //insertar datos en la tabla persona
     public int insert(Persona persona) throws SQLException {
          Connection conn = null;
          PreparedStatement stmt = null;
          int registros = 0;

          try {
               //ternario para comparar si la conexion esta activa.
               conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
               stmt = conn.prepareStatement(SQL_INSERT);
               //se agregan los parametros a la consulta
               stmt.setString(1, persona.getNombre());
               stmt.setString(2, persona.getApellido());
               stmt.setString(3, persona.getEmail());
               stmt.setString(4, persona.getTelefono());

               //se ejecuta la consulta
               registros = stmt.executeUpdate();
          } finally {
               Conexion.close(stmt);
               if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
               }
          }
          return registros;
     }


     //actualizar datos en la tabla persona
     public int update(Persona persona) throws SQLException {
          Connection conn = null;
          PreparedStatement stmt = null;
          int registros = 0;

          try {
               //ternario para comparar si la conexion esta activa.

               conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
               stmt = conn.prepareStatement(SQL_UPDATE);
               //se agregan los parametros a la consulta
               stmt.setString(1, persona.getNombre());
               stmt.setString(2, persona.getApellido());
               stmt.setString(3, persona.getEmail());
               stmt.setString(4, persona.getTelefono());
               stmt.setInt(5, persona.getId_persona());

               //se ejecuta la consulta
               registros = stmt.executeUpdate();
          } finally {
               Conexion.close(stmt);
               if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
               }
          }
          return registros;
     }

     //ELIMINAR REGISTROS
     public int delete(Persona persona) throws SQLException {
          Connection conn = null;
          PreparedStatement stmt = null;
          int registros = 0;

          try {
               //ternario para comparar si la conexion esta activa.
               conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
               stmt = conn.prepareStatement(SQL_DELETE);
               //se agregan los parametros a la consulta
               stmt.setInt(1, persona.getId_persona());

               //se ejecuta la consulta
               registros = stmt.executeUpdate();
          } finally {
               Conexion.close(stmt);
               if (this.conexionTransaccional == null) {
                    Conexion.close(conn);
               }
          }
          return registros;
     }


}

package com.example.proyecto1;


import com.example.proyecto1.Usuarios; // Importa tu modelo de Usuarios (asegúrate de la ruta)
import com.example.proyecto1.ConexionBDRegistro; // Asegúrate de que esta ruta de importación sea correcta

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioBD { // Renombrado de UsuarioDAO a UsuarioBD

    //Valida las credenciales de un usuario en la base de datos.

     public boolean validarCredenciales(String correo, String contrasena) {
         //Cuenta el número de filas que cumplen la condición. Si devuelve 1, significa que encontró un usuario con ese correo y contraseña. Si devuelve 0, no lo encontró.
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo_institucional = ? AND contrasena = ?";
        try (Connection conn = ConexionBDRegistro.getConnection(); // Obtiene la conexión a la base de datos
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {//Captura cualquier excepción SQL que pueda ocurrir durante la operación de la base de datos
            System.err.println("Error al validar credenciales: " + e.getMessage());//imprime el mensaje de error
        }
        return false;//Si ocurre una excepción o no se encuentran las credenciales, la funcion devuelve false
    }

    //Obtiene todos los datos de un usuario a partir de su correo institucional.

    public Usuarios obtenerUsuarioPorCorreoInstitucional(String correoInstitucional) {
         //Recupera todos los detalles de un usuario de la base de datos basándose en su correoInstitucional
        String query = "SELECT nombre, apellido_paterno, apellido_materno, grado_academico, correo_personal, numero_telefono, correo_institucional, contrasena FROM usuarios WHERE correo_institucional = ?";
        try (Connection conn = ConexionBDRegistro.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correoInstitucional);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuarios( // Usa el constructor de Usuarios
                        rs.getString("correo_institucional"), // Primero el correo institucional
                        rs.getString("nombre"),               // Luego el nombre
                        rs.getString("apellido_paterno"),    // Luego apellido paterno
                        rs.getString("apellido_materno"),    // Luego apellido materno
                        rs.getString("grado_academico"),      // Luego grado académico
                        rs.getString("correo_personal"),      // Luego correo personal
                        rs.getString("numero_telefono"),      // Luego número de teléfono
                        rs.getString("contrasena")            // Finalmente la contraseña
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo institucional: " + e.getMessage());
        }
        return null;
    }

    //Actualiza el correo personal y el número de teléfono de un usuario.
    public boolean actualizarUsuario(Usuarios usuario) {
        // Solo actualiza correo_personal y numero_telefono
        String query = "UPDATE usuarios SET correo_personal = ?, numero_telefono = ? WHERE correo_institucional = ?";
        // Especifica las columnas a actualizar y sus nuevos valores
        try (Connection conn = ConexionBDRegistro.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getCorreoPersonal());// Establece el nuevo correo personal del usuario.
            stmt.setString(2, usuario.getNumeroTelefono()); // Establece el nuevo número de teléfono del usuario.
            stmt.setString(3, usuario.getCorreoInstitucional());//Establece el correo institucional que se usará en la cláusula WHERE para identificar al usuario a actualizar.

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;//Si rowsAffected es mayor que 0, significa que al menos una fila fue actualizada exitosamente, y el método devuelve true

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
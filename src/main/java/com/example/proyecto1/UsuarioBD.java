package com.example.proyecto1;


import com.example.proyecto1.Usuarios; // Importa tu modelo de Usuarios (asegúrate de la ruta)
import com.example.proyecto1.ConexionBDRegistro; // Asegúrate de que esta ruta de importación sea correcta

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioBD { // Renombrado de UsuarioDAO a UsuarioBD

    /**
     * Valida las credenciales de un usuario en la base de datos.
     * @param correo Correo institucional del usuario.
     * @param contrasena Contraseña.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarCredenciales(String correo, String contrasena) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo_institucional = ? AND contrasena = ?";
        try (Connection conn = ConexionBDRegistro.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene todos los datos de un usuario a partir de su correo institucional.
     * @param correoInstitucional Correo institucional del usuario.
     * @return Un objeto Usuarios si se encuentra, null si no.
     */
    // Mantiene el nombre singular para coincidir con tu PerfilController actual
    public Usuarios obtenerUsuarioPorCorreoInstitucional(String correoInstitucional) {
        String query = "SELECT nombre, apellido_paterno, apellido_materno, grado_academico, correo_personal, numero_telefono, correo_institucional, contrasena FROM usuarios WHERE correo_institucional = ?";
        try (Connection conn = ConexionBDRegistro.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correoInstitucional);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuarios( // Usa el constructor de Usuarios
                        rs.getString("nombre"),
                        rs.getString("apellido_paterno"),
                        rs.getString("apellido_materno"),
                        rs.getString("correo_institucional"),
                        rs.getString("correo_personal"),
                        rs.getString("numero_telefono"), // Coincide con el nombre de la columna y el nuevo getter
                        rs.getString("grado_academico"),
                        rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo institucional: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el correo personal y el número de teléfono de un usuario.
     * @param usuario El objeto Usuarios con los datos actualizados y el correo institucional para la identificación.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    // Mantiene el nombre singular para coincidir con tu PerfilController actual
    public boolean actualizarUsuario(Usuarios usuario) {
        // Solo actualiza correo_personal y numero_telefono
        String query = "UPDATE usuarios SET correo_personal = ?, numero_telefono = ? WHERE correo_institucional = ?";
        try (Connection conn = ConexionBDRegistro.getConnection(); // Usa ConexionBDRegistro
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getCorreoPersonal());
            stmt.setString(2, usuario.getNumeroTelefono()); // Coincide con el nuevo getter
            stmt.setString(3, usuario.getCorreoInstitucional());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
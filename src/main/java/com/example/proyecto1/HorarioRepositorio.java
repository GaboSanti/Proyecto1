package com.example.proyecto1;


import com.example.proyecto1.model.HorarioUsuariosPorDia;
import com.google.gson.Gson;
import java.sql.*;

public class HorarioRepositorio {
    private final Gson gson = new Gson();

    /**
     * Guarda el objeto de horario en la tabla 'horario_json'
     */
    public boolean guardarHorario(HorarioUsuariosPorDia horario) {
        String json = gson.toJson(horario);

        String sql = "INSERT INTO horario_json (correo, datos_json) VALUES (?, ?)";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horario.getCorreo());
            stmt.setString(2, json);

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera el horario guardado desde la base por correo
     */
    public HorarioUsuariosPorDia obtenerHorarioPorCorreo(String correo) {
        String sql = "SELECT datos_json FROM horario_json WHERE correo = ?";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String json = rs.getString("datos_json");
                return gson.fromJson(json, HorarioUsuariosPorDia.class);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

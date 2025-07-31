package com.example.proyecto1;

import com.example.proyecto1.model.HorarioUsuariosPorDia;
import com.google.gson.Gson;
import java.sql.*;
import java.io.StringReader;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

public class HorarioRepositorio {
    private final Gson gson = new Gson();

    public boolean guardarHorario(HorarioUsuariosPorDia horario) {
        String json = gson.toJson(horario.getHorarioMap());

        String sqlUpdate = "UPDATE HORARIO SET DIA_HORA = ? WHERE CORREO_INSTITUCIONAL = ?";
        String sqlInsert = "INSERT INTO HORARIO (CORREO_INSTITUCIONAL, DIA_HORA) VALUES (?, ?)";

        try (Connection conn = ConexionBDRegistro.getConnection()) {

            try (PreparedStatement stUpdate = conn.prepareStatement(sqlUpdate)) {
                stUpdate.setCharacterStream(1, new StringReader(json));
                stUpdate.setString(2, horario.getCorreo());
                int filasUpdate = stUpdate.executeUpdate();
                if (filasUpdate > 0) return true;
            }

            try (PreparedStatement stInsert = conn.prepareStatement(sqlInsert)) {
                stInsert.setString(1, horario.getCorreo());
                stInsert.setCharacterStream(2, new StringReader(json));
                int filasInsert = stInsert.executeUpdate();
                return filasInsert > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HorarioUsuariosPorDia obtenerHorarioPorCorreo(String correo) {
        String sql = "SELECT DIA_HORA FROM HORARIO WHERE CORREO_INSTITUCIONAL = ?";

        try (Connection conn = ConexionBDRegistro.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String json = rs.getString("DIA_HORA");
                Map<String, List<Integer>> horarioMap = gson.fromJson(
                        json, new TypeToken<Map<String, List<Integer>>>() {}.getType()
                );
                return new HorarioUsuariosPorDia(correo, horarioMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


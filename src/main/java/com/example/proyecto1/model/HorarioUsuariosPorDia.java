package com.example.proyecto1.model;

import java.util.List;
import java.util.Map;

public class HorarioUsuariosPorDia {

    private String correo;
    private Map<String, List<Integer>> horarioMap;

    public HorarioUsuariosPorDia(String correo, Map<String, List<Integer>> horarioMap) {
        this.correo = correo;
        this.horarioMap = horarioMap;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Map<String, List<Integer>> getHorarioMap() {
        return horarioMap;
    }

    public void setHorarioMap(Map<String, List<Integer>> horarioMap) {
        this.horarioMap = horarioMap;
    }
}







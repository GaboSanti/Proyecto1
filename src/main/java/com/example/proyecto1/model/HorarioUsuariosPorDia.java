package com.example.proyecto1.model;


import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class HorarioUsuariosPorDia {

        private String correo;

        @SerializedName("horario")
        private Map<String, List<Integer>> horarioMap;

        // Constructor
        public HorarioUsuariosPorDia(String correo, Map<String, List<Integer>> horarioMap) {
            this.correo = correo;
            this.horarioMap = horarioMap;
        }

        // Getter: correo
        public String getCorreo() {
            return correo;
        }

        // Setter: correo
        public void setCorreo(String correo) {
            this.correo = correo;
        }

        // Getter: mapa de horarios
        public Map<String, List<Integer>> getHorarioMap() {
            return horarioMap;
        }

        // Setter: mapa de horarios
        public void setHorarioMap(Map<String, List<Integer>> horarioMap) {
            this.horarioMap = horarioMap;
        }
    }

    //hola



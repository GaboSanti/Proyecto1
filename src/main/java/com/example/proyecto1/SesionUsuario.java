package com.example.proyecto1;

public class SesionUsuario {
    private static String correoInstitucional;

    public static void setCorreoInstitucional(String correo) {
        correoInstitucional = correo;
    }

    public static String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public static void limpiarSesion() {
        correoInstitucional = null;
    }
}

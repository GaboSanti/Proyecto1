package com.example.proyecto1;
//se utiliza para almacenar el correoInstitucional despues de cada inicio de sesión
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

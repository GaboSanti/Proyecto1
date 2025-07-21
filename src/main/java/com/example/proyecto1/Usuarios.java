package com.example.proyecto1;

public class Usuarios {

    private String correoInstitucional;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String gradoAcademico;
    private String correoPersonal;
    private String numeroTelefono;
    private String contraseña;

    // Constructor para crear un nuevo usuario (sin ID, ya que es auto-generado en BD)
    public Usuarios(String correoInstitucional, String nombre, String apellidoPaterno, String apellidoMaterno,
                    String gradoAcademico, String correoPersonal, String numeroTelefono, String contraseña) {
        this.correoInstitucional = correoInstitucional;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.gradoAcademico = gradoAcademico;
        this.correoPersonal = correoPersonal;
        this.numeroTelefono = numeroTelefono;
        this.contraseña = contraseña;
    }


    // Getters
    public String getCorreoInstitucional() { return correoInstitucional; }
    public String getNombre() { return nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public String getGradoAcademico() { return gradoAcademico; }
    public String getCorreoPersonal() { return correoPersonal; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public String getContrasena() { return contraseña; }

    // Setters (solo si los necesitas, ej. para actualizar propiedades)
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    public void setGradoAcademico(String gradoAcademico) { this.gradoAcademico = gradoAcademico; }
    public void setCorreoPersonal(String correoPersonal) { this.correoPersonal = correoPersonal; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
    public void setContrasena(String contrasena) { this.contraseña = contraseña; }

}

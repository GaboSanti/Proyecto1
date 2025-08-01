package com.example.proyecto1;

import com.example.proyecto1.UsuarioBD; // Importa UsuarioBD
import com.example.proyecto1.Usuarios; // Importa  Usuarios
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;//para la fecha
import java.time.format.DateTimeFormatter;//para formato de fechas
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.fxml.Initializable;



public class AdminController implements Initializable {

    @FXML private Button btnAsignar;
    @FXML private Button btnExportar;
    @FXML private DatePicker fecha_inicio;
    @FXML private DatePicker fecha_cierre;
    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblFecha;
    @FXML private Label lblPeriodo;
    @FXML private Label lblNombre;

    private UsuarioBD usuarioBD;   //Una instancia de la clase UsuarioBD que se utiliza para interactuar con la base de datos
    private Usuarios usuariosActual;//Un objeto de la clase Usuarios que contendrá la información del usuario actualmente logueado
    String correoSesion = SesionUsuario.getCorreoInstitucional();//Obtiene el correo institucional del usuario de una clase SesionUsuario


    @FXML
    public void initialize(URL url, ResourceBundle rb){
        usuarioBD = new UsuarioBD();
        mostrarFechaActual();// Llama a la funcion para mostrar la fecha actual en la interfaz
        mostrarNombreUsuarioLogueado();
    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();//obtiene la fecha actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");//define el formato de fecha
        String fechaFormateada = fechaActual.format(formatter);//formatea la fecha para tenerla como string
        lblFecha.setText(fechaFormateada);//visualiza la fecha
    }

    public void onAsignarFecha() {
        LocalDate fechaSeleccionada = fecha_inicio.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String fechaInicio = fechaSeleccionada.format(formatter);

        LocalDate fechaSeleccionadaFinal = fecha_cierre.getValue();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String fechaCierre = fechaSeleccionadaFinal.format(formatter1);
        lblPeriodo.setText("Periodo asignado: "+fechaInicio +" al "+ fechaCierre);

    }

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }


    @FXML
    protected void onIrAdmin(ActionEvent event) throws IOException {
        cambiarVentana("Admin.fxml", event, "Administrador");
    }

    @FXML
    protected void onIrHorario(ActionEvent event) throws IOException {
        cambiarVentana("Horario.fxml", event, "Horario");
    }

    @FXML
    protected void onIrPerfil(ActionEvent event) throws IOException {
        cambiarVentana("Perfil.fxml", event, "Perfil");
    }
    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }

    private void mostrarNombreUsuarioLogueado() {
        if (correoSesion != null && !correoSesion.isEmpty()) {
            // Se realiza una consulta a la BD SOLO para obtener el nombre completo
            Usuarios usuarioSesion = usuarioBD.obtenerUsuarioPorCorreoInstitucional(correoSesion);

            if (usuarioSesion != null) {
                String nombreCompleto = (usuarioSesion.getNombre() != null ? usuarioSesion.getNombre() : "") + " " +
                        (usuarioSesion.getApellidoPaterno() != null ? usuarioSesion.getApellidoPaterno() : "" + " "+
                        (usuarioSesion.getApellidoMaterno() != null ? usuarioSesion.getApellidoMaterno() : "" + " ")

                        );
                lblNombre.setText(nombreCompleto.trim());
            } else {
                lblNombre.setText("Usuario Desconocido");
            }
        } else {
            lblNombre.setText("Sesión No Iniciada");
        }
    }

}

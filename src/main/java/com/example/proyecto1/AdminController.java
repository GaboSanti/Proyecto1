package com.example.proyecto1;

import javafx.fxml.FXML;
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
import java.time.LocalDate;//para la fecha
import java.time.format.DateTimeFormatter;//para formato de fechas
import javafx.scene.Node;


public class AdminController {

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



    @FXML
    public void initialize() {//para obtener la fecha de la computadora y mostrarla en un label
        // Obtener la fecha actual de la computadora
        LocalDate fechaActual = LocalDate.now();

        // Definir el formato deseado para la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");

        // Formatear la fecha actual a String
        String fechaFormateada = fechaActual.format(formatter);

        // Establecer el texto del Label con la fecha actual
        lblFecha.setText(fechaFormateada);
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

}

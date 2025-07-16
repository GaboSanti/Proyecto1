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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void onAsignarFecha() {
        LocalDate fechaSeleccionada = fecha_inicio.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
        String fechaInicio = fechaSeleccionada.format(formatter);

        LocalDate fechaSeleccionadaFinal = fecha_cierre.getValue();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM");
        String fechaCierre = fechaSeleccionadaFinal.format(formatter1);
        lblFecha.setText(fechaInicio +"/"+ fechaCierre);

    }



    @FXML
    protected void onIrHorario(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Horario.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Horario");
        stage.show();
    }

    @FXML
    protected void onIrPerfil(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Perfil");
        stage.show();
    }


}

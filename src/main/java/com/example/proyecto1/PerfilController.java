package com.example.proyecto1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class PerfilController {

    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblCorreo_institucional;
    @FXML private Label lblCorreo_personal;
    @FXML private Label lblTelefono;
    @FXML private ComboBox comBox_grado;
    @FXML private Button btnModificar_correo;
    @FXML private Button btnModificar_telefono;

    @FXML private TextField correoField;
    @FXML private TextField telefonoField;

    public void onModificarCorreo() {
        correoField.setText(lblCorreo_personal.getText());
        correoField.setVisible(true);
        lblCorreo_personal.setVisible(false);
        correoField.requestFocus();
    }

    @FXML
    public void onModificarTelefono() {
        telefonoField.setText(lblTelefono.getText());
        telefonoField.setVisible(true);
        lblTelefono.setVisible(false);
        telefonoField.requestFocus();
    }

    @FXML
    public void onGuardar() {
        String nuevoCorreo = correoField.getText().trim();
        lblCorreo_personal.setText(nuevoCorreo);
        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true);

        String nuevoTelefono = telefonoField.getText().trim();
        lblTelefono.setText(nuevoTelefono);
        telefonoField.setVisible(false);
        lblTelefono.setVisible(true);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Modificacion exitosa", "Datos modificados correctamente ");

    }


    @FXML
    protected void onIrAdmin(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));//para llamar a uhna ventana
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Administrador");
        stage.show();

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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); //Espera que el usuario cierre la ventana antes de seguir
    }
    public void setCorreo(String correo) {
        lblCorreo_institucional.setText(correo); // Muestra el correo en el label
    }

}

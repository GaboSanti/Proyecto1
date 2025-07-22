package com.example.proyecto1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.io.IOException;

import java.time.LocalDate; // ¡Importa LocalDate!
import java.time.format.DateTimeFormatter;

public class PerfilController {

    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblFecha;
    @FXML private Label lblCorreo_institucional;
    @FXML private Label lblCorreo_personal;
    @FXML private Label lblTelefono;
    @FXML private ComboBox<String> comBox_grado;
    @FXML private Button btnModificar_correo;
    @FXML private Button btnModificar_telefono;
    @FXML private TextField correoField;
    @FXML private TextField telefonoField;
    @FXML private Label lblApellido_PaternoUsuario;
    @FXML private Label lblApellido_MaternoUsuario;





    @FXML
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
        if (!nuevoCorreo.isEmpty()) {
            lblCorreo_personal.setText(nuevoCorreo);
        }
        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true);

        String nuevoTelefono = telefonoField.getText().trim();
        if (!nuevoTelefono.isEmpty()) {
            lblTelefono.setText(nuevoTelefono);
        }
        telefonoField.setVisible(false);
        lblTelefono.setVisible(true);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Modificación exitosa", "Datos modificados correctamente");
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

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void setCorreo(String correo) {
        lblCorreo_institucional.setText(correo);
    }
}

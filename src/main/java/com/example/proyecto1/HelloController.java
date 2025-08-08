package com.example.proyecto1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;
import java.sql.*;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;



public class HelloController {


    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtPassword;
    @FXML
    private AnchorPane paneLogin;


    @FXML
    private void initialize(){
        Platform.runLater(() -> {
            Stage stage = (Stage) paneLogin.getScene().getWindow();
            stage.setMaximized(true);
            stage.centerOnScreen();

            // para que crezca con la ventana
            paneLogin.prefWidthProperty().bind(stage.widthProperty());
            paneLogin.prefHeightProperty().bind(stage.heightProperty());
        });
    }



    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        String correoIngresado = txtCorreo.getText();
        String passwordIngresado = txtPassword.getText();

        if (validarFormulario(correoIngresado, passwordIngresado)) {
            if (compararDatos(correoIngresado, passwordIngresado)) {
                SesionUsuario.setCorreoInstitucional(correoIngresado);
                cambiarVentana("Horario.fxml", event, "Horario");
                mostrarAlerta(Alert.AlertType.INFORMATION, "Información", "Selecciona tu horario disponible dando click en las casillas.");

            }else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Datos incorrectas \nEl correo o contraseña son incorrectos.");
            }
        }
    }

    @FXML
    protected void irRegistro(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registro.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setScene(new Scene(root));
        stage.setTitle("Registro");
    }

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.show();
    }

    private boolean validarFormulario(String correoIngresado, String passwordIngresado ) {
        if (correoIngresado.isEmpty() || passwordIngresado.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Campos vacíos \nPor favor complete todos los campos.");
            return false;
        }
        return true;
    }

    private boolean compararDatos(String correo, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBDRegistro.getConnection();
            String sql = "SELECT COUNT(*) FROM usuarios WHERE correo_institucional = ? AND contrasena = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error de base de datos \nOcurrió un error al consultar las credenciales.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
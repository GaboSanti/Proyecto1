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

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtPassword;

    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        String correoIngresado = txtCorreo.getText();
        String passwordIngresado = txtPassword.getText();

        if (validarFormulario(correoIngresado, passwordIngresado)) {
            if (compararDatos(correoIngresado, passwordIngresado)) {
                cambiarVentana("Horario.fxml", event, "Horario");
                SesionUsuario.setCorreoInstitucional(correoIngresado);
            }else {
                mostrarAlerta("Error", "Datos incorrectas",
                        "El correo o contraseña son incorrectos.");
            }
        }
    }

    @FXML
    protected void irRegistro(ActionEvent event) throws IOException {
        cambiarVentana("Registro.fxml", event, "Registro");
    }

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }

    private boolean validarFormulario(String correoIngresado, String passwordIngresado ) {
        if (correoIngresado.isEmpty() || passwordIngresado.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos",
                    "Por favor complete todos los campos.");
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
            mostrarAlerta("Error", "Error de base de datos",
                    "Ocurrió un error al consultar las credenciales.");
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

    private void mostrarAlerta(String titulo, String header, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
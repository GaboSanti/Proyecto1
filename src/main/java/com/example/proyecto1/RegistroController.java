package com.example.proyecto1;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido1;
    @FXML private TextField txtApellido2;
    @FXML private ComboBox<String> comboBox;
    @FXML private TextField txtCorreo1;
    @FXML private TextField txtCorreo2;
    @FXML private TextField txtTelefono;

    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnTogglePassword;

    @FXML private PasswordField txtConfirmarPassword;
    @FXML private Label lblPasswordHint;
    @FXML private Label lblConfirmarPasswordHint;

    @FXML private Button btn_registrar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.setItems(FXCollections.observableArrayList("Licenciatura", "Maestría", "Doctorado"));

        btn_registrar.setOnAction(event -> validarFormulario());

        btnTogglePassword.setOnAction(e -> togglePasswordVisibility());
        txtPassword.textProperty().bindBidirectional(txtPasswordVisible.textProperty());

        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> validarSeguridadPassword(newVal));
        txtPasswordVisible.textProperty().addListener((obs, oldVal, newVal) -> validarSeguridadPassword(newVal));

        txtConfirmarPassword.textProperty().addListener((obs, oldVal, newVal) -> validarCoincidenciaPassword());

        txtTelefono.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (!e.getCharacter().matches("\\d") || txtTelefono.getText().length() >= 10) {
                e.consume();
            }
        });
    }

    private void togglePasswordVisibility() {
        boolean visible = txtPasswordVisible.isVisible();
        txtPasswordVisible.setVisible(!visible);
        txtPasswordVisible.setManaged(!visible);
        txtPassword.setVisible(visible);
        txtPassword.setManaged(visible);
    }

    private void validarFormulario() {
        if (txtNombre.getText().isEmpty() || txtApellido1.getText().isEmpty() || txtApellido2.getText().isEmpty() ||
                comboBox.getValue() == null || txtCorreo1.getText().isEmpty() || txtCorreo2.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() || txtPassword.getText().isEmpty() || txtConfirmarPassword.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos.");
            return;
        }

        if (txtTelefono.getText().length() != 10) {
            marcarError(txtTelefono, true);
            mostrarAlerta(Alert.AlertType.ERROR, "Teléfono inválido", "Debe tener 10 dígitos.");
            return;
        } else {
            marcarError(txtTelefono, false);
        }

        if (!esPasswordSegura(txtPassword.getText())) {
            marcarError(txtPassword, true);
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseña insegura", "Debe tener mayúscula, minúscula, número y símbolo.");
            return;
        }

        if (!txtPassword.getText().equals(txtConfirmarPassword.getText())) {
            marcarError(txtConfirmarPassword, true);
            lblConfirmarPasswordHint.setText("Las contraseñas no coinciden.");
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        Usuarios nuevoUsuario = new Usuarios(
                txtCorreo1.getText(),
                txtNombre.getText(),
                txtApellido1.getText(),
                txtApellido2.getText(),
                comboBox.getValue(),
                txtCorreo2.getText(),
                txtTelefono.getText(),
                txtPassword.getText()
        );

        if (registrarUsuario(nuevoUsuario)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "¡Usuario registrado correctamente!");
            limpiarCampos();
        }
    }

    private boolean registrarUsuario(Usuarios usuario) {
        String sql = "INSERT INTO usuarios (correo_institucional, nombre, apellido_paterno, apellido_materno, grado_academico, correo_personal, numero_telefono, contraseña) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBDRegistro.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getCorreoInstitucional());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellidoPaterno());
            pstmt.setString(4, usuario.getApellidoMaterno());
            pstmt.setString(5, usuario.getGradoAcademico());
            pstmt.setString(6, usuario.getCorreoPersonal());
            pstmt.setString(7, usuario.getNumeroTelefono());
            pstmt.setString(8, usuario.getContrasena());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Correo duplicado", "Ese correo institucional ya está registrado.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al registrar.");
            }
            return false;
        }
    }

    private void validarSeguridadPassword(String password) {
        if (esPasswordSegura(password)) {
            lblPasswordHint.setText("");
            marcarError(txtPassword, false);
        } else {
            lblPasswordHint.setText("Mínimo 8 caracteres, mayúscula, minúscula, número y símbolo.");
            marcarError(txtPassword, true);
        }
    }

    private void validarCoincidenciaPassword() {
        if (!txtConfirmarPassword.getText().equals(txtPassword.getText())) {
            lblConfirmarPasswordHint.setText("Las contraseñas no coinciden.");
            marcarError(txtConfirmarPassword, true);
        } else {
            lblConfirmarPasswordHint.setText("");
            marcarError(txtConfirmarPassword, false);
        }
    }

    private boolean esPasswordSegura(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");
    }

    private void marcarError(Control campo, boolean error) {
        campo.setStyle(error ? "-fx-border-color: red; -fx-border-width: 2;" : "");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido1.clear();
        txtApellido2.clear();
        comboBox.getSelectionModel().clearSelection();
        txtCorreo1.clear();
        txtCorreo2.clear();
        txtTelefono.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        txtConfirmarPassword.clear();
    }
}

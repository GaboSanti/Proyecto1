package com.example.proyecto1;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
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

        // Se cambió para que el manejador de eventos reciba el ActionEvent
        btn_registrar.setOnAction(this::validarFormulario);

        btnTogglePassword.setOnAction(e -> togglePasswordVisibility());
        // Se asegura que ambos campos se actualicen mutuamente
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> validarSeguridadPassword(newVal));

        txtConfirmarPassword.textProperty().addListener((obs, oldVal, newVal) -> validarCoincidenciaPassword());

        txtTelefono.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (!e.getCharacter().matches("\\d") || txtTelefono.getText().length() >= 10) {
                e.consume();
            }
        });

        // Configuración inicial de la visibilidad
        txtPasswordVisible.setVisible(false);
        txtPasswordVisible.setManaged(false);
        txtPassword.setVisible(true);
        txtPassword.setManaged(true);
    }

    // Método corregido para alternar la visibilidad de la contraseña
    private void togglePasswordVisibility() {
        boolean isPasswordVisible = txtPassword.isVisible();

        txtPasswordVisible.setVisible(isPasswordVisible);
        txtPasswordVisible.setManaged(isPasswordVisible);
        txtPassword.setVisible(!isPasswordVisible);
        txtPassword.setManaged(!isPasswordVisible);

        // Se asegura de que el foco se mantenga en el campo visible
        if (isPasswordVisible) {
            txtPasswordVisible.requestFocus();
        } else {
            txtPassword.requestFocus();
        }
    }

    // El método ahora recibe el ActionEvent para poder acceder a la ventana actual
    private void validarFormulario(ActionEvent event) {
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

        // Se modificaron los constructores para usar .trim() en todos los campos de texto
        Usuarios nuevoUsuario = new Usuarios(
                txtCorreo1.getText().trim(),
                txtNombre.getText().trim(),
                txtApellido1.getText().trim(),
                txtApellido2.getText().trim(),
                comboBox.getValue().trim(),
                txtCorreo2.getText().trim(),
                txtTelefono.getText().trim(),
                txtPassword.getText()
        );

        if (registrarUsuario(nuevoUsuario)) {
            // Se modifica la lógica para manejar la alerta y el cambio de escena
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Registro exitoso");
            alerta.setHeaderText(null);
            alerta.setContentText("¡Usuario registrado correctamente!");

            // Captura el resultado de la alerta
            Optional<ButtonType> result = alerta.showAndWait();

            // Si el usuario hace clic en Aceptar, se cambia de escena
            if (result.isPresent() && result.get() == ButtonType.OK) {
                limpiarCampos();
                cambiarEscena(event, "hello-view.fxml");
            }
        }
    }

    private boolean registrarUsuario(Usuarios usuario) {
        String sql = "INSERT INTO usuarios (correo_institucional, nombre, apellido_paterno, apellido_materno, grado_academico, correo_personal, numero_telefono, contrasena) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            // Se mejora el manejo de errores para mostrar más información
            if (e.getErrorCode() == 1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Correo duplicado", "Ese correo institucional ya está registrado.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error al registrar.");
                // Imprime el error completo para facilitar la depuración
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("Error Code: " + e.getErrorCode());
                System.out.println("Message: " + e.getMessage());
                e.printStackTrace();
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

        marcarError(txtPassword, false);
        marcarError(txtConfirmarPassword, false);
    }

    private void cambiarEscena(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Carga");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo cargar la vista de inicio.");
            alerta.showAndWait();
        }
    }
}

package com.example.proyecto1;

import javafx.collections.FXCollections; // Necesario para el ComboBox
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {

    // Componentes del archivo .fxml
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido1; // APELIDO PATERNO
    @FXML private TextField txtApellido2; // APELIDO MATERNO
    @FXML private ComboBox<String> comboBox; // GRADO ACADÉMICO
    @FXML private TextField txtCorreo1; // CORREO INSTITUCIONAL
    @FXML private TextField txtCorreo2; // CORREO PERSONAL
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtPassword;
    @FXML private Button btn_registrar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializa el ComboBox con las opciones de grado académico
        comboBox.setItems(FXCollections.observableArrayList("Licenciatura", "Maestría", "Doctorado"));

        // Asocia el método 'handleRegistroButton' al clic del botón Registrar
        btn_registrar.setOnAction(event -> handleRegistroButton());
    }

    // Método que se llama cuando el botón "REGISTRAR" es presionado
    @FXML
    private void handleRegistroButton() {
        // 1. Validar que todos los campos obligatorios estén llenos
        if (txtNombre.getText().isEmpty() || txtApellido1.getText().isEmpty() ||
                comboBox.getValue() == null || txtCorreo1.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, completa todos los campos obligatorios.");
            return; // Detiene la ejecución si hay campos vacíos obligatorios
        }

        // 2. Obtener los datos del formulario
        String nombre = txtNombre.getText();
        String apellidoPaterno = txtApellido1.getText();
        String apellidoMaterno = txtApellido2.getText();
        String gradoAcademico = comboBox.getValue();
        String correoInstitucional = txtCorreo1.getText();
        String correoPersonal = txtCorreo2.getText();
        String telefono = txtTelefono.getText();
        String contrasena = txtPassword.getText();

        // 3. Crear un objeto Usuario con los datos
        Usuarios nuevoUsuario = new Usuarios(
                correoInstitucional,
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                gradoAcademico,
                correoPersonal,
                telefono,
                contrasena
        );

        // 4. Intentar registrar el usuario en la base de datos
        if (registrarUsuario(nuevoUsuario)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "¡Usuario registrado correctamente!");
            clearFormFields(); // Limpia los campos del formulario

            // Abrir la ventana del perfil y pasar el correo institucional
            abrirVentanaPerfil(correoInstitucional);
        } else {
            // El mensaje de error será más específico desde registrarUsuario
            // Si el correo ya existe, registrarUsuario ya lo maneja
        }
    }

    /**
     * Inserta los datos de un usuario en la tabla 'usuarios' de Oracle.
     *
     * @param usuario El objeto Usuario a registrar.
     * @return true si el usuario se registró exitosamente, false en caso contrario.
     */
    private boolean registrarUsuario(Usuarios usuario) {
        // Consulta SQL para insertar datos en la tabla 'usuarios' de Oracle.

        String sql = "INSERT INTO usuarios (correo_institucional, nombre, apellido_paterno, apellido_materno, grado_academico, correo_personal, numero_telefono, contraseña) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionBDRegistro.getConnection(); // Obtiene la conexión a la BD
            pstmt = conn.prepareStatement(sql); // Prepara la sentencia SQL

            // Asigna los valores del objeto Usuario a los parámetros de la consulta SQL
            pstmt.setString(1, usuario.getCorreoInstitucional());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellidoPaterno());
            pstmt.setString(4, usuario.getApellidoMaterno());
            pstmt.setString(5, usuario.getGradoAcademico());
            pstmt.setString(6, usuario.getCorreoPersonal());
            pstmt.setString(7, usuario.getNumeroTelefono());
            pstmt.setString(8, usuario.getContrasena());

            int affectedRows = pstmt.executeUpdate(); // Ejecuta la inserción
            return affectedRows > 0; // Retorna true si se insertó al menos una fila

        } catch (SQLException e) {
            System.err.println("Error al registrar el usuario en la base de datos: " + e.getMessage());
            if (e.getErrorCode() == 1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Registro Fallido", "El correo institucional ya está registrado. Por favor, usa otro.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al intentar registrar el usuario. Inténtalo de nuevo.");
            }
            return false; // Retorna false si ocurre algún error
        } finally {
            // Asegura que los recursos de la base de datos se cierren siempre
            ConexionBDRegistro.closeConnection(conn);
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    // Muestra una ventana de alerta al usuario.
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Limpia todos los campos del formulario de registro.
    private void clearFormFields() {
        txtNombre.clear();
        txtApellido1.clear();
        txtApellido2.clear();
        comboBox.getSelectionModel().clearSelection();
        txtCorreo1.clear();
        txtCorreo2.clear();
        txtTelefono.clear();
        txtPassword.clear();
    }

    // Método para abrir la vista del perfil y pasarle el correo
    private void abrirVentanaPerfil(String correoInstitucional) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del perfil
            PerfilController perfilController = loader.getController();
            // Asumiendo que PerfilController tiene un método setCorreo para recibir el dato
            perfilController.setCorreo(correoInstitucional);

            // Mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Inicio de Sesión");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual (opcional, si quieres que la ventana de registro desaparezca)
            Stage currentStage = (Stage) btn_registrar.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo abrir la ventana de perfil.");
        }
    }
}


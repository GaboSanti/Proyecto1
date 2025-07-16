package com.example.proyecto1;
//Estas librerías se ocupan para vincular los elementos del archivo .fxml con el controller
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroController {

    //Componentes del archivo .fxml
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido1;
    @FXML private TextField txtApellido2;
    @FXML private ComboBox<String> comboBox;
    @FXML private TextField txtCorreo1;
    @FXML private TextField txtCorreo2;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtPassword;
    @FXML private Button btn_registrar;

    //Método inicializar()
    @FXML
    private void inicializar() {
        //Cuando el usuario hace click en el botón, se llama al método validarFormulario()
        btn_registrar.setOnAction(event -> validarFormulario());
    }

    //Método validarFormulario()
    //Acá validamos que todos los datos estén LLENOS
    private void validarFormulario() {
        boolean algunCampoVacio = txtNombre.getText().isEmpty() || txtApellido1.getText().isEmpty() || txtApellido2.getText().isEmpty() ||
                comboBox.getValue() == null || txtCorreo1.getText().isEmpty() || txtCorreo2.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() || txtPassword.getText().isEmpty();

        if (algunCampoVacio) {
            //Si algún campo está vacío, muestra esta alerta
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos.");
        } else {
            //Si todos los campos están llenos, muestra una alerta de éxito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "¡Todos los campos han sido completados!");

            // Obtener el correo institucional
            String correoInstitucional = txtCorreo1.getText();

            // Abrir la ventana del perfil y pasar el correo
            abrirVentanaPerfil(correoInstitucional);
        }
    }

    //Método mostrarAlerta()
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); //Espera que el usuario cierre la ventana antes de seguir
    }

    //Método para abrir la vista del perfil y pasarle el correo
    private void abrirVentanaPerfil(String correoInstitucional) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del perfil
            PerfilController perfilController = loader.getController();
            perfilController.setCorreo(correoInstitucional); // Pasar el correo

            // Mostrar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Perfil de Usuario");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual (opcional)
            Stage currentStage = (Stage) btn_registrar.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de perfil.");
        }
    }
}


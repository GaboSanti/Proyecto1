package com.example.proyecto1;

//Esta librerias se ocupan para vincular los elementos del archivo .fxml con el controller
import javafx.fxml.FXML;
import javafx.scene.control.*;

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


    //Metodo inicializar()
    @FXML
    private void inicializar() {
        //Validamos que los campos esten COMPLETOS
        //Cuando el usuario hace click , se llama al metodo validarFormulario()
        btn_registrar.setOnAction(event -> validarFormulario());
    }
    //Metodo validar formulario
    //Acá validamos que todos los datos esten LLENOS
    private void validarFormulario() {
        //Utilizamos isEmpty para verificar si algún campo esta vacio
        //En el caso de combobox verificamos que no sea null
        boolean algunCampoVacio = txtNombre.getText().isEmpty() || txtApellido1.getText().isEmpty() || txtApellido2.getText().isEmpty() ||
                comboBox.getValue() == null || txtCorreo1.getText().isEmpty() || txtCorreo2.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() || txtPassword.getText().isEmpty();
        if (algunCampoVacio) {
            //Si algun campo esta vacio muestra esta alerta
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todos los campos.");
        } else {
            //Si todos los campos estan llenos, muestra una alerta de exito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "¡Todos los campos han sido completados!");
        }
    }
    //Metodo mostrarAlerta()
    //Esta es una funcion reutilizable de alertas emergentes en pantalla
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait(); //Espera que el usuario cierre la ventana antes de seguir
    }
}

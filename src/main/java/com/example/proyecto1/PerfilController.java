package com.example.proyecto1;

import com.example.proyecto1.UsuarioBD; // Importa UsuarioBD
import com.example.proyecto1.Usuarios; // Importa  Usuarios

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.net.URL;
import java.util.ResourceBundle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PerfilController implements Initializable {

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
    @FXML private Label lblGradoAcademico; // Etiqueta para mostrar el grado académico

    private UsuarioBD usuarioBD; // Renombrado a UsuarioBD
    private Usuarios usuariosActual; // Objeto para mantener los datos del usuario logueado

    // El correo institucional se obtiene de la sesión
    // ¡ADVERTENCIA: FIJAR EL CORREO AQUÍ ES SOLO PARA PRUEBAS!
    // En una aplicación real, esto debe venir de SesionUsuarios.getCorreoInstitucional();
    String correoSesion = SesionUsuario.getCorreoInstitucional(); // Mantengo tu valor para pruebas



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioBD = new UsuarioBD(); // Inicializa el objeto para interactuar con la base de datos
        mostrarFechaActual(); // Muestra la fecha actual en la interfaz
        cargarDatosUsuarios(); // Carga los datos del usuario desde la base de datos al iniciar la vista
    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        lblFecha.setText(fechaFormateada);
    }

    private void cargarDatosUsuarios() {
        if (correoSesion != null && !correoSesion.isEmpty()) {
            // CORRECCIÓN: Llama al método con el nombre singular 'obtenerUsuarioPorCorreoInstitucional'
            usuariosActual = usuarioBD.obtenerUsuarioPorCorreoInstitucional(correoSesion);
            if (usuariosActual != null) {
                lblNombreUsuario.setText(usuariosActual.getNombre());
                lblApellido_PaternoUsuario.setText(usuariosActual.getApellidoPaterno());
                lblApellido_MaternoUsuario.setText(usuariosActual.getApellidoMaterno());
                lblCorreo_institucional.setText(usuariosActual.getCorreoInstitucional());
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal() != null ? usuariosActual.getCorreoPersonal() : "");
                lblTelefono.setText(usuariosActual.getNumeroTelefono() != null ? usuariosActual.getNumeroTelefono() : "");
                lblGradoAcademico.setText(usuariosActual.getGradoAcademico());

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Datos", "No se encontraron los datos del usuario logueado. Revise el correo de sesión o la conexión a la base de datos.");

                try {
                    cambiarVentana("hello-view.fxml", null, "Iniciar Sesión");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No hay usuario logueado. Redirigiendo a inicio de sesión.");
            try {
                cambiarVentana("hello-view.fxml", null, "Iniciar Sesión");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
        if (usuariosActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay datos de usuario para guardar.");
            return;
        }

        boolean cambiosRealizados = false;

        if (correoField.isVisible()) {
            String nuevoCorreo = correoField.getText().trim();
            if (!nuevoCorreo.equals(usuariosActual.getCorreoPersonal())) {
                usuariosActual.setCorreoPersonal(nuevoCorreo.isEmpty() ? null : nuevoCorreo);
                cambiosRealizados = true;
            }
        }

        if (telefonoField.isVisible()) {
            String nuevoTelefono = telefonoField.getText().trim();
            // CORRECCIÓN: Usa getNumeroTelefono() para que coincida con el modelo Usuarios y UsuarioBD
            if (!nuevoTelefono.equals(usuariosActual.getNumeroTelefono())) {
                usuariosActual.setNumeroTelefono(nuevoTelefono.isEmpty() ? null : nuevoTelefono);
                cambiosRealizados = true;
            }
        }

        if (cambiosRealizados) {
            // CORRECCIÓN: Llama al método con el nombre singular 'actualizarUsuario'
            if (usuarioBD.actualizarUsuario(usuariosActual)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Modificación Exitosa", "Datos modificados correctamente.");
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal() != null ? usuariosActual.getCorreoPersonal() : "");
                // CORRECCIÓN: Usa getNumeroTelefono() para que coincida con el modelo Usuarios y UsuarioBD
                lblTelefono.setText(usuariosActual.getNumeroTelefono() != null ? usuariosActual.getNumeroTelefono() : "");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un problema al actualizar los datos en la base de datos.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin Cambios", "No se realizaron cambios en los datos de contacto.");
        }

        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true);
        telefonoField.setVisible(false);
        lblTelefono.setVisible(true);
        comBox_grado.setVisible(false);
        lblGradoAcademico.setVisible(true);
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
        cargarDatosUsuarios();
    }

    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        // ¡IMPORTANTE! Asegúrate de que la clase de sesión se llame 'SesionUsuarios' o 'SesionUsuario'
        // según como la tengas definida en tu proyecto. Si es 'SesionUsuarios', cámbialo aquí:
        // SesionUsuarios.limpiarSesion();
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage;
        if (event != null) {
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = new Stage();
        }
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
}
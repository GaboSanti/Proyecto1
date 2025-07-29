package com.example.proyecto1;

import com.example.proyecto1.UsuarioBD; // Importa UsuarioBD
import com.example.proyecto1.Usuarios; // Importa el modelo Usuarios
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

    // --- Componentes FXML (¡ASEGÚRATE que los fx:id en tu FXML coincidan EXACTAMENTE con estos!) ---
    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblFecha;
    @FXML private Label lblCorreo_institucional;
    @FXML private Label lblCorreo_personal;
    @FXML private Label lblTelefono;
    @FXML private ComboBox<String> comBox_grado; // Se mantiene por si tu FXML lo tiene, aunque no se modificará
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
    String correoSesion = SesionUsuario.getCorreoInstitucional(); // Usa SesionUsuarios

    /**
     * Método de inicialización del controlador. Se ejecuta automáticamente al cargar el FXML.
     * @param url La ubicación relativa del objeto raíz.
     * @param rb Los recursos que pueden ser utilizados por el objeto raíz.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioBD = new UsuarioBD(); // Inicializa el objeto para interactuar con la base de datos
        mostrarFechaActual(); // Muestra la fecha actual en la interfaz
        cargarDatosUsuarios(); // Carga los datos del usuario desde la base de datos al iniciar la vista

        // Configura el ComboBox con opciones de grado académico (si bien no se modifica, se mantiene configurado)
        ObservableList<String> grados = FXCollections.observableArrayList(
                "Licenciatura", "Maestría", "Doctorado", "Técnico"
        );
        comBox_grado.setItems(grados);
        comBox_grado.setVisible(false); // Asegura que el ComboBox esté oculto inicialmente
    }

    /**
     * Muestra la fecha actual en el Label 'lblFecha'.
     */
    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        lblFecha.setText(fechaFormateada);
    }

    /**
     * Carga los datos del usuario logueado desde la base de datos y los muestra en la interfaz.
     */
    private void cargarDatosUsuarios() {
        if (correoSesion != null && !correoSesion.isEmpty()) {
            // Intenta obtener los datos del usuario de la base de datos
            usuariosActual = usuarioBD.obtenerUsuarioPorCorreoInstitucional(correoSesion);
            if (usuariosActual != null) {
                // Rellena los Labels con la información obtenida
                lblNombreUsuario.setText(usuariosActual.getNombre());
                lblApellido_PaternoUsuario.setText(usuariosActual.getApellidoPaterno());
                lblApellido_MaternoUsuario.setText(usuariosActual.getApellidoMaterno());
                lblCorreo_institucional.setText(usuariosActual.getCorreoInstitucional());
                // Usa operador ternario para manejar valores nulos y mostrar cadena vacía si no hay datos
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal() != null ? usuariosActual.getCorreoPersonal() : "");
                lblTelefono.setText(usuariosActual.getNumeroTelefono() != null ? usuariosActual.getNumeroTelefono() : "");
                lblGradoAcademico.setText(usuariosActual.getGradoAcademico() != null ? usuariosActual.getGradoAcademico() : "");

                // Si el ComboBox debe reflejar el valor actual (no editable), selecciona el grado académico
                if (usuariosActual.getGradoAcademico() != null && !usuariosActual.getGradoAcademico().isEmpty()) {
                    comBox_grado.getSelectionModel().select(usuariosActual.getGradoAcademico());
                }

            } else {
                // Si el usuario no se encuentra en la DB, muestra una alerta y redirige al login
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Datos", "No se encontraron los datos del usuario logueado.");
                SesionUsuario.limpiarSesion(); // Limpia la sesión actual
                try {
                    cambiarVentana("hello-view.fxml", null, "Iniciar Sesión"); // Redirige al login
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Si no hay un correo en la sesión, muestra una alerta y redirige al login
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No hay usuario logueado. Redirigiendo a inicio de sesión.");
            try {
                cambiarVentana("hello-view.fxml", null, "Iniciar Sesión"); // Redirige al login
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onModificarCorreo() {
        correoField.setText(lblCorreo_personal.getText()); // Carga el texto actual en el campo de edición
        correoField.setVisible(true); // Hace visible el campo de edición
        lblCorreo_personal.setVisible(false); // Oculta la etiqueta
        correoField.requestFocus(); // Pone el foco en el campo de edición
    }


    @FXML
    public void onModificarTelefono() {
        telefonoField.setText(lblTelefono.getText()); // Carga el texto actual en el campo de edición
        telefonoField.setVisible(true); // Hace visible el campo de edición
        lblTelefono.setVisible(false); // Oculta la etiqueta
        telefonoField.requestFocus(); // Pone el foco en el campo de edición
    }


    //accion  a realizar cuando se de clic en boton de gardar que es cuando se desea guardar los cambios ya ea del telefono o correo personal
    @FXML
    public void onGuardar() {
        if (usuariosActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay datos de usuario para guardar.");
            return;
        }

        boolean cambiosRealizados = false;

        // para actualizar el correo personal
        if (correoField.isVisible()) { // Si el campo de edición de correo está visible, significa que pudo ser modificado
            String nuevoCorreo = correoField.getText().trim();
            if (!nuevoCorreo.equals(usuariosActual.getCorreoPersonal())) { // Compara con el valor actual en el objeto Usuarios
                usuariosActual.setCorreoPersonal(nuevoCorreo.isEmpty() ? null : nuevoCorreo); // Asigna el nuevo valor (o null si está vacío)
                cambiosRealizados = true;
            }
        }

        //  para actualizar el número de teléfono
        if (telefonoField.isVisible()) { // Si el campo de edición de teléfono está visible
            String nuevoTelefono = telefonoField.getText().trim();
            if (!nuevoTelefono.equals(usuariosActual.getNumeroTelefono())) { // Compara con el valor actual en el objeto Usuarios
                usuariosActual.setNumeroTelefono(nuevoTelefono.isEmpty() ? null : nuevoTelefono); // Asigna el nuevo valor (o null si está vacío)
                cambiosRealizados = true;
            }
        }

        if (cambiosRealizados) {
            // Si se realizaron cambios en correo personal o teléfono, intenta actualizar en la DB
            if (usuarioBD.actualizarUsuario(usuariosActual)) { // Llama al método de actualización del UsuarioBD
                mostrarAlerta(Alert.AlertType.INFORMATION, "Modificación Exitosa", "Datos modificados correctamente.");
                // Actualiza los Labels en  para reflejar los datos recién guardados
                //datos que se van a modificar
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal() != null ? usuariosActual.getCorreoPersonal() : "");
                lblTelefono.setText(usuariosActual.getNumeroTelefono() != null ? usuariosActual.getNumeroTelefono() : "");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un problema al actualizar los datos en la base de datos.");
            }
        } else {
            // Si no hubo cambios en los campos editables, se informa al usuario
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin Cambios", "No se realizaron cambios en los datos de contacto.");
        }

        // Después de guardar (o intentar guardar), oculta los campos de edición y muestra las etiquetas nuevamente
        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true);
        telefonoField.setVisible(false);
        lblTelefono.setVisible(true);
        comBox_grado.setVisible(false); // Asegúrate de que el ComboBox también se oculte
        lblGradoAcademico.setVisible(true); // Asegúrate de que la etiqueta de grado se muestre
    }

    //accion  a realizar cuando se de clic en boton admin

    @FXML
    protected void onIrAdmin(ActionEvent event) throws IOException {
        cambiarVentana("Admin.fxml", event, "Administrador");
    }

    //accion  a realizar cuando se de clic en boton horario
    @FXML
    protected void onIrHorario(ActionEvent event) throws IOException {
        cambiarVentana("Horario.fxml", event, "Horario");
    }

    //accion  a realizar cuando se de clic en boton perfil
    @FXML
    protected void onIrPerfil(ActionEvent event) throws IOException {
        // Al ir al perfil, recargamos los datos para asegurarnos de que estén actualizados
        cargarDatosUsuarios();
    }

   //accion  a realizar cuando se de clic en boton salir
    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        SesionUsuario.limpiarSesion(); // Limpia los datos de la sesión actual
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }
//funcion para cambiar las ventanas
    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage;
        if (event != null) { // Si hay un evento (ej. clic de botón), usa la ventana actual
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        } else { // Si no hay evento (ej. llamado desde initialize para redirigir), crea una nueva etapa
            stage = new Stage();
        }
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }
//funcion para mostrar las alertas de mensaje
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
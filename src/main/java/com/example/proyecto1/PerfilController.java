package com.example.proyecto1;

import com.example.proyecto1.UsuarioBD; // Importa UsuarioBD
import com.example.proyecto1.Usuarios; // Importa  Usuarios

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

    @FXML private Label lblNombreUsuario; // Muestra Nombre del usuario visualmente
    @FXML private Label lblFecha;   //muestra la fecha de la computadora
    @FXML private Label lblCorreo_institucional; // Muestra el correo institucional visualmente
    @FXML private Label lblCorreo_personal; // Muestra correo personal  visualmente
    @FXML private Label lblTelefono; // Muestra el telefono visualmente
    @FXML private TextField correoField; // Campo de texto para Correo Personal
    @FXML private TextField telefonoField; // Campo de texto para Teléfono (
    @FXML private Label lblApellido_PaternoUsuario; // Muestra Apellido Paterno visualmente
    @FXML private Label lblApellido_MaternoUsuario; // Muestra Apellido Materno visualmente
    @FXML private Label lblGradoAcademico; // Muestra Grado academico  visualmente
    @FXML private Label lblNombre; //Muestra nombre completo del usuario en la parte superior
    @FXML private Button btnAdmin;
    @FXML private Label lblPeriodo;



    private UsuarioBD usuarioBD;   //Una instancia de la clase UsuarioBD que se utiliza para interactuar con la base de datos
    private Usuarios usuariosActual;//Un objeto de la clase Usuarios que contendrá la información del usuario actualmente logueado

    String correoSesion = SesionUsuario.getCorreoInstitucional();//Obtiene el correo institucional del usuario de una clase SesionUsuario


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioBD = new UsuarioBD();//Inicializa la conexión/gestión de la base de dato
        mostrarFechaActual();// Llama a la funcion para mostrar la fecha actual en la interfaz
        cargarDatosUsuarios();//Llama a una funcion para cargar la información del usuario desde la base de datos y mostrarla.
        mostrarNombreUsuarioLogueado();//llama la funcion para mostrar el nombre del usuario
        verificarAccesoAdmin(btnAdmin); // Llama a la función para verificar si no es admin se desactivaa
    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();//obtiene la fecha actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");//define el formato de fecha
        String fechaFormateada = fechaActual.format(formatter);//formatea la fecha para tenerla como string
        lblFecha.setText(fechaFormateada);//visualiza la fecha
    }

    private void cargarDatosUsuarios() {//Verifica si hay un correo válido
        if (correoSesion != null && !correoSesion.isEmpty()) {//Si lo hay, usa usuarioBD.obtenerUsuarioPorCorreoInstitucional() para recuperar los datos del usuario de la base de datos
            usuariosActual = usuarioBD.obtenerUsuarioPorCorreoInstitucional(correoSesion);
            if (usuariosActual != null) {
                // Aquí es donde asignas los valores a los  Labels
                lblNombreUsuario.setText(usuariosActual.getNombre()); // Asigna Nombre a lblNombreUsuario
                if(lblNombre != null) {
                    lblNombre.setText(usuariosActual.getNombre() + " " + usuariosActual.getApellidoPaterno());
                }
                lblApellido_PaternoUsuario.setText(usuariosActual.getApellidoPaterno()); // Asigna ApellidoPaterno a lblApellido_PaternoUsuario
                lblApellido_MaternoUsuario.setText(usuariosActual.getApellidoMaterno()); // Asigna ApellidoMaterno a lblApellido_MaternoUsuario
                lblCorreo_institucional.setText(usuariosActual.getCorreoInstitucional()); // Asigna CorreoInstitucional a lblCorreo_institucional
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal() != null ? usuariosActual.getCorreoPersonal() : ""); // Asigna CorreoPersonal a lblCorreo_personal
                lblTelefono.setText(usuariosActual.getNumeroTelefono() != null ? usuariosActual.getNumeroTelefono() : ""); // Asigna NumeroTelefono a lblTelefono
                lblGradoAcademico.setText(usuariosActual.getGradoAcademico() != null ? usuariosActual.getGradoAcademico() : ""); // Asigna GradoAcademico a lblGradoAcademico

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
        //Permiten al usuario editar el valor de un Label convirtiéndolo temporalmente en un TextField
        correoField.setText(lblCorreo_personal.getText()); // Obtiene el texto del Label que actualmente muestra el correo personal
        correoField.setVisible(true);//Hace visible el TextField
        lblCorreo_personal.setVisible(false); // Oculta el Label que muestra el correo personal
        correoField.requestFocus();

        correoField.setLayoutX(lblCorreo_personal.getLayoutX());
        correoField.setLayoutY(lblCorreo_personal.getLayoutY());
    }

    @FXML
    public void onModificarTelefono() {
        telefonoField.setText(lblTelefono.getText()); // Obtiene el texto del Label que actualmente muestra el teléfono
        telefonoField.setVisible(true);
        lblTelefono.setVisible(false); // Oculta el Label que muestra el teléfono
        telefonoField.requestFocus();

        telefonoField.setLayoutX(lblTelefono.getLayoutX());
        telefonoField.setLayoutY(lblTelefono.getLayoutY());
    }

    @FXML
    public void onGuardar() {
        if (usuariosActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay datos de usuario para guardar.");
            return;
        }

        boolean cambiosRealizados = false;// Bandera para saber si se hizo algún cambio que requiera una actualización en la base de datos.

        if (correoField.isVisible()) {
            String nuevoCorreo = correoField.getText().trim();

            if (!nuevoCorreo.equals(usuariosActual.getCorreoPersonal())) {
                usuariosActual.setCorreoPersonal(nuevoCorreo);
                cambiosRealizados = true;
            }
        }

        if (telefonoField.isVisible()) {
            String nuevoTelefono = telefonoField.getText().trim();
            // Ya no necesitamos la lógica de null, ya que el campo es NOT NULL en la DB
            // y lo estamos manejando con cadena vacía.
            if (!nuevoTelefono.equals(usuariosActual.getNumeroTelefono())) {
                usuariosActual.setNumeroTelefono(nuevoTelefono);
                cambiosRealizados = true;//llama a usuarioBD.actualizarUsuario(usuariosActual) para guardar los cambios en la base de datos.
            }
        }

        if (cambiosRealizados) {


            if (usuarioBD.actualizarUsuario(usuariosActual)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Modificación Exitosa", "Datos modificados correctamente.");
                // Actualiza los Labels con los nuevos valores.
                lblCorreo_personal.setText(usuariosActual.getCorreoPersonal()); // lblCorreo_Personal ahora muestra el Correo Personal
                lblTelefono.setText(usuariosActual.getNumeroTelefono()); // lblTelefono ahora muestra el Teléfono
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un problema al actualizar los datos en la base de datos.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin Cambios", "No se realizaron cambios en los datos de contacto.");
        }

        // Restablecer la visibilidad de los elementos a su estado original
        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true); // El Label que muestra el correo personal debe volver a ser visible
        telefonoField.setVisible(false);
        lblTelefono.setVisible(true); // El Label que muestra el teléfono debe volver a ser visible
    }
    //Cuando el usuario de clic en el boton Admin lo dirigira  a la ventana correspondiente
    @FXML
    protected void onIrAdmin(ActionEvent event) throws IOException {
        cambiarVentana("Admin.fxml", event, "Administrador");
    }

    //Cuando el usuario de clic en el boton Horario lo dirigira  a la ventana correspondiente
    @FXML
    protected void onIrHorario(ActionEvent event) throws IOException {
        cambiarVentana("Horario.fxml", event, "Horario");
    }

    //Cuando el usuario de clic en el boton perfil lo dirigira  a la ventana correspondiente
    @FXML
    protected void onIrPerfil(ActionEvent event) throws IOException {
        cargarDatosUsuarios();//Llama a cargarDatosUsuarios() para recargar los datos del perfil
        // ya sea si se navega de vuelta a esta vista o si hay que refrescar la información.
    }

    //Cuando el usuario de clic en el boton salir lo dirigira  a la ventana correspondiente
    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }

    //metodo  para realizar el cambio de ventanas
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

    // metodo  para mostrar mensajes de alerta al usuario
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //Obtiene el nombre completo del usuario actualmente logueado
     //y lo muestra en el Label de la esquina superior derecha

    private void mostrarNombreUsuarioLogueado() {
        if (correoSesion != null && !correoSesion.isEmpty()) {
            // Se realiza una consulta a la BD SOLO para obtener el nombre completo
            Usuarios usuarioSesion = usuarioBD.obtenerUsuarioPorCorreoInstitucional(correoSesion);

            if (usuarioSesion != null) {
                String nombreCompleto = (usuarioSesion.getNombre() != null ? usuarioSesion.getNombre() : "") + " " +
                        (usuarioSesion.getApellidoPaterno() != null ? usuarioSesion.getApellidoPaterno() : "") + " " +
                        (usuarioSesion.getApellidoMaterno() != null ? usuarioSesion.getApellidoMaterno() : "");
                lblNombre.setText(nombreCompleto.trim());
            } else {
                lblNombre.setText("Usuario Desconocido");
            }
        } else {
            lblNombre.setText("Sesión No Iniciada");
        }
    }

    //funcion para desactivar si el correo no coincide con el del Admin
    private void verificarAccesoAdmin(Button btnAdmin) {
        String correoAdmin = "20243rd025@utez.edu.mx"; //correo  del administrador
        if (correoSesion != null && !correoSesion.equals(correoAdmin)) {
            btnAdmin.setDisable(true); // Desactiva el botón si el usuario no es el admin
        }
    }

    // Funcion para recibir y mostrar el periodo
    public void setPeriodo(String periodo) {
        if (lblPeriodo != null) {
            lblPeriodo.setText(periodo);
        }
    }

}
package com.example.proyecto1;

import com.example.proyecto1.UsuarioBD; // Importa UsuarioBD
import com.example.proyecto1.Usuarios; // Importa  Usuarios
import com.example.proyecto1.HorarioController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;//para la fecha
import java.time.format.DateTimeFormatter;//para formato de fechas
import java.util.ResourceBundle;
import javafx.scene.Node;

import javafx.stage.FileChooser;//para la funcion de exportar
import java.io.File;

public class AdminController implements Initializable {

    @FXML private Button btnAsignar;
    @FXML private Button btnExportar;
    @FXML private DatePicker fecha_inicio;
    @FXML private DatePicker fecha_cierre;
    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblFecha;
    @FXML private Label lblPeriodo;
    @FXML private Label lblNombre;
    private String periodoAsignado; // Variable para almacenar el periodo


    private UsuarioBD usuarioBD;   //Una instancia de la clase UsuarioBD que se utiliza para interactuar con la base de datos
    private Usuarios usuariosActual;//Un objeto de la clase Usuarios que contendrá la información del usuario actualmente logueado
    String correoSesion = SesionUsuario.getCorreoInstitucional();//Obtiene el correo institucional del usuario de una clase SesionUsuario


    @FXML
    public void initialize(URL url, ResourceBundle rb){
        usuarioBD = new UsuarioBD();
        mostrarFechaActual();// Llama a la funcion para mostrar la fecha actual en la interfaz
        mostrarNombreUsuarioLogueado();
        cargarPeriodoDesdeBD();// Carga el periodo al inicializar

    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();//obtiene la fecha actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");//define el formato de fecha
        String fechaFormateada = fechaActual.format(formatter);//formatea la fecha para tenerla como string
        lblFecha.setText(fechaFormateada);//visualiza la fecha
    }

    public void onAsignarFecha() {
        LocalDate fechaSeleccionada = fecha_inicio.getValue();
        LocalDate fechaSeleccionadaFinal = fecha_cierre.getValue();
        if (fechaSeleccionada != null && fechaSeleccionadaFinal != null) {
            // Guarda el periodo en la base de datos
            String sql = "INSERT INTO periodo (fecha_inicio, fecha_fin) VALUES (?, ?)";

            try (Connection conn = ConexionBDRegistro.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDate(1, java.sql.Date.valueOf(fechaSeleccionada));
                pstmt.setDate(2, java.sql.Date.valueOf(fechaSeleccionadaFinal));

                pstmt.executeUpdate();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Periodo Asignado con exito", "Periodo guardado en la base de datos de Oracle. ");


                // Vuelve a cargar el periodo para actualizar la etiqueta
                cargarPeriodoDesdeBD();

            } catch (SQLException e) {
                System.err.println("Error al guardar el periodo en la BD: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor, selecciona ambas fechas.");
        }
    }


    @FXML
    protected void onIrAdmin(ActionEvent event) throws IOException {
        cambiarVentana("Admin.fxml", event, "Administrador");
    }

    @FXML
    protected void onIrPerfil(ActionEvent event) throws IOException {
        cambiarVentana("Perfil.fxml", event, "Perfil");
    }

    @FXML
    protected void onIrHorario(ActionEvent event) throws IOException {
        cambiarVentana("Horario.fxml", event, "Horario");
    }
    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }

    @FXML
    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        boolean maximized = stage.isMaximized();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));              // sin width/height
        } else {
            stage.getScene().setRoot(root);               // reutiliza la Scene
        }

        stage.setTitle(titulo);

    }


    // metodo  para mostrar mensajes de alerta al usuario
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

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

    @FXML
    public void onExportar() {
        // Abre un cuadro de diálogo para que el usuario elija dónde guardar el archivo.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Horario CSV");
        // Agrega un filtro para que el usuario solo pueda guardar archivos de tipo csv
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv")
                //new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt")

        );

        // Muestra el cuadro de diálogo y obtén la ruta del archivo seleccionado
        File archivoSeleccionado = fileChooser.showSaveDialog(new Stage());

        //  Verifica que el usuario haya seleccionó una ubicación
        if (archivoSeleccionado != null) {
            //  Crea una instancia  y llama a la función de exportación.
            HorarioRepositorio repo = new HorarioRepositorio();
            boolean exito = repo.exportarHorarios(archivoSeleccionado.getAbsolutePath());

            // Muestra un mensaje al usuario con el resultado
            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación Exitosa", "El horario se ha exportado correctamente a: " + archivoSeleccionado.getAbsolutePath());
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Exportación", "Hubo un problema al exportar el horario.");
            }
        }
    }


    // funcion para cargar el periodo desde la base de datos ---
    private void cargarPeriodoDesdeBD() {
        String sql = "SELECT fecha_inicio, fecha_fin FROM periodo ORDER BY fecha_id DESC FETCH FIRST 1 ROW ONLY";

        try (Connection conn = ConexionBDRegistro.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();
                LocalDate fechaFin = rs.getDate("fecha_fin").toLocalDate();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
                String periodoGuardado = "Periodo asignado: " + fechaInicio.format(formatter) + " al " + fechaFin.format(formatter);
                lblPeriodo.setText(periodoGuardado);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar el periodo desde la BD: " + e.getMessage());
        }
    }
}

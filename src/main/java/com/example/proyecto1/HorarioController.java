package com.example.proyecto1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.example.proyecto1.util.HorarioUtils;
import com.example.proyecto1.model.HorarioUsuariosPorDia;
import javafx.stage.Stage;

public class HorarioController {

    @FXML
    private GridPane gridPaneHorario;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnPerfil;
    @FXML
    private Button btnHorario;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnAdmin;
    @FXML
    private Pane paneHorario;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblNombre;

    private UsuarioBD usuarioBD;   //Una instancia de la clase UsuarioBD que se utiliza para interactuar con la base de datos
    private Usuarios usuariosActual;//Un objeto de la clase Usuarios que contendrá la información del usuario actualmente logueado

    String correoSesion = SesionUsuario.getCorreoInstitucional();//Obtiene el correo institucional del usuario de una clase SesionUsuario


    private final Map<String, List<Integer>> horarioSeleccionado = new LinkedHashMap<>();
    private boolean horarioBloqueado = false;

    @FXML
    public void initialize() {
        usuarioBD = new UsuarioBD();
        mostrarFechaActual();
        mostrarNombreUsuarioLogueado();


        for (Node node : gridPaneHorario.getChildren()) {
            if (node instanceof Button boton) {
                boton.setStyle("-fx-background-color: #FF6666; -fx-border-color: black; -fx-border-width: 1px;");
                boton.setOnAction(e -> {
                    if (horarioBloqueado) return;

                    String currentStyle = boton.getStyle();
                    if (currentStyle.contains("#FF6666")) {
                        boton.setStyle("-fx-background-color: #5DF563; -fx-border-color: black; -fx-border-width: 1px;");
                    } else {
                        boton.setStyle("-fx-background-color: #FF6666; -fx-border-color: black; -fx-border-width: 1px;");
                    }
                });
            }
        }

        cargarHorarioDesdeBD();
    }

    private void cargarHorarioDesdeBD() {
        String correo = SesionUsuario.getCorreoInstitucional();
        if (correo == null) return;

        HorarioRepositorio repo = new HorarioRepositorio();
        HorarioUsuariosPorDia horarioGuardado = repo.obtenerHorarioPorCorreo(correo);
        if (horarioGuardado == null) return;

        horarioSeleccionado.clear();

        Map<String, List<Integer>> horarioMap = horarioGuardado.getHorarioMap();

        for (Map.Entry<String, List<Integer>> entry : horarioMap.entrySet()) {
            String dia = entry.getKey();
            List<Integer> intervalos = entry.getValue();
            int col = HorarioUtils.diaToId(dia);

            for (Integer row : intervalos) {
                for (Node node : gridPaneHorario.getChildren()) {
                    if (node instanceof Button boton) {

                        int colIndex = GridPane.getColumnIndex(boton) == null ? 0 : GridPane.getColumnIndex(boton);
                        int rowIndex = GridPane.getRowIndex(boton) == null ? 0 : GridPane.getRowIndex(boton);

                        if (colIndex == col && rowIndex == row) {
                            boton.setStyle("-fx-background-color: #5DF563; -fx-border-color: black; -fx-border-width: 1px;");
                            horarioSeleccionado.putIfAbsent(dia, new ArrayList<>());
                            if (!horarioSeleccionado.get(dia).contains(row)) {
                                horarioSeleccionado.get(dia).add(row);
                            }
                        }
                    }
                }
            }
        }

        horarioBloqueado = true;
        btnGuardar.setDisable(true);
    }

    @FXML
    private void guardarHorario() {
        if (horarioBloqueado) {
            return;
        }

        horarioSeleccionado.clear();

        for (Node node : gridPaneHorario.getChildren()) {
            if (node instanceof Button boton) {
                String style = boton.getStyle();
                if (style.contains("#5DF563")) {
                    int col = GridPane.getColumnIndex(boton) == null ? 0 : GridPane.getColumnIndex(boton);
                    int row = GridPane.getRowIndex(boton) == null ? 0 : GridPane.getRowIndex(boton);

                    String dia = HorarioUtils.idToDiaSigla(col);

                    horarioSeleccionado.putIfAbsent(dia, new ArrayList<>());
                    if (!horarioSeleccionado.get(dia).contains(row)) {
                        horarioSeleccionado.get(dia).add(row);
                    }
                }
            }
        }

        String correo = SesionUsuario.getCorreoInstitucional();
        if (correo == null) return;

        HorarioUsuariosPorDia horario = new HorarioUsuariosPorDia(correo, horarioSeleccionado);
        HorarioRepositorio repo = new HorarioRepositorio();
        boolean resultado = repo.guardarHorario(horario);

        if (resultado) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado", "¡Horario Registrado Correctamente.!");
            horarioBloqueado = true;
            btnGuardar.setDisable(true);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el horario");
        }
    }

    @FXML
    private void modificarHorario() {
        horarioBloqueado = false;
        btnGuardar.setDisable(false);
        mostrarAlerta(Alert.AlertType.INFORMATION, "Edición", "Solo puedes modificar tu horario durante el periodo asignado.");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    protected void onPerfil(ActionEvent event) throws IOException {
        cambiarVentana("Perfil.fxml", event, "Perfil");
    }

    @FXML
    protected void onAdmin(ActionEvent event) throws IOException {
        cambiarVentana("Admin.fxml", event, "Administrador");
    }

    @FXML
    protected void onSalir(ActionEvent event) throws IOException {
        cambiarVentana("hello-view.fxml", event, "Iniciar Sesion");
    }

    @FXML
    protected void onHorario(ActionEvent event) throws IOException {
        cambiarVentana("Horario.fxml", event, "Horario");
    }

    private void cambiarVentana(String fxml, ActionEvent event, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }

    private void mostrarFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        lblFecha.setText(fechaActual.format(formatter));
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

    public boolean exportarDatosDeHorario(String rutaArchivo) {
        HorarioRepositorio repo = new HorarioRepositorio();
        return repo.exportarHorarios(rutaArchivo);
    }



}



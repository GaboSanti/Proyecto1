package com.example.proyecto1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.example.proyecto1.util.HorarioUtils;
import com.example.proyecto1.model.HorarioUsuariosPorDia;

public class HorarioController {

    @FXML
    private GridPane gridPaneHorario;
    @FXML
    private Button btnGuardar;
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

    private final Map<String, List<Integer>> horarioSeleccionado = new LinkedHashMap<>();

    @FXML
    public void initialize() {

        for (Node node : gridPaneHorario.getChildren()) {
            if (node instanceof Button boton) {
                boton.setStyle("-fx-background-color: #FF6666; -fx-border-color: black; -fx-border-width: 1px;");

                boton.setOnAction(e -> {
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
        if (correo == null) {
            return;
        }

        HorarioRepositorio repo = new HorarioRepositorio();
        HorarioUsuariosPorDia horarioGuardado = repo.obtenerHorarioPorCorreo(correo);

        if (horarioGuardado == null) {
            return;
        }

        Map<String, List<Integer>> horarioMap = horarioGuardado.getHorarioMap();

        for (Map.Entry<String, List<Integer>> entry : horarioMap.entrySet()) {
            String dia = entry.getKey(); // Ej: "LU"
            List<Integer> intervalos = entry.getValue(); // Ej: [1, 2, 3]
            int col = HorarioUtils.diaToId(dia) - 1;

            for (Integer row : intervalos) {
                for (Node node : gridPaneHorario.getChildren()) {
                    if (node instanceof Button boton) {
                        int colIndex = GridPane.getColumnIndex(boton) != null ? GridPane.getColumnIndex(boton) : 0;
                        int rowIndex = GridPane.getRowIndex(boton) != null ? GridPane.getRowIndex(boton) : 0;

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

    }

    @FXML
    private void guardarHorario() {

        for (Node node : gridPaneHorario.getChildren()) {
            if (node instanceof Button boton) {
                String style = boton.getStyle();
                if (style.contains("#5DF563")) {
                    int col = GridPane.getColumnIndex(boton) != null ? GridPane.getColumnIndex(boton) : 0;
                    int row = GridPane.getRowIndex(boton) != null ? GridPane.getRowIndex(boton) : 0;

                    String dia = HorarioUtils.idToDiaSigla(col + 1); // Porque col 0 = LU (1)

                    horarioSeleccionado.putIfAbsent(dia, new ArrayList<>());
                    if (!horarioSeleccionado.get(dia).contains(row)) {
                        horarioSeleccionado.get(dia).add(row);
                    }
                }
            }
        }

        String correo = SesionUsuario.getCorreoInstitucional();
        System.out.println("Correo: " + correo);
        System.out.println("Horario seleccionado: " + horarioSeleccionado);

        if (correo == null) {
            return;
        }

        HorarioUsuariosPorDia horario = new HorarioUsuariosPorDia(correo, horarioSeleccionado);
        HorarioRepositorio repo = new HorarioRepositorio();
        boolean resultado = repo.guardarHorario(horario);

        if (resultado) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Guardado", "¡Horario registrado correctamente!");
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Error", "No se pudo guardar el horario");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}



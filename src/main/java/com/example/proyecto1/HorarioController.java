package com.example.proyecto1;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HorarioController {

    @FXML private GridPane gridPaneHorario;
    @FXML private Button btnGuardar;

    private boolean[][] estadoCeldas;

    private final String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
    private final String[] timeSlots = {
            "7:00 - 8:00", "8:00 - 9:00", "9:00 - 10:00", "10:00 - 11:00",
            "11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
            "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00",
            "19:00 - 20:00", "20:00 - 21:00"
    };

    @FXML
    public void initialize() {
        estadoCeldas = new boolean[timeSlots.length][daysOfWeek.length];

        gridPaneHorario.setPadding(new Insets(10));
        gridPaneHorario.setHgap(1);
        gridPaneHorario.setVgap(1);
        gridPaneHorario.getColumnConstraints().clear();

        // Primera columna para los horarios
        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPrefWidth(100);
        timeCol.setHgrow(Priority.NEVER);
        gridPaneHorario.getColumnConstraints().add(timeCol);

        // Columnas para cada día
        for (int i = 0; i < daysOfWeek.length; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setHgrow(Priority.ALWAYS);
            dayCol.setHalignment(HPos.CENTER);
            gridPaneHorario.getColumnConstraints().add(dayCol);
        }

        // Etiquetas de los días
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            GridPane.setHalignment(dayLabel, HPos.CENTER);
            gridPaneHorario.add(dayLabel, i + 1, 0); // columna i+1, fila 0
        }

        // Etiquetas de los horarios
        for (int i = 0; i < timeSlots.length; i++) {
            Label timeLabel = new Label(timeSlots[i]);
            timeLabel.setFont(Font.font("Arial", 12));
            gridPaneHorario.add(timeLabel, 0, i + 1); // columna 0, fila i+1
        }

        // Botones para cada celda de horario
        for (int row = 0; row < timeSlots.length; row++) {
            for (int col = 0; col < daysOfWeek.length; col++) {
                Button cell = new Button();
                cell.setStyle("-fx-background-color: #FF9999;");
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                final int r = row;
                final int c = col;

                cell.setOnAction(e -> {
                    estadoCeldas[r][c] = !estadoCeldas[r][c];
                    cell.setStyle(estadoCeldas[r][c]
                            ? "-fx-background-color: #53DB59;"
                            : "-fx-background-color: #FF9999;");
                });

                gridPaneHorario.add(cell, col + 1, row + 1);
            }
        }
    }
}

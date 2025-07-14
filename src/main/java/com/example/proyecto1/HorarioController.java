package com.example.proyecto1;

import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Platform;
import javafx.stage.Stage;

public class HorarioController {

    @FXML private GridPane gridPaneHorario;
    @FXML private Button btnGuardar;
    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Pane paneHorario;

    @FXML
    public void initialize() {
        for (javafx.scene.Node node : gridPaneHorario.getChildren()) {
            if (node instanceof Button) {
                Button boton = (Button) node;


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

    }
}


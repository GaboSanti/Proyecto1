package com.example.proyecto1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class PerfilController {

    @FXML private Button btnPerfil;
    @FXML private Button btnHorario;
    @FXML private Button btnSalir;
    @FXML private Button btnAdmin;
    @FXML private Label lblNombreUsuario;
    @FXML private Label lblCorreo_institucional;
    @FXML private Label lblCorreo_personal;
    @FXML private Label lblTelefono;
    @FXML private ComboBox comBox_grado;
    @FXML private Button btnModificar_correo;
    @FXML private Button btnModificar_telefono;
    @FXML private Button btnModificar_grado;

    @FXML private TextField correoField;
    @FXML private TextField correotelefono;

    @FXML
    public void initilizing(){
        correoField.setOnAction(e -> {
        lblCorreo_personal.setText(correoField.getText());
        correoField.setVisible(false);
        lblCorreo_personal.setVisible(true);
        });
    }

    @FXML
    public void onModificarCorreo(){
            lblCorreo_personal.setText(correoField.getText());
            correoField.setVisible(false);
            lblCorreo_personal.setVisible(true);
            correoField.requestFocus();

    }
}

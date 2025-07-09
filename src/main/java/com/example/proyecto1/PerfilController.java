package com.example.proyecto1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;


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

    @FXML private TextField correoField;
    @FXML private TextField telefonoField;


    public void onModificarCorreo() {
        // Mostrar el campo de texto para editar el correo
        correoField.setText(lblCorreo_personal.getText()); // Cargar el correo actual
        correoField.setVisible(true); // Mostrar el campo de texto
        lblCorreo_personal.setVisible(false); // Ocultar el label
        correoField.requestFocus(); // Enfocar el campo de texto
    }
    @FXML
    public void onGuardar() {
        // Obtener el nuevo correo del campo de texto
        String nuevoCorreo = correoField.getText().trim();
        // Actualizar el label con el nuevo correo
        lblCorreo_personal.setText(nuevoCorreo);
        correoField.setVisible(false); // Ocultar el campo de texto
        lblCorreo_personal.setVisible(true); // Mostrar el label actualizado

        // Obtener el nuevo correo del campo de texto
        String nuevotelefono = telefonoField.getText().trim();
        // Actualizar el label con el nuevo correo
        lblTelefono.setText(nuevoCorreo);
        telefonoField.setVisible(false); // Ocultar el campo de texto
        lblTelefono.setVisible(true); // Mostrar el label actualizado
    }


    @FXML
    public void onModificarTelefono() {
        telefonoField.setText(lblTelefono.getText());
        telefonoField.setVisible(true);
        lblTelefono.setVisible(false);
        telefonoField.requestFocus();
    }

}

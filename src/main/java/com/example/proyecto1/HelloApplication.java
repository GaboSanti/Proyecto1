package com.example.proyecto1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 832);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        stage.setMaximized(true);


        //SEGUNDA PANTALLA, INVESTIGAR COMO PASAR A LA OTRA PANTALLA APARTIR DE UN BOTON Y QUE NO SE ABRAN AL MISMO TIEMPO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Horario.fxml"));
        Parent root = loader.load();
        Stage nuevoStage = new Stage();
        nuevoStage.setTitle("Horario");
        nuevoStage.setScene(new Scene(root));
        nuevoStage.show();

        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}
module com.example.proyecto1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;
    requires java.sql;
    requires com.google.gson;


    opens com.example.proyecto1.model to com.google.gson;

    opens com.example.proyecto1 to javafx.fxml;
    exports com.example.proyecto1;
}
module com.example.proyecto1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proyecto1 to javafx.fxml;
    exports com.example.proyecto1;
}
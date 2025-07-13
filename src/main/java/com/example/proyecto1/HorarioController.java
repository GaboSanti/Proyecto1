import javafx.scene.control.Button;
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
        // Asegura que el Stage esté listo antes de usarlo
        Platform.runLater(() -> {
            // Obtener el Stage desde el pane raíz
            Stage stage = (Stage) paneHorario.getScene().getWindow();

            // Maximizar la ventana
            stage.setMaximized(true);

            // Asegurar que el contenido se reacomode correctamente
            stage.setResizable(true);
            paneHorario.requestLayout();
            gridPaneHorario.requestLayout();
        });
    }
}

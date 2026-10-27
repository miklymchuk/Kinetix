import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class CarSimulationController implements Initializable {

    @FXML
    private Slider carAccelerationSlider;

    @FXML
    private Polygon carBody;

    @FXML
    private Circle wheel1;

    @FXML
    private Circle wheel2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}

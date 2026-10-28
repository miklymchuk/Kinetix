package com.mycompany.kinetix;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class CarSimulationController implements Initializable {

    @FXML
    private Slider velocitySlider;

    @FXML
    private Polygon carBody;

    @FXML
    private Circle wheel1;

    @FXML
    private Circle wheel2;
    
    private TranslateTransition movingCarBody = new TranslateTransition();
    private TranslateTransition movingWheel1 = new TranslateTransition();
    private TranslateTransition movingWheel2 = new TranslateTransition();
    private RotateTransition rotatingWheel1 = new RotateTransition();
    private RotateTransition rotatingWheel2 = new RotateTransition();
    private ParallelTransition movingCar = new ParallelTransition(movingCarBody, movingWheel1, movingWheel2, rotatingWheel1, rotatingWheel2);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movingCarBody.setNode(carBody);
        movingWheel1.setNode(wheel1);
        movingWheel2.setNode(wheel2);
        rotatingWheel1.setNode(wheel1);
        rotatingWheel2.setNode(wheel2);
        
        movingCarBody.setInterpolator(Interpolator.LINEAR);
        movingWheel1.setInterpolator(Interpolator.LINEAR);
        movingWheel2.setInterpolator(Interpolator.LINEAR);
        rotatingWheel1.setInterpolator(Interpolator.LINEAR);
        rotatingWheel2.setInterpolator(Interpolator.LINEAR);
    }
    
    public void carMovement() {
        double velocity = velocitySlider.getValue();
        if (velocity <= 0) {
            return;
        }
        
        double startX = carBody.getTranslateX();
    }
}

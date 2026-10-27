package com.mycompany.kinetix;

import java.net.URL;
import java.util.ResourceBundle;
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
    private Slider carAccelerationSlider;

    @FXML
    private Polygon carBody;

    @FXML
    private Circle wheel1;

    @FXML
    private Circle wheel2;
    
    private int animationDuration;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TranslateTransition movingCarBody = new TranslateTransition();
        TranslateTransition movingWheel1 = new TranslateTransition();
        TranslateTransition movingWheel2 = new TranslateTransition();
        
        RotateTransition rotatingWheel1 = new RotateTransition();
        RotateTransition rotatingWheel2 = new RotateTransition();
        
        ParallelTransition movingCar = new ParallelTransition(movingCarBody, movingWheel1, movingWheel2, rotatingWheel1, rotatingWheel2);
        
    }
}

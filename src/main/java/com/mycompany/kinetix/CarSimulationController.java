package com.mycompany.kinetix;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class CarSimulationController implements Initializable {

    @FXML
    private Label accelerationSliderLabel;

    @FXML
    private Polygon carBody;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Slider accelerationSlider;

    @FXML
    private Circle wheel1;

    @FXML
    private Circle wheel2;

    @FXML
    void pauseBtnPressed(ActionEvent event) {
        
    }

    @FXML
    void playBtnPressed(ActionEvent event) {
        carMovement();
        accelerationSlider.setDisable(true);
    }

    @FXML
    void resetBtnPressed(ActionEvent event) {
        accelerationSlider.setDisable(false);
    }

    private TranslateTransition movingCarBody = new TranslateTransition();
    private TranslateTransition movingWheel1 = new TranslateTransition();
    private TranslateTransition movingWheel2 = new TranslateTransition();
    private RotateTransition rotatingWheel1 = new RotateTransition();
    private RotateTransition rotatingWheel2 = new RotateTransition();
    private ParallelTransition movingCar = new ParallelTransition(movingCarBody, movingWheel1, movingWheel2, rotatingWheel1, rotatingWheel2);
    private double duration;
    private double distance = 1500;

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
        double acceleration = accelerationSlider.getValue();
        if (acceleration <= 0) {
            return;
        }
        
        double startX = carBody.getTranslateX();
        double endX = startX + distance;
        duration = MechanicsUtility.time(distance, acceleration);
        movingCarBody.setDuration(Duration.seconds(duration));
        movingCarBody.setFromX(startX);
        movingCarBody.setToX(endX);
        movingWheel1.setDuration(Duration.seconds(duration));
        movingWheel1.setFromX(wheel1.getCenterX());
        movingWheel1.setToX(wheel1.getCenterX() + distance);
        movingWheel2.setDuration(Duration.seconds(duration));
        movingWheel2.setFromX(wheel2.getCenterX());
        movingWheel2.setToX(wheel2.getCenterX() + distance);
        
        movingCar.play();
    }
}

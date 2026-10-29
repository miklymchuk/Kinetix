package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
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
    
    @FXML Button returnBtn;

    @FXML
    private Slider accelerationSlider;

    @FXML
    private Circle wheel1;

    @FXML
    private Circle wheel2;

    @FXML
    void pauseBtnPressed(ActionEvent event) {
        if (movingCar != null) {
            movingCar.pause();
        }
    }

    @FXML
    void playBtnPressed(ActionEvent event) {
        if (movingCar != null && movingCar.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (movingCar != null && movingCar.getStatus() == Animation.Status.PAUSED) {
            movingCar.play();
            return;
        }

        carMovement();
    }

    @FXML
    void resetBtnPressed(ActionEvent event) {
        if (movingCar != null) {
            movingCar.stop();
        }

        carBody.setTranslateX(0);
        wheel1.setTranslateX(0);
        wheel2.setTranslateX(0);
        wheel1.setRotate(0);
        wheel2.setRotate(0);
        accelerationSlider.setDisable(false);
    }
    
    @FXML
    void returnBtnPressed(ActionEvent event) {
        switchScene("linearMotionSelection.fxml");
    }

    private TranslateTransition movingCarBody = new TranslateTransition();
    private TranslateTransition movingWheel1 = new TranslateTransition();
    private TranslateTransition movingWheel2 = new TranslateTransition();
    private RotateTransition rotatingWheel1 = new RotateTransition();
    private RotateTransition rotatingWheel2 = new RotateTransition();
    private ParallelTransition movingCar;
    private double distance = 1900;
    private static final double PIXELS_PER_METER = 5;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movingCarBody.setNode(carBody);
        movingWheel1.setNode(wheel1);
        movingWheel2.setNode(wheel2);
        rotatingWheel1.setNode(wheel1);
        rotatingWheel2.setNode(wheel2);

        movingCarBody.setInterpolator(MechanicsUtility.accelerationInterpolator);
        movingWheel1.setInterpolator(MechanicsUtility.accelerationInterpolator);
        movingWheel2.setInterpolator(MechanicsUtility.accelerationInterpolator);
        rotatingWheel1.setInterpolator(MechanicsUtility.accelerationInterpolator);
        rotatingWheel2.setInterpolator(MechanicsUtility.accelerationInterpolator);
        
        accelerationSliderLabel.textProperty().bind(accelerationSlider.valueProperty().asString("%.1f m/s²"));
    }
    
    /**
     * provides movement for the car and its components
     */
    public void carMovement() {
        double acceleration = accelerationSlider.getValue();
        if (acceleration <= 0) {
            return;
        }

        if (movingCar != null) {
            movingCar.stop();
        }

        double distanceInMeters = distance / PIXELS_PER_METER;
        double seconds = MechanicsUtility.time(distanceInMeters, acceleration);
        Duration time = Duration.seconds(seconds);

        double startBodyX = carBody.getTranslateX();
        movingCarBody.setDuration(time);
        movingCarBody.setFromX(startBodyX);
        movingCarBody.setToX(startBodyX + distance);

        double startWheel1X = wheel1.getTranslateX();
        movingWheel1.setDuration(time);
        movingWheel1.setFromX(startWheel1X);
        movingWheel1.setToX(startWheel1X + distance);

        double startWheel2X = wheel2.getTranslateX();
        movingWheel2.setDuration(time);
        movingWheel2.setFromX(startWheel2X);
        movingWheel2.setToX(startWheel2X + distance);

        double degrees = Math.toDegrees(distance / wheel1.getRadius());
        rotatingWheel1.setDuration(time);
        rotatingWheel1.setFromAngle(wheel1.getRotate());
        rotatingWheel1.setByAngle(degrees);
        rotatingWheel2.setDuration(time);
        rotatingWheel2.setFromAngle(wheel2.getRotate());
        rotatingWheel2.setByAngle(degrees);

        movingCar = new ParallelTransition(movingCarBody, movingWheel1, movingWheel2, rotatingWheel1, rotatingWheel2);
        accelerationSlider.setDisable(true);
        movingCar.play();
        movingCar.setOnFinished(event -> accelerationSlider.setDisable(false));
    }
    
    /**
     * switches the scene of the stage
     * @param fxml the fxml file
     */
    public void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

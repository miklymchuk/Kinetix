package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * FXML Controller class
 *
 * @author 2540408
 */
public class TwoDKinematicsController implements Initializable {
    @FXML
    private Label height;
    @FXML
    private Label velocity;
    @FXML
    private Label acceleration;
    @FXML
    private Button returnBtn;
    @FXML
    private Rectangle cannon;
    @FXML
    private Slider angleSlider;
    @FXML
    private Slider velocitySlider;
    @FXML
    private Label angleSliderLabel;
    @FXML
    private Label velocitySliderLabel;
    @FXML
    private Button playBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Pane groundLevel;
    
    private double ballVelocity;
    private double angle;
    private boolean paused = false;
    
    private static final double BALL_RADIUS = 19;
    private static final int MAX_BALLS_IN_SCENE = 3;
    
    private static final double PIXELS_PER_METER = 4;
    private static final double MAX_ANGLE = 90;
    private static final double MAX_VELOCITY = 60;
    private static final double DEFAULT_ANGLE = 45;
    private static final double DEFAULT_VELOCITY = 30;
    
    private final List<Circle> circles = new ArrayList<>();
    private final List<ParallelTransition> animations = new ArrayList<>();
    private ParallelTransition latest;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        angleSlider.setMin(0);
        angleSlider.setMax(MAX_ANGLE);
        angleSlider.setMajorTickUnit(15);
        angleSlider.setValue(DEFAULT_ANGLE);
        
        velocitySlider.setMin(0);
        velocitySlider.setMax(MAX_VELOCITY);
        velocitySlider.setMajorTickUnit(10);
        velocitySlider.setValue(DEFAULT_VELOCITY);
        
        angle = angleSlider.getValue();
        ballVelocity = velocitySlider.getValue();
        cannon.setRotate(90 - angle);
        
        angleSlider.valueProperty().addListener((obs, oldV, newV) -> {
            angle = (double) newV;
            cannon.setRotate(90 - angle);
        });
        
        velocitySlider.valueProperty().addListener((obs, oldV, newV) -> {
            ballVelocity = (double) newV;
        });
    }    
    
    @FXML
    private void returnBtnPressed(ActionEvent event) {
        switchScene("selectpage.fxml");
    }

    @FXML
    private void cannonClicked(MouseEvent event) {
        generateCannonBall();
    }

    @FXML
    private void playBtnPressed(ActionEvent event) {
        paused = false;
        
        for (ParallelTransition animation : animations) {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.play();
            }
        }
    }

    @FXML
    private void pauseBtnPressed(ActionEvent event) {
        paused = true;
        
        for (ParallelTransition animation : animations) {
            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.pause();
            }
        }
    }

    @FXML
    private void resetBtnPressed(ActionEvent event) {
    }
    
    /**
     * switches the scene of the stage
     * @param fxml the fxml file
     */
    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void generateCannonBall() {
        Circle circle = new Circle();
        circle.setCenterX(cannon.getLayoutX());
        circle.setCenterY(cannon.getLayoutY());
        circle.setRadius(BALL_RADIUS);
        circles.add(circle);
        groundLevel.getChildren().add(circle);
    }
    
    public void canonBallMotion(Circle circle) {
        double startX = circle.getCenterX();
        double startY = circle.getCenterY();
        
        TranslateTransition transX = new TranslateTransition();
        TranslateTransition transY = new TranslateTransition();
        
        transX.setFromX(startX);
        transX.setToX(MechanicsUtility.projectileX(ballVelocity, angle, time));
        
        transY.setFromY(startY);
        transY.setToY(MechanicsUtility.projectileY(ballVelocity, angle, time));
        
        transX.setDuration(Duration.seconds(time));
        transY.setDuration(Duration.seconds(time));
    }
    
    public void resetBalls() {
        
    }
}

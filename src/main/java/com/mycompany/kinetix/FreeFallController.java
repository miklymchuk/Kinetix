/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * FXML Controller class
 *
 * @author Michael Klymchuk
 */
public class FreeFallController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private HBox infoBox;
    @FXML
    private Label heightInfo;
    @FXML
    private Label velocityInfo;
    @FXML
    private Label accelerationInfo;
    @FXML
    private Pane simulationPane;
    @FXML
    private Circle freeFallObject;
    @FXML
    private VBox fieldsBox;
    @FXML
    private Label heightLbl;
    @FXML
    private HBox heightSliderHBox;
    @FXML
    private Slider heightSlider;
    @FXML
    private TextField heightValue;
    @FXML
    private VBox gravityVBox;
    @FXML
    private Label gravityLbl;
    @FXML
    private RadioButton earthRadioBtn;
    @FXML
    private RadioButton moonRadioBtn;
    @FXML
    private RadioButton marsRadioBtn;
    @FXML
    private Button startBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private ToggleGroup gravityButtons;
    
    private double height;
    
    private double currentHeight;
    
    private double time;
    
    private double currentTime;
    
    private double currentVelocity;
    
    private double acceleration = MechanicsUtility.GRAVITY_CONSTANT;
    
    TranslateTransition animation = new TranslateTransition();
    
    long startTime = System.currentTimeMillis();
    
    @FXML
    private void startButtonPressed(ActionEvent event) {
        freeFallObject.setTranslateY(height * -10);
        time = MechanicsUtility.time(height, acceleration);
        animation.setNode(freeFallObject);
        animation.setDuration(Duration.seconds(time));
        animation.setFromY(freeFallObject.getTranslateY());
        animation.setToY(0);
        animation.setInterpolator(MechanicsUtility.accelerationInterpolator);
        animation.play();
    }
    
    @FXML
    private void resetButtonPressed(ActionEvent event) {
        animation.stop();
        freeFallObject.setTranslateY(0);
        heightInfo.setText("Height: 0m");
        velocityInfo.setText("Velocity: 0m/s");
        accelerationInfo.setText("Acceleration: 0m/s²");
        
    }
    
    @FXML
    private void exitButtonPressed(ActionEvent event) {
        switchScene("LinearMotionSelection.fxml");
    }
    
    /**
     * switches the scene of the stage
     * @param fxml the fxml file
     */
    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        heightValue.textProperty().bind(heightSlider.valueProperty().asString("%.1f m"));
        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.doubleValue();
        });
        
        freeFallObject.translateYProperty().addListener(change -> {
            currentHeight = (freeFallObject.getTranslateY() / -10);
            String currentHeightText = "" + currentHeight;
            if (currentHeight >= 10) {
                currentHeightText = currentHeightText.substring(0, 4);
            } else if (currentHeight == 0) {
                currentHeightText = "0";
            } else {
                currentHeightText = currentHeightText.substring(0, 3);
            }
            heightInfo.setText("Height: " + currentHeightText + "m");
            
            currentTime = MechanicsUtility.time(height - currentHeight, acceleration);
            currentVelocity = MechanicsUtility.finalVelocity(0, acceleration, currentTime);
            String currentVelocityText = "" + currentVelocity;
            if (currentVelocity >= 10) {
                currentVelocityText = currentVelocityText.substring(0, 4);
            } else if (currentVelocity == 0) {
                currentVelocityText = "0";
            } else {
                currentVelocityText = currentVelocityText.substring(0, 3);
            }
            velocityInfo.setText("Velocity: " + currentVelocityText + "m/s");
            
            accelerationInfo.setText("Acceleration: " + acceleration + "m/s²");
        });
        
        gravityButtons.selectedToggleProperty().addListener((ov, oldButton, newButton) -> {
            if (earthRadioBtn.isSelected()) {
                acceleration = MechanicsUtility.GRAVITY_CONSTANT;
            } else if (moonRadioBtn.isSelected()) {
                acceleration = MechanicsUtility.MOON_GRAVITY_CONSTANT;
            } else {
                acceleration = MechanicsUtility.MARS_GRAVITY_CONSTANT;
            }
        });
              
    }
}

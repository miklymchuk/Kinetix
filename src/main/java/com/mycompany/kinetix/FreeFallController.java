/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
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
 * @author mikly
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
    
    private double height;
    
    @FXML
    private void startButtonPressed(ActionEvent event) {
        freeFallObject.setTranslateY(height * -10);
        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), freeFallObject);
        animation.setFromY(freeFallObject.getTranslateY());
        animation.setToY(0);
        animation.setCycleCount(1);
        animation.setInterpolator(Interpolator.LINEAR);
        animation.play();
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
        heightValue.textProperty().bind(heightSlider.valueProperty().asString("%.0f"));
        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.doubleValue();
        });
    }    
    
}

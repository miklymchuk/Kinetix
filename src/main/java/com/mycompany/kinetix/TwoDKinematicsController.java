package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    
    private static final double BALL_RADIUS = 19;
    
    private List<Circle> circles = new ArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    }

    @FXML
    private void pauseBtnPressed(ActionEvent event) {
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
    
    public void canonBallMotion() {
        
    }
}

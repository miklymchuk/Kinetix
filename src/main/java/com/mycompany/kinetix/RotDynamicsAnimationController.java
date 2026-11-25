/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Paul Djeudo
 */
public class RotDynamicsAnimationController implements Initializable {

    @FXML private Button returnBtn;
    @FXML private Label calculatedTorqueLabel;
    @FXML private Button pullBtn;
    @FXML private Button resetBtn;
    @FXML private StackPane wheel;
    @FXML private Slider radiusSlider;
    @FXML private Slider forceSlider;
    @FXML private Slider angleSlider;
    @FXML private Label momentOfInertiaLabel;

    // The wheel as drawn in the FXML corresponds to this radius in meters.
    private static final double BASE_RADIUS_M = 0.5;
    private static final double DISK_MASS_KG = 2.0; // placeholder, change as needed

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyRadius(newVal.doubleValue());
            updateReadouts();
        });

        // The force and angle sliders also affect torque, so refresh on those too
        forceSlider.valueProperty().addListener((obs, o, n) -> updateReadouts());
        angleSlider.valueProperty().addListener((obs, o, n) -> updateReadouts());

        // Set the initial state
        applyRadius(radiusSlider.getValue());
        updateReadouts();
    }
    
    /** Resizes the wheel visually. Scaling happens around the node's center. */
    private void applyRadius(double radiusMeters) {
        double scale = radiusMeters / BASE_RADIUS_M;
        wheel.setScaleX(scale);
        wheel.setScaleY(scale);
    }
    
    private void updateReadouts() {
        double r = radiusSlider.getValue();
        double force = forceSlider.getValue();
        double thetaRad = Math.toRadians(angleSlider.getValue());

        double inertia = 0.5 * DISK_MASS_KG * r * r;   // solid disk: I = ½mr²
        double torque = force * r * Math.sin(thetaRad); // τ = rF·sinθ

        calculatedTorqueLabel.setText(String.format("%.2f", torque));
        momentOfInertiaLabel.setText(String.format("%.3f", inertia));
    }

    @FXML
    private void returnBtnPressed(ActionEvent event) {
        switchScene("selectpage.fxml");
    }

    @FXML
    private void pullBtnPressed(ActionEvent event) {
        
        
    }

    @FXML
    private void resetBtnPressed(ActionEvent event) {
        
        
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

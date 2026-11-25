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
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

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

    // Rope pieces
    @FXML private Arc ropeArc;
    @FXML private Line ropeLeft;
    @FXML private Line ropeRight;

    // The wheel as drawn in the FXML corresponds to this radius in meters.
    private static final double BASE_RADIUS_M = 0.5;
    private static final double DISK_MASS_KG = 2.0; // placeholder, change as needed

    // Wheel geometry as drawn in the FXML (outer radius in pixels)
    private static final double WHEEL_BASE_PX = 152.0;
    // Length of the hanging rope sides in pixels (contact point down to y=775)
    private static final double ROPE_LENGTH_PX = 386.0;

    // Wheel center in scene coordinates, captured at startup
    private double centerX;
    private double centerY;
    private double ropeHalfStroke;

    // Current rope centerline radius (updated by applyRadius)
    private double ropeR;

    // Starting slider values, used by Reset
    private double defaultRadius;
    private double defaultForce;
    private double defaultAngle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // StackPane is 304x304, so its center is layout position + 152
        centerX = wheel.getLayoutX() + WHEEL_BASE_PX;
        centerY = wheel.getLayoutY() + WHEEL_BASE_PX;
        ropeHalfStroke = ropeArc.getStrokeWidth() / 2.0;

        // Remember the FXML starting values for Reset
        defaultRadius = radiusSlider.getValue();
        defaultForce = forceSlider.getValue();
        defaultAngle = angleSlider.getValue();

        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyRadius(newVal.doubleValue());
            updateReadouts();
        });

        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyAngle(newVal.doubleValue());
            updateReadouts();
        });

        forceSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateReadouts());

        // Set the initial state
        applyRadius(radiusSlider.getValue());
        updateReadouts();
    }

    /** Resizes the wheel and keeps the rope hugging it at every size. */
    private void applyRadius(double radiusMeters) {
        double scale = radiusMeters / BASE_RADIUS_M;

        // Wheel (scales around its center)
        wheel.setScaleX(scale);
        wheel.setScaleY(scale);

        // Rope centerline = wheel edge + half the rope's thickness
        ropeR = WHEEL_BASE_PX * scale + ropeHalfStroke;

        // Arc shares the wheel's center
        ropeArc.setCenterX(centerX);
        ropeArc.setCenterY(centerY);
        ropeArc.setRadiusX(ropeR);
        ropeArc.setRadiusY(ropeR);

        // Left side is always vertical and tangent to the wheel
        ropeRight.setStartX(centerX - ropeR);
        ropeRight.setEndX(centerX - ropeR);
        ropeRight.setStartY(centerY + ROPE_LENGTH_PX);
        ropeRight.setEndY(centerY);

        // Right side depends on the pull angle
        applyAngle(angleSlider.getValue());
    }

    /**
     * Tilts the pulled (right) rope. θ is the angle between the pull and the
     * wheel's radius: 90° = straight down (tangent), 0° = straight outward.
     */
    private void applyAngle(double angleDeg) {
        double theta = Math.toRadians(angleDeg);

        // The rope leaves the wheel at the right end of the arc
        double contactX = centerX + ropeR;
        double contactY = centerY;

        ropeLeft.setEndX(contactX);
        ropeLeft.setEndY(contactY);
        ropeLeft.setStartX(contactX + ROPE_LENGTH_PX * Math.cos(theta));
        ropeLeft.setStartY(contactY + ROPE_LENGTH_PX * Math.sin(theta));
    }

    private void updateReadouts() {
        double r = radiusSlider.getValue();
        double force = forceSlider.getValue();
        double thetaRad = Math.toRadians(angleSlider.getValue());

        double inertia = 0.5 * DISK_MASS_KG * r * r;    // solid disk: I = ½mr²
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
        // Stop any spin and put the wheel back at its starting orientation
        wheel.setRotate(0);

        // Restore the sliders; their listeners rebuild the visuals and readouts
        radiusSlider.setValue(defaultRadius);
        forceSlider.setValue(defaultForce);
        angleSlider.setValue(defaultAngle);

        // When the pull animation exists, also stop it here and
        // re-enable the sliders.
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

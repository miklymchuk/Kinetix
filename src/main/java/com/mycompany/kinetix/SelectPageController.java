package com.mycompany.kinetix;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SelectPageController {

    @FXML
    private Button linMomentumButton;

    @FXML
    private Button projectileMotionButton;

    @FXML
    private Button rotDynamicsButton;

    @FXML
    private Button straightLineMotionButton;
    
    @FXML
    private Button returnButton;
    
    @FXML
    void linMomentumButtonPressed(ActionEvent event) {

    }

    @FXML
    void linearMotionButtonPressed(ActionEvent event) {

    }

    @FXML
    void projectileMotionButtonPressed(ActionEvent event) {

    }

    @FXML
    void rotDynamicsButtonPressed(ActionEvent event) {

    }
    
    @FXML
    void returnButtonPressed(ActionEvent event) {
        switchScene("MainMenu.fxml");
    }
    
    /**
     * switches the scene of the stage
     * @param fxml the FXML file
     */
    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

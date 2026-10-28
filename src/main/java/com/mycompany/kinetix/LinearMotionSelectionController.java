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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Michael Klymchuk
 */
public class LinearMotionSelectionController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Label oneDimKinematicsLbl;
    @FXML
    private Button returnBtn;
    @FXML
    private HBox simulationButtonHBox;
    @FXML
    private Button carAnimationBtn;
    @FXML
    private Button freeFallBtn;
    
    @FXML
    void returnButtonPressed(ActionEvent event) {
        switchScene("selectpage.fxml");
    }
    
    @FXML
    void carAnimationBtnPressed(ActionEvent event) {
        switchScene("CarAnimation.fxml");
    }

    @FXML
    void freeFallButtonPressed(ActionEvent event) {
        switchScene("freeFall.fxml");
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}

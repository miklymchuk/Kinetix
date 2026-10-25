package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Main Menu controller class
 *
 * @author 2540408
 */
public class MainMenu implements Initializable {


    @FXML
    private Button startBtn;
    @FXML
    private Button exitBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startBtn.setOnAction(event -> switchScene("selectpage.fxml"));
        exitBtn.setOnAction(event-> Platform.exit());
    }    
    
    /**
     * switches the scene of the stage
     * @param fxml the fxml file
     */
    private void switchScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

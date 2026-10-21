/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.kinetix;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
/**
 * FXML Controller class
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
        startBtn.setOnAction(event -> {});
        
        exitBtn.setOnAction(event-> {
            Platform.exit();
        });
    }    
    
}

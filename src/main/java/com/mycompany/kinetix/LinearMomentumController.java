package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LinearMomentumController implements Initializable {

    @FXML
    private GridPane buttonGrid;

    @FXML
    private GridPane mainGrid;

    @FXML
    private Label massOneLabel;

    @FXML
    private Slider massOneSlider;

    @FXML
    private TextField massOneText;

    @FXML
    private Label massOneTitle;

    @FXML
    private HBox massSliderBoxOne;

    @FXML
    private Label massTwoLabel;

    @FXML
    private Slider massTwoSlider;

    @FXML
    private TextField massTwoText;

    @FXML
    private Label massTwoTitle;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button returnBtn;

    @FXML
    private BorderPane root;

    @FXML
    private Label simulationTitle;

    @FXML
    private Button startBtn;

    @FXML
    private Label velocityOneLabel;

    @FXML
    private Slider velocityOneSlider;

    @FXML
    private TextField velocityOneText;

    @FXML
    private HBox velocitySliderBoxOne;

    @FXML
    private Label velocityTwoLabel;

    @FXML
    private Slider velocityTwoSlider;

    @FXML
    private TextField velocityTwoText;
    
    @FXML
    private Circle massOneCircle;
    
    @FXML
    private Circle massTwoCircle;
    
    @FXML
    private Label massOneCircleLabel;
    
    @FXML
    private Label massTwoCircleLabel;
    
    @FXML
    private Group massOneGroup;
    
    @FXML
    private Group massTwoGroup;
    
    private double massOne;
    
    private double velocityOne;
    
    private double massOneTime;
    
    private double massTwoTime;
    
    private double massTwo;
    
    private double velocityTwo;
    
    private double massOneMomentum;
    
    private double massTwoMomentum;
    
    private double totalMomentum;
    
    private TranslateTransition massOneTransition;
    
    private TranslateTransition massTwoTransition;
    
    private ParallelTransition collisionTransition;
    
    @FXML
    void startBtnPressed(ActionEvent event) {
        massOneMomentum = MechanicsUtility.momentum(massOne, velocityOne);
        massTwoMomentum = MechanicsUtility.momentum(massTwo, velocityTwo);
        totalMomentum = massOneMomentum + massTwoMomentum;
        
        massOneTime = MechanicsUtility.timeFromVelocity(20, velocityOne);
        massOneTransition.setDuration(Duration.seconds(massOneTime));
        
        massTwoTime = MechanicsUtility.timeFromVelocity(20, velocityTwo);
        massTwoTransition.setDuration(Duration.seconds(massTwoTime));
        
        massOneSlider.setDisable(true);
        velocityOneSlider.setDisable(true);
        massTwoSlider.setDisable(true);
        velocityTwoSlider.setDisable(true);
        collisionTransition.play();
    }
    
    @FXML
    void pauseBtnPressed(ActionEvent event) {

    }
    
    @FXML
    void resetBtnPressed(ActionEvent event) {

    }
    
    @FXML
    void returnBtnPressed(ActionEvent event) {
        switchScene("selectpage.fxml");
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
        massOneText.textProperty().bind(massOneSlider.valueProperty().asString("%.1fkg"));
        massOneSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            massOne = newValue.doubleValue();
            
            String massOneString = massOne + "";
            
            if (massOne == 10) {
                massOneCircleLabel.setText("Mass: " + massOneString.substring(0, 4) + "kg");
            } else {
                massOneCircleLabel.setText("Mass: " + massOneString.substring(0, 3) + "kg");
            }
        });
        
        velocityOneText.textProperty().bind(velocityOneSlider.valueProperty().asString("%.1fm/s"));
        velocityOneSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            velocityOne = newValue.doubleValue();
        });
        
        massTwoText.textProperty().bind(massTwoSlider.valueProperty().asString("%.1fkg"));
        massTwoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            massTwo = newValue.doubleValue();
            
            String massTwoString = massTwo + "";
            
            if (massTwo == 10) {
                massTwoCircleLabel.setText("Mass: " + massTwoString.substring(0, 4) + "kg");
            } else {
                massTwoCircleLabel.setText("Mass: " + massTwoString.substring(0, 3) + "kg");
            }
        });
        
        velocityTwoText.textProperty().bind(velocityTwoSlider.valueProperty().asString("%.1fm/s"));
        velocityTwoSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            velocityTwo = newValue.doubleValue();
        });
        
        massOneGroup.getChildren().addAll(massOneCircle, massOneCircleLabel);
        massTwoGroup.getChildren().addAll(massTwoCircle, massTwoCircleLabel);
        
        massOneTransition = new TranslateTransition(Duration.ZERO, massOneGroup);
        massOneTransition.setToX(2000);
        massOneTransition.setInterpolator(Interpolator.LINEAR);
        
        massTwoTransition = new TranslateTransition(Duration.ZERO, massTwoGroup);
        massTwoTransition.setToX(-2000);
        massTwoTransition.setInterpolator(Interpolator.LINEAR);
        
        collisionTransition = new ParallelTransition();
        collisionTransition.getChildren().addAll(massOneTransition, massTwoTransition);
        
        massOneGroup.translateXProperty().addListener(cl -> {
            if (massOneCircle.getLayoutX() + massOneGroup.getTranslateX() + 100 > massTwoCircle.getLayoutX() + massTwoGroup.getTranslateX()) {
                collisionTransition.stop();
                
            }
        });
        
        massTwoGroup.translateXProperty().addListener(cl -> {
            if (massOneCircle.getLayoutX() + massOneGroup.getTranslateX() + 100 > massTwoCircle.getLayoutX() + massTwoGroup.getTranslateX()) {
                collisionTransition.stop();
            }
        });
    }   
}

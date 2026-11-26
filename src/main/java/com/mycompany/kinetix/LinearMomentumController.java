package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
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
    
    private double momentumOne;
    
    private double momentumTwo;
    
    private TranslateTransition massOnePreCollisionTransition;
    
    private TranslateTransition massTwoPreCollisionTransition;
    
    private ParallelTransition preCollisionTransition;
    
    private TranslateTransition massOnePostCollisionTransition;
    
    private TranslateTransition massTwoPostCollisionTransition;
    
    private ParallelTransition postCollisionTransition;
    
    private Boolean postCollision;
    
    @FXML
    void startBtnPressed(ActionEvent event) {
        if ((velocityOne == 0 && velocityTwo == 0) || preCollisionTransition.getStatus() == Status.RUNNING || preCollisionTransition.getStatus() == Status.PAUSED || postCollisionTransition.getStatus() == Status.RUNNING || postCollisionTransition.getStatus() == Status.PAUSED) {
            return;
        }
        
        momentumOne = MechanicsUtility.momentum(massOne, velocityOne);
        momentumTwo = MechanicsUtility.momentum(massTwo, velocityTwo);
        
        massOneTime = MechanicsUtility.timeFromVelocity(20, velocityOne);
        massOnePreCollisionTransition.setDuration(Duration.seconds(massOneTime));
        
        massTwoTime = MechanicsUtility.timeFromVelocity(20, velocityTwo);
        if (massTwoTime < 0) {
            massTwoTime*=-1;
        }
        massTwoPreCollisionTransition.setDuration(Duration.seconds(massTwoTime));
        
        massOneSlider.setDisable(true);
        velocityOneSlider.setDisable(true);
        massTwoSlider.setDisable(true);
        velocityTwoSlider.setDisable(true);
        preCollisionTransition.play();
    }
    
    @FXML
    void pauseBtnPressed(ActionEvent event) {
        if (postCollision == false && preCollisionTransition.getStatus() == Status.RUNNING) {
            preCollisionTransition.pause();
            pauseBtn.setText("Play");
        } else if (postCollision == false && preCollisionTransition.getStatus() == Status.PAUSED) {
            preCollisionTransition.playFrom(preCollisionTransition.getCurrentTime());
            pauseBtn.setText("Pause");
        } else if (postCollision == true && postCollisionTransition.getStatus() == Status.RUNNING) {
            postCollisionTransition.pause();
            pauseBtn.setText("Play");
        } else if (postCollision == true && postCollisionTransition.getStatus() == Status.PAUSED) {
            postCollisionTransition.playFrom(postCollisionTransition.getCurrentTime());
            pauseBtn.setText("Pause");
        }
    }
    
    @FXML
    void resetBtnPressed(ActionEvent event) {
        preCollisionTransition.stop();
        postCollisionTransition.stop();
        massOneSlider.setDisable(false);
        velocityOneSlider.setDisable(false);
        massTwoSlider.setDisable(false);
        velocityTwoSlider.setDisable(false);
        massOneGroup.setTranslateX(0);
        massTwoGroup.setTranslateX(0);
        velocityOne = velocityOneSlider.getValue();
        velocityTwo = velocityTwoSlider.getValue();
        if (velocityTwo > 0) {
            velocityTwo*=-1;
        }
        pauseBtn.setText("Pause");
        postCollision = false;
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
        massOne = 1;
        massTwo = 1;
        velocityOne = 0;
        velocityTwo = 0;
        
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
            if (velocityTwo > 0) {
                velocityTwo*=-1;
            }
        });
        
        massOneGroup.getChildren().addAll(massOneCircle, massOneCircleLabel);
        massTwoGroup.getChildren().addAll(massTwoCircle, massTwoCircleLabel);
        
        massOnePreCollisionTransition = new TranslateTransition(Duration.ZERO, massOneGroup);
        massOnePreCollisionTransition.setToX(2000);
        massOnePreCollisionTransition.setInterpolator(Interpolator.LINEAR);
        
        massTwoPreCollisionTransition = new TranslateTransition(Duration.ZERO, massTwoGroup);
        massTwoPreCollisionTransition.setToX(-2000);
        massTwoPreCollisionTransition.setInterpolator(Interpolator.LINEAR);
        
        massOnePostCollisionTransition = new TranslateTransition(Duration.ZERO, massOneGroup);
        massOnePostCollisionTransition.setToX(2000);
        massOnePostCollisionTransition.setInterpolator(Interpolator.LINEAR);
        
        massTwoPostCollisionTransition = new TranslateTransition(Duration.ZERO, massTwoGroup);
        massTwoPostCollisionTransition.setToX(-2000);
        massTwoPostCollisionTransition.setInterpolator(Interpolator.LINEAR);
        
        preCollisionTransition = new ParallelTransition();
        preCollisionTransition.getChildren().addAll(massOnePreCollisionTransition, massTwoPreCollisionTransition);
        postCollisionTransition = new ParallelTransition();
        postCollisionTransition.getChildren().addAll(massOnePostCollisionTransition, massTwoPostCollisionTransition);
        postCollision = false;
        
        massTwoGroup.translateXProperty().addListener(cl -> {
            if ((massOneCircle.getLayoutX() + massOneGroup.getTranslateX() + 100 > massTwoCircle.getLayoutX() + massTwoGroup.getTranslateX()) && postCollision == false) {
                preCollisionTransition.stop();
                double oldVelocityOne = velocityOne;
                velocityOne = MechanicsUtility.finalVelocity(massOne, velocityOne, massTwo, velocityTwo, momentumTwo);
                velocityTwo = MechanicsUtility.finalVelocity(massTwo, velocityTwo, massOne, oldVelocityOne, momentumOne);
                massOnePostCollisionTransition.setDuration(Duration.seconds(10));
                massTwoPostCollisionTransition.setDuration(Duration.seconds(10));
                double displacementOne = MechanicsUtility.finalPosition(velocityOne, 10) * 100;
                double displacementTwo = MechanicsUtility.finalPosition(velocityTwo, 10) * 100;
                massOnePostCollisionTransition.setToX(massOneGroup.getTranslateX() + displacementOne);
                massTwoPostCollisionTransition.setToX(massTwoGroup.getTranslateX() + displacementTwo);
                postCollision = true;
                postCollisionTransition.play();
            }
            
            if (postCollisionTransition.getCurrentTime().toString().equals(massTwoPostCollisionTransition.getDuration().toString()) && postCollision == true) {
                massOneSlider.setDisable(false);
                velocityOneSlider.setDisable(false);
                massTwoSlider.setDisable(false);
                velocityTwoSlider.setDisable(false);
                massOneGroup.setTranslateX(0);
                massTwoGroup.setTranslateX(0);
                velocityOne = velocityOneSlider.getValue();
                velocityTwo = velocityTwoSlider.getValue();
                if (velocityTwo > 0) {
                    velocityTwo*=-1;
                }
                postCollision = false;
            }
        });
        
        
    }   
}

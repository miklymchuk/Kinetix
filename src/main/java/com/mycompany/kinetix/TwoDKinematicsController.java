package com.mycompany.kinetix;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private double ballVelocity;
    private double angle;
    private boolean paused = false;

    private static final double BALL_RADIUS = 19;
    private static final int MAX_BALLS_IN_SCENE = 50;

    private static final double PIXELS_PER_METER = 20;
    private static final double MAX_ANGLE = 90;
    private static final double MAX_VELOCITY = 25;
    private static final double DEFAULT_ANGLE = 45;
    private static final double DEFAULT_VELOCITY = 15;

    private final List<Circle> circles = new ArrayList<>();
    private final List<ParallelTransition> animations = new ArrayList<>();
    private ParallelTransition latest;
    
    private MediaPlayer cannonSound;
    private static final double RECOIL_DISTANCE = 12;
    private TranslateTransition recoilAnimation;
    
    private static final int DUST_PARTICLES = 8;
    private static final double DUST_RADIUS = 4;
    private static final double DUST_SPREAD = 40;
    private static final double DUST_RISE = 25;
    private static final double DUST_MILLIS = 500;
    
    private final List<Circle> dustParticles = new ArrayList<>();
    private final List<ParallelTransition> dustAnimations = new ArrayList<>();
    
    private static final long COOLDOWN_MILLIS = 500;
    private long lastFireTime = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        angleSlider.setMin(0);
        angleSlider.setMax(MAX_ANGLE);
        angleSlider.setMajorTickUnit(15);
        angleSlider.setValue(DEFAULT_ANGLE);

        velocitySlider.setMin(0);
        velocitySlider.setMax(MAX_VELOCITY);
        velocitySlider.setMajorTickUnit(5);
        velocitySlider.setValue(DEFAULT_VELOCITY);

        angle = angleSlider.getValue();
        ballVelocity = velocitySlider.getValue();
        cannon.setRotate(90 - angle);

        angleSlider.valueProperty().addListener((obs, oldV, newV) -> {
            angle = (double) newV;
            cannon.setRotate(90 - angle);
        });

        velocitySlider.valueProperty().addListener((obs, oldV, newV) -> {
            ballVelocity = (double) newV;
        });
        
        URL soundLink = getClass().getResource("/com/mycompany/kinetix/sounds/cannonFire.mp3");

        if (soundLink != null) {
            Media media = new Media(soundLink.toExternalForm());
            cannonSound = new MediaPlayer(media);
        }
        
        cannonRecoil();
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
        paused = false;

        for (ParallelTransition animation : animations) {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.play();
            }
        }
    }

    @FXML
    private void pauseBtnPressed(ActionEvent event) {
        paused = true;

        for (ParallelTransition animation : animations) {
            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.pause();
            }
        }
    }

    @FXML
    private void resetBtnPressed(ActionEvent event) {
        resetBalls();
    }

    /**
     * switches the scene of the stage
     *
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

    /**
     * generates a cannon ball when the user clicks the cannon with their mouse
     */
    public void generateCannonBall() {
        if (paused) {
            return;
        }
        
        long now = System.currentTimeMillis();
        if (now - lastFireTime < COOLDOWN_MILLIS) {
            return;
        }
        lastFireTime = now;

        if (circles.size() >= MAX_BALLS_IN_SCENE) {
            removeBall(0);
        }

        double angleRad = Math.toRadians(angle);
        double centerX = cannon.getLayoutX() + cannon.getWidth() / 2;
        double centerY = cannon.getLayoutY() + cannon.getHeight() / 2;
        double halfBarrel = cannon.getHeight() / 2;
        double startX = centerX + halfBarrel * Math.cos(angleRad);
        double startY = centerY - halfBarrel * Math.sin(angleRad);

        Circle circle = new Circle(startX, startY, BALL_RADIUS);
        circle.setFill(Color.hsb(Math.random() * 360, 0.8, 0.9));
        circle.setStroke(Color.BLACK);
        circle.setMouseTransparent(true);
        circles.add(circle);
        groundLevel.getChildren().add(0, circle);

        cannonBallMotion(circle);
        cannonRecoil();
    }

    public void cannonBallMotion(Circle circle) {
        final double speed = ballVelocity;
        final double angleRad = Math.toRadians(angle);

        double groundY = groundLevel.getHeight() > 0 ? groundLevel.getHeight() : groundLevel.getPrefHeight();
        final double startHeight = Math.max(0, (groundY - BALL_RADIUS - circle.getCenterY()) / PIXELS_PER_METER);

        final double flightTime = Math.max(0.01, MechanicsUtility.flightTime(speed, angleRad, startHeight));

        TranslateTransition transX = new TranslateTransition(Duration.seconds(flightTime), circle);
        transX.setByX(MechanicsUtility.projectileX(speed, angleRad, flightTime) * PIXELS_PER_METER);
        transX.setInterpolator(Interpolator.LINEAR);

        TranslateTransition transY = new TranslateTransition(Duration.seconds(flightTime), circle);
        transY.setByY(-PIXELS_PER_METER);
        transY.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double fraction) {
                return MechanicsUtility.projectileY(speed, angleRad, fraction * flightTime);
            }
        });

        ParallelTransition animation = new ParallelTransition(transX, transY);
        animation.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (animation == latest) {
                updateInfoLabels(speed, angleRad, startHeight, newTime.toSeconds());
            }
        });
        
        if (cannonSound != null) {
            cannonSound.stop();
            cannonSound.seek(Duration.ZERO);
            cannonSound.play();
        }
        
        animation.setOnFinished(event -> {
            dustOnLandingAnimation(circle.getCenterX() + circle.getTranslateX(), circle.getCenterY() + circle.getTranslateY() + BALL_RADIUS);
            
            if (animation == latest) {
                velocity.setText("Velocity: 0.0 m/s");
                acceleration.setText("Acceleration: 0.0 m/s²");
            }
        });
        
        animations.add(animation);
        latest = animation;
        animation.play();
    }

    /**
     * updates the info labels of the latest cannon ball fired aka the height
     * velocity and acceleration labels
     *
     * @param speed the speed of the cannon ball
     * @param angleRad the angle of the cannon
     * @param startHeight the start height
     * @param t the flight time
     */
    private void updateInfoLabels(double speed, double angleRad, double startHeight, double t) {
        double g = MechanicsUtility.GRAVITY_CONSTANT;
        double vx = MechanicsUtility.velocityX(speed, angleRad);
        double vy = MechanicsUtility.finalVelocity((MechanicsUtility.velocityY(speed, angleRad)), -g, t);
        double h = startHeight + MechanicsUtility.projectileY(speed, angleRad, t);

        height.setText(String.format("Height: %.1f m", Math.max(0, h)));
        velocity.setText(String.format("Velocity: %.1f m/s", MechanicsUtility.speed(vx, vy)));
        acceleration.setText("Acceleration: 9.8 m/s²");
    }

    /**
     * resets the animations, cannonballs and the info labels
     */
    public void resetBalls() {
        lastFireTime = 0;
        latest = null;
        paused = false;
        for (ParallelTransition animation : animations) {
            animation.stop();
        }

        groundLevel.getChildren().removeAll(circles);
        animations.clear();
        circles.clear();
        
        for (ParallelTransition dustAnimation : dustAnimations) {
            dustAnimation.stop();
        }
        
        groundLevel.getChildren().removeAll(dustParticles);
        dustAnimations.clear();
        dustParticles.clear();
        
        height.setText("Height: ");
        velocity.setText("Velocity: ");
        acceleration.setText("Acceleration: ");
    }

    /**
     * removes the ball when the limit of cannon balls in the scene reaches the
     * max
     *
     * @param index the index of the cannon ball being removed
     */
    private void removeBall(int index) {
        ParallelTransition animation = animations.remove(index);
        animation.stop();
        if (animation == latest) {
            latest = null;
        }

        groundLevel.getChildren().remove(circles.remove(index));
    }
    
    /**
     * simulates the recoil of the cannon barrel
     */
    private void cannonRecoil() {
        if (recoilAnimation != null) {
            recoilAnimation.stop();
        }
        
        cannon.setTranslateX(0);
        cannon.setTranslateY(0);
        
        double angleRad = Math.toRadians(angle);
        
        recoilAnimation = new TranslateTransition(Duration.millis(400), cannon);
        recoilAnimation.setByX(-RECOIL_DISTANCE * Math.cos(angleRad));
        recoilAnimation.setByY(RECOIL_DISTANCE * Math.sin(angleRad));
        recoilAnimation.setCycleCount(2);
        recoilAnimation.setAutoReverse(true);
        recoilAnimation.play();
    }
    
    /**
     * adds dust when a cannon ball lands on the floor
     * @param x the x of the cannon ball
     * @param y the y of the cannon ball
     */
    private void dustOnLandingAnimation(double x, double y) {
        for (int i = 0; i < DUST_PARTICLES; i++) {
            double a = Math.PI * (0.08 + 0.84 * i / (DUST_PARTICLES - 1));
            
            Circle particle = new Circle(x, y, DUST_RADIUS, Color.DIMGRAY);
            particle.setMouseTransparent(true);
            dustParticles.add(particle);
            groundLevel.getChildren().add(particle);
            
            TranslateTransition move = new TranslateTransition(Duration.millis(DUST_MILLIS), particle);
            move.setByX(Math.cos(a) * DUST_SPREAD);
            move.setByY(-Math.sin(a) * DUST_RISE);
            
            FadeTransition fade = new FadeTransition(Duration.millis(DUST_MILLIS), particle);
            fade.setFromValue(0.9);
            fade.setToValue(0);
            
            ScaleTransition shrink = new ScaleTransition(Duration.millis(DUST_MILLIS), particle);
            shrink.setToX(0.3);
            shrink.setToY(0.3);
            
            ParallelTransition dustAnimation = new ParallelTransition(move, fade, shrink);
            dustAnimation.setOnFinished(event -> {
                groundLevel.getChildren().remove(particle);
                dustParticles.remove(particle);
                dustAnimations.remove(dustAnimation);
            });
            
            dustAnimations.add(dustAnimation);
            dustAnimation.play();
        }
    }
}

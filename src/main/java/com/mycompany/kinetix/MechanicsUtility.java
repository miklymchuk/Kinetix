package com.mycompany.kinetix;
import java.lang.Math;
import javafx.animation.Interpolator;

/**
 *
 * @author Daniel Savulescu
 */
public class MechanicsUtility {
    public static final double GRAVITY_CONSTANT = 9.81;
    public static Interpolator accelerationInterpolator = new Interpolator() { //Custom interpolator
        @Override
        protected double curve(double t) {
            return t * t;
        }
    };
    
    private MechanicsUtility() {};
    /*
    Note: all measurements are in meters, meters per second, meters per second square, seconds, radians, kilograms, newtons,
    kilogram meter per second, newton meter, depending on the situation
    */
    
    
    //1D Kinematics
    
    /**
     * calculates the final position of the object
     * @param x the initial position
     * @param v the initial velocity
     * @param a the acceleration
     * @param t the time of the displacement
     * @return the final position
     */
    public static double finalPosition(double x, double v, double a, double t) {
        return x + v * t + 0.5 * a * Math.pow(t, 2);
    }
    
    public static double time(double x, double a) {
        double t = Math.sqrt(2 * x / a);
        return t;
    }
    
    /**
     * calculates the final velocity of the object
     * @param v the initial velocity
     * @param a the acceleration
     * @param t the time of the displacement
     * @return the final velocity
     */
    public static double finalVelocity(double v, double a, double t) {
        return v + a * t; 
    }
    
    
    //2D Kinematics
    
    /**
     * calculates the final position in x
     * @param v the velocity
     * @param angleRad the angle at which the velocity is set
     * @param t the time of the displacement
     * @return the final position in x
     */
    public static double projectileX(double v, double angleRad, double t) {
        return v * Math.cos(angleRad) * t;
    }
    
    /**
     * calculates the final position in y
     * @param v the velocity
     * @param angleRad the angle at which the velocity is set
     * @param t the time of the displacement
     * @return the final position in y
     */
    public static double projectileY(double v, double angleRad, double t) {
        return v * Math.sin(angleRad) * t - 0.5 * GRAVITY_CONSTANT * Math.pow(t, 2);
    }
    
    /**
     * calculates the speed in x
     * @param v the velocity
     * @param angleRad the angle at which the projectile is launched
     * @return the speed in x
     */
    public static double velocityX(double v, double angleRad) {
        return v * Math.cos(angleRad);
    }
    
    /**
     * calculates the speed in y
     * @param v the velocity
     * @param angleRad the angle at which the projectile is launched
     * @return the speed in y
     */
    public static double velocityY(double v, double angleRad) {
        return v * Math.sin(angleRad);
    }
    
    /**
     * calculates the speed
     * @param vx the velocity in x
     * @param vy the velocity in y
     * @return the speed
     */
    public static double speed(double vx, double vy) {
        return Math.hypot(vx, vy);
    }
    
    public static double flightTime(double v, double angleRad, double startHeight) {
        double vy = velocityY(v, angleRad);
        return (vy + Math.sqrt(vy * vy + 2 * GRAVITY_CONSTANT * startHeight)) / GRAVITY_CONSTANT;
    }
    
    //Linear momentum
    
    /**
     * calculates the momentum
     * @param mass the mass of the object
     * @param velocity the velocity of the object
     * @return the momentum of the object
     */
    public static double momentum(double mass, double velocity) {
        return mass * velocity;
    }
    
    //Rotational Dynamics
    
    /**
     * calculates the torque of an object
     * @param force the force on the object
     * @param radius the radius of the object
     * @param angleRad the angle at which the force was applied
     * @return the torque of the object
     */
    public static double torque(double force, double radius, double angleRad) {
        return radius * force * Math.sin(angleRad);
    }
}

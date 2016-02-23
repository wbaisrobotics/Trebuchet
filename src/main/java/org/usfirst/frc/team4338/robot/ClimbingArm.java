package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * The two climbing arms of the robot
 */
public class ClimbingArm {
    private Solenoid leftArm;
    private Solenoid rightArm;
    private Victor winchMotor;
    //private Encoder climbDistanceEncoder;
    private boolean isDeployed;
    private boolean towerClimbed;

    /**
     *
     */
    public ClimbingArm(){
        leftArm = new Solenoid(6);
        rightArm = new Solenoid(7);
        winchMotor = new Victor(8);
        //climbDistanceEncoder = new Encoder(0, 1, false, CounterBase.EncodingType.k4X);
        isDeployed = false;
        towerClimbed = false;
    }

    /**
     * Deploys the arms that hook the robot to the rung on the tower
     * Pre: The robot is roughly in position, can be hooked by driving forward after arms are
     * fully extended.
     * Post: The arms are both hooked on the tower rung.
     */
    public void deployArms(){
        leftArm.set(true);
        rightArm.set(true);
        isDeployed = true;
    }

    /**
     * Pulls up the robot to the desired distance with a winch motor and distance encoder.
     * Pre: The climbing arms are both hooked on the tower rung.
     * Post: The robot is raised to the "scaled" position.
     */
    public void climbTower(){
        leftArm.set(false);
        rightArm.set(false);
        //winch pulls up the robot to right distance based on encoder
    }

    public boolean isDeployed(){
        return isDeployed;
    }
}

package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 */
public class ClimbingArm {
    private Solenoid leftArm;
    private Solenoid rightArm;
    private Victor winchMotor;
    private Encoder climbDistanceEncoder;
    private boolean towerClimbed;

    /**
     *
     */
    public ClimbingArm(){
        leftArm = new Solenoid(6);
        rightArm = new Solenoid(7);
        winchMotor = new Victor(8);
        climbDistanceEncoder = new Encoder(0, 1, false, CounterBase.EncodingType.k4X);
        towerClimbed = false;
    }

    /**
     *
     */
    public void deployArms(){

    }

    /**
     *
     */
    public void climbTower(){

    }
}

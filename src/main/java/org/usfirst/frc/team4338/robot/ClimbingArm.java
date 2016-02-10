package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 */
public class ClimbingArm {
    private Victor leftArm;
    private Victor rightArm;
    private Victor winchMotor;
    private Encoder climbDistanceEncoder;
    private boolean towerClimbed;

    public ClimbingArm(){
        leftArm = new Victor();
        rightArm = new Victor();
        winchMotor = new Victor();
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

package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class Claw {
    private CANTalon leftClaw;
    private CANTalon rightClaw;

    /**
     *
     */
    public Claw(){
        //Device ID#0 reserved for easily adding new CAN devices
        leftClaw = new CANTalon(1);
        rightClaw = new CANTalon(2);
    }

    /**
     * Moves the left claw with a given value from -1 to 1.
     * Pre: None
     * Post: The speed of the left claw is set to a given value
     */
    public void moveLeftClaw(double value){
        leftClaw.set(value);
    }

    /**
     * Moves the right claw with a given value from -1 to 1
     * Pre: None
     * Post: The speed of the right claw is set to a given value
     */
    public void moveRightClaw(double value){
        rightClaw.set(value);
    }
}

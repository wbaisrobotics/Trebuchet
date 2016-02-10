package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class Claw {
    private CANTalon leftClaw;
    private CANTalon rightClaw;

    public Claw(){
        //Device ID#0 reserved for easily adding new CAN devices
        leftClaw = new CANTalon(1);
        rightClaw = new CANTalon(2);
    }

    /**
     *
     */
    public void moveLeftClaw(){

    }

    /**
     *
     */
    public void moveRightClaw(){

    }

    /**
     * if position is determinable
     */
    public void setNeutral(){

    }
}

package org.usfirst.frc.team4338.robot;

/**
 *
 */
public enum State {
    SQUAT(0, 0), LOAD(1, 0), TRAVEL(2, 0), LOWSHOT(3, 0), HIGHSHOT(4, 0);

    private int stateID;
    private double angle;

    State(int stateID, double angle){
        this.stateID = stateID;
        this.angle = angle;
    }

    public int getID(){
        return stateID;
    }

    public double getAngle(){
        return angle;
    }
}

package org.usfirst.frc.team4338.robot;

/**
 * Enumeration of the different states that the robot can be in for various actions.
 */
public enum State {
    //ALL ID's MUST BE DIFFERENT
    //ID's are in ascending order to indicate position of rotation.
    SQUAT(0, 0), LOAD(1, 0), TRAVEL(2, 0), LOWSHOT(3, 0), HIGHSHOT(4, 0);

    private int stateID;
    private double angle;

    State(int stateID, double angle){
        this.stateID = stateID;
        this.angle = angle;
    }

    /**
     * Gets the ID of the state
     * @return
     */
    public int getID(){
        return stateID;
    }

    /**
     * Gets the angle of shooter rotation for a given state
     * @return
     */
    public double getAngle(){
        return angle;
    }
}

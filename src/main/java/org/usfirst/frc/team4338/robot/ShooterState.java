package org.usfirst.frc.team4338.robot;

/**
 * Enumeration of the different states that the shooter can be in for various
 * actions.
 */
public enum ShooterState {
	// ALL ID's MUST BE DIFFERENT
	// ID's are in ascending order to indicate position of rotation.
	SQUAT(0, -45), LOAD(1, -20), TRAVEL(2, 0), HIGHSHOT(4, 68);

	private int stateID;
	private double angle;

	private ShooterState(int stateID, double angle) {
		this.stateID = stateID;
		this.angle = angle;
	}

	/**
	 * Gets the angle of shooter rotation for a given state
	 * 
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Gets the ID of the state
	 * 
	 * @return
	 */
	public int getID() {
		return stateID;
	}
}

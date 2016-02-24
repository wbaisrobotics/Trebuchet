package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.Victor;

/**
 *
 */
public class Roller {
	public static final double LOAD = -1;
	public static final double SHOOT_ASSIST = 1;

	private Victor roller;

	/**
	 *
	 */
	public Roller() {
		roller = new Victor(7);
	}

	/**
	 *
	 */
	public void load() {
		roller.set(LOAD);
	}

	/**
	 *
	 */
	public void shootAssist() {
		roller.set(SHOOT_ASSIST);
	}

	/**
	 *
	 */
	public void stop() {
		roller.set(0);
	}
}

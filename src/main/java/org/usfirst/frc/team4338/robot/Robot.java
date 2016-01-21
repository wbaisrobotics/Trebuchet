package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot {
	private Controller controller;

	// Motors
	private RobotDrive drive;
	// Controls
	private Joystick leftJoystick;
	private Joystick rightJoystick;

	// Vision
	private CameraServer server;

	/**
	 * 
	 */
	public Robot() {
		super();
	}

	@Override
	public void autonomousInit() {
		super.autonomousInit();
	}

	@Override
	public void autonomousPeriodic() {
		super.autonomousPeriodic();
	}

	@Override
	public void disabledInit() {
		super.disabledInit();
	}

	@Override
	public void disabledPeriodic() {
		super.disabledPeriodic();
	}

	@Override
	public void robotInit() {
		super.robotInit();

		// Setup vision
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");

		// Setup controls
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		controller = new Controller(2);

		// Setup drive
		drive = new RobotDrive(1, 2);
	}

	@Override
	public void teleopInit() {
		super.teleopInit();
	}

	@Override
	public void teleopPeriodic() {
		super.teleopPeriodic();

		// drive.tankDrive(leftJoystick.getY(), rightJoystick.getY());
	}

	@Override
	public void testInit() {
		super.testInit();
	}

	@Override
	public void testPeriodic() {
		super.testPeriodic();
	}

}

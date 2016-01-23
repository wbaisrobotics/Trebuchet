package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	// Controls
	private Joystick leftJoystick;
	private Joystick rightJoystick;
	private Controller controller;

	// Motors
	private RobotDrive drive;
	private Servo leftGearShiftServo;
	private Servo rightGearShiftServo;

	// Vision
	private CameraServer server;
	
	// Utility
	private Timer timer;

	/**
	 * The robot for the competition.
	 */
	public Robot() {
		super();

		// Set up vision
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");

		// Set up controls
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		controller = new Controller(2);

		// Set up motors
		drive = new RobotDrive(1, 2);
		leftGearShiftServo = new Servo(3);
		rightGearShiftServo = new Servo(4);

		timer = new Timer();
	}

	/**
	 * Initialization code first boot of robot.
	 * This method is called when the robot is turned on.
	 */
	@Override
	public void robotInit(){
		super.robotInit();
	}

	/**
	 * Initialization code for autonomous mode. This method is called each time
	 * the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		super.autonomousInit();
	}

	/**
	 * Periodic code for autonomous mode. This method is called periodically at
	 * a regular rate while the robot is in autonomous mode.
	 */
	@Override
	public void autonomousPeriodic() {
		super.autonomousPeriodic();
	}

	/**
	 * Initialization code for disabled mode. This method is called each time
	 * the robot enters disabled mode.
	 */
	@Override
	public void disabledInit() {
		super.disabledInit();
	}

	/**
	 * Periodic code for disabled mode. This method is called periodically at a
	 * regular rate while the robot is in disabled mode.
	 */
	@Override
	public void disabledPeriodic() {
		super.disabledPeriodic();
	}

	/**
	 * Initialization code for teleop mode. This method is called each time the
	 * robot enters teleop mode.
	 */
	@Override
	public void teleopInit() {
		super.teleopInit();
	}

	/**
	 * Periodic code for teleop mode. This method is called periodically at a
	 * regular rate while the robot is in teleop mode.
	 */
	@Override
	public void teleopPeriodic() {
		super.teleopPeriodic();

		//Shift to lower gear
		if(leftJoystick.getTrigger() && rightJoystick.getTrigger()){

		} else{

		}

		drive.tankDrive(leftJoystick.getY(), rightJoystick.getY());
	}

	/**
	 * Initialization code for test mode. This method is called each time the
	 * robot enters test mode.
	 */
	@Override
	public void testInit() {
		super.testInit();
	}

	/**
	 * Periodic code for test mode. This method is called periodically at a
	 * regular rate while the robot is in test mode.
	 */
	@Override
	public void testPeriodic() {
		super.testPeriodic();
	}

}

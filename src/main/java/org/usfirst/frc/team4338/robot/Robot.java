package org.usfirst.frc.team4338.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4338.robot.vision.Camera;
import org.usfirst.frc.team4338.robot.vision.Particle;
import org.usfirst.frc.team4338.robot.vision.ParticleReport;
import org.usfirst.frc.team4338.robot.vision.ScoringResult;
import org.usfirst.frc.team4338.robot.vision.TapeTarget;
import org.usfirst.frc.team4338.robot.vision.Target;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private static final double MIN_SCORE = 75d;

	// Controls
	private Joystick leftJoystick;
	private Joystick rightJoystick;
	private Controller controller;

	// Motors
	private RobotDrive drive;
	private Victor shooter;
	private Servo leftGearShiftServo;
	private Servo rightGearShiftServo;

	// Vision
	private Camera camera;

	// Utility
	private Timer timer;

	/**
	 * The robot for the competition.
	 */
	public Robot() {
		super();

		// Set up vision
		camera = new Camera(Camera.DEFAULT_VIEW_ANGLE);

		// Set up controls
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		controller = new Controller(2);

		// Set up motors
		drive = new RobotDrive(0, 1);
		shooter = new Victor(4);
		leftGearShiftServo = new Servo(2);
		rightGearShiftServo = new Servo(3);

		timer = new Timer();
	}

	/**
	 * Initialization code for autonomous mode. This method is called each time
	 * the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		// TODO
	}

	/**
	 * Periodic code for autonomous mode. This method is called periodically at
	 * a regular rate while the robot is in autonomous mode.
	 */
	@Override
	public void autonomousPeriodic() {
		// TODO
	}

	/**
	 * Initialization code for disabled mode. This method is called each time
	 * the robot enters disabled mode.
	 */
	@Override
	public void disabledInit() {
		// TODO
	}

	/**
	 * Periodic code for disabled mode. This method is called periodically at a
	 * regular rate while the robot is in disabled mode.
	 */
	@Override
	public void disabledPeriodic() {
		// TODO
	}

	/**
	 * Initialization code first boot of robot. This method is called when the
	 * robot is turned on.
	 */
	@Override
	public void robotInit() {
		// TODO
	}

	private boolean targetVisible() {
		Target target = new TapeTarget();
		NIVision.imaqColorThreshold(camera.getBinaryFrame(), camera.getFrame(), 255, ColorMode.HSV,
				target.getHueRange(), target.getSatRange(), target.getValRange());
		CameraServer.getInstance().setImage(camera.getBinaryFrame());
		int numParticles = NIVision.imaqCountParticles(camera.getBinaryFrame(), 1);

		List<ParticleReport> reports = new ArrayList<ParticleReport>();
		Image binaryFrame = camera.getBinaryFrame();
		for (int i = 0; i < numParticles; i++)
			reports.add(new Particle(binaryFrame, i).createReport());

		boolean visible = false;
		for (ParticleReport report : reports) {
			ScoringResult result = new ScoringResult(report);
			if (result.getAspectScore() >= MIN_SCORE && result.getAreaScore() >= MIN_SCORE)
				visible = true;
		}

		return visible;
	}

	/**
	 * Initialization code for teleop mode. This method is called each time the
	 * robot enters teleop mode.
	 */
	@Override
	public void teleopInit() {
		// TODO
	}

	/**
	 * Periodic code for teleop mode. This method is called periodically at a
	 * regular rate while the robot is in teleop mode.
	 */
	@Override
	public void teleopPeriodic() {
		camera.captureImage();
		
		SmartDashboard.putBoolean("Target visible", targetVisible());

		// Shift to lower gear
		if (leftJoystick.getTrigger() || rightJoystick.getTrigger()) {
			leftGearShiftServo.setAngle(50);
			rightGearShiftServo.setAngle(50);
		} else {
			leftGearShiftServo.setAngle(110);
			rightGearShiftServo.setAngle(110);
		}

		shooter.set(controller.getRightJoyY());

		drive.tankDrive(leftJoystick.getY(), rightJoystick.getY());
	}

	/**
	 * Initialization code for test mode. This method is called each time the
	 * robot enters test mode.
	 */
	@Override
	public void testInit() {
		// TODO
	}

	/**
	 * Periodic code for test mode. This method is called periodically at a
	 * regular rate while the robot is in test mode.
	 */
	@Override
	public void testPeriodic() {
		// TODO
	}

}

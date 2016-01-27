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
	private static final int PERIODIC_DELAY = 5;

	// Vision
	private Camera camera;
	private Controller controller;
	// Motors
	private RobotDrive drive;

	private Servo leftGearShiftServo;
	// Controls
	private Joystick leftJoystick;
	private Servo rightGearShiftServo;
	private Joystick rightJoystick;

	// Shooting
	private Victor shooterBelt1;
	private Victor shooterBelt2;
	private Victor shooterAngleMotor;
	private DoubleSolenoid ballFlicker;

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
		shooterBelt1 = new Victor(4);
		shooterBelt2 = new Victor(5);
		leftGearShiftServo = new Servo(2);
		rightGearShiftServo = new Servo(3);
		shooterAngleMotor = new Victor(6);

		// Set up solenoid
		ballFlicker = new DoubleSolenoid(7, 8);
		ballFlicker.set(DoubleSolenoid.Value.kReverse);
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
	 * Initialization code for the first boot of the robot. This method is
	 * called when the robot is turned on.
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
		boolean triggersDown = leftJoystick.getTrigger() || rightJoystick.getTrigger();
		double angle = triggersDown ? 50 : 110;
		leftGearShiftServo.setAngle(angle);
		rightGearShiftServo.setAngle(angle);

		drive.tankDrive(leftJoystick.getY(), rightJoystick.getY());

		Timer.delay((double) PERIODIC_DELAY / 1000);

		// TODO Create gear toggle method

		if (controller.getButtonA()) {
			// Creep / Angle
			leftGearShiftServo.setAngle(50);
			rightGearShiftServo.setAngle(50);
			// drive.tankDrive(VAL, VAL);
			
			// Toggle shooter angle
			// If the color of the floor changes
			drive.tankDrive(0, 0);

			// Spin up belts
			shooterBelt1.set(1);
			shooterBelt2.set(1);
			Timer.delay(2);

			// Shoot
			ballFlicker.set(DoubleSolenoid.Value.kForward);
			Timer.delay(0.5);
			ballFlicker.set(DoubleSolenoid.Value.kReverse);
		}
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

package org.usfirst.frc.team4338.robot;

import org.usfirst.frc.team4338.robot.vision.Camera;
import org.usfirst.frc.team4338.robot.vision.TapeTarget;
import org.usfirst.frc.team4338.robot.vision.VisionThread;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class RobotOld extends IterativeRobot {
    private static final long PERIODIC_DELAY = 5;

    private double angle;
    private DoubleSolenoid ballFlicker;
    private Camera camera;
    private Controller controller;
    private RobotDrive drive;
    private AnalogGyro gyro;
    private double kp = 0.03; // What's this?
    private Servo leftGearShiftServo;
    private Joystick driveJoystick;
    private Joystick leftJoystick;
    private Servo rightGearShiftServo;
    private Joystick rightJoystick;
    private Victor shooterAngleMotor;
    private Victor shooterBelt1;
    private Victor shooterBelt2;
    private Victor leftPushArm;
    private Victor rightPushArm;
    private TapeTarget target;
    private Thread visionThread;

    /**
     * The robot for the competition.
     */
    public RobotOld() {
        super();

        // Set up vision
        camera = new Camera(Camera.DEFAULT_VIEW_ANGLE);

        // Set up controls
        driveJoystick = new Joystick(0);
        controller = new Controller(1);

        // Set up motors
        drive = new RobotDrive(0, 1);
        drive.setExpiration(.1);
        shooterBelt1 = new Victor(4);
        shooterBelt2 = new Victor(5);
        leftGearShiftServo = new Servo(8);
        rightGearShiftServo = new Servo(9);
        shooterAngleMotor = new Victor(6);
        leftPushArm = new Victor(2);
        rightPushArm = new Victor(3);

        // Set up solenoid
        //Are these parameters pwm or off a pneumatic control board?
        ballFlicker = new DoubleSolenoid(7, 8);
        ballFlicker.set(DoubleSolenoid.Value.kReverse);

        gyro = new AnalogGyro(0);
        target = new TapeTarget();
        visionThread = new Thread(new VisionThread(camera, target));
    }

    /**
     * Initialization code for autonomous mode. This method is called each time
     * the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        // TODO
        gyro.reset();
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

    /**
     * Shifts the tank drive into a low (fast) gear or a high (slow) gear with a
     * 0 or 1 toggle respectively.
     *
     * @param state
     */
    private void shiftGear(int state) {
        if (state == 0) {
            leftGearShiftServo.setAngle(110);
            rightGearShiftServo.setAngle(110);
        } else if (state == 1) {
            leftGearShiftServo.setAngle(50);
            rightGearShiftServo.setAngle(50);
        }
    }

    /**
     * Initialization code for teleop mode. This method is called each time the
     * robot enters teleop mode.
     */
    @Override
    public void teleopInit() {
        // TODO
        gyro.reset();
    }

    /**
     * Periodic code for teleop mode. This method is called periodically at a
     * regular rate while the robot is in teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        camera.captureImage();

        if (!visionThread.isAlive())
            visionThread.start();

        angle = gyro.getAngle();

        if(controller.getButtonRB()){
            rightPushArm.set(1);
        } else if(controller.getRightTrigger() > 0){
            rightPushArm.set(-1);
        } else{
            rightPushArm.set(0);
        }
        if(controller.getButtonLB()){
            leftPushArm.set(1);
        } else if(controller.getLeftTrigger() > 0){
            leftPushArm.set(-1);
        } else{
            leftPushArm.set(0);
        }

        // Shift to high gear if either joystick trigger is pulled
        //shiftGear(driveJoystick.getTrigger() ? 1 : 0);

        //drive.tankDrive(driveJoystick.getY() - driveJoystick.getX(), driveJoystick.getY() + driveJoystick.getX());
        drive.tankDrive(controller.getRightJoyY() - controller.getRightJoyX(), controller.getRightJoyY() + controller.getRightJoyX());

        Timer.delay((double) PERIODIC_DELAY / 1000);

		/*
		//Commented out for now
		if (controller.getButtonA()) {
			// Creep / Angle
			shiftGear(1);
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
			Timer.delay(1);
			ballFlicker.set(DoubleSolenoid.Value.kReverse);
			shooterBelt1.set(0);
			shooterBelt2.set(0);
		}
		*/
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

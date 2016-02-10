package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class Robot extends IterativeRobot {
    private static final long PERIODIC_DELAY = 5;

    private Shooter shooter;
    private BallLoader ballLoader;
    private Claw claw;
    private ClimbingArm climbingArm;

    private RobotDrive drive;

    private DoubleSolenoid leftGearShifter;
    private DoubleSolenoid rightGearShifter;

    private Controller controller;

    private AnalogGyro gyro;
    private double angle;

    /**
     *
     */
    public Robot(){
        shooter = new Shooter();
        ballLoader = new BallLoader();
        claw = new Claw();
        climbingArm = new ClimbingArm();

        drive = new RobotDrive(0, 1);
        drive.setExpiration(0.1);

        leftGearShifter = new DoubleSolenoid(2, 3);
        rightGearShifter = new DoubleSolenoid(4, 5);

        controller = new Controller(0);

        gyro = new AnalogGyro(1);
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
        // TODO
        angle = gyro.getAngle();

        double x = controller.getRightJoyX();
        x = Math.signum(x) * Math.pow(x, 2);
        double y = controller.getRightJoyY();
        y = Math.signum(y) * Math.pow(y, 2);
        drive.tankDrive(0.75 * y - 0.75 * x, 0.75 * y + 0.75 * x);

        if(controller.getButtonRB()){

        } else if(controller.getRightTrigger() > 0){

        } else{
        }
        if(controller.getButtonLB()){

        } else if(controller.getLeftTrigger() > 0){

        } else{

        }

        Timer.delay((double) PERIODIC_DELAY / 1000);
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

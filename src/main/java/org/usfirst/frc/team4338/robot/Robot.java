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
    private double kp = 0.03;

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

        leftGearShifter = new DoubleSolenoid(0, 1);
        rightGearShifter = new DoubleSolenoid(2, 3);

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
        autoMoveStraight(0.6, 2);
    }

    /**
     * This autonomous method drives the robot straight with a given velocity
     * >0 = forward, <0 = backward, 0 = stop
     * @param velocity
     * @param time the amount of seconds to drive straight
     */
    private void autoMoveStraight(double velocity, double time){
        double bearing = gyro.getAngle();
        double start = Timer.getFPGATimestamp();

        while(Timer.getFPGATimestamp() - start < time){
            angle = gyro.getAngle();

            //drive.tankDrive(y - x, y + x);
            drive.tankDrive(velocity - (angle - bearing) * kp, velocity + (angle - bearing) * kp);

            Timer.delay((double) PERIODIC_DELAY / 1000);
        }

        drive.tankDrive(0, 0);
    }

    /**
     * Turns the robot by a given angle
     * @param turnAngle the amount to turn
     */
    private void autoTurn(double turnAngle){
        double bearing = gyro.getAngle();

        while(angle - bearing < turnAngle){
            angle = gyro.getAngle();

            //Change this
            drive.tankDrive(angle * kp, angle * kp);

            Timer.delay((double) PERIODIC_DELAY / 1000);
        }

        drive.tankDrive(0, 0);
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
        x = 0.8 * Math.signum(x) * Math.pow(x, 2);
        double y = controller.getRightJoyY();
        y = 0.9 * Math.signum(y) * Math.pow(y, 2);
        drive.tankDrive(y - x, y + x);

        if(controller.getButtonRB()){
            claw.moveRightClaw(1);
        } else if(controller.getRightTrigger() > 0){
            claw.moveRightClaw(-1);
        } else{
            claw.moveRightClaw(0);
        }
        if(controller.getButtonLB()){
            claw.moveLeftClaw(1);
        } else if(controller.getLeftTrigger() > 0){
            claw.moveLeftClaw(-1);
        } else{
            claw.moveLeftClaw(0);
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

package org.usfirst.frc.team4338.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import org.usfirst.frc.team4338.robot.vision.Camera;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The main robot class.
 * Contains the various components of the Robot
 */
public class Robot extends IterativeRobot {
    private static final double PERIODIC_DELAY = 0.005;

    private boolean debugMode = false;
    private DigitalInput debugSwitch;
    private double lastDebounceTime = 0;
    private double debounceDelay = 0.05;
    private boolean buttonState = false;
    private boolean lastButtonState = false;

    private Compressor compressor;

    //Camera
    private int session;
    private Image frame;

    //Robot objects
    private Shooter shooter;
    private Roller roller;
    private Claw claw;
    private ClimbingArm climbingArm;

    //Drive system
    private RobotDrive drive;

    //Gear shifters
    private DoubleSolenoid leftGearShifter;
    private DoubleSolenoid rightGearShifter;

    //Controller
    private Controller controller;

    //Gyro stuff
    private AnalogGyro gyro;
    private double angle;
    private double kp = 0.03;

    /**
     *
     */
    public Robot(){
        debugSwitch = new DigitalInput(0);

        compressor = new Compressor(0);

        //Camera
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);

        shooter = new Shooter();
        roller = new Roller();
        claw = new Claw();
        climbingArm = new ClimbingArm();

        drive = new RobotDrive(0, 1);
        drive.setExpiration(0.1);

        leftGearShifter = new DoubleSolenoid(0, 1);
        rightGearShifter = new DoubleSolenoid(2, 3);

        controller = new Controller(0);

        gyro = new AnalogGyro(0);
    }

    /**
     * Initialization code for the first boot of the robot. This method is
     * called when the robot is turned on.
     */
    @Override
    public void robotInit() {
        compressor.setClosedLoopControl(true);
    }

    /**
     * Initialization code for autonomous mode. This method is called each time
     * the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        gyro.reset();
    }

    /**
     * Periodic code for autonomous mode. This method is called periodically at
     * a regular rate while the robot is in autonomous mode.
     */
    @Override
    public void autonomousPeriodic() {
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

            Timer.delay(PERIODIC_DELAY);
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

            Timer.delay(PERIODIC_DELAY);
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
     * Initialization code for teleop mode. This method is called each time the
     * robot enters teleop mode.
     */
    @Override
    public void teleopInit() {
        gyro.reset();
        NIVision.IMAQdxStartAcquisition(session);
    }

    /**
     * Periodic code for teleop mode. This method is called periodically at a
     * regular rate while the robot is in teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        //Toggle debug mode with switch
        //If the switch doesn't work then have a toggle button (start)
        //^Will need debouncing to make switching non-trivial
        /*
        if(debugSwitch.get()){
            debugMode = true;
        } else{
            debugMode = false;
        }
        */

        //Gets the current reading of the debug button
        boolean debugReading = controller.getButtonStart();

        //Check if the last debounce time needs to be reset
        if(debugReading != lastButtonState){
            lastDebounceTime = Timer.getFPGATimestamp();
        }

        //Longer than debounce delay so its the actual state of the button
        if(Timer.getFPGATimestamp() - lastDebounceTime > debounceDelay){
            //Change the button state if it has changed
            if(buttonState != debugReading){
                buttonState = debugReading;

                //Only toggle the debug state if the new button state is true
                if(buttonState){
                    debugMode = !debugMode;
                }
            }
        }

        //Save the reading for next loop
        lastButtonState = debugReading;

        //Capture camera image
        NIVision.IMAQdxGrab(session, frame, 1);
        NIVision.imaqDrawLineOnImage(frame, frame, NIVision.DrawMode.DRAW_VALUE, new NIVision.Point(0, 500), new NIVision.Point(640, 410), 0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame, NIVision.DrawMode.DRAW_VALUE, new NIVision.Point(295, 0), new NIVision.Point(370, 480), 0.0f);
        CameraServer.getInstance().setImage(frame);

        if(debugMode){
            debug();
        } else{
            drive();
            pollInput();
        }

        //Print information to smart dashboard
        SmartDashboard.putBoolean("debugMode", debugMode);
        SmartDashboard.putNumber("robot angle", gyro.getAngle());
        SmartDashboard.putNumber("shooter angle", shooter.getAngle());
        SmartDashboard.putNumber("left light sensor", shooter.getLeftLightSensorValue());
        SmartDashboard.putNumber("right light sensor", shooter.getRightLightSensorValue());

        Timer.delay(PERIODIC_DELAY);
    }

    /**
     * Handles the driving of the robot.
     * Takes into account if turning, uses the gyro when going straight, and
     * projects the x and y values of the controller onto an x^2 graph to make it exponential.
     */
    private void drive(){
        //TODO Add gyro driving straight?? (no time....)
        angle = gyro.getAngle();

        double x = controller.getRightJoyX();
        x = 0.8 * Math.signum(x) * Math.pow(x, 2);
        double y = controller.getRightJoyY();
        y = 0.9 * Math.signum(y) * Math.pow(y, 2);

        drive.tankDrive(y - x, y + x);
    }

    /**
     * Polls the controller, etc for input and reacts accordingly
     */
    private void pollInput(){
        if(controller.getButtonA()){ //secondary input
            //Start climb
            if(controller.getButtonBack()){
                climbingArm.deployArms();
            }
            //Individually move claws
            if(controller.getButtonLB()){ //Move left claw up
                claw.moveLeftClaw(1);
            } else if(controller.getLeftTrigger() > 0){ //Move left claw down
                claw.moveLeftClaw(-1);
            } else{
                claw.moveLeftClaw(0);
            }
            if(controller.getButtonRB()){ //Move right claw up
                claw.moveRightClaw(1);
            } else if(controller.getRightTrigger() > 0){ //Move right claw down
                claw.moveRightClaw(-1);
            } else{
                claw.moveRightClaw(0);
            }
            //Manual lifter control
            if(controller.getPOV() == 0){ //lifters up
                shooter.moveLifters(1);
            } else if(controller.getPOV() == 180){ //lifters down
                shooter.moveLifters(-1);
            } else{
                shooter.moveLifters(0);
            }
        } else { //primary input
            //State changing
            if (controller.getPOV() == 90) { //Increase state
                shooter.increaseState();
            } else if (controller.getPOV() == 270) { //Decrease state
                shooter.decreaseState();
            }
            //Gear shifting
            //Default in Low slow gear
            if (controller.getButtonRS()) {
                shiftHigh();
            } else {
                shiftLow();
            }
            //Shooting
            if (controller.getButtonRB()) { //shoot high
                shooter.shoot();
            } else if (controller.getRightTrigger() > 0) { //shoot low
                roller.shootAssist();
                shooter.shoot();
                roller.stop();
            }
            //Ball loading
            if (controller.getLeftTrigger() > 0) {
                roller.load();
                shooter.loadBall();
            } else {
                roller.stop();
                shooter.stopBelts();
            }
            //Finish climb if arms are deployed
            if (controller.getButtonBack() && climbingArm.isDeployed()) {
                climbingArm.climbTower();
            }
            //Claw control
            if (controller.getPOV() == 180) { //move claws up
                claw.moveClaws(1);
            } else if (controller.getPOV() == 0) { //move claws down
                claw.moveClaws(-1);
            } else {
                claw.moveClaws(0);
            }
        }
    }

    /**
     * DEBUG MODE METHOD
     */
    public void debug(){
        //Individually control shooter lifter motors
        if(controller.getButtonLB()){ //Move left lifter motor up
            shooter.moveLeftLifter(1);
        } else if(controller.getLeftTrigger() > 0){ //Move left lifter motor down
            shooter.moveLeftLifter(-1);
        } else{
            shooter.moveLeftLifter(0);
        }
        if(controller.getButtonRB()){ //Move right lifter motor up
            shooter.moveRightLifter(1);
        } else if(controller.getRightTrigger() > 0){ //Move right lifter motor down
            shooter.moveRightLifter(-1);
        } else{
            shooter.moveRightLifter(0);
        }

        //calibration??
    }

    /**
     * Shifts the robot drive into high gear.
     * Faster but less torque
     */
    public void shiftHigh(){
        leftGearShifter.set(DoubleSolenoid.Value.kReverse);
        rightGearShifter.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Shifts the robot drive into low gear.
     * Slower but more torque
     */
    public void shiftLow(){
        leftGearShifter.set(DoubleSolenoid.Value.kForward);
        rightGearShifter.set(DoubleSolenoid.Value.kForward);
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
    
    public Shooter getShooter() {
    	return shooter;
    }
}

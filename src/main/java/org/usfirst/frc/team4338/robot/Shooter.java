package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.*;

/**
 * The shooter of the robot
 */
public class Shooter {
    private AnalogGyro gyro;
    private AnalogInput lightSensor;
    private Victor belts;
    private Victor leftLifter;
    private Victor rightLifter;
    private Servo leftLockingServo;
    private Servo rightLockingServo;
    private DoubleSolenoid launchingPiston;

    private boolean travelLocked;
    private boolean pistonReady;
    private final double lightSensorThreshold = 3200;

    private enum State {
        SQUAT(0), LOAD(1), TRAVEL(2), LOWSHOT(3), HIGHSHOT(4);

        private int stateID;

        State(int stateID){
            this.stateID = stateID;
        }

        public int getID(){
            return stateID;
        }
    }

    private State state;

    /**
     *
     */
    public Shooter(){
        gyro = new AnalogGyro(1);
        lightSensor = new AnalogInput(2);
        belts = new Victor(2);
        leftLifter = new Victor(3);
        rightLifter = new Victor(4);
        leftLockingServo = new Servo(5);
        rightLockingServo = new Servo(6);
        launchingPiston = new DoubleSolenoid(4, 5);

        travelLocked = true;
        pistonReady = false;

        state = State.TRAVEL;
    }

    /**
     * Calibrates the travel position of the shooter by getting a min and max angle where the light sensor spikes and
     * is set in between them
     * Pre: Shooter is in the travel position
     * Post: Shooter is more accurately in the travel position for optimal light spike reading
     */
    public void calibrate(){
        double upper;
        double lower;

        unlockFromTravel();

        //Get upper angle
        while(getAngle() <= 45){
            moveLifters(0.5);
        }
        while(!lightAboveThreshold()){
            moveLifters(-0.25);
        }
        upper = getAngle();

        //Get lower angle
        while(getAngle() >= -45){
            moveLifters(-0.5);
        }
        while(!lightAboveThreshold()){
            moveLifters(0.25);
        }
        lower = getAngle();

        //GO IN BETWEEN

        gyro.reset();
    }

    /**
     * Controls all of the automatic actions that it takes to load a ball into the shooter
     * Pre: Shooter is empty (position of shooter to be determined later)
     * Post: Perform sequence to load a ball so Shooter has a ball, and is in the travel position
     */
    public void loadBall(){

    }

    /**
     * Moves the shooter to the lowest Squat position to travel under the Low Bar obstacle.
     * Pre: None
     * Post: If the shooter is not in the SquatPosition, then make sure the LockingServo is in the
     * TravelUnlocked state, and move the side motors until they are in the Squat postion based on the ShooterGyro.
     */
    public void squatPosition(){

    }

    /**
     *Moves the shooter to the tilted Load position so that a new ball can be loaded into the robot.
     * Pre: None
     * Post: If the shooter is not in the LoadPosition, Then check that TravelLocked is false
     * (if not, unlock the shooter from Travel mode), move the shooter to the angled LoadPosition based on the ShooterGyro.
     */
    public void loadPosition(){

    }

    /**
     * Moves the shooter to the horizontal Travel position, and secures the front of the shooter for travel over rough terrain.
     * Pre: None
     * Post: If the shooter is not in the TravelPosition, then check that the TravelLocked is false,
     * move the shooter to the horizontal TravelPostion based on the ShooterGyro,
     * activate the LockingServo to the TravelLocked position and update all state variables involved.
     */
    public void travelPosition(){
        //gyro position 0
        //When light sensor value peaks above threshold

        if(state.getID() > State.TRAVEL.getID()){ //Shooter needs to be lowered
            while(!lightAboveThreshold()){
                moveLifters(-1 * (Math.abs(getAngle()) <= 30 ? 0.25 : 1)); //Move at quarter speed when close to travel pos
            }
        } else if(state.getID() < State.TRAVEL.getID()){ //Shooter needs to be raised
            while(!lightAboveThreshold()){
                moveLifters(1 * (Math.abs(getAngle()) <= 30 ? 0.25 : 1)); //Move at quarter speed when close to travel pos
            }
        }

        lockForTravel();
        gyro.reset();
        state = State.TRAVEL;
    }

    /**
     * Activates the sequence of events that will shoot at one of the low target holes
     * Pre: Shooter has a ball loaded, and robot is facing a Low target hole
     * Post: Robot transitions to the LowShot position (unlocking from TravelPosition if necessary),
     * creeps to shooting postion based on ColorSensor, Spins up the Top & Bottom motors, activates the LaunchingPistion,
     * recovers the LaunchingPiston to PistonReady, returns the robot to Travel position.
     */
    public void shootLowPosition(){

    }

    /**
     * Activates the sequence of events that will shoot at one of the high target holes
     * Pre: Shooter has ball loaded, and robot is facing a High target hole
     * Post: Robot transitions to the HighShot position (unlocking from TravelPosition if necessary),
     * creeps to shooting postion based on ColorSensor, Spins up the Top & Bottom motors, activates the LaunchingPiston,
     * recovers the LaunchingPiston to PistonReady, returns the robot to Travel position.
     */
    public void shootHighPosition(){

    }

    /**
     * Activates the LockingServo to slide the locking-cam into place so the front of the robot is stable and
     * safe to travel over rough terrain.
     * Pre: Shooter must be in the travel position (based on ShooterGyro) to allow the locking-cam to slide into position.
     * Post: If TravelLocked is false AND in TravelPosition, then activate the LockingServo to the
     * TravelLocked position, else do nothing.
     */
    public void lockForTravel(){
        leftLockingServo.setAngle(170);
        rightLockingServo.setAngle(10);
        travelLocked = true;
    }

    /**
     * Activates the LockingServo to slide the locking-cam out of place so the front of the robot is no longer stable
     * and safe to travel over rough terrain.
     * Pre: Shooter must be in the travel position (based on ShooterGyro) to allow the locking-cam to slide into position.
     * Post: If TravelLocked is true AND in TravelPosition, then activate the LockingServo to the
     * TravelUnlocked position, else do nothing.
     */
    public void unlockFromTravel(){
        leftLockingServo.setAngle(60);
        rightLockingServo.setAngle(110);
        travelLocked = false;
    }

    /**
     * Moves both lifter motors at the same speed.
     * @param value speed/direction of the motors
     */
    public void moveLifters(double value){
        leftLifter.set(value);
        rightLifter.set(value);
    }

    /**
     * Used for individually moving the left lifter motor for lining up with the right lifter motor.
     * @param value speed/direction of the motor
     */
    public void debugLeftLifter(double value){
        leftLifter.set(value);
    }

    /**
     * Used for individually moving the right lifter motor for lining up with the left lifter motor.
     * @param value speed/direction of the motor
     */
    public void debugRightLifter(double value){
        rightLifter.set(value);
    }

    /**
     * Gets the angle of the shooter gyro
     * @return
     */
    public double getAngle(){
        return gyro.getAngle();
    }

    /**
     * Gets the intensity of light read from the light sensor
     * @return
     */
    public double getLightSensorValue(){
        return lightSensor.getValue();
    }

    /**
     * Gets if the current light sensor value is above the threshold, meaning aligned with the LED
     * @return
     */
    public boolean lightAboveThreshold(){
        return getLightSensorValue() >= lightSensorThreshold;
    }
}

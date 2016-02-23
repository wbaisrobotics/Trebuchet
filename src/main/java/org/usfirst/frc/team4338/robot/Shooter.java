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

    private ShooterState state;

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

        state = ShooterState.TRAVEL;
    }

    public ShooterState getState() {
		return state;
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

        if(travelLocked){ //Check if servos are locked
            unlockFromTravel();
        }

        //Get upper angle
        while(getAngle() <= 45){
            moveLifters(0.5);
        }
        moveLifters(0);
        while(!lightAboveThreshold()){
            moveLifters(-0.15);
        }
        moveLifters(0);
        upper = getAngle();

        //Get lower angle
        while(getAngle() >= -45){
            moveLifters(-0.5);
        }
        moveLifters(0);
        while(!lightAboveThreshold()){
            moveLifters(0.15);
        }
        moveLifters(0);
        lower = getAngle();

        //Go in between of upper and lower angle
        while(getAngle() <= getAngle() + (upper - lower) / 2){
            moveLifters(0.15);
        }
        moveLifters(0);

        gyro.reset();
    }

    /**
     * Changes the state of the robot and moves the shooter accordingly
     * Used for transferring between states that depend on only the gyro.
     * Do not use this method for transferring to the Travel position as the use of the light sensor is required.
     * @param newState
     */
    private void changeState(ShooterState newState){
        if(travelLocked){ //Check if locked for travel
            unlockFromTravel();
        }

        if(state.getID() < newState.getID()){ //Shooter needs to be raised
            //Use gyro to move shooter
            while(getAngle() < newState.getAngle()){
                moveLifters(0.5);
            }
            moveLifters(0);
        } else if(state.getID() > newState.getID()){ //Shooter needs to be lowered
            //Use gyro to move shooter
            while(getAngle() > newState.getAngle()){
                moveLifters(-0.5);
            }
            moveLifters(0);
        }

        state = newState;
    }

    /**
     * Increases the state of the robot
     */
    public void increaseState(){
        switch(state){
            case SQUAT: changeState(ShooterState.LOAD); break;
            case LOAD: changeState(ShooterState.TRAVEL); break;
            case TRAVEL: changeState(ShooterState.HIGHSHOT); break; //Probably change to travelState() call
            case HIGHSHOT: break;
        }
    }

    /**
     * Decreases the state of the robot
     */
    public void decreaseState(){
        switch(state){
            case SQUAT: break;
            case LOAD: changeState(ShooterState.SQUAT); break;
            case TRAVEL: changeState(ShooterState.LOAD); break; //Probably change to travelState() call
            case HIGHSHOT: changeState(ShooterState.TRAVEL); break;
        }
    }

    /**
     * Controls all of the automatic actions that it takes to load a ball into the shooter
     * Pre: Shooter is empty (position of shooter to be determined later)
     * Post: Perform sequence to load a ball so Shooter has a ball, and is in the travel position
     */
    public void loadBall(){
        belts.set(1);
    }

    /**
     * Moves the shooter to the horizontal Travel state, and secures the front of the shooter for travel over rough terrain.
     * Pre: None
     * Post: If the shooter is not in the Travel state, then check that the TravelLocked is false,
     * move the shooter to the horizontal Travel state based on the ShooterGyro,
     * activate the LockingServo to the TravelLocked state and update all state variables involved.
     */
    public void travelState(){
        double lastTimeReading = Timer.getFPGATimestamp();
        double lastAngleReading = getAngle();

        //gyro position 0
        //When light sensor value peaks above threshold
        if(state.getID() < ShooterState.TRAVEL.getID()){ //Shooter needs to be raised
            while(!lightAboveThreshold()){
                //moveLifters(1 * (Math.abs(getAngle()) <= 25 ? 0.15 : 1)); //Move at 15% speed when close to travel pos
                moveLifters(0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    break;
                }
            }
            moveLifters(0);
        } else if(state.getID() > ShooterState.TRAVEL.getID()){ //Shooter needs to be lowered
            while(!lightAboveThreshold()){
                //moveLifters(-1 * (Math.abs(getAngle()) <= 25 ? 0.15 : 1)); //Move at 15% speed when close to travel pos
                moveLifters(-0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() < 0){
                    break;
                }
            }
            moveLifters(0);
        }

        lockForTravel();
        gyro.reset();
        state = ShooterState.TRAVEL;
    }

    /**
     * Shoots the ball
     * Pre: There is a ball in the robot
     * Post: The ball is launched
     */
    public void shoot(){
        belts.set(-3);
        Timer.delay(1);
        launchingPiston.set(DoubleSolenoid.Value.kForward);
        Timer.delay(0.5);
        launchingPiston.set(DoubleSolenoid.Value.kReverse);
        belts.set(0);
    }

    /**
     * Activates the LockingServo to slide the locking-cam into place so the front of the robot is stable and
     * safe to travel over rough terrain.
     * Pre: Shooter must be in the travel state (based on ShooterGyro) to allow the locking-cam to slide into position.
     * Post: If TravelLocked is false AND in Travel state, then activate the LockingServo to the
     * TravelLocked state, else do nothing.
     */
    public void lockForTravel(){
        leftLockingServo.setAngle(170);
        rightLockingServo.setAngle(10);
        travelLocked = true;
    }

    /**
     * Activates the LockingServo to slide the locking-cam out of place so the front of the robot is no longer stable
     * and safe to travel over rough terrain.
     * Pre: Shooter must be in the travel state (based on ShooterGyro) to allow the locking-cam to slide into position.
     * Post: If TravelLocked is true AND in Travel state, then activate the LockingServo to the
     * TravelUnlocked state, else do nothing.
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

        //FOR DEBUGGING ONLY
        //TODO REMOVE AFTER TESTING
        if(value < 0){
            state = ShooterState.SQUAT;
        } else if(value > 0){
            state = ShooterState.HIGHSHOT;
        }
    }

    /**
     * Turns off the shooting belts
     */
    public void stopBelts(){
        belts.set(0);
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

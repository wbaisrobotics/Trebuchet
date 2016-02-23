package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.*;

/**
 * The shooter of the robot
 */
public class Shooter {
    private AnalogGyro gyro;
    private AnalogInput leftLightSensor;
    private AnalogInput rightLightSensor;
    private Victor belts;
    private Victor leftLifter;
    private Victor rightLifter;
    private Servo leftLockingServo;
    private Servo rightLockingServo;
    private DoubleSolenoid launchingPiston;

    private boolean travelLocked;
    private final double leftLightSensorThreshold = 3200;
    private final double rightLightSensorThreshold = 3200;

    private ShooterState state;

    /**
     *
     */
    public Shooter(){
        gyro = new AnalogGyro(1);
        leftLightSensor = new AnalogInput(2);
        rightLightSensor = new AnalogInput(3);
        belts = new Victor(2);
        leftLifter = new Victor(3);
        rightLifter = new Victor(4);
        leftLockingServo = new Servo(5);
        rightLockingServo = new Servo(6);
        launchingPiston = new DoubleSolenoid(4, 5);

        travelLocked = true;

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
        while(!leftLightAboveThreshold() && !rightLightAboveThreshold()){
            moveLifters(-0.15);
        }
        moveLifters(0);
        upper = getAngle();

        //Get lower angle
        while(getAngle() >= -45){
            moveLifters(-0.5);
        }
        moveLifters(0);
        while(!leftLightAboveThreshold() && !rightLightAboveThreshold()){
            moveLifters(0.15);
        }
        moveLifters(0);
        lower = getAngle();

        //Go in between of upper and lower angle
        while(getAngle() <= (upper - lower) / 2){
            moveLifters(0.15);
        }
        moveLifters(0);

        gyro.reset();
    }

    /**
     * Changes the state of the robot and moves the shooter accordingly
     * Used for transferring between states that depend on only the gyro.
     * Do NOT use this method for transferring to the Travel position as the use of the light sensor is required to
     * recalibrate the gyro (it drifts)
     * @param newState state to transfer to
     */
    private void changeState(ShooterState newState){
        if(travelLocked){ //Check if locked for travel
            gyro.reset();
            unlockFromTravel();
        }

        //If changing state from squat, change to travel state first to reset the gyro for accuracy
        //If changing state from load to lower state, change to travel state first to reset the gyro for accuracy
        if(state.getID() == ShooterState.SQUAT.getID() || state.getID() == ShooterState.LOAD.getID()) {
            travelState();
        }

        if(state.getID() < newState.getID()){ //Shooter needs to be raised
            //Use gyro to move shooter
            while(getAngle() < newState.getAngle()){
                moveLifters(0.25);
            }
            moveLifters(0);
        } else if(state.getID() > newState.getID()){ //Shooter needs to be lowered
            //Use gyro to move shooter
            while(getAngle() > newState.getAngle()){
                moveLifters(-0.25);
            }
            moveLifters(0);
        }

        state = newState;
    }

    /**
     * Increases the state of the robot
     * Pre: None
     * Post: The state of the shooter is increased
     */
    public void increaseState(){
        switch(state){
            case SQUAT: changeState(ShooterState.LOAD); break;
            case LOAD: travelState(); break;
            case TRAVEL: changeState(ShooterState.HIGHSHOT); break;
            case HIGHSHOT: break;
        }
    }

    /**
     * Decreases the state of the robot
     * Pre: None
     * Post: The state of the shooter is decreased
     */
    public void decreaseState(){
        switch(state){
            case SQUAT: break;
            case LOAD: changeState(ShooterState.SQUAT); break;
            case TRAVEL: changeState(ShooterState.LOAD); break;
            case HIGHSHOT: travelState(); break;
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
        boolean broken = false;

        //gyro position 0
        //When light sensor value peaks above threshold
        if(state.getID() < ShooterState.TRAVEL.getID()){ //Shooter needs to be raised
            //Raise shooter until one of the light sensors reaches the travel state
            while(!leftLightAboveThreshold() && !rightLightAboveThreshold()){
                moveLifters(0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveLifters(0);
            //If left shooter lifter is off, correct it
            while(!leftLightAboveThreshold()){
                moveLeftLifter(0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveLeftLifter(0);
            //If right shooter lifter is off, correct it
            while(!rightLightAboveThreshold()){
                moveRightLifter(0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveRightLifter(0);
        } else if(state.getID() > ShooterState.TRAVEL.getID()){ //Shooter needs to be lowered
            //Lower shooter until one of the light sensors reaches the travel state
            while(!leftLightAboveThreshold() && !rightLightAboveThreshold()){
                moveLifters(-0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveLifters(0);
            //If left shooter lifter is off, correct it
            while(!leftLightAboveThreshold()){
                moveLeftLifter(-0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveLeftLifter(0);
            //If right shooter lifter is off, correct it
            while(!rightLightAboveThreshold()){
                moveRightLifter(-0.15);

                //Check for over rotation
                //Check if a new angle needs to be recorded every 5th of a second
                if(Timer.getFPGATimestamp() - lastTimeReading >= 0.2){
                    lastAngleReading = getAngle();
                }
                //If angle is increasing and past rough travel state angle something is wrong
                if(Math.abs(getAngle()) - lastAngleReading > 0 && getAngle() > 0){
                    broken = true;
                    break;
                }
            }
            moveRightLifter(0);
        }

        if(!broken){
            lockForTravel();
            gyro.reset();
            state = ShooterState.TRAVEL;
        }
    }

    /**
     * Shoots the ball
     * Pre: There is a ball in the robot
     * Post: The ball is launched
     */
    public void shoot(){
        belts.set(-3);
        Timer.delay(2);
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
    public void moveLeftLifter(double value){
        leftLifter.set(value);
    }

    /**
     * Used for individually moving the right lifter motor for lining up with the left lifter motor.
     * @param value speed/direction of the motor
     */
    public void moveRightLifter(double value){
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
     * Gets the intensity of light read from the left light sensor
     * @return
     */
    public double getLeftLightSensorValue(){
        return leftLightSensor.getValue();
    }

    /**
     * Gets the intensity of light read from the right light sensor
     * @return
     */
    public double getRightLightSensorValue(){
        return rightLightSensor.getValue();
    }

    /**
     * Gets if the current left light sensor value is above the left threshold, meaning aligned with the LED
     * @return
     */
    public boolean leftLightAboveThreshold(){
        return getLeftLightSensorValue() >= leftLightSensorThreshold;
    }

    /**
     * Gets if the current right light sensor value is above the right threshold, meaning aligned with the LED
     * @return
     */
    public boolean rightLightAboveThreshold(){
        return getRightLightSensorValue() >= rightLightSensorThreshold;
    }
}

package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;

/**
 * The shooter of the robot
 */
public class Shooter {
    private AnalogGyro gyro;

    private Victor belts;
    private Victor lifters;

    private Servo lockingServo;
    private boolean travelLocked;

    private DoubleSolenoid launchingPiston;
    private boolean pistonReady;

    private enum Position {SQUAT, LOAD, TRAVEL, LOWSHOT, HIGHSHOT};
    private Position position;

    /**
     *
     */
    public Shooter(){
        gyro = new AnalogGyro(2);
        belts = new Victor(2);
        lifters = new Victor(3);
        lockingServo = new Servo(4);
        launchingPiston = new DoubleSolenoid(4, 5);

        travelLocked = true;
        pistonReady = false;
        position = Position.TRAVEL;
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

    }

    /**
     * Activates the LockingServo to slide the locking-cam out of place so the front of the robot is no longer stable
     * and safe to travel over rough terrain.
     * Pre: Shooter must be in the travel position (based on ShooterGyro) to allow the locking-cam to slide into position.
     * Post: If TravelLocked is true AND in TravelPosition, then activate the LockingServo to the
     * TravelUnlocked position, else do nothing.
     */
    public void unlockFromTravel(){

    }
}

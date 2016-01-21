package org.usfirst.frc.team4338.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	private CameraServer server;
	
	/**
	 * 
	 */
	public Robot(){
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture("cam0");
	}

	@Override
	public void robotInit() {
		super.robotInit();
	}
	
	@Override
	public void autonomousInit() {
		super.autonomousInit();
	}

	@Override
	public void autonomousPeriodic() {
		super.autonomousPeriodic();
	}

	@Override
	public void disabledInit() {
		super.disabledInit();
	}

	@Override
	public void disabledPeriodic() {
		super.disabledPeriodic();
	}
	
	@Override
	public void teleopInit() {
		super.teleopInit();
	}
	
	@Override
	public void teleopPeriodic() {
		super.teleopPeriodic();
	}
	
	@Override
	public void testInit() {
		super.testInit();
	}
	
	@Override
	public void testPeriodic() {
		super.testPeriodic();
	}
	
}

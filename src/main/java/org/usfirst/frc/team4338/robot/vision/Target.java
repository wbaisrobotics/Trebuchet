package org.usfirst.frc.team4338.robot.vision;

import com.ni.vision.NIVision.Range;

public abstract class Target {

	public abstract Range getHueRange();

	public abstract Range getSatRange();

	public abstract Range getValRange();

}

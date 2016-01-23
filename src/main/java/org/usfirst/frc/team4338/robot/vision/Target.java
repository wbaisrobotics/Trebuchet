package org.usfirst.frc.team4338.robot.vision;

import com.ni.vision.NIVision.Range;

public interface Target {

	public Range getHueRange();

	public Range getSatRange();

	public Range getValRange();

}

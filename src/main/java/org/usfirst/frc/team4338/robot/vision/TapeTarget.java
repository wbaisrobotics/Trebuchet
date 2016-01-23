package org.usfirst.frc.team4338.robot.vision;

import com.ni.vision.NIVision.Range;

public class TapeTarget implements Target {
	@Override
	public Range getHueRange() {
		return new Range(190, 260);
	}

	@Override
	public Range getSatRange() {
		return new Range(88, 255);
	}

	@Override
	public Range getValRange() {
		return new Range(134, 255);
	}

}

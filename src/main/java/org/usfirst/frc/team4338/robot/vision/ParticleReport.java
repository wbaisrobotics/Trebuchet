package org.usfirst.frc.team4338.robot.vision;

import java.util.Comparator;

public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport> {
	private double area;
	private BoundingRectangle boundingRectangle;
	private double imageAreaRatio;

	public ParticleReport() {
		this(new BoundingRectangle());
	}

	public ParticleReport(BoundingRectangle boundingRectangle) {
		this.boundingRectangle = boundingRectangle;
	}

	@Override
	public int compare(ParticleReport report1, ParticleReport report2) {
		return (int) (report1.area - report2.area);
	}

	@Override
	public int compareTo(ParticleReport report) {
		return (int) (report.area - area);
	}

	public double getArea() {
		return area;
	}

	public BoundingRectangle getBoundingRectangle() {
		return boundingRectangle;
	}

	public double getImageAreaRatio() {
		return imageAreaRatio;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public void setBoundingRectangle(BoundingRectangle boundingRectangle) {
		this.boundingRectangle = boundingRectangle;
	}

	public void setImageAreaRatio(double imageAreaRatio) {
		this.imageAreaRatio = imageAreaRatio;
	}

}

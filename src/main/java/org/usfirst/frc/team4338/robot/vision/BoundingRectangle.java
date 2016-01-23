package org.usfirst.frc.team4338.robot.vision;

public class BoundingRectangle {
	private double top, right, bottom, left;

	public BoundingRectangle() {
		this(0, 0, 0, 0);
	}

	public BoundingRectangle(double top, double right, double bottom, double left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public double getArea() {
		return getWidth() * getHeight();
	}

	public double getBottom() {
		return bottom;
	}

	public double getHeight() {
		return getBottom() - getTop();
	}

	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double getTop() {
		return top;
	}

	public double getWidth() {
		return getRight() - getLeft();
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public void setTop(double top) {
		this.top = top;
	}

}

package org.usfirst.frc.team4338.robot.vision;

import java.awt.Dimension;

import org.usfirst.frc.team4338.robot.ShooterState;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.GetImageSizeResult;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Point;

public class HUD {

	private Camera camera;

	public HUD(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return camera;
	}

	private Dimension getSize(Image frame) {
		GetImageSizeResult size = NIVision.imaqGetImageSize(frame);
		return new Dimension(size.width, size.height);
	}

	public void render(Image frame) {
		Dimension size = getSize(frame);
		renderVerticalLine(frame, size);
		renderHighshotLine(frame, size);
	}

	private void renderHighshotLine(Image frame, Dimension size) {
		if (!showShootingIndicator(ShooterState.HIGHSHOT))
			return;
		Point left = new Point(0, (int) size.getHeight() / 2);
		Point right = new Point((int) size.getWidth() - 1, (int) size.getHeight() / 2);
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, left, right, 0f);
	}

	private void renderVerticalLine(Image frame, Dimension size) {
		Point top = new Point((int) size.getWidth() / 2, 0);
		Point bottom = new Point((int) size.getWidth() / 2, (int) size.getHeight() - 1);
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, top, bottom, 0f);
	}

	private boolean showShootingIndicator(ShooterState state) {
		return camera.getRobot().getShooter().getState() == state;
	}

}

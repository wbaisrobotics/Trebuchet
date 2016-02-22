package org.usfirst.frc.team4338.robot.vision;

import java.awt.Dimension;

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
	
	public void render(Image frame) {
		Dimension size = getSize(frame);
		renderVerticalLine(frame, size);
	}
	
	private Dimension getSize(Image frame) {
		GetImageSizeResult size = NIVision.imaqGetImageSize(frame);
		return new Dimension(size.width, size.height);
	}
	
	private void renderVerticalLine(Image frame, Dimension size) {
		Point top = new Point((int) size.getWidth() / 2, 0);
		Point bottom = new Point((int) size.getWidth() / 2, (int) size.getHeight());
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, top, bottom, 0f);
	}
	
}

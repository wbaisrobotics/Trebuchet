package org.usfirst.frc.team4338.robot.vision;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.GetImageSizeResult;
import com.ni.vision.NIVision.IMAQdxCameraControlMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

public class Camera {
	public static final double DEFAULT_VIEW_ANGLE = 60d;

	private Image frame, binaryFrame;
	private int session;
	private double viewAngle;

	public Camera(double viewAngle) {
		this.viewAngle = viewAngle;
		initializeVision();
	}

	public double computeDistance(Image image, ParticleReport report, double targetWidth) {
		GetImageSizeResult size = NIVision.imaqGetImageSize(image);
		double normalizedWidth = 2 * report.getBoundingRectangle().getWidth() / size.width;
		return targetWidth / (12 * normalizedWidth * Math.tan(viewAngle * Math.PI / 360d));
	}

	private void createImages() {
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
	}

	public Image getBinaryFrame() {
		return binaryFrame;
	}

	public Image getImage() {
		NIVision.IMAQdxGrab(session, frame, 1);
		return frame;
	}

	private void initializeVision() {
		createImages();
		session = NIVision.IMAQdxOpenCamera("cam0", IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
	}

	public void setBinaryFrame(Image binaryFrame) {
		this.binaryFrame = binaryFrame;
	}
}

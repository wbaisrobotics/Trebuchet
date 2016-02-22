package org.usfirst.frc.team4338.robot.vision;

import org.usfirst.frc.team4338.robot.Robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.GetImageSizeResult;
import com.ni.vision.NIVision.IMAQdxCameraControlMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {
	public static final String DEFAULT_CAMERA_NAME = "cam0";
	public static final double DEFAULT_VIEW_ANGLE = 60d;

	private Robot robot;
	private Image frame, binaryFrame;
	private CameraServer server;
	private int session;
	private double viewAngle;
	private HUD hud;

	public Camera(Robot robot, double viewAngle) {
		this.robot = robot;
		this.viewAngle = viewAngle;
		server = CameraServer.getInstance();
		hud = new HUD(this);
		initializeVision();
	}

	public void captureImage() {
		NIVision.IMAQdxGrab(session, frame, 1);
		hud.render(frame);
		server.setImage(frame);
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

	public Image getFrame() {
		return frame;
	}

	private void initializeVision() {
		createImages();
		IMAQdxCameraControlMode mode = IMAQdxCameraControlMode.CameraControlModeController;
		session = NIVision.IMAQdxOpenCamera(DEFAULT_CAMERA_NAME, mode);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
	}

	public void setBinaryFrame(Image binaryFrame) {
		this.binaryFrame = binaryFrame;
	}
	
	public Robot getRobot() {
		return robot;
	}
}

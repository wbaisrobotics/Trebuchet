package org.usfirst.frc.team4338.robot.vision;

import java.util.ArrayList;
import java.util.List;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionThread implements Runnable {
	private static final double MIN_SCORE = 75d;

	private Camera camera;
	private List<ScoringResult> results;
	private Target target;

	public VisionThread(Camera camera, Target target) {
		this.camera = camera;
		this.target = target;
		results = new ArrayList<ScoringResult>();
	}

	@Override
	public void run() {
		NIVision.imaqColorThreshold(camera.getBinaryFrame(), camera.getFrame(), 255, ColorMode.HSV,
				target.getHueRange(), target.getSatRange(), target.getValRange());
		Image binaryFrame = camera.getBinaryFrame();
		CameraServer.getInstance().setImage(binaryFrame);
		int particleCount = NIVision.imaqCountParticles(camera.getBinaryFrame(), 1);
		for (int i = 0; i < particleCount; i++)
			results.add(new ScoringResult(new Particle(binaryFrame, i).createReport()));
		SmartDashboard.putBoolean("Target visible", targetVisible());
	}

	private boolean targetVisible() {
		for (ScoringResult result : results)
			if (result.getAspectScore() >= MIN_SCORE && result.getAreaScore() >= MIN_SCORE)
				return true;
		return false;
	}

}

package org.usfirst.frc.team4338.robot.vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionRobot extends SampleRobot {

	private static final double SCORE_MIN = 75d;

	private Camera camera;

	public VisionRobot() {
		camera = new Camera(Camera.DEFAULT_VIEW_ANGLE);
	}

	@Override
	public void autonomous() {
		while (isAutonomous() && isEnabled()) {
			SmartDashboard.putBoolean("Target visible", targetVisible());
			Timer.delay(.005);
		}
	}

	private boolean targetVisible() {
		Target target = new TapeTarget();
		NIVision.imaqColorThreshold(camera.getBinaryFrame(), camera.getImage(), 255, ColorMode.HSV,
				target.getHueRange(), target.getSatRange(), target.getValRange());
		CameraServer.getInstance().setImage(camera.getBinaryFrame());
		int numParticles = NIVision.imaqCountParticles(camera.getBinaryFrame(), 1);

		if (numParticles == 0)
			return false;

		List<ParticleReport> reports = new ArrayList<ParticleReport>();
		Image binaryFrame = camera.getBinaryFrame();
		for (int i = 0; i < numParticles; i++)
			reports.add(new Particle(binaryFrame, i).createReport());

		Collections.sort(reports);
		ScoringResult score = new ScoringResult(reports.get(0));

		return score.getAspectScore() >= SCORE_MIN && score.getAreaScore() >= SCORE_MIN;
	}
}

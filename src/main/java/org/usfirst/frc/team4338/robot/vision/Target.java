package org.usfirst.frc.team4338.robot.vision;

import java.util.ArrayList;
import java.util.List;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Range;

import edu.wpi.first.wpilibj.CameraServer;

public abstract class Target {
	private static final double MIN_SCORE = 75d;

	public abstract Range getHueRange();

	public abstract Range getSatRange();

	public abstract Range getValRange();

	public boolean isVisible(Camera camera) {
		NIVision.imaqColorThreshold(camera.getBinaryFrame(), camera.getFrame(), 255, ColorMode.HSV, getHueRange(),
				getSatRange(), getValRange());
		CameraServer.getInstance().setImage(camera.getBinaryFrame());
		int numParticles = NIVision.imaqCountParticles(camera.getBinaryFrame(), 1);

		List<ParticleReport> reports = new ArrayList<ParticleReport>();
		Image binaryFrame = camera.getBinaryFrame();
		for (int i = 0; i < numParticles; i++)
			reports.add(new Particle(binaryFrame, i).createReport());

		boolean visible = false;
		for (ParticleReport report : reports) {
			ScoringResult result = new ScoringResult(report);
			if (result.getAspectScore() >= MIN_SCORE && result.getAreaScore() >= MIN_SCORE)
				visible = true;
		}

		return visible;
	}

}

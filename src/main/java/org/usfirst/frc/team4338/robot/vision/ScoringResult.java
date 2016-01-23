package org.usfirst.frc.team4338.robot.vision;

public class ScoringResult {
	private ParticleReport report;

	public ScoringResult(ParticleReport report) {
		this.report = report;
	}

	public double getAreaScore() {
		BoundingRectangle rectangle = report.getBoundingRectangle();
		double particleRatio = report.getArea() / rectangle.getArea();

		// Tape 7" edge -> 49" bounding rectangle
		// Tape 2" wide -> 24" of rectangle covered
		return ratioToScore(particleRatio * 49 / 24);
	}

	public double getAspectScore() {
		BoundingRectangle rectangle = report.getBoundingRectangle();
		return ratioToScore(rectangle.getWidth() / rectangle.getHeight());
	}

	public ParticleReport getReport() {
		return report;
	}

	private double ratioToScore(double ratio) {
		double score = 100 * (1 - Math.abs(1 - ratio));
		return Math.max(0, Math.min(100, score));
	}
}

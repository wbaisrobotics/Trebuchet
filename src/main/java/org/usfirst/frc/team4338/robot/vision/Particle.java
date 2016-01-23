package org.usfirst.frc.team4338.robot.vision;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;

public class Particle {

	private Image image;
	private int index;

	public Particle(Image binaryFrame, int index) {
		image = binaryFrame;
		this.index = index;
	}

	public ParticleReport createReport() {
		BoundingRectangle rectangle = measureDimensions();
		ParticleReport report = new ParticleReport(rectangle);
		report.setImageAreaRatio(NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_AREA_BY_IMAGE_AREA));
		report.setArea(NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_AREA));
		return report;
	}

	public Image getBinaryFrame() {
		return image;
	}

	public int getIndex() {
		return index;
	}

	public BoundingRectangle measureDimensions() {
		double top, right, bottom, left;
		top = NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_BOUNDING_RECT_TOP);
		right = NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_BOUNDING_RECT_RIGHT);
		bottom = NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_BOUNDING_RECT_BOTTOM);
		left = NIVision.imaqMeasureParticle(image, index, 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
		return new BoundingRectangle(top, right, bottom, left);
	}

}

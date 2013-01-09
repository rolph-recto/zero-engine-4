package engine.util;

/*
 * Projection class
 * Represents the projection of a shape along an axis
 */
public class Projection {
	private double min, max;
	
	public Projection(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public void setMin(double min) {
		this.min = min;
	}
	
	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return this.min;
	}
	
	public double getMax() {
		return this.max;
	}

}

package engine.util;

//Abstract class shape
//Inherited by Rectangle and Polygon
abstract class Shape {
	protected double pos_x, pos_y, scale_x, scale_y, rotation;
	
	public void setPosition(double x, double y) {
		this.pos_x = x;
		this.pos_y = y;
	}
	
	public void setPosX(double x) {
		this.pos_x = x;
	}
	
	public void setPosY(double y) {
		this.pos_y = y;
	}
	
	public double getPosX() {
		return this.pos_x;
	}
	
	public double getPosY() {
		return this.pos_y;
	}
	
	//changes relative size of shape
	public void setScale(double scale_x, double scale_y) {
		if ((scale_x > 0.0) && (scale_y > 0.0)) {
			this.scale_x = scale_x;
			this.scale_y = scale_y;
		}
		this.reset();
	}
	
	//convenience function; scale equally for both x and y axes
	public void setScale(double scale) {
		this.setScale(scale, scale);
	}
	
	//since the x and y axes have separate scale values,
	//return the mean for getScale()
	//return the actual values for getScaleX() and getScaleY()
	public double getScale() {
		return (this.scale_x + this.scale_y)/2;
	}
	
	public double getScaleX() {
		return this.scale_x;
	}
	
	public double getScaleY() {
		return this.scale_y;
	}
	
	//changes angle of rotation of the shape
	public void setRotation(double rotation) {
		//set to coterminals if < 0 or > 360
		if (rotation < 0.0) {
			this.rotation = rotation - (Math.ceil(rotation/360.0)*360.0);
		}
		else if (rotation > 360.0) {
			this.rotation = rotation - (Math.ceil(rotation/360));
		}
		else {
			this.rotation = rotation;
		}
		this.reset();
	}
	
	//sets vertices to correct position according to scale, rotation and position
	protected abstract void reset();
}

package engine.util;

//Abstract class shape
//Inherited by Rectangle and Polygon
public abstract class Shape {
	protected double pos_x, pos_y, scale, rotation;
	
	protected Shape(double x, double y) {
		this.scale = 1.0;
		this.rotation = 0.0;
		this.pos_x = x;
		this.pos_y = y;
	}
	
	protected Shape() {
		this(0.0, 0.0);
	}
	
	public void setPosition(double x, double y) {
		this.pos_x = x;
		this.pos_y = y;
		this.reset();
	}
	
	public void setPosX(double x) {
		this.setPosition(x, this.pos_y);
	}
	
	public void setPosY(double y) {
		this.setPosition(this.pos_x, y);
	}
	
	public double getPosX() {
		return this.pos_x;
	}
	
	public double getPosY() {
		return this.pos_y;
	}
	
	//changes relative size of shape
	public void setScale(double scale) {
		if (scale > 0.0) {
			this.scale = scale;
		}
		this.reset();
	}
	
	public double getScale() {
		return this.scale;
	}
	
	//changes angle of rotation of the shape
	public void setRotation(double rotation) {
		//set to coterminals if < 0 or > 360
		if (rotation < 0.0) {
			this.rotation = rotation - (Math.ceil(rotation/360.0)*360.0);
		}
		else if (rotation >= 360.0) {
			this.rotation = rotation - (Math.ceil(rotation/360)*360.0);
		}
		else {
			this.rotation = rotation;
		}
		this.reset();
	}
	
	//adds to the current rotation
	public void addRotation(double dr) {
		this.setRotation(this.rotation + dr);
	}
	
	public double getRotation() {
		return this.rotation;
	}
	
	//sets vertices to correct position according to scale, rotation and position
	protected abstract void reset();
	
	protected abstract Projection project(Vector2D axis);
	
	protected abstract Vector2D[] getSeparatingAxes(Shape s);

    //is the shape colliding with another shape?
    //use Separating Axis Theorem to check collision
    public boolean collision(Shape s) {
    	//get separating axes for which to test collisions
    	Vector2D[] axes1 = this.getSeparatingAxes(s);
    	Vector2D[] axes2 = s.getSeparatingAxes(s);
    	Vector2D[] axes = new Vector2D[axes1.length + axes2.length];
    	System.arraycopy(axes1, 0, axes, 0, axes1.length);
    	System.arraycopy(axes2, 0, axes, axes1.length, axes2.length);
    	
    	//find projections of p1 and p2 for each separating axis
    	for (Vector2D axis : axes) {
    		Projection p1_proj = this.project(axis);
    		Projection p2_proj = s.project(axis);
    		
    		//lowest of the projection values
    		double min = (p1_proj.getMin() < p2_proj.getMin()) ? p1_proj.getMin() : p2_proj.getMin();
    		//highest of the projection values
    		double max = (p1_proj.getMax() > p2_proj.getMax()) ? p1_proj.getMax() : p2_proj.getMax();
    		//length of the two projections added together
    		double length = (p1_proj.getMax()-p1_proj.getMin()) + (p2_proj.getMax()-p2_proj.getMin());
    		
    		//check if projections are intersecting
    		//intersects if either polygon's max is greater than min of other polygon
    		//if one of the projections don't intersect, then the polygons don't intersect
    		if ( length <= max-min ) {
    			return false;
    		}
    	}
    	
    	//if we have gotten this far, that means all the projections intersect
    	//and the shapes intersect
    	return true;
    }
    
    public abstract Shape clone();
}

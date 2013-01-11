package engine.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * Abstract class shape
 * Inherited by Rectangle and Polygon
 */
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
	
	public void translate(double x, double y) {
		this.setPosition(this.pos_x+x, this.pos_y+y);
	}
	
	public double getPosX() {
		return this.pos_x;
	}
	
	public double getPosY() {
		return this.pos_y;
	}
	
	//changes relative size of shape
	public void setScale(double scale) {
		if (scale <= 0.0) {
			throw new IllegalArgumentException("Shape: Scale value must be positive");
		}
		this.scale = scale;
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
    	Vector2D[] axes2 = s.getSeparatingAxes(this);
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
    
    //get minimum translator vector
	//this assumes that shape projections collide for all axes
	//(i.e., the shapes collide)
    public Vector2D getMTV(Shape s) {
    	//get separating axes
    	Vector2D[] axes1 = this.getSeparatingAxes(s);
    	Vector2D[] axes2 = s.getSeparatingAxes(this);
    	Vector2D[] axes = new Vector2D[axes1.length + axes2.length];
    	System.arraycopy(axes1, 0, axes, 0, axes1.length);
    	System.arraycopy(axes2, 0, axes, axes1.length, axes2.length);
    	
    	double smallest_overlap = -1.0;
    	Vector2D smallest_overlap_axis = null;
    	
    	//find projections of p1 and p2 for each separating axis
    	for (Vector2D axis : axes) {
    		double overlap = 0.0;
    		Projection p1_proj = this.project(axis);
    		Projection p2_proj = s.project(axis);
    		
    		//calculate overlap
    		if (p1_proj.getMax() > p2_proj.getMin()) {
    			overlap = p1_proj.getMax() - p2_proj.getMin();
    		}
    		else if (p2_proj.getMax() > p1_proj.getMin()) {
    			overlap = p2_proj.getMax() - p1_proj.getMin();
    		}
    		
    		if (overlap < smallest_overlap || smallest_overlap == -1.0) {
    			smallest_overlap = overlap;
    			smallest_overlap_axis = axis;
    		}
    	}
    	
    	Vector2D mtv = new Vector2D(smallest_overlap_axis.getX(), smallest_overlap_axis.getY());
    	mtv.normalize();
    	mtv.setMagnitude(smallest_overlap);
    	return mtv;
    }
    
    public abstract Shape clone();
    
    //save the shape into a stream
    //subclasses should override this method if they have more info to write
    public void save(OutputStream out) throws IOException {
    	DataOutputStream data_out = new DataOutputStream(out);
    	
    	data_out.writeDouble(this.pos_x);
    	data_out.writeDouble(this.pos_y);
    	data_out.writeDouble(this.scale);
    	data_out.writeDouble(this.rotation);
    }
    
    //load shape data from a stream
    //subclasses should override this method if they have more info to read
    public void load(InputStream in) throws IOException {
    	DataInputStream data_out = new DataInputStream(in);
    	this.pos_x = data_out.readDouble();
    	this.pos_y = data_out.readDouble();
    	this.scale = data_out.readDouble();
    	this.rotation = data_out.readDouble();
    }
}

package engine.util;

public class Circle extends Shape {
	public Circle(double pos_x, double pos_y, double radius) {
		super(pos_x, pos_y);
		this.setScale(radius);
	}
	
	public Circle(double radius) {
		this(0.0, 0.0, radius);
	}
	
	//treat the scale as the radius, and vice versa!
	public void setRadius(double radius) {
		this.setScale(radius);
	}
	
	public double getRadius() {
		return this.getScale();
	}
	
	//don't need to do reset anything for a circle!
	protected void reset() {}
	
	//projection of a circle onto a vector
	protected Projection project(Vector2D axis) {
		//first, find dot product of center and axis
		//this gives the "coordinate" of the center
		//along the axis
		double center_proj = axis.dot(new Vector2D(this.pos_x, this.pos_y));
		
		//the projection of the circle is just its size radiating out of its center
		//i.e., its radius (the scale is used b/c in this Circle class radius == scale)
		return new Projection(center_proj-this.scale, center_proj+this.scale);
	}

	//return the separating axes of this shape with respect to another shape
	protected Vector2D[] getSeparatingAxes(Shape s) {
		//if the other shape is a circle, then
		//the only separating axis is the vector between the centers
		if (s instanceof Circle) {
			Vector2D axis = new Vector2D(s.getPosX()-this.pos_x, s.getPosY()-this.pos_y);
			axis.normalize();
			return new Vector2D[] { axis };
		}
		//if the other shape is a polygon, then
		//the separating axes are defined by the vertices to the center of this circle
		else if (s instanceof Polygon) {
			Polygon p = (Polygon) s;
			Vector2D[] vert_list = p.getVertices();
			Vector2D[] axes = new Vector2D[vert_list.length];
			
			for (int i=0; i<vert_list.length; i++) {
				axes[i] = new Vector2D(vert_list[i].getX()-this.pos_x, vert_list[i].getY()-this.pos_y);
				axes[i].normalize();
			}
			
			return axes;
		}
		
		//other shape unknown; cannot determine what are the separating axes
		return null;
	}

}

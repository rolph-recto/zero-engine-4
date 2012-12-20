package engine.util;

import java.util.*;

//Polygon class
//used for representing entity shapes
public class Polygon extends Shape {
	protected ArrayList<Point2D> base_vertices; //this is unmodified by translations, rotations, etc.
	protected ArrayList<Point2D> vertices;
	
	public Polygon(ArrayList<Point2D> v) {
		this.base_vertices = new ArrayList<Point2D>(v);
		this.vertices = new ArrayList<Point2D>(v);
		this.scale_x = 1.0;
		this.scale_y = 1.0;
		this.rotation = 0.0;
	}
	
	//sets vertices to correct position according to scale, rotation and position
	protected void reset() {
        //iterate through each vertex
		for (int i=0; i<this.vertices.size(); i++) {
			//set scale
			this.vertices.get(i).setX( this.base_vertices.get(i).getX()*this.scale_x );
			this.vertices.get(i).setY( this.base_vertices.get(i).getY()*this.scale_y );
			
			//set rotation
			double rx = (Math.cos(Math.toRadians(this.rotation)) * this.vertices.get(i).getX())
					- (Math.sin(Math.toRadians(this.rotation)) * this.vertices.get(i).getY());
			double ry = (Math.sin(Math.toRadians(this.rotation)) * this.vertices.get(i).getX()) +
					(Math.cos(Math.toRadians(this.rotation)) * this.vertices.get(i).getY());
			
			//set position
			this.vertices.get(i).setX(rx + this.pos_x);
			this.vertices.get(i).setY(ry + this.pos_y);
		}
	}
}

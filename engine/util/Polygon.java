package engine.util;

import java.util.*;

//Polygon class
//used for representing entity shapes
public class Polygon extends Shape {
	protected Point2D[] base_vertices; //this is unmodified by translations, rotations, etc.
	protected Point2D[] vertices;
	
	public Polygon(Point2D[] v) {
		this.base_vertices = new Point2D[v.length];
		this.vertices = new Point2D[v.length];
		for (int i=0; i< v.length; i++) {
			this.base_vertices[i] = new Point2D(v[i].getX(), v[i].getY());
			this.vertices[i] = new Point2D(v[i].getX(), v[i].getY());
		}
		this.scale_x = 1.0;
		this.scale_y = 1.0;
		this.rotation = 0.0;
	}
	
	//sets vertices to correct position according to scale, rotation and position
	protected void reset() {
        //iterate through each vertex
		for (int i=0; i<this.vertices.length; i++) {
			//set scale
			this.vertices[i].setX( this.base_vertices[i].getX()*this.scale_x );
			this.vertices[i].setY( this.base_vertices[i].getY()*this.scale_y );
			
			//set rotation
			double angle = Math.toRadians(this.rotation);
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);
			double rx = (cos * this.vertices[i].getX()) - (sin * this.vertices[i].getY());
			double ry = (sin * this.vertices[i].getX()) + (cos * this.vertices[i].getY());
			
			//set position
			this.vertices[i].setX(rx + this.pos_x);
			this.vertices[i].setY(ry + this.pos_y);
		}
	}
	
	public int getNumVertices() {
		return this.vertices.length;
	}
	
	public Point2D[] getVertices() {
		return this.vertices;
	}
}

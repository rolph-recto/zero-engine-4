package engine.util;

import java.util.*;

//Polygon class
//used for representing entity shapes
public class Polygon extends Shape {
	protected Vector2D[] base_vertices; //this is unmodified by translations, rotations, etc.
	protected Vector2D[] vertices;
	
	public Polygon(Vector2D[] v, double pos_x, double pos_y) {
		super(pos_x, pos_y);
		this.base_vertices = new Vector2D[v.length];
		this.vertices = new Vector2D[v.length];
		for (int i=0; i< v.length; i++) {
			this.base_vertices[i] = new Vector2D(v[i].getX(), v[i].getY());
			this.vertices[i] = new Vector2D(v[i].getX(), v[i].getY());
		}
		
		this.reset();
	}
	
	public Polygon(Vector2D[] v) {
		this(v, 0.0, 0.0);
	}
	
	//sets vertices to correct position according to scale, rotation and position
	protected void reset() {
        //iterate through each vertex
		for (int i=0; i<this.vertices.length; i++) {
			//set scale
			this.vertices[i].setX( this.base_vertices[i].getX()*this.scale );
			this.vertices[i].setY( this.base_vertices[i].getY()*this.scale );
			
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
	
	public Vector2D[] getVertices() {
		return this.vertices;
	}

	public Vector2D[] getBaseVertices() {
		return this.base_vertices;
	}
	
	//calculate direction vectors of the edges
	public Vector2D[] getEdgeVectors() {
		Vector2D[] edge_list = new Vector2D[this.base_vertices.length];
		
		//director of edge is the difference between the next vertex's coords and this vertex's coords
		for (int i=0; i<this.vertices.length; i++) {
			int next = (i < this.vertices.length-1) ? i+1 : 0;
			edge_list[i] = new Vector2D(this.vertices[next].getX() - this.vertices[i].getX(), this.vertices[next].getY() - this.vertices[i].getY());
		}
		
		return edge_list;
	}
	
	public Vector2D[] getNormalVectors() {
		Vector2D[] edge_list = this.getEdgeVectors();
		Vector2D[] normal_list = new Vector2D[edge_list.length];
		
		for (int i=0; i<edge_list.length; i++) {
			normal_list[i] = new Vector2D(-edge_list[i].getY(), edge_list[i].getX());
			normal_list[i].normalize();
		}
		
		return normal_list;
	}

	//return projection of the shape onto an axis
	//projection is the min and max of the
	//dotproduct of the axis vector and the shape's vertices
	public Projection project(Vector2D axis) {
		double min = this.vertices[0].dot(axis);
		double max = min;
		
		//iterate through each vertex to find the min and max
		//i starts at 1 not 0 b/c we already took into account the first index (0) above
		for (int i=1; i<this.vertices.length; i++) {
			double dot = this.vertices[i].dot(axis);
			
			if (dot < min) {
				min = dot;
			}
			if (dot > max) {
				max = dot;
			}
		}
		
		return new Projection(min, max);
	}
	
	protected Vector2D[] getSeparatingAxes(Shape s) {
		return this.getNormalVectors();
	}

}

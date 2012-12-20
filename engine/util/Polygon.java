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

	public Point2D[] getBaseVertices() {
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
	
	public Vector2D[] getNormalVectors(boolean normalized) {
		Vector2D[] edge_list = this.getEdgeVectors();
		Vector2D[] normal_list = new Vector2D[edge_list.length];
		
		for (int i=0; i<edge_list.length; i++) {
			normal_list[i] = new Vector2D(-edge_list[i].getY(), edge_list[i].getX());
			if (normalized) {
				normal_list[i].normalize();
			}
		}
		
		return normal_list;
	}
	
	public Vector2D[] getNormalVectors() {
		return this.getNormalVectors(false);
	}
}

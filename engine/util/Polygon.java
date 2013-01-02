package engine.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

//Polygon class
//used for representing entity shapes
public class Polygon extends Shape {
	protected Vector2D[] base_vertices; //this is unmodified by translations, rotations, etc.
	protected Vector2D[] vertices;
	
	public Polygon(Vector2D[] v, double pos_x, double pos_y) {
		//a polygon must have at least 3 sides
		//i.e., the polygon with the least number of sides is the triangle
		super(pos_x, pos_y);
		
		if (v.length < 3) {
			throw new IllegalArgumentException("Polygon: Vertex list must have at least 3 elements");
		}
		
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
	
	public Polygon(InputStream in) throws IOException {
		super();
		this.load(in);
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
			normal_list[i].normalize(); //MUST NORMALIZE AXES OR COLLISION DETECTION WILL FAIL!
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
	
	//Separating axes for a polygon are its normal vectors
	protected Vector2D[] getSeparatingAxes(Shape s) {
		return this.getNormalVectors();
	}
	
	public Shape clone() {
		return new Polygon(this.base_vertices, this.pos_x, this.pos_y);
	}

	public void save(OutputStream out) throws IOException {
		super.save(out);
		
		DataOutputStream data_out = new DataOutputStream(out);
		data_out.writeInt(this.base_vertices.length);
		for (Vector2D vertex : this.base_vertices) {
			data_out.writeDouble(vertex.getX());
			data_out.writeDouble(vertex.getY());
		}
	}
	
	public void load(InputStream in) throws IOException {
		super.load(in);
		DataInputStream data_in = new DataInputStream(in);
		int num_vertex = data_in.readInt();
		this.base_vertices = new Vector2D[num_vertex];
		this.vertices = new Vector2D[num_vertex];
		for (int i=0; i<num_vertex; i++) {
			double x = data_in.readDouble();
			double y = data_in.readDouble();
			this.base_vertices[i] = new Vector2D(x, y);
			this.vertices[i] = new Vector2D(x, y);
		}
		
		//make sure this.vertices has the right information
		this.reset();
	}
	
	
}

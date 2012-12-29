package engine.util;

import java.math.*;

//A 2D vector class, because apparently the Java standard library doesn't have one already
public class Vector2D {
	private double x, y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	//set magnitude by normalizing the vector
	//and multiplying by a scalar value
	public void setMagnitude(double magnitude) {
		this.normalize();
		this.mul(magnitude);
	}
	
	//magnitude of vector
	public double getMagnitude() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	//addition
	public static Vector2D add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.getX()+v2.getX(), v1.getY()+v2.getY());
	}
	
	public void add(Vector2D v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	
	//subtraction
	public static Vector2D sub(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.getX()-v2.getX(), v1.getY()-v2.getY());
	}
	
	public void sub(Vector2D v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	
	//multiplication
	public static Vector2D mul(Vector2D v1, double scalar) {
		return new Vector2D(v1.getX()*scalar, v1.getY()*scalar);
	}
	
	public void mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	
	//division
	public static Vector2D div(Vector2D v1, double scalar) {
		return new Vector2D(v1.getX()*scalar, v1.getY()*scalar);
	}
	
	public void div(double scalar) {
		this.x /= scalar;
		this.y /= scalar;
	}
	
	//normalize vector (turn into unit vector)
	public static Vector2D normalize(Vector2D v1) {
		return Vector2D.div(v1, v1.getMagnitude());
	}
	
	public void normalize() {
		this.div(this.getMagnitude());
	}
	
	//dot product
	public double dot(Vector2D v2) {
		return (this.x*v2.getX())+(this.y*v2.getY());
	}
	
	//returns a vector perpendicular to this one
	//can be thought of as a 2D cross product
	//there are two perpendicular vectors;
	//if usual is false, return (-y, x)
	//else, return (y, -x)
	public static Vector2D perpendicular(Vector2D v1) {
		return Vector2D.perpendicular(v1, false);
	}
	
	public static Vector2D perpendicular(Vector2D v1, boolean usual) {
		if (usual == false) {
			return new Vector2D(-v1.getY(), v1.getX());
		}
		else {
			return new Vector2D(v1.getY(), -v1.getX());
		}
	}
	
	public void perpendicular() {
		this.perpendicular(false);
	}
	
	//there are always two perpendicular vectors:
	//if usual is false, return (-y, x)
	//if usual is true, return (y, -x)
	public void perpendicular(boolean usual) {
		if (usual == false) {
			double c = this.x;
			this.x = -this.y;
			this.y = c;
		}
		else {
			double c = this.x;
			this.x = this.y;
			this.y = -c;
		}
	}
	
	//checks whether the vector is parallel with another vector
	//two vectors are parallel if their absolute unit components are the same
	public boolean isParallel(Vector2D v2) {
		Vector2D v1_norm = Vector2D.normalize(this);
		Vector2D v2_norm = Vector2D.normalize(v2);
		
		return ((Math.abs(v1_norm.getX()) == Math.abs(v2_norm.getX())) && (Math.abs(v1_norm.getY()) == Math.abs(v2_norm.getY())));
	}
}

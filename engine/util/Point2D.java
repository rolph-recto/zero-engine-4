package engine.util;

//2D point class
//Very similar to the Point2D class
public class Point2D {
	private double x, y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	//addition
	public static Point2D add(Point2D v1, Point2D v2) {
		return new Point2D(v1.getX()+v2.getX(), v1.getY()+v2.getY());
	}
	
	public void add(Point2D v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	
	//subtraction
	public static Point2D sub(Point2D v1, Point2D v2) {
		return new Point2D(v1.getX()-v2.getX(), v1.getY()-v2.getY());
	}
	
	public void sub(Point2D v2) {
		this.x += v2.getX();
		this.y += v2.getY();
	}
	
	//multiplication
	public static Point2D mul(Point2D v1, double scalar) {
		return new Point2D(v1.getX()*scalar, v1.getY()*scalar);
	}
	
	public void mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
	}
	
	//division
	public static Point2D div(Point2D v1, double scalar) {
		return new Point2D(v1.getX()*scalar, v1.getY()*scalar);
	}
	
	public void div(double scalar) {
		this.x /= scalar;
		this.y /= scalar;
	}
}

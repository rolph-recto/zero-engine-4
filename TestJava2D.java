import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.color.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import engine.util.*;

public class TestJava2D extends Canvas {
	private static Polygon p1 = new Polygon(new Point2D[] {new Point2D(-20.0, -20.0), 
			new Point2D(20.0, -20.0), new Point2D(20.0, 20.0), new Point2D(-20.0, 20.0)});
	private static Polygon p2 = new Polygon(new Point2D[] {new Point2D(-20.0, -20.0), 
			new Point2D(20.0, -20.0), new Point2D(20.0, 20.0), new Point2D(-20.0, 20.0)});
	
	private static BufferedImage img;
	private static TexturePaint texture_img;

	public TestJava2D() {
		super();

        JFrame frame = new JFrame("Java 2D Skeleton");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        try {
            this.img = ImageIO.read(this.getClass().getResource("texture.bmp"));
            this.texture_img = new TexturePaint(this.img, new Rectangle(-20, -20, 20, 20));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        p1.setPosition(100.0, 100.0);
        p2.setPosition(100.0, 120.0);
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			p1.addRotation(1.0);
			p2.setPosY(p2.getPosY()+0.05);
			this.repaint();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {}
		}
	}
	
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);
        
        int[] xvert = new int[p1.getNumVertices()];
        int[] yvert = new int[p1.getNumVertices()];
        Point2D[] vert_list = p1.getBaseVertices();
        
        AffineTransform transform = new AffineTransform();
        transform.translate(p1.getPosX(), p1.getPosY());
        transform.scale(p1.getScaleX(), p1.getScaleY());
        transform.rotate(Math.toRadians(p1.getRotation()));
        
        for (int i=0; i<vert_list.length; i++) {
        	xvert[i] = (int)vert_list[i].getX();
        	yvert[i] = (int)vert_list[i].getY();
        }
        
        g2d.setTransform(transform);
        if (this.collision()) {
        	g2d.setColor(Color.RED);
        }
        else g2d.setColor(Color.WHITE);
        g2d.drawPolygon(xvert, yvert, vert_list.length);
        
        int[] xvert2 = new int[p2.getNumVertices()];
        int[] yvert2 = new int[p2.getNumVertices()];
        Point2D[] vert_list2 = p2.getBaseVertices();
        
        transform.setToIdentity();
        transform.translate(p2.getPosX(), p2.getPosY());
        transform.scale(p2.getScaleX(), p2.getScaleY());
        transform.rotate(Math.toRadians(p2.getRotation()));
        
        for (int i=0; i<vert_list2.length; i++) {
        	xvert2[i] = (int)vert_list2[i].getX();
        	yvert2[i] = (int)vert_list2[i].getY();
        }
        
        g2d.setTransform(transform);
        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(xvert2, yvert2, vert_list2.length);
        //g2d.setPaint(this.texture_img);
        //g2d.fillPolygon(xvert, yvert, vert_list.length);
    }
    
    //are p1 and p2 colliding?
    //use Separating Axis Theorem to check collision
    public boolean collision() {
    	//get separating axes for which to test collisions
    	Vector2D[] axes1 = p1.getNormalVectors();
    	Vector2D[] axes2 = p2.getNormalVectors();
    	
    	//find projections of p1 and p2 for each separating axis
    	for (Vector2D axis : axes1) {
    		double[] p1_proj = p1.project(axis);
    		double[] p2_proj = p2.project(axis);
    		
    		System.out.println("PROJ1 :" + p1_proj[0] + " " + p1_proj[1] + " PROJ2 : " + p2_proj[0] + " " + p2_proj[1]);
    		//check if projections are intersecting
    		//intersects if either polygon's max is greater than min of other polygon
    		//if one of the projections don't intersect, then the polygons don't intersect
    		if (!(((p1_proj[1] > p2_proj[0]) && (p1_proj[1] < p2_proj[1])) || ((p2_proj[1] > p1_proj[0]) && (p2_proj[1] < p1_proj[1])))) {
    			return false;
    		}
    	}
    	
    	//now do axes from p2
    	for (Vector2D axis : axes2) {
    		double[] p1_proj = p1.project(axis);
    		double[] p2_proj = p2.project(axis);
    		
    		System.out.println("PROJ1 :" + p1_proj[0] + " " + p1_proj[1] + " PROJ2 : " + p2_proj[0] + " " + p2_proj[1]);
    		//check if projections are intersecting
    		//intersects if either polygon's max is greater than min of other polygon
    		//if one of the projections don't intersect, then the polygons don't intersect
    		if (!(((p1_proj[1] > p2_proj[0]) && (p1_proj[1] < p2_proj[1])) || ((p2_proj[1] > p1_proj[0]) && (p2_proj[1] < p1_proj[1])))) {
    			return false;
    		}
    	}
    	
    	//if we have gotten this far, that means all the projections intersect
    	//and the polygons intersect
    	return true;
    }

    public static void main(String[] args) {
    	TestJava2D t = new TestJava2D();
    	
    	t.mainLoop();
    }
}
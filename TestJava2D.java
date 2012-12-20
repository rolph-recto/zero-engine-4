import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.*;
import javax.swing.*;

import engine.util.*;

public class TestJava2D extends Canvas {
	private static Polygon p = new Polygon(new Point2D[] {new Point2D(-20.0, -20.0), new Point2D(20.0, -20.0),
			new Point2D(20.0, 20.0), new Point2D(-20.0, 20.0)});

	public TestJava2D() {
		super();

        JFrame frame = new JFrame("Java 2D Skeleton");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			p.setPosition(100.0, 50.0);
			p.addRotation(1.0);
			this.repaint();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {}
		}
	}
	
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        int[] xvert = new int[p.getNumVertices()];
        int[] yvert = new int[p.getNumVertices()];
        Point2D[] vert_list = p.getVertices();
        
        for (int i=0; i<vert_list.length; i++) {
        	xvert[i] = (int)vert_list[i].getX();
        	yvert[i] = (int)vert_list[i].getY();
        }
        
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xvert, yvert, vert_list.length);
    }

    public static void main(String[] args) {
    	TestJava2D t = new TestJava2D();
    	
    	t.mainLoop();
    }
}
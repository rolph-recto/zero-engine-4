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
	private static Polygon p = new Polygon(new Point2D[] {new Point2D(-20.0, -20.0), 
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);
        
        int[] xvert = new int[p.getNumVertices()];
        int[] yvert = new int[p.getNumVertices()];
        Point2D[] vert_list = p.getBaseVertices();
        
        AffineTransform transform = new AffineTransform();
        transform.translate(p.getPosX(), p.getPosY());
        transform.scale(p.getScaleX(), p.getScaleY());
        transform.rotate(Math.toRadians(p.getRotation()));
        
        for (int i=0; i<vert_list.length; i++) {
        	xvert[i] = (int)vert_list[i].getX();
        	yvert[i] = (int)vert_list[i].getY();
        }
        
        g2d.setTransform(transform);
        g2d.setPaint(this.texture_img);
        g2d.fillPolygon(xvert, yvert, vert_list.length);
    }

    public static void main(String[] args) {
    	TestJava2D t = new TestJava2D();
    	
    	t.mainLoop();
    }
}
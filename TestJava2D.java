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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import engine.*;
import engine.msgtype.EntityMoveMessage;
import engine.util.*;


class Player extends Entity {
	public Player() {}
}

class PlayerController extends Controller {
	private Player player;
	
	public PlayerController() {
		super();
	}
	
	public PlayerController(Level l, Entity player) {
		super(l, player);
		this.player = (Player)player;
	}
	
	/* Override addEntity method
	 * set added entity as this.player
	 */
	@Override
	public boolean addEntity(Entity e) {
		boolean added = super.addEntity(e);
		
		if (added) {
			this.player = (Player)e;
			return true;
		}
		else return false;
	}
	
	public void onMessage(Message msg) {
		EntityMoveMessage move_msg;
		switch (msg.getType()) {
		default:
			break;
		}
	}
}

class PlayerType implements EntityType {
	public static final PlayerType instance = new PlayerType();
	private static Model model;
	
	private PlayerType() {
		Vector2D[] verts = new Vector2D[] {
			new Vector2D(-10.0, -10.0), 
			new Vector2D(10.0, -10.0), 
			new Vector2D(10.0, 10.0), 
			new Vector2D(-10.0, 10.0) 
		};
		Polygon p = new Polygon(verts);
		PlayerType.model = new Model((Shape)p, null);
	}
	
	public String getName() {
		return "PlayerType";
	}
	
	public Class<? extends Controller> getControllerClass() {
		return PlayerController.class;
	}
	
	public Entity createEntity() {
		return new Player();
	}
	
	public Model getModel() {
		return PlayerType.model;
	}
	
	public boolean isHivemind() {
		return false;
	}
}

public class TestJava2D extends Canvas {
	private BufferedImage img;
	private TexturePaint texture_img;
	
	private Level level;
	private View view;

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
        
        this.level = new Level();
        this.view = new View(this.level, 640, 416);
        this.view.setPosition(0, 32);
        
        this.level.createEntity(PlayerType.instance, 20, 20);
        this.level.createEntity(PlayerType.instance, 100, 100);
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			this.view.setCamPosition(this.view.getCamX(), this.view.getCamY()+1);
			this.repaint();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {}
		}
	}
	
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);
        
    	this.view.drawObjects(g);
    	/*
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 640, 480);
        
        int[] xvert = new int[p1.getNumVertices()];
        int[] yvert = new int[p1.getNumVertices()];
        Vector2D[] vert_list = p1.getBaseVertices();
        
        AffineTransform transform = new AffineTransform();
        transform.translate(p1.getPosX(), p1.getPosY());
        transform.scale(p1.getScale(), p1.getScale());
        transform.rotate(Math.toRadians(p1.getRotation()));
        
        for (int i=0; i<vert_list.length; i++) {
        	xvert[i] = (int)vert_list[i].getX();
        	yvert[i] = (int)vert_list[i].getY();
        }
        
        g2d.setTransform(transform);
        if (p1.collision(c3)) g2d.setColor(Color.RED);
        else g2d.setColor(Color.WHITE);
        g2d.drawPolygon(xvert, yvert, vert_list.length);
        
        int[] xvert2 = new int[p2.getNumVertices()];
        int[] yvert2 = new int[p2.getNumVertices()];
        Vector2D[] vert_list2 = p2.getBaseVertices();
        
        transform.setToIdentity();
        transform.translate(p2.getPosX(), p2.getPosY());
        transform.scale(p2.getScale(), p2.getScale());
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
        
        transform.setToIdentity();
        g2d.setTransform(transform);
        g2d.setColor(Color.WHITE);
        g2d.drawOval((int)(c3.getPosX()-c3.getRadius()), (int)(c3.getPosY()-c3.getRadius()),
        		(int)(c3.getRadius()*2), (int)(c3.getRadius()*2));
        
        transform.setToIdentity();
        g2d.setTransform(transform);
        if (c4.collision(c3)) g2d.setColor(Color.CYAN);
        else g2d.setColor(Color.WHITE);
        g2d.drawOval((int)(c4.getPosX()-c4.getRadius()), (int)(c4.getPosY()-c4.getRadius()),
        		(int)(c4.getRadius()*2), (int)(c4.getRadius()*2));
        */
    }

    public static void main(String[] args) {
    	TestJava2D t = new TestJava2D();
    	
    	t.mainLoop();
    }
}
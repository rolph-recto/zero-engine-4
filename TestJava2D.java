import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.color.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
		case LEVEL_UPDATE:
			move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, 0.0, 0.0, 1.0);
			this.player.onMessage(move_msg);
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
		Sprite s = null;
		try {
            s=new Sprite("player",
                    TestJava2D.class.getClassLoader().getResourceAsStream("hero.bmp"),
                    32,32);
		}
		catch (IOException e) {}
		
		PlayerType.model = new Model((Shape)p, s);
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
	
	public boolean isHivemind() {
		return false;
	}
	
	public Model getModel() {
		return PlayerType.model;
	}
	
	public boolean isDynamic() {
		return true;
	}
	
	public double getFriction() {
		return 0.0;
	}
}

public class TestJava2D extends JFrame {
	private BufferedImage img;
	private TexturePaint texture_img;
	
	private Level level;
	private View view;
	private Tileset tileset;
	private TileData tile_data;
	private Map map;

	public TestJava2D() {
		super();
		
		/*
        JFrame frame = new JFrame("Java 2D Skeleton");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setSize(640, 480);
		this.setVisible(true);
        this.createBufferStrategy(2);
		
        try {
            this.img = ImageIO.read(this.getClass().getResource("texture.bmp"));
            this.texture_img = new TexturePaint(this.img, new Rectangle(-20, -20, 20, 20));
            this.tileset = new Tileset("Test",
            		TestJava2D.class.getClassLoader().getResourceAsStream("tileset.bmp"),
            		32, 32);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        this.tile_data = new TileData(this.tileset);
        this.tile_data.addTileTemplate("0", 0, null);
        this.tile_data.addTileTemplate("1", 1, null);
        this.tile_data.addTileTemplate("2", 2, null);
        this.tile_data.addTileTemplate("3", 3, null);
        this.tile_data.addTileTemplate("4", 4, null);
        this.tile_data.addTileTemplate("5", 5, null);
        this.tile_data.addTileTemplate("6", 6, null);
        this.tile_data.addTileTemplate("7", 7, null);
        this.tile_data.addTileTemplate("8", 8, null);
        this.tile_data.addTileTemplate("9", 9, null);
        
        this.map = new Map(this.tile_data, 50, 50);
        
        this.level = new Level(this.map, null);
        this.view = new View(this.level, 640, 416);
        this.view.setPosition(0, 32);
        
        this.level.createEntity(PlayerType.instance, 20, 20);
        this.level.createEntity(PlayerType.instance, 100, 100);
        
        this.mainLoop();
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			this.view.setCamPosition(this.view.getCamX()+1, this.view.getCamY()+1);
			this.level.update();
			this.repaint();
			try {
				Thread.sleep(30);
			}
			catch (InterruptedException e) {}
		}
	}
	
    public void paint(Graphics g) {
    	BufferStrategy bf = this.getBufferStrategy();
    	Graphics g2 = null;
     
    	try {
    		g2 = bf.getDrawGraphics();
        	Graphics2D g2d = (Graphics2D)g2;
        	
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, 640, 480);
            
        	this.view.draw(g2d);
    	}
    	finally {
    		g.dispose();
    	}
    
    	bf.show(); 
    }

    public static void main(String[] args) {
    	new TestJava2D();
    }
}
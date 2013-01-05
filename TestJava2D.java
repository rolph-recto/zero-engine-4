import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.xml.sax.SAXException;

import engine.Controller;
import engine.Entity;
import engine.EntityType;
import engine.Level;
import engine.Map;
import engine.Model;
import engine.MsgType;
import engine.ResourceDB;
import engine.TileData;
import engine.View;
import engine.msgtype.EntityMoveMessage;
import engine.msgtype.LevelMessage;
import engine.util.Message;
import engine.util.Polygon;
import engine.util.Shape;
import engine.util.Sprite;
import engine.util.Tileset;
import engine.util.Vector2D;

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
		switch (msg.getType()) {
		case LEVEL_UPDATE:
			LevelMessage level_msg = (LevelMessage)msg;
			boolean[] key_state = level_msg.getKeyState();
			EntityMoveMessage move_msg = null;
			
			if (key_state[KeyEvent.VK_UP]) {
				move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, 0.0, -1.0, 0.0);				
			}
			else if (key_state[KeyEvent.VK_DOWN]) {
				move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, 0.0, 1.0, 0.0);	
			}
			if (key_state[KeyEvent.VK_LEFT]) {
				move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, -1.0, 0.0, 0.0);				
			}
			else if (key_state[KeyEvent.VK_RIGHT]) {
				move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, 1.0, 0.0, 0.0);	
			}
			
			if (move_msg != null) {
				this.player.onMessage(move_msg);
			}
		default:
			break;
		}
	}
}

class PlayerType implements EntityType {
	public static final PlayerType instance = new PlayerType();

	private PlayerType() {}
	
	public String getName() {
		return "player";
	}
	
	public Controller createController() {
		return new PlayerController();
	}
	
	public Entity createEntity() {
		return new Player();
	}
	
	public boolean isHivemind() {
		return false;
	}
	
	public boolean isDynamic() {
		return true;
	}
	
	public double getFriction() {
		return 0.0;
	}
}

public class TestJava2D extends JFrame implements KeyListener {
	private BufferedImage img;
	private TexturePaint texture_img;
	
	private Level level;
	private View view;
	private Tileset tileset;
	private TileData tile_data;
	private Map map;
	private boolean[] key_state;

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
        this.addKeyListener(this);
        
        this.key_state = new boolean[525];
		
		ResourceDB db = new ResourceDB();
		try {
			db.loadResources("resource.xml");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		db.addEntityType("player", PlayerType.instance);
		
		/*
        try {
            this.tileset = new Tileset("tileset.tls");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */
        
        /*
        Polygon rect = new Polygon( new Vector2D[]{
        	new Vector2D(-16.0,-16.0),
        	new Vector2D(16.0,-16.0),
        	new Vector2D(16.0,16.0),
        	new Vector2D(-16.0,16.0)});
		
        this.tile_data = new TileData(this.tileset);
        this.tile_data.addTileTemplate("One", (short)0, rect);
        this.tile_data.addTileTemplate("Two", (short)1, rect);
        this.tile_data.addTileTemplate("Three", (short)2, rect);
        this.tile_data.addTileTemplate("Four", (short)3, rect);
        this.tile_data.addTileTemplate("Five", (short)4, rect);
        this.tile_data.addTileTemplate("Six", (short)5, rect);
        this.tile_data.addTileTemplate("Seven", (short)6, rect);
        this.tile_data.addTileTemplate("Eight", (short)7, rect);
        this.tile_data.addTileTemplate("Nine", (short)8, rect);
        this.tile_data.addTileTemplate("Ten", (short)9, rect);
        */
		
		/*
        try {
        	this.tile_data = new TileData("tiledata.txt");
        }
        catch (IOException e) {
        	System.out.println("ERROR IO");
        }
        */
        
        /*
        this.map = new Map(this.tile_data, 50, 50);
        this.map.fillLayer("base", (short)1);
        
        this.map.addLayer("overlay", 50, 50, -1);
        this.map.setPointAtLayer("overlay", 0, 0, (short)2);
        this.map.setPointAtLayer("overlay", 3, 5, (short)2);
        this.map.setPointAtLayer("overlay", 3, 5, (short)2);
        this.map.setPointAtLayer("overlay", 4, 5, (short)2);
        this.map.setPointAtLayer("overlay", 5, 5, (short)2);
        this.map.setPointAtLayer("overlay", 6, 5, (short)2);
        this.map.setPointAtLayer("overlay", 7, 5, (short)2);
        this.map.setPointAtLayer("overlay", 24, 24, (short)2);
        this.map.setPointAtLayer("overlay", 23, 23, (short)2);
        this.map.setPointAtLayer("overlay", 22, 22, (short)2);
        */
        try {
        	//this.map.save("map.txt");
        	//this.map.load("map.txt");
        	this.map = new Map(db.getTileData("tiledata"), "map.txt");
        }
        catch (IOException e) {}
        
        /*
		Vector2D[] verts = new Vector2D[] {
				new Vector2D(-10.0, -10.0), 
				new Vector2D(10.0, -10.0), 
				new Vector2D(10.0, 10.0), 
				new Vector2D(-10.0, 10.0) 
		};
		Polygon p = new Polygon(verts);
		Sprite s = null;
		try {
            s=new Sprite("hero.spr");
		}
		catch (IOException e) {}
		catch (ClassNotFoundException e) {}
		
		Model model_player = null;
		try {
			model_player = new Model("model.txt");
		}
		catch (IOException e) {}
		catch (ClassNotFoundException e) {}
		
		db.addModel("player", model_player);
		*/
			
        this.level = new Level(this.map, db);
        this.view = new View(this.level, 640, 416);
        this.view.setPosition(0, 32);
        
        this.level.createEntity("player", "player1", 200, 200);
        
        this.mainLoop();
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			this.view.focusTo("player1");
			this.level.update(this.key_state);
			this.repaint();
			try {
				Thread.sleep(0);
			}
			catch (InterruptedException e) {}
		}
	}
	
    public void paint(Graphics g) {
    	BufferStrategy bf = this.getBufferStrategy();
    	Graphics g2;
     
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
    
    //key event methods
    
    public void keyPressed(KeyEvent e) {
    	this.key_state[e.getKeyCode()] = true;
    }
    
    public void keyReleased(KeyEvent e) {
    	this.key_state[e.getKeyCode()] = false;
    }

	public void keyTyped(KeyEvent arg0) {}
}
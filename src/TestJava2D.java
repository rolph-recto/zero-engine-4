import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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
import engine.util.Circle;
import engine.util.Listener;
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
			
			//move the player
			boolean[] key_state = level_msg.getKeyState();
			double move_x, move_y;
			move_x = 0.0;
			move_y = 0.0;
			
			if (key_state[KeyEvent.VK_UP]) {
				move_y = -5.0;		
			}
			else if (key_state[KeyEvent.VK_DOWN]) {
				move_y = 5.0;
			}
			if (key_state[KeyEvent.VK_LEFT]) {
				move_x = -5.0;			
			}
			else if (key_state[KeyEvent.VK_RIGHT]) {
				move_x = 5.0;
			}

			//change player orientation
			double move_rot = 0.0;
			int mouse_x = level_msg.getMouseX();
			int mouse_y = level_msg.getMouseY();
			double comp_x = mouse_x - this.player.getPosX();
			double comp_y = mouse_y - this.player.getPosY();
			//find unit vector (||v||cos(t)i, ||v||sin(t)jd), solve for theta
			double magnitude = Math.sqrt(Math.pow(comp_x, 2) + Math.pow(comp_y, 2));
			double cos = comp_x/magnitude;
			double angle = Math.toDegrees(Math.acos(cos));
			if (comp_y > 0) {
				angle = 360.0 - angle;
			}
			angle = (angle >= 360.0) ? 0 : angle;
			
			move_rot = angle - this.player.getRotation();
			
			EntityMoveMessage move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player,
					move_x, move_y, move_rot);				
			
			this.player.onMessage(move_msg);
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
	
	public boolean isBullet() {
		return false;
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
	private Entity player;

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
            this.tileset = new Tileset(ImageIO.read(new File("tileset.png")), 32, 32);
            this.tileset.save("tileset.tls");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        
        Polygon rect = new Polygon( new Vector2D[]{
        	new Vector2D(-16.0,-16.0),
        	new Vector2D(16.0,-16.0),
        	new Vector2D(16.0,16.0),
        	new Vector2D(-16.0,16.0)});
        
        Polygon triangle = new Polygon(new Vector2D[]{
        	new Vector2D(16.0,-16.0),
        	new Vector2D(16.0,16.0),
        	new Vector2D(-16.0,16.0)});
		
        this.tile_data = new TileData(this.tileset);
        this.tile_data.addTileTemplate("One", (short)0, rect, true);
        this.tile_data.addTileTemplate("Two", (short)1, rect, true);
        this.tile_data.addTileTemplate("Three", (short)2, rect, true);
        this.tile_data.addTileTemplate("Four", (short)3, rect, false);
        this.tile_data.addTileTemplate("Five", (short)4, rect, false);
        this.tile_data.addTileTemplate("Six", (short)5, rect, false);
        this.tile_data.addTileTemplate("Seven", (short)6, rect, false);
        this.tile_data.addTileTemplate("Eight", (short)7, rect, false);
        this.tile_data.addTileTemplate("Nine", (short)8, rect, false);
        this.tile_data.addTileTemplate("Ten", (short)9, rect, false);
		
        try {
        	//this.tile_data = new TileData("tiledata.txt");
        	this.tile_data.save("tiledata.txt");
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
        	this.map.setPointAtLayer("base", 15, 15, (short)6);
        	this.map.setPointAtLayer("base", 15, 16, (short)5);
        	this.map.setPointAtLayer("base", 16, 14, (short)6);
        	this.map.setPointAtLayer("base", 16, 15, (short)5);
        	this.map.setPointAtLayer("base", 16, 16, (short)5);
        	this.map.setPointAtLayer("base", 17, 13, (short)6);
        	this.map.setPointAtLayer("base", 17, 14, (short)5);
        	this.map.setPointAtLayer("base", 17, 15, (short)5);
        	this.map.setPointAtLayer("base", 17, 16, (short)5);
        	this.map.setLayerOrder("overlay", 1);
        }
        catch (IOException e) {}
        
        /*
		Circle c = new Circle(16.0);
		Sprite s = null;
		try {
            s=new Sprite(ImageIO.read(new File("hero.png")), 32, 32);
		}
		catch (IOException e) {}
		
		Model model_player = new Model(c, s);
		try {
			model_player.save("model.txt");
		}
		catch (IOException e) {}
		
		db.addModel("player", model_player);
		*/
			
        this.level = new Level(this.map, db);
        this.view = new View(this.level, 640, 416);
        this.view.setPosition(0, 32);
        
        long id = this.level.createEntity("player", "player1", 200, 200);
        this.player = this.level.getEntityById(id);
        
        this.mainLoop();
	}
	
	public void mainLoop() {
		boolean done = false;
		while (done == false) {
			//get mouse coordinates
			int mouse_x = (int)MouseInfo.getPointerInfo().getLocation().getX();
			int mouse_y = (int)MouseInfo.getPointerInfo().getLocation().getY();
			//change mouse location from screen coords to level coords
			mouse_x = mouse_x - this.view.getPosX() + this.view.getCamX();
			mouse_y = mouse_y - this.view.getPosY() + this.view.getCamY();

			this.view.focusTo("player1");
			this.level.update(this.key_state, mouse_x, mouse_y);
			this.repaint();
			try {
				Thread.sleep(30);
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
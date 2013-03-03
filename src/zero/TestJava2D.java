package zero;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.xml.sax.SAXException;

import engine.Entity;
import engine.Level;
import engine.Map;
import engine.Model;
import engine.MsgType;
import engine.ResourceDB;
import engine.TileData;
import engine.View;
import engine.msgtype.EntityCollisionMessage;
import engine.msgtype.EntityMessage;
import engine.util.Circle;
import engine.util.Listener;
import engine.util.Message;
import engine.util.Polygon;
import engine.util.Sprite;
import engine.util.Tileset;
import engine.util.Vector2D;


class EntityListener implements Listener {
	public void onMessage(Message msg) {
		EntityCollisionMessage col_msg;
		EntityMessage entity_msg;
		switch(msg.getType()) {
		case ENTITY_COLLIDE_ENTITY:
			col_msg = (EntityCollisionMessage)msg;
			System.out.println(col_msg.getEntity()+" "+col_msg.getEntity2()+" "+col_msg.getEntity2().isDead());
			break;
		case ENTITY_COLLIDE_WALL:
			col_msg = (EntityCollisionMessage)msg;
			System.out.println(col_msg.getEntity()+" "+col_msg.getWallX()+" "+col_msg.getWallY());
			break;
		case ENTITY_DESTROY:
			entity_msg = (EntityMessage)msg;
			System.out.println("MISTAH KURTZ, HE DEAD "+entity_msg.getEntity());
			break;
		default:
			break;
		}
	}
}

public class TestJava2D extends JFrame implements KeyListener {
	private static final long serialVersionUID = -7164203224196166699L;
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
		
		/*
        try {
            this.tileset = new Tileset(ImageIO.read(new File("tileset.png")), 32, 32);
            this.tileset.save("tileset.tls");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */
        
        
        Polygon rect = new Polygon( new Vector2D[]{
        	new Vector2D(-18.0,-18.0),
        	new Vector2D(18.0,-18.0),
        	new Vector2D(18.0,18.0),
        	new Vector2D(-18.0,18.0)});
        
        Polygon triangle = new Polygon(new Vector2D[]{
        	new Vector2D(16.0,-16.0),
        	new Vector2D(16.0,16.0),
        	new Vector2D(-16.0,16.0)});
		
        this.tile_data = new TileData(db.getTileset("tileset"));
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
        	this.map = new Map(this.tile_data, "map.txt");
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
        
		Circle c = new Circle(1.0);
		Sprite s = null;
		try {
            s=new Sprite(ImageIO.read(new File("bullet.png")), 2, 2);
		}
		catch (IOException e) {}
		
		Model model_bullet = new Model(c, s);
		try {
			model_bullet.save("bullet.txt");
		}
		catch (IOException e) {}
		
		db.addModel("bullet", model_bullet);
		
		db.addEntityType("heavy", HeavyType.instance);
		db.addEntityType("ranger", RangerType.instance);
		db.addEntityType("bullet", BulletType.instance);
		
        this.level = new Level(this.map, db);
        this.view = new View(this.level, 640, 416);
        this.view.setPosition(0, 32);
        
        long id = this.level.createEntity("ranger", "player1", 500, 200);
        long id2 = this.level.createEntity("ranger", "player2", 700, 200);
        this.player = this.level.getEntityById(id);
        Player player2 = (Player)this.level.getEntityById(id2);
        player2.setEnabled(false);
        this.player.addSubscriber(new EntityListener(), MsgType.ENTITY_COLLIDE_ENTITY);
        this.player.addSubscriber(new EntityListener(), MsgType.ENTITY_COLLIDE_WALL);
        player2.addSubscriber(new EntityListener(), MsgType.ENTITY_COLLIDE_ENTITY);
        player2.addSubscriber(new EntityListener(), MsgType.ENTITY_COLLIDE_WALL);
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
			int off_x = (int)Math.floor((mouse_x-this.player.getPosX())*0.25);
			off_x = (off_x >= -275) ? off_x : -275;
			off_x = (off_x <= 275) ? off_x : 275;
			int off_y = (int)Math.floor((mouse_y-this.player.getPosY())*0.25);
			off_y = (off_y >= -150) ? off_y : -150;
			off_y = (off_y <= 150) ? off_y : 150;

			this.view.focusTo("player1", off_x, off_y);
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
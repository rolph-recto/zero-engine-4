package engine;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;

import engine.util.Sprite;
import engine.util.Tileset;


/*
 * Resource Database class
 * Contains all resources used in the game
 */
public final class ResourceDB {
	private final HashMap<String, BufferedImage> image_db;
	private final HashMap<String, Object> sound_db; //Specify sound type later
	private final HashMap<String, Sprite> sprite_db;
	private final HashMap<String, Tileset> tileset_db;
	private final HashMap<String, TileData> tiledata_db;
	private final HashMap<String, Model> model_db;
	private final HashMap<String, EntityType> entity_type_db;
	
	public ResourceDB() {
		this.image_db = new HashMap<String, BufferedImage> ();
		this.sound_db = new HashMap<String, Object> (); //Specify sound type later
		this.sprite_db = new HashMap<String, Sprite> ();
		this.tileset_db = new HashMap<String, Tileset> ();
		this.tiledata_db = new HashMap<String, TileData> ();
		this.model_db = new HashMap<String, Model> ();
		this.entity_type_db = new HashMap<String, EntityType> ();
	}
	
	public void addImage(String name, BufferedImage img) {
		this.image_db.put(name, img);
	}
	
	public BufferedImage getImage(String name) {
		BufferedImage img = this.image_db.get(name);
		if (img == null) {
			throw new IllegalArgumentException("ResourceDB: No image with name '"+name+"' exists");
		}
		return this.image_db.get(name);
	}
	
	//Specify sound type later
	public void addSound(String name, Object type) {
		this.sound_db.put(name, type);
	}
	
	//Specify sound type later
	public Object getSound(String name) {
		Object sound = this.sound_db.get(name);
		if (sound == null) {
			throw new IllegalArgumentException("ResourceDB: No sound with name '"+name+"' exists");
		}
		return this.sound_db.get(name);
	}
	
	public void addSprite(String name, Sprite spr) {
		this.sprite_db.put(name, spr);
	}
	
	public Sprite getSprite(String name) {
		Sprite spr = this.sprite_db.get(name);
		if (spr == null) {
			throw new IllegalArgumentException("ResourceDB: No sprite with name '"+name+"' exists");
		}
		return this.sprite_db.get(name);
	}
	
	public void addTileset(String name, Tileset tile) {
		this.tileset_db.put(name, tile);
	}
	
	public Tileset getTileset(String name) {
		Tileset tile = this.tileset_db.get(name);
		if (tile == null) {
			throw new IllegalArgumentException("ResourceDB: No tileset with name '"+name+"' exists");
		}
		return this.tileset_db.get(name);
	}
	
	public void addTileData(String name, TileData tile) {
		this.tiledata_db.put(name, tile);
	}
	
	public TileData getTileData(String name) {
		TileData tile = this.tiledata_db.get(name);
		if (tile == null) {
			throw new IllegalArgumentException("ResourceDB: No tile data with name '"+name+"' exists");
		}
		return this.tiledata_db.get(name);
	}

	public void addModel(String name, Model model) {
		this.model_db.put(name, model);
	}
	
	public Model getModel(String name) {
		Model model = this.model_db.get(name);
		if (model == null) {
			throw new IllegalArgumentException("ResourceDB: No model with name '"+name+"' exists");
		}
		return this.model_db.get(name);
	}
	
	public void addEntityType(String name, EntityType type) {
		this.entity_type_db.put(name, type);
	}
	
	public EntityType getEntityType(String name) {
		EntityType type = this.entity_type_db.get(name);
		if (type == null) {
			throw new IllegalArgumentException("ResourceDB: No entity type with name '"+name+"' exists");
		}
		return this.entity_type_db.get(name);
	}
	
	public void clearDB() {
		this.image_db.clear();
		this.sound_db.clear();
		this.sprite_db.clear();
		this.tileset_db.clear();
		this.tiledata_db.clear();
		this.model_db.clear();
		this.entity_type_db.clear();
	}
	
	//load resources from an XML file
	public void loadResources(String file) throws SAXException, IOException, ClassNotFoundException {
		XMLHandler handler = new XMLHandler();
		SAXParser parser = new SAXParser();
		
		//parse the XML file
		parser.setContentHandler(handler);
		parser.parse(file);
		
		//now load the resources from the handler
		this.clearDB(); //clear the DB first
		
		//load images
		HashMap<String, Path> images = handler.getImage();
        for (Entry<String, Path> entry : images.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            BufferedImage img = ImageIO.read(new FileInputStream(path.toString()));
            this.addImage(name, img);
        }
		//load sounds
		HashMap<String, Path> sounds = handler.getSound();
        for (Entry<String, Path> entry : sounds.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            //no loading method yet, since the sound resource isn't implemented yet
        }
		//load sprites
		HashMap<String, Path> sprites = handler.getSprite();
        for (Entry<String, Path> entry : sprites.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            Sprite s = new Sprite(path.toString());
            this.addSprite(name, s);
        }
		//load tilesets
		HashMap<String, Path> tilesets = handler.getTileset();
        for (Entry<String, Path> entry : tilesets.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            Tileset tile = new Tileset(path.toString());
            this.addTileset(name, tile);
        }
		//load tile datas
		HashMap<String, Path> tiledatas = handler.getTileData();
        for (Entry<String, Path> entry : tiledatas.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            TileData tile = new TileData(path.toString());
            this.addTileData(name, tile);
        }
		//load model
		HashMap<String, Path> models = handler.getModel();
        for (Entry<String, Path> entry : models.entrySet()) {
            String name = entry.getKey();
            Path path = entry.getValue();
            
            Model m = new Model(path.toString());
            this.addModel(name, m);
        }
	}

	private class XMLHandler extends DefaultHandler {
		//these are the resources to load
		private HashMap<String, Path> image;
		private HashMap<String, Path> sound;
		private HashMap<String, Path> sprite;
		private HashMap<String, Path> tileset;
		private HashMap<String, Path> tiledata;
		private HashMap<String, Path> model;
		
		public XMLHandler() {
			this.image = new HashMap<String, Path> ();
			this.sound = new HashMap<String, Path> ();
			this.sprite = new HashMap<String, Path> ();
			this.tileset = new HashMap<String, Path> ();
			this.tiledata = new HashMap<String, Path> ();
			this.model = new HashMap<String, Path> ();
		}
		
		public void startElement(String uri, String local_name, String q_name, Attributes atts) {
			FileSystem fs = FileSystems.getDefault();
			
			//load the resource from a file and put it in a hashmap
			//image resource
			if (local_name.equalsIgnoreCase("image")) {
				Path p = fs.getPath("resource", "image", atts.getValue("file"));
				String name = atts.getValue("name");
				this.image.put(name, p);
			}
			//sound resource
			else if (local_name.equalsIgnoreCase("sound")) {
				Path p = fs.getPath("resource", "sound", atts.getValue("file"));
				String name = atts.getValue("name");
				this.sound.put(name, p);
			}
			//sprite resource
			else if (local_name.equalsIgnoreCase("sprite")) {
				Path p = fs.getPath("resource", "sprite", atts.getValue("file"));
				String name = atts.getValue("name");
				this.sprite.put(name, p);
			}
			//tileset resource
			else if (local_name.equalsIgnoreCase("tileset")) {
				Path p = fs.getPath("resource", "tileset", atts.getValue("file"));
				String name = atts.getValue("name");
				this.tileset.put(name, p);
			}
			//tile data resource
			else if (local_name.equalsIgnoreCase("tiledata")) {
				Path p = fs.getPath("resource", "tiledata", atts.getValue("file"));
				String name = atts.getValue("name");
				this.tiledata.put(name, p);
			}
			//model resource
			else if (local_name.equalsIgnoreCase("model")) {
				Path p = fs.getPath("resource", "model", atts.getValue("file"));
				String name = atts.getValue("name");
				this.model.put(name, p);
			}
		}

		public HashMap<String, Path> getImage() {
			return this.image;
		}

		public HashMap<String, Path> getSound() {
			return this.sound;
		}

		public HashMap<String, Path> getSprite() {
			return this.sprite;
		}

		public HashMap<String, Path> getTileset() {
			return this.tileset;
		}

		public HashMap<String, Path> getTileData() {
			return this.tiledata;
		}

		public HashMap<String, Path> getModel() {
			return this.model;
		}
		
	}
}

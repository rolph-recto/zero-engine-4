package engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import engine.util.Polygon;
import engine.util.Shape;
import engine.util.Tileset;

/*
 * TileTemplate class
 * Contains info about one type of tile
 */
class TileTemplate {
	protected String name;
	protected short index; //index of the image used in Tileset
	protected Polygon shape;
	
	public TileTemplate(String name, short index, Polygon shape) {
		this.name = name;
		this.index = index;
		this.shape = shape;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getIndex() {
		return this.index;
	}

	public void setIndex(short index) {
		this.index = index;
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public void setShape(Polygon s) {
		this.shape = s;
	}
}

/*
 * TileData class
 * Contains list of tile templates
 * and a tileset (i.e., the graphical representation of the tiles)
 */
public class TileData {
	protected TileTemplate[] tile_list;
	protected Tileset tileset;
	
	public TileData(Tileset t) {
		this.setTileset(t);
	}
	
	public TileData(Tileset t, InputStream input) throws IOException {
		this.setTileset(t);
		this.loadTemplates(input);
	}
	
	public TileData(Tileset t, String file) throws IOException {
		this.setTileset(t);
		this.loadTemplates(file);
	}
	
	public TileData(InputStream input) throws IOException {
		this.load(input);
	}
	
	public TileData(String file) throws IOException {
		this.load(file);
	}
	
	public Tileset getTileset() { 
		return this.tileset;
	}
	
	//this erases all existing tile templates!
	public void setTileset(Tileset t) {
		this.tileset = t;
		this.tile_list = new TileTemplate[t.getNumTiles()];
	}
	
	public int getNumTemplates() { 
		return this.tile_list.length;
	}
	
	public int getTileWidth() {
		return this.tileset.getTileWidth();
	}
	
	public int getTileHeight() {
		return this.tileset.getTileHeight();
	}
	
	public TileTemplate getTileTemplate(int index) {
		if (index < 0 || index >= this.tile_list.length) {
			throw new IllegalArgumentException("TileData: Invalid index for tile template");
		}
		
		return this.tile_list[index];
	}
	
	public void addTileTemplate(String name, short index, Polygon shape) {
		if (index < 0 || index >= this.tile_list.length) {
			throw new IllegalArgumentException("TileData: Invalid index for tile template");
		}
		
		this.tile_list[index] = new TileTemplate(name, index, shape);
	}
	
	public void setTileTemplates(TileTemplate[] tiles) {
		if (tiles.length != this.tile_list.length) {
			throw new IllegalArgumentException("TileData: Number of tile templates do not match number of tiles in tileset");
		}
		this.tile_list = tiles;
	}

	//write tile templates to stream
	public void saveTemplates(OutputStream out) throws IOException {
		DataOutputStream data_out = new DataOutputStream(out);
		data_out.writeInt(this.tile_list.length);
		for (int i=0; i<this.tile_list.length; i++) {
			TileTemplate tile = this.tile_list[i];
			data_out.writeInt(tile.name.length());
			data_out.writeChars(tile.name);
			tile.getShape().save(data_out);
		}
	}

	//write tileset and tile templates to a stream
	public void save(OutputStream out) throws IOException {
		this.saveTemplates(out);
		this.tileset.save(out);
	}
	
	public void saveTemplates(String file) throws IOException {
		FileOutputStream in = new FileOutputStream(file);
		try {
			this.saveTemplates(in);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			in.close();
		}
	}
	
	public void save(String file) throws IOException {
		FileOutputStream in = new FileOutputStream(file);
		try {
			this.save(in);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			in.close();
		}
	}
	
	//load tile templates only
	public void loadTemplates(InputStream in) throws IOException {
		DataInputStream data_in = new DataInputStream(in);
		
		int num_tiles = data_in.readInt();
		this.tile_list = new TileTemplate[num_tiles];
		for (short i=0; i<num_tiles; i++) {
			int name_len = data_in.readInt();
			char[] name = new char[name_len];
			String name_str = "";
			for (int j=0; j<name_len; j++) {
				name_str += data_in.readChar();
			}
			
			Polygon shape = new Polygon(data_in);
			
			this.tile_list[i] = new TileTemplate(name_str, i, shape);
		}
	}
	
	//read tileset and tile templates from a stream
	public void load(InputStream in) throws IOException {
		this.loadTemplates(in);
		this.tileset = new Tileset(in);
	}
	
	public void loadTemplates(String file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			this.loadTemplates(in);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			in.close();
		}
	}
	
	public void load(String file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			this.load(in);
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			in.close();
		}
	}
}

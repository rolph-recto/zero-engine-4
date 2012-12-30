package engine;

import engine.util.Shape;
import engine.util.Tileset;

/*
 * TileTemplate class
 * Contains info about one type of tile
 */
class TileTemplate {
	protected String name;
	protected int index; //index of the image used in Tileset
	protected Shape shape;
	
	public TileTemplate(String name, int index, Shape shape) {
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

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public void setShape(Shape s) {
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
	
	public TileTemplate getTileTemplate(int index) {
		if (index < 0 || index >= this.tile_list.length) {
			throw new IllegalArgumentException("TileData: Invalid index for tile template");
		}
		
		return this.tile_list[index];
	}
	
	public void addTileTemplate(String name, int index, Shape shape) {
		if (index < 0 || index >= this.tile_list.length) {
			throw new IllegalArgumentException("TileData: Invalid index for tile template");
		}
		
		this.tile_list[index] = new TileTemplate(name, index, shape);
	}
}

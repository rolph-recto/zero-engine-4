package engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/*
 * MapLayer class
 * Represents one layer of a map
 */
final class MapLayer {
	private String name;
	private int order; //determines whether the layer will be drawn to the front or to the back
	//a note about order: the object layer is assumed to have an order value of 0,
	//so any map layers behind it should have a negative order value
	//and any layers in front of it should have a positive order value
	private short[][] data; //2D array of TileTemplate indices
	
	public MapLayer(String name, int width, int height, int order) {
		this.name = name;
		this.setDimensions(width, height);
		this.order = order;
	}
	
	public MapLayer(String name, short[][] data, int order) {
		this.name = name;
		this.data = data;
		this.order = order;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getWidth() {
		return this.data.length;
	}
	
	public int getHeight() {
		return this.data[0].length;
	}
	
	//set the dimensions of the layer
	//warning: THIS DOES NOT PRESERVE EXISTING DATA!
	public void setDimensions(int width, int height, short fill) {
		this.data = new short[width][height];
		this.fillData(fill);
	}
	
	public void setDimensions(int width, int height) {
		this.setDimensions(width, height, (short)0);
	}
	
	//return the index at a specific point
	public short getPointData(int x, int y) {
		if (x < 0 || x > this.data.length || y < 0 || y > this.data[0].length) {
			throw new IllegalArgumentException("MapLayer: Invalid map layer coordinate");
		}
		
		return this.data[x][y];
	}
	
	public short[][] getData() {
		return this.data;
	}
	
	//set the index of one coordinate point
	public void setPointData(int x, int y, short index) {
		if (x < 0 || x > this.data.length || y < 0 || y > this.data[0].length) {
			throw new IllegalArgumentException("MapLayer: Invalid map layer coordinate");
		}
		
		this.data[x][y] = index;
	}
	
	//set the entire data array
	public void setData(short[][] map) {
		this.data = map;
	}
	
	//set all indices of the layer to a certain index
	public void fillData(short index) {
		for (int x=0; x < this.data.length; x++) {
			for (int y=0; y < this.data[x].length; y++) {
				this.data[x][y] = index;
			}
		}
	}
}

/*
 * Map class
 * Contains layout of tiles in a level
 */
public class Map {
	//monotonic increasing list of layers (ordered by the layers' order value)
	//the base layer is automatically created at the constructor
	protected ArrayList<MapLayer> layers;
	//tile_data deliberately does not have a modifier because
	//that would allow the tile data to change while the map data stays the same,
	//which would break the map data
	protected TileData tile_data;
	
	protected Map(TileData t) {
		this.tile_data = t;
		this.layers = new ArrayList<MapLayer>();
	}
	
	public Map(TileData t, int width, int height) {
		this(t);
		//the base layer has an order value of -2 not -1 so that
		//an extra overlay layer can be put above it and below the object layer
		this.addLayer("base", width, height, -2);
	}
	
	public Map(TileData t, short[][] data) {
		this(t);
		//the base layer has an order value of -2 not -1 so that
		//an extra overlay layer can be put above it and below the object layer
		this.addLayer("base", data, -2);
	}
	
	public Map(TileData t, InputStream in) throws IOException {
		this(t);
		this.load(in);
	}
	
	public Map(TileData t, String file) throws IOException {
		this(t);
		this.load(file);
	}
	
	public TileData getTileData() {
		return this.tile_data;
	}
	
	//check if the index is a valid tile template index
	public boolean isValidIndex(int index) {
		return (index >= 0 && index < this.tile_data.getNumTemplates());
	}
	
	//check if a 2D array contains all valid tile template indices
	public boolean isValidMap(short[][] data) {
		for (int x=0; x < data.length; x++) {
			for (int y=0; y < data[x].length; y++) {
				int index = data[x][y];
				if (this.isValidIndex(index)) {
					return false;
				}
			}
		}
		
		//if the code reaches here, then all the indices are valid
		return true;
	}
	
	protected MapLayer getLayerByName(String name) {
		for (int i=0; i < this.layers.size(); i++) {
			if (this.layers.get(i).getName().equals(name)) {
				return this.layers.get(i);
			}
		}
		//if we have reached this far, then no layer with that name exists
		return null;
	}
	
	public boolean layerExists(String name) {
		for (int i=0; i < this.layers.size(); i++) {
			if (this.layers.get(i).getName().equals(name)) {
				return true;
			}
		}
		//if we have reached this far, then no layer with that name exists
		return false;
	}
	
	public MapLayer getBaseLayer() {
		return this.getLayerByName("base");
	}
	
	public ArrayList<MapLayer> getLayerList() {
		return this.layers;
	}
	
	
	//internal method; inserts a layer in the list according to its order value
	//the lower the order value, the higher a layer is on the list (with 0 being the highest)
	protected void insertLayer(MapLayer layer) {
		boolean inserted = false;
		int i = 0;
		while (inserted == false && i<this.layers.size()) {
			if (layer.getOrder() < this.layers.get(i).getOrder()) {
				this.layers.add(i, layer);
				inserted = true;
			}
			i++;
		}
		//if the layer has not been inserted, then it is
		//in front of all the other layers
		if (inserted == false) {
			this.layers.add(layer);
		}
	}

	//add a new layer to the map
	public void addLayer(String name, int width, int height, short fill, int order) {
		//check if a layer with the same name already exists
		if (this.getLayerByName(name) != null) {
			throw new IllegalArgumentException("Map: A layer with that name already exists");
		}
		if (order == 0) {
			throw new IllegalArgumentException("Map: Order value of 0 is reserved for Entities");
		}
		
		MapLayer layer = new MapLayer(name, width, height, order);
		layer.fillData(fill);
		this.insertLayer(layer);
	}
	
	public void addLayer(String name, int width, int height, int order) {
		this.addLayer(name, width, height, (short)0, order);
	}
	
	public void addLayer(String name, short[][] data, int order) {
		//check if a layer with the same name already exists
		if (this.getLayerByName(name) != null) {
			throw new IllegalArgumentException("Map: A layer with that name already exists");
		}
		//check if the data is valid
		if (this.isValidMap(data) == false) {
			throw new IllegalArgumentException("Map: Data contains invalid tile template indices");
		}
		if (order == 0) {
			throw new IllegalArgumentException("Map: Order value of 0 is reserved for Entities");
		}
		
		MapLayer layer = new MapLayer(name, data, order);
		this.insertLayer(layer);
	}
	
	//remove a layer from the map
	public void removeLayer(String name) {
		for (MapLayer layer : this.layers) {
			if (layer.getName().equals(name)) {
				this.layers.remove(layer);
				return;
			}
		}
	}
	
	//change the order value of a layer
	public void setLayerOrder(String name, int order) {
		MapLayer layer = this.getLayerByName(name);
		if (layer == null) {
			throw new IllegalArgumentException("Map: Invalid layer name");
		}
		if (order == 0) {
			throw new IllegalArgumentException("Map: Order value of 0 is reserved for Entities");
		}
		
		//remove and insert the layer again with the new order value
		//only do this if the order value is actually different
		if (layer.getOrder() != order) {
			this.layers.remove(layer);
			layer.setOrder(order);
			this.insertLayer(layer);
		}
	}
	
	//set an tile template index at a specific point of a layer
	public void setPointAtLayer(String name, int x, int y, short index) {
		MapLayer layer = this.getLayerByName(name);
		if (layer == null) {
			throw new IllegalArgumentException("Map: Invalid layer name");
		}
		
		layer.setPointData(x, y, index);
	}
	
	//fill a layer with a specific time template index
	public void fillLayer(String name, short fill) {
		if (this.isValidIndex(fill) == false) {
			throw new IllegalArgumentException("Map: Invalid tile template index");
		}
		
		MapLayer layer = this.getLayerByName(name);
		if (layer == null) {
			throw new IllegalArgumentException("Map: Invalid layer name");
		}
		
		layer.fillData(fill);
	}
	
	//save map to a stream
	public void save(OutputStream out) throws IOException {
		DataOutputStream data_out = new DataOutputStream(out);
		
		data_out.writeInt(this.layers.size());
		for (MapLayer layer : this.layers) {
			String name = layer.getName();
			int order = layer.getOrder();
			short[][] data = layer.getData();
			
			data_out.writeInt(name.length());
			data_out.writeChars(name);
			data_out.writeInt(order);
			data_out.writeInt(data.length);
			data_out.writeInt(data[0].length);
			for (int x=0; x<data.length; x++) {
				for (int y=0; y<data[x].length; y++) {
					data_out.writeShort(data[x][y]);
				}
			}
		}
	}
	
	//save map to a file
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
	
	//load map from a stream
	public void load(InputStream in) throws IOException {
		DataInputStream data_in = new DataInputStream(in);
		
		int num_layers = data_in.readInt();
		this.layers.clear();
		//layers
		for (int i=0; i<num_layers; i++) {
			//name
			String name = "";
			int name_len = data_in.readInt();
			for (int j=0; j<name_len; j++) {
				name += data_in.readChar();
			}
			
			//order
			int order = data_in.readInt();
			
			//map data
			int width = data_in.readInt();
			int height = data_in.readInt();
			short[][] data = new short[width][height];
			for (int x=0; x<width; x++) {
				for (int y=0; y<height; y++) {
					data[x][y] = data_in.readShort();
				}
			}
			
			MapLayer layer = new MapLayer(name, data, order);
			this.layers.add(layer);
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

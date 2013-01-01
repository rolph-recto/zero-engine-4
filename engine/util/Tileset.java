package engine.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 * Tileset class
 * Divides an image into a grid of tiles
 */
public class Tileset {
	protected BufferedImage image;
	protected int tile_width, tile_height;
	protected int rows, columns;
	
	public Tileset(BufferedImage image, int tw, int th) {
		this.create(image, tw, th);
	}
	
	public Tileset(URL input, int tw, int th) throws IOException {
		this.create(input, tw, th);
	}
	
	public Tileset(InputStream input, int tw, int th) throws IOException {
		this.create(input, tw, th);
	}
	
	public Tileset(String file) throws IOException {
		this.load(file);
	}
	
	public Tileset(InputStream input) throws IOException {
		this.load(input);
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public int getNumTiles() {
		return this.rows*this.columns;
	}
	
	public int getTileWidth() { 
		return this.tile_width;
	}
	
	public int getTileHeight() {
		return this.tile_height;
	}
	
	//create a new tileset from a new image
	public void create(BufferedImage image, int tw, int th) {
		if (image.getWidth()%tw != 0 || image.getHeight()%th != 0) {
			throw new IllegalArgumentException("Tileset: Tile dimensions must divide image dimensions evenly");
		}
		
		this.image = image;
		this.tile_width = tw;
		this.tile_height = th;
		this.rows = (int)(image.getHeight()/this.tile_height);
		this.columns = (int)(image.getWidth()/this.tile_width);
	}
	
	//create tileset from URL path
	public void create(URL input, int tw, int th)
	throws IOException {
        //convert input stream to image
        BufferedImage image=ImageIO.read(input);
        this.create(image, tw, th);
	}
	
	//create tileset from input stream
	public void create(InputStream input, int tw, int th)
	throws IOException {
        //convert input stream to image
        BufferedImage image=ImageIO.read(input);
        this.create(image, tw, th);
	}
	
	//draw a tile
	public void draw(Graphics2D g2d, int tile_num, int pos_x, int pos_y) {
		if (tile_num < 0 || tile_num >= rows*columns) {
			throw new IllegalArgumentException("Tileset: Tile index is invalid");
		}
		
		int tile_row, tile_column; //position of the tile to be drawn
		tile_row = (int)Math.floor(tile_num/this.columns);
		tile_column = tile_num - (tile_row*this.columns);
		
		int dx1, dy1, dx2, dy2; //destination (i.e. screen) coordinates
		int sx1, sy1, sx2, sy2; //source coordinates
		
		dx1 = pos_x;
		dy1 = pos_y;
		dx2 = dx1+this.tile_width;
		dy2 = dy1+this.tile_height;
		
		sx1 = tile_column*this.tile_width;
		sy1 = tile_row*this.tile_height;
		sx2 = sx1+this.tile_width;
		sy2 = sy1+this.tile_height;
		
		g2d.drawImage(this.image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}
	
    
    //save sprite object to a stream
    public void save(OutputStream out) throws IOException {
    	out.write(this.tile_width);
    	out.write(this.tile_height);
    	
    	ImageIO.write(this.image, "png", out);
    }
    
    //save sprite object to a file
	public void save(String file) throws FileNotFoundException, IOException {
    	FileOutputStream out = new FileOutputStream(file);
    	try {
	    	this.save(out);
    	}
    	catch (IOException e) {
    		out.close();
    		throw e;
    	}
    	finally {
	    	out.close();
    	}
    }
	
	//load sprite from a stream
	public void load(InputStream in) throws IOException {
    	this.tile_width = in.read();
    	this.tile_height = in.read();
    	
		this.image = ImageIO.read(in);
		
		this.rows = (int)(image.getHeight()/this.tile_height);
		this.columns = (int)(image.getWidth()/this.tile_width);
	}
	
	//load the sprite from a file
	public void load(String file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			this.load(in);
		}
		catch (IOException e) {
			System.out.println("NOOOo");
			in.close();
			throw e;
		}
		finally {
			in.close();
		}
	}
}

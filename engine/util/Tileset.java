package engine.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class Tileset {
	protected String name;
	protected BufferedImage image;
	protected int tile_width, tile_height;
	protected int rows, columns;
	
	public Tileset(String name, BufferedImage image, int tw, int th) {
		this.setName(name);
		this.create(image, tw, th);
	}
	
	public Tileset(String name, URL input, int tw, int th) throws IOException {
		this.setName(name);
		this.create(input, tw, th);
	}
	
	
	public Tileset(String name, InputStream input, int tw, int th) throws IOException {
		this.setName(name);
		this.create(input, tw, th);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public void create(URL input, int tw, int th)
	throws IOException {
        //convert input stream to image
        BufferedImage image=ImageIO.read(input);
        this.create(image, tw, th);
	}
	
	public void create(InputStream input, int tw, int th)
	throws IOException {
        //convert input stream to image
        BufferedImage image=ImageIO.read(input);
        this.create(image, tw, th);
	}
	
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
}

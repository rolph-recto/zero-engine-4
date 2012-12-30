package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import engine.util.*;

/*
 * Viewport class
 * Draws levels
 */
public class View {
	protected int pos_x, pos_y;
	protected int cam_x, cam_y;
	protected int cam_width, cam_height;
	protected Level level;
	protected Rectangle2D clip_rect;
	
	public View(Level l, int w, int h) throws NullPointerException {
		if (l == null) {
			throw new NullPointerException();
		}
		this.level = l;
		this.clip_rect = new Rectangle2D.Float();
		this.setPosition(0, 0);
		this.setCamPosition(0, 0);
		this.setCamDimension(w, h);
	}
	
	public int getPosX() {
		return this.pos_x;
	}
	
	public int getPosY() {
		return this.pos_y;
	}
	
	public void setPosition(int x, int y) {
		this.pos_x = x;
		this.pos_y = y;
	}
	
	public void setPosX(int x) {
		this.pos_x = x;
	}
	
	public void setPosY(int y) {
		this.pos_y = y;
	}
	
	public int getCamX() {
		return this.cam_x;
	}
	
	public int getCamY() {
		return this.cam_y;
	}
	
	public void setCamPosition(int x, int y) {
		this.cam_x = (x >= 0) ? x : 0;
		this.cam_y = (y >= 0) ? y : 0;
		
		MapLayer base = this.level.getMap().getBaseLayer();
		int tile_width = this.level.getMap().getTileData().getTileWidth();
		int tile_height = this.level.getMap().getTileData().getTileHeight();
		int map_width = base.getWidth()*tile_width;
		int map_height = base.getHeight()*tile_height;
		
		//camera position is bound by the map dimension minus the camera dimension
		this.cam_x = (this.cam_x <= map_width-this.cam_width) ? this.cam_x : map_width-this.cam_width;
		this.cam_y = (this.cam_y <= map_height-this.cam_height) ? this.cam_y : map_height-this.cam_height;
	}
	
	public int getCamHeight() {
		return this.cam_height;
	}
	
	public int getCamWidth() {
		return this.cam_width;
	}
	
	public void setCamDimension(int w, int h) throws IllegalArgumentException {
		if ((w < 0) || (h < 0)) {
			throw new IllegalArgumentException("View: Camera dimensions must be positive");
		}
		
		this.cam_width = w;
		this.cam_height = h;
	}
	
	public void setCamWidth(int w) {
		this.setCamDimension(w, this.cam_height);
	}
	
	public void setCamHeight(int h) {
		this.setCamDimension(this.cam_height, h);
	}
	
	protected void setClipRect(Graphics g) {
		this.clip_rect.setRect(this.pos_x, this.pos_y, this.cam_width, this.cam_height);
		g.setClip(this.clip_rect);
	}
	
	//draw a solid color background
	public void drawBackground(Graphics2D g2d, Color color) {
		g2d.setColor(color);
		g2d.fillRect(this.pos_x, this.pos_y, this.cam_width, this.cam_height);
	}
	
	//draw entity shapes
	public void drawEntityOutlines(Graphics2D g2d, Color color) {
		if (this.level == null) {
			throw new RuntimeException("View: Level hook is null");
		}
		
		ArrayList<Entity> entity_list = this.level.getEntityList();
		AffineTransform transform = new AffineTransform();
		
		for (Entity e : entity_list) {
			Shape s = e.getModel().getShape();
			
			//drawing polygons
			if (s instanceof Polygon) {
				Polygon p = (Polygon)s;
		        int[] xvert = new int[p.getNumVertices()];
		        int[] yvert = new int[p.getNumVertices()];
		        Vector2D[] vert_list = p.getBaseVertices();
		        
		        transform.setToIdentity();
		        transform.translate(p.getPosX()+this.pos_x-this.cam_x, p.getPosY()+this.pos_y-this.cam_y);
		        transform.scale(p.getScale(), p.getScale());
		        transform.rotate(Math.toRadians(p.getRotation()));
		        
		        for (int i=0; i<vert_list.length; i++) {
		        	xvert[i] = (int)vert_list[i].getX();
		        	yvert[i] = (int)vert_list[i].getY();
		        }
		        
		        g2d.setTransform(transform);
		        g2d.setColor(color);
		        g2d.drawPolygon(xvert, yvert, vert_list.length);
			}
			//drawing circles
			else if (s instanceof Circle) {
				Circle c = (Circle)s;
				
				transform.setToIdentity();
				g2d.setTransform(transform);
		        g2d.setColor(color);
		        g2d.drawOval((int)(c.getPosX()-c.getRadius())+this.pos_x-this.cam_x,
		        		(int)(c.getPosY()-c.getRadius())+this.pos_y-this.cam_y,
		        		(int)(c.getRadius()*2), (int)(c.getRadius()*2));
			}
		}
		
		transform.setToIdentity();
		g2d.setTransform(transform);
	}
	
	//draw entity sprites
	public void drawEntities(Graphics2D g2d) {
		if (this.level == null) {
			throw new RuntimeException("View: Level hook is null");
		}
		
		ArrayList<Entity> entity_list = this.level.getEntityList();
		AffineTransform transform = new AffineTransform();

		for (Entity e : entity_list) {
			Model m = e.getModel();
			Shape s = m.getShape();
			Sprite spr = m.getSprite();
			
			transform.setToIdentity();
			transform.translate(s.getPosX()+this.pos_x-this.cam_x, s.getPosY()+this.pos_y-this.cam_y);
			transform.rotate(Math.toRadians(s.getRotation()));
			g2d.setTransform(transform);
			
			spr.setPosition((int)(-spr.getWidth()/2), (int)(-spr.getHeight()/2));
			spr.draw(g2d);
		}
		
		transform.setToIdentity();
		g2d.setTransform(transform);
	}
	
	//draw parts of the map that are behind entities
	public void drawMapBackground(Graphics2D g2d) {
		ArrayList<MapLayer> layers = this.level.getMap().getLayerList();
		
		for (MapLayer layer : layers) {
			//once all the background layers have been drawn, stop drawing
			if (layer.getOrder() > 0) {
				break;
			}
			this.drawMapLayer(g2d, layer);
		}
	}
	
	//draw parts of the map that are in front of entities
	public void drawMapForeground(Graphics2D g2d) {
		ArrayList<MapLayer> layers = this.level.getMap().getLayerList();
		
		for (MapLayer layer : layers) {
			//only draw foreground layers
			if (layer.getOrder() > 0) {
				this.drawMapLayer(g2d, layer);
			}
		}
	}
	
	//draw all the layers of the map
	public void drawMap(Graphics2D g2d) {
		ArrayList<MapLayer> layers = this.level.getMap().getLayerList();
		
		for (MapLayer layer : layers) {
			this.drawMapLayer(g2d, layer);
		}
	}
	
	//draw a single layer
	public void drawMapLayer(Graphics2D g2d, String name) {
		MapLayer layer = this.level.getMap().getLayerByName(name);
		this.drawMapLayer(g2d, layer);
	}
	
	//draw a single layer
	//this method is not public because that would allow users
	//to draw random layers that are not part of the level map
	protected void drawMapLayer(Graphics2D g2d, MapLayer layer) {		
		Map map = this.level.getMap();
		MapLayer base = map.getBaseLayer();
		Tileset tileset = map.getTileData().getTileset();
		int tile_width, tile_height;
		int cam_x, cam_y;
		
		tile_width = map.getTileData().getTileWidth();
		tile_height = map.getTileData().getTileHeight();
		
		//cam position is relative to the base layer;
		//if other layers have different dimensions than 
		//the base layer, then cam position for that layer
		//must be calculated
		
		//cam position is the same if it's the base layer
		if (layer.getName().equals(base.getName())) {
			cam_x = this.cam_x;
			cam_y = this.cam_y;
		}
		else {
			//treat camera position as a ratio relative to the layer dimensions
			double cam_x_ratio = this.cam_x/(base.getWidth()*tile_width);
			cam_x = (int)Math.floor(cam_x_ratio * (layer.getWidth()*tile_width));
			
			double cam_y_ratio = this.cam_y/(base.getHeight()*tile_height);
			cam_y = (int)Math.floor(cam_y_ratio * (layer.getHeight()*tile_height));
		}
		
		int offset_width, offset_height; //offset of camera dimension relative to tile dimensions
		int offset_x, offset_y; //offset of camera position relative to tile dimensions
		int tile_x, tile_y; //number of tiles to be drawn for each axis;
		
		offset_width = this.cam_width - (int)(Math.floor(this.cam_width/tile_width)*tile_width);
		offset_height = this.cam_height - (int)(Math.floor(this.cam_height/tile_height)*tile_height);
		
		offset_x = cam_x - (int)(Math.floor(cam_x/tile_width)*tile_width);
		offset_y = cam_y - (int)(Math.floor(cam_y/tile_height)*tile_height);
		
		//if the cam position offset is greater than the cam dimension offset,
		//the camera must draw one extra tile
		tile_x = (offset_x <= offset_width) ? (int)(Math.ceil(this.cam_width/tile_width))
				: ((int)Math.ceil(this.cam_width/tile_width))+1;
		tile_y = (offset_y <= offset_height) ? (int)(Math.ceil(this.cam_height/tile_height))
				: ((int)Math.ceil(this.cam_height/tile_height))+1;
		
		//finally, draw the visible tiles
		int tile_num, pos_x, pos_y;
		for (int y=0; y < tile_y; y++) {
			for (int x=0; x < tile_x; x++) {
				tile_num = layer.getPointData(x, y);
				pos_x = this.pos_x - offset_x + (x*tile_width);
				pos_y = this.pos_y - offset_y + (y*tile_height);
				tileset.draw(g2d, tile_num, pos_x, pos_y);
			}
		}
	}
	
	//draw everything
	public void draw(Graphics2D g2d) {
		this.setClipRect(g2d);
		//this.drawBackground(g2d, Color.WHITE);
		//this.drawEntityOutlines(g2d, Color.RED);
		this.drawMapBackground(g2d);
		this.drawEntities(g2d);
		this.drawMapForeground(g2d);
		g2d.setClip(null);
	}
	
	//focuses the camera to a specific Entity
	public void focusTo(int entity_id) {
		Entity e = this.level.getEntityById(entity_id);
		int cam_x = (int)(e.getPosX() - (this.cam_width/2));
		int cam_y = (int)(e.getPosY() - (this.cam_height/2));
		this.setCamPosition(cam_x, cam_y);
	}
}
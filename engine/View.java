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
		// TODO add validation for map dimension
		this.cam_x = (x >= 0) ? x : 0;
		this.cam_y = (y >= 0) ? y : 0;
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
		this.setClipRect(g2d);
		
		g2d.setColor(color);
		g2d.fillRect(this.pos_x, this.pos_y, this.cam_width, this.cam_height);
	}
	
	//draw entity shapes
	public void drawEntityOutlines(Graphics2D g2d, Color color) {
		if (this.level == null) {
			throw new RuntimeException("View: Level hook is null");
		}
		
		this.setClipRect(g2d);
		
		ArrayList<Entity> entity_list = this.level.getEntityList();
		AffineTransform transform = new AffineTransform();
		
		// TODO implement drawing textures, not just outlines
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
		this.setClipRect(g2d);
		AffineTransform transform = new AffineTransform();

		for (Entity e : entity_list) {
			Model m = e.getModel();
			Shape s = m.getShape();
			Sprite spr = m.getSprite();
			
			transform.setToIdentity();
			transform.translate(s.getPosX()+this.pos_x-this.cam_x, s.getPosY()+this.pos_y-this.cam_y);
			transform.rotate(Math.toRadians(s.getRotation()));
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2d.setTransform(transform);
			
			spr.setPosition((int)(-spr.getWidth()/2), (int)(-spr.getHeight()/2));
			spr.draw(g2d);
		}
		
		transform.setToIdentity();
		g2d.setTransform(transform);
	}
	
	public void draw(Graphics2D g2d) {
		this.drawBackground(g2d, Color.WHITE);
		this.drawEntityOutlines(g2d, Color.RED);
		this.drawEntities(g2d);
	}
	
	//
	public void focusTo(Entity e) {
		
	}
}
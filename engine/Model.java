package engine;

import java.awt.image.BufferedImage;

import engine.util.*;

public class Model {
	private final Shape shape;
	private final BufferedImage image;
	
	public Model(Shape s, BufferedImage img) {
		this.shape = s;
		this.image = img;
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
}

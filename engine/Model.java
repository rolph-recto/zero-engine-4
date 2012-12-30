package engine;

import java.awt.image.BufferedImage;

import engine.util.*;

/*
 * Model class
 * Graphical representation of an Entity
 */
public class Model {
	private final Shape shape;
	private final Sprite sprite;
	
	public Model(Shape s, Sprite spr) {
		this.shape = s;
		this.sprite = spr;
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
}

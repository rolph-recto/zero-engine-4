package engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import engine.util.*;

/*
 * Model class
 * Graphical representation of an Entity
 */
public final class Model {
	private Shape shape;
	private Sprite sprite;
	
	public Model(Shape s, Sprite spr) {
		this.shape = s;
		this.sprite = spr;
	}
	
	public Model(InputStream in) throws ClassNotFoundException, IOException {
		this.load(in);
	}
	
	public Model(String file) throws ClassNotFoundException, IOException {
		this.load(file);
	}
	
	public Shape getShape() {
		return this.shape;
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	private void saveModel(OutputStream out) throws IOException {
		DataOutputStream data_out = new DataOutputStream(out);
		if (this.shape instanceof Circle ) {
			data_out.writeByte(0);
		}
		else if (this.shape instanceof Polygon) {
			data_out.writeByte(1);
		}
		this.shape.save(data_out);
	}
	
	private void save(OutputStream out) throws IOException {
		this.saveModel(out);
		this.sprite.save(out);
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
	
	private void loadModel(InputStream in) throws IOException {
		DataInputStream data_in = new DataInputStream(in);
		
		byte shape_type = data_in.readByte();
		if (shape_type == 0) {
			this.shape = new Circle(data_in);
		}
		else {
			this.shape = new Polygon(data_in);
		}
	}
	
	private void load(InputStream in) throws IOException, ClassNotFoundException {
		this.loadModel(in);
		this.sprite = new Sprite(in);
	}
	
	public void load(String file) throws IOException, ClassNotFoundException {
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

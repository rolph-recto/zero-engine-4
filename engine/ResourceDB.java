package engine;

import java.util.*;

/*
 * Resource Database class
 * Contains all resources used in the game
 */
public final class ResourceDB {
	private final HashMap<String, EntityType> entity_type_db;
	private final HashMap<String, Object> model_db; //Specify model type later
	private final HashMap<String, Object> sound_db; //Specify sound type later
	
	public ResourceDB() {
		this.entity_type_db = new HashMap<String, EntityType> ();
		this.model_db = new HashMap<String, Object> (); //Specify model type later
		this.sound_db = new HashMap<String, Object> (); //Specify sound type later
	}
	
	public void addEntityType(String name, EntityType type) {
		this.entity_type_db.put(name, type);
	}
	
	public EntityType getEntityType(String name) {
		return this.entity_type_db.get(name);
	}

	//Specify model type later
	public void addModel(String name, Object type) {
		this.model_db.put(name, type);
	}
	
	//Specify model type later
	public Object getModel(String name) {
		return this.model_db.get(name);
	}
	
	//Specify sound type later
	public void addSound(String name, Object type) {
		this.sound_db.put(name, type);
	}
	
	//Specify sound type later
	public Object getSound(String name) {
		return this.sound_db.get(name);
	}
}

//Controller.java
//Rolph Recto

package engine;

import java.util.*;

import engine.util.Listener;

/*
 * Controller class
 * Manipulates Entities
 */
public abstract class Controller implements Listener {
	protected long id;
	protected Level level; //level in which the controller belongs
	protected ArrayList<Entity> entity_list;
	
	protected Controller() {
		this.entity_list = new ArrayList<Entity> ();
	}
	
	protected Controller(long id, Level l) {
		this();
		this.setId(id);
		this.setLevel(l);
	}
	
	protected Controller(long id, Level l, Entity e) {
		this(id, l);
		this.addEntity(e);
	}
	
	protected Controller(Level l, Entity e) {
		this.setLevel(l);
		this.addEntity(e);
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public void setLevel(Level l) {
		this.level = l;
	}
	
	//add an entity to the controller
	//returns true if entity is added
	//returns false if entity is already in the list
	public boolean addEntity(Entity e) {
		for (Entity e2 : this.entity_list) {
			if (e == e2) return false;
		}
		
		this.entity_list.add(e);
		e.addSubscriber(this, MsgType.MSG_ALL);
		return true;
	}
	
	//remove an entity from the controller
	public boolean removeEntity(Entity e) {
		return this.removeEntity(e.getId());
	}
	
	//remove an entity from the controller
	//returns true if an entity was removed
	//returns false if there is no entity with the given id
	public boolean removeEntity(long id) {
		for (Entity e : this.entity_list) {
			if (e.getId() == id) {
				e.removeSubscriber(this);
				this.entity_list.remove(e);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Entity> getEntityList() {
		return this.getEntityList();
	}
	
	public Entity getEntityById(long id) {
		for (Entity e : this.entity_list) {
			if (e.getId() == id) return e;
		}
		return null;
	}
	
	public Entity getEntityByIndex(int index)  {
		return this.entity_list.get(index);
	}
	
	//convenience function for controllers with one entity
	public Entity getEntity() {
		if (this.entity_list.size() >= 1) {
			return this.entity_list.get(0);
		}
		else return null;
	}
	
	//subclasses can modify this by overriding the method
	//controllers with higher priority are updated first
	public int getPriority() {
		return 0;
	}
}
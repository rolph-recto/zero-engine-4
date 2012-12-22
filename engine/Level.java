//Level.java
//Rolph Recto

package engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import engine.util.*;
import engine.msgtype.*;

/*
 * Level class
 * Contains all data of one level
 */
public class Level extends Dispatcher implements Listener {
	enum IdType { ENTITY, CONTROLLER; }
	protected final ArrayList<Entity> entity_list;
	protected final ArrayList<Controller> ctrl_list;
	protected long entity_id; //next id to assign if entity id list is empty
	protected ArrayList<Long> entity_id_list; //list of free ids
	protected long ctrl_id; //next id to assign if ctrl id list is empty
	protected ArrayList<Long> ctrl_id_list; //list of free ids
	protected LevelMessage update_msg; //message used to update controller
	protected ResourceDB resources;
	
	public Level() {
		super();
		this.entity_list = new ArrayList<Entity> ();
		this.ctrl_list = new ArrayList<Controller> ();
		this.entity_id = 0;
		this.entity_id_list = new ArrayList<Long> ();
		this.ctrl_id = 0;
		this.ctrl_id_list = new ArrayList<Long> ();
		this.update_msg = new LevelMessage(MsgType.LEVEL_UPDATE, this);
	}
	
	public Level(ResourceDB db) {
		this();
		this.resources = db;
	}
	
	//returns a free id
	protected long getFreeId(IdType type) {
		//entity ids
		if (type == IdType.ENTITY) {
			//if the id list is empty, increment the id counter
			if (this.entity_id_list.isEmpty()) {
				this.entity_id++;
				return this.entity_id-1;
			}
			//if the list is not empty, get an id from it
			else return (long)this.entity_id_list.remove(0);
		}
		//controller ids
		else {
			//if the id list is empty, increment the id counter
			if (this.ctrl_id_list.isEmpty()) {
				this.ctrl_id++;
				return this.ctrl_id-1;
			}
			//if the list is not empty, get an id from it
			else return (long)this.ctrl_id_list.remove(0);
		}
	}
	
	//insert an existing entity into the level
	public long insertEntity(Entity e) {
		e.setId(this.getFreeId(IdType.ENTITY));
		this.entity_list.add(e);	
		return e.getId();
	}
	
	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(String type_name, double x, double y, double z)  {
		EntityType type = this.resources.getEntityType(type_name);
		if (type == null) return -1;
		else return this.createEntity(type, x, y);
	}

	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(EntityType type, double x, double y) {
		// TODO ADD CODE Validate type (check if it exists in the resource database)
		
		//validate position
		if (!this.validPosition(x, y)) {
			throw new IllegalArgumentException("Invalid position for entity");
		}
		
		//create new entity
		Entity e = type.createEntity();
		Model model_template = type.getModel();
		Model model = new Model(model_template.getShape().clone(), model_template.getImage());
		e.setType(type);
		e.setModel(model);
		e.setPosition(x, y);
		
		//Create controller
		//If EntityType is hivemind,
		//see if a controller for the type already exists
		boolean create_controller = true;
		if (type.isHivemind()) {
			for (Controller c : this.ctrl_list) {
				if (type.getControllerClass().isInstance(c)) {
					c.addEntity(e);
					create_controller = false;
					break;
				}
			}
		}
		//create a new controller for the entity
		if (create_controller) {
			Controller c;
			try {
				Constructor construct = type.getControllerClass().getConstructor();
				construct.setAccessible(true);
				c = (Controller)construct.newInstance();
				c.addEntity(e);
				this.insertController(c);
			}
			//don't create the entity if there are any exceptions whatsoever
			catch (Exception ex) {
				return -1;
			}
		}
		
		//insert the entity only if the controller was created successfully;
		//that's why this line is all the way at the bottom
		this.insertEntity(e);
		
		//Broadcast message ENTITY_CREATE
		this.broadcast(new EntityMessage(MsgType.ENTITY_CREATE, e));
		
		return e.getId();
	}
	
	//remove an entity from the level
	public boolean removeEntity(long id) {
		for (Entity e : this.entity_list) {
			if (e.getId() == id) {
				entity_list.remove(e);
				//add the entity's id to the list of free ids
				this.entity_id_list.add(e.getId());

				//Broadcast message ENTITY_DESTROY
				this.broadcast(new EntityMessage(MsgType.ENTITY_DESTROY, e));
				
				return true;
			}
		}
		//there was no entity with the id provided
		return false;
	}
	
	//return Entity object from its ID
	public Entity getEntityById(long id) {
		for (Entity e : this.entity_list) {
			if (e.getId() == id) return e;
		}
		return null;
	}
	
	//return list of entities in the level
	public ArrayList<Entity> getEntityList() {
		return this.entity_list;
	}
	
	public long insertController(Controller c) {
		long id = this.getFreeId(IdType.CONTROLLER);
		c.setId(id);
		
		//insert controller in ordered list
		//the highest its priority, the closer
		//it is to the top of the list
		boolean insert = false;
		for (int i=0; i<this.ctrl_list.size(); i++) {
			if (c.priority >= this.ctrl_list.get(i).priority) {
				this.ctrl_list.add(i, c);
				insert = true;
				break;
			}
		}
		//if the ctrl has the lowest priority,
		//add it to the bottom of the list
		if (!insert) this.ctrl_list.add(c);
		
		return id;
	}
	
	//remove a controller
	public boolean removeController(long id) {
		for (Controller c : this.ctrl_list) {
			if (c.getId() == id) {
				this.ctrl_list.remove(c);
				return true;
			}
		}
		return false;
	}
	
	//check if position is within the level
	// TODO finish position validation code
	protected boolean validPosition(double x, double y) {
		return ((x >= 0.0) && (y >= 0.0));
	}
	
	//move entities according to their acceleration and velocity
	protected void updatePhysics() {
		for (Entity e : this.entity_list) {
			e.setMoved(false); //trip only when position changes
			e.move(1.0); //move with 1 time step
			e.setAcceleration(0.0, 0.0, 0.0); //forces must apply themselves to objects every frame
		}
	}
	
	//check if objects are colliding, then broadcast collision messages
	protected void checkCollisions() {
		
	}
	
	//update controllers
	//this method is called each frame
	public void update() {
		//update physics
		this.updatePhysics();
		
		//check collisions
		this.checkCollisions();
		
		//broadcast update message
		for (Controller c : this.ctrl_list) {
			c.onMessage(this.update_msg);
		}
		
		//send the message to all the other listeners
		this.broadcast(this.update_msg);
	}
	
	//level received a message
	public void onMessage(Message msg) {
		switch (msg.getType()) {
		default:
			break;
		}
	}
}

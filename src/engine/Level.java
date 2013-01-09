//Level.java
//Rolph Recto

package engine;

import java.util.ArrayList;

import engine.msgtype.EntityCollisionMessage;
import engine.msgtype.EntityMessage;
import engine.msgtype.EntityMoveMessage;
import engine.msgtype.LevelMessage;
import engine.util.Dispatcher;
import engine.util.Listener;
import engine.util.Message;
import engine.util.Shape;
import engine.util.Vector2D;

/*
 * Collision data class
 * contains maps of entities and tiles used for broad-phase collision detection
 */
final class CollisionMap implements Listener {
	//each cell has a list of entities
	//updates automatically for each entity movement
	private ArrayList<ArrayList<ArrayList<Entity>>> entity_map;
	private Level level;
	
	public CollisionMap(Level level) {
		this.level = level;
		this.initArrays();
	}
	
	private void initArrays() {
		MapLayer base = this.level.getMap().getBaseLayer();
		int width = base.getWidth();
		int height = base.getHeight();
		
		this.entity_map = new ArrayList<ArrayList<ArrayList<Entity>>>(width);
		for (int x=0; x<width; x++) {
			//set columns
			this.entity_map.add(new ArrayList<ArrayList<Entity>>(height));
			
			//set rows
			for (int y=0; y<height; y++) {
				this.entity_map.get(x).add(new ArrayList<Entity>());
			}
		}
	}
	
	//add an entity into the map
	public void addEntity(Entity e) {
		int tile_width = this.level.getMap().getTileData().getTileWidth();
		int tile_height = this.level.getMap().getTileData().getTileHeight();
		int x = (int)(Math.floor(e.getPosX()/tile_width));
		int y = (int)(Math.floor(e.getPosY()/tile_height));
		
		this.entity_map.get(x).get(y).add(e);
	}
	
	//remove an entity from the map
	public void removeEntity(Entity e) {
		int tile_width = this.level.getMap().getTileData().getTileWidth();
		int tile_height = this.level.getMap().getTileData().getTileHeight();
		int x = (int)(Math.floor(e.getPosX()/tile_width));
		int y = (int)(Math.floor(e.getPosY()/tile_height));
		
		this.entity_map.get(x).get(y).remove(e);
	}
	
	public void updateEntity(Entity e) {
		int tile_width = this.level.getMap().getTileData().getTileWidth();
		int tile_height = this.level.getMap().getTileData().getTileHeight();
		
		int old_x = (int)(Math.floor(e.getOldPosX()/tile_width));
		int old_y = (int)(Math.floor(e.getOldPosY()/tile_height));
		
		int x = (int)(Math.floor(e.getPosX()/tile_width));
		int y = (int)(Math.floor(e.getPosY()/tile_height));
		
		//only switch if the coords are different
		if (old_x != x || old_y != y) {
			this.entity_map.get(old_x).get(old_y).remove(e);
			this.entity_map.get(x).get(y).add(e);
		}
	}
	
	public void onMessage(Message msg) {
		switch(msg.getType()) {
		case ENTITY_MOVE:
			//update the object map when an entity moves
			EntityMoveMessage move_msg = (EntityMoveMessage)msg;
			this.updateEntity(move_msg.getEntity());
			break;
		default:
			break;
		}
	}
	
	public ArrayList<ArrayList<ArrayList<Entity>>> getEntityMap() {
		return this.entity_map;
	}
}

/*
 * Level class
 * Contains all data of one level
 */
public class Level extends Dispatcher implements Listener {
	enum IdType { ENTITY, CONTROLLER; }
	protected Map map; //does not have a modifier method, not a good idea to change maps halfway through a game
	protected final ArrayList<Entity> entity_list;
	protected final ArrayList<Controller> ctrl_list;
	protected long entity_id; //next id to assign if entity id list is empty
	protected ArrayList<Long> entity_id_list; //list of free ids
	protected long ctrl_id; //next id to assign if ctrl id list is empty
	protected ArrayList<Long> ctrl_id_list; //list of free ids
	protected ResourceDB resources;
	protected CollisionMap col_map;
	protected LevelMessage update_msg; //message used to update controller
	
	protected Level() {
		super();
		this.entity_list = new ArrayList<Entity> ();
		this.ctrl_list = new ArrayList<Controller> ();
		this.entity_id = 0;
		this.entity_id_list = new ArrayList<Long> ();
		this.ctrl_id = 0;
		this.ctrl_id_list = new ArrayList<Long> ();
		this.update_msg = new LevelMessage(MsgType.LEVEL_UPDATE, this, null, 0, 0);
	}
	
	public Level(Map m, ResourceDB db) {
		this();
		this.map = m;
		this.resources = db;
		this.col_map = new CollisionMap(this);
	}
	
	public Map getMap() {
		return this.map;
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
		//check if the entity isn't in the level already
		for (Entity e2: this.entity_list) {
			if (e == e2) return e.getId();
		}
		//if it is not in the level already, set an id for the entity and add it
		e.setId(this.getFreeId(IdType.ENTITY));
		
		//set up collision map
		e.addSubscriber(this.col_map, MsgType.ENTITY_MOVE);
		this.col_map.addEntity(e);
		
		this.entity_list.add(e);
		
		return e.getId();
	}
	
	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(String type_name, double x, double y)  {
		EntityType type = this.resources.getEntityType(type_name);
		return this.createEntity(type, x, y);
	}

	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(EntityType type, double x, double y) {
		return this.createEntity(type, "", x, y);
	}
	
	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(String type_name, String entity_name, double x, double y)  {
		EntityType type = this.resources.getEntityType(type_name);
		return this.createEntity(type, entity_name, x, y);
	}
	
	//create entity and inserts it into the level
	//returns object ID
	public long createEntity(EntityType type, String name, double x, double y) {
		// TODO ADD CODE Validate type (check if it exists in the resource database)
		
		//validate position
		if (!this.validPosition(x, y)) {
			throw new IllegalArgumentException("Invalid position for entity");
		}
		
		//create new entity
		Entity e = type.createEntity();
		//Model model_template = type.getModel();
		Model model_template = this.resources.getModel(type.getName());
		Model model = new Model(model_template.getShape().clone(), model_template.getSprite().clone());
		e.setType(type);
		e.setModel(model);
		e.setPosition(x, y);
		
		//set entity properties
		e.setName(name);
		e.setDynamic(type.isDynamic());
		e.setFriction(type.getFriction());
		e.setCollisionMask(type.getCollisionMask());
		e.setCollisionType(type.getCollisionType());
		
		//set controller
		Controller ctrl = type.createController();
		ctrl.addEntity(e);
		ctrl.setLevel(this);
		this.insertController(ctrl);

		this.insertEntity(e);
		
		//Broadcast message ENTITY_CREATE
		this.broadcast(new EntityMessage(MsgType.ENTITY_CREATE, e));
		
		return e.getId();
	}
	
	//remove an entity from the level
	public boolean removeEntity(long id) {
		for (Entity e : this.entity_list) {
			if (e.getId() == id) {
				this.entity_list.remove(e);
				this.col_map.removeEntity(e);
				
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
	
	//return Entity object from its name
	public Entity getEntityByName(String name) {
		for (Entity e : this.entity_list) {
			if (e.getName().equals(name)) return e;
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
			if (c.getPriority() >= this.ctrl_list.get(i).getPriority()) {
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
			e.setVelocity(e.getVelX()*e.getFriction(), e.getVelY()*e.getFriction(),
					e.getVelRot()*e.getFriction()); //apply friction
			e.setAcceleration(0.0, 0.0, 0.0); //forces must apply themselves to objects every frame
		}
	}
	
	//check if objects are colliding, then broadcast collision messages
	protected void checkCollisions() {
		TileData tile_data = this.map.getTileData();
		MapLayer base = this.map.getBaseLayer();
		ArrayList<ArrayList<ArrayList<Entity>>> entity_map = this.col_map.getEntityMap();
		int tile_width = this.map.getTileData().getTileWidth();
		int tile_height = this.map.getTileData().getTileHeight();
		int map_width = this.map.getBaseLayer().getWidth();
		int map_height = this.map.getBaseLayer().getHeight();
		
		for (Entity e : this.entity_list) {
			//only check for collisions if the entity moved
			if (e.getMoved()) {
				Shape e_shape = e.getModel().getShape();
				int entity_cell_x = (int)(Math.floor(e.getPosX()/tile_width));
				int entity_cell_y = (int)(Math.floor(e.getPosY()/tile_height));
				
				//first, create list of tiles to check
				//must check tiles adjacent to the one the entity is in
				//(broad-phase collision detection)
				int tile_x1 = (entity_cell_x-1 >= 0) ? entity_cell_x-1 : 0;
				int tile_x2 = (entity_cell_x+1 < map_width ) ? entity_cell_x+1 : map_width-1;
				int tile_y1 = (entity_cell_y-1 >= 0) ? entity_cell_y-1 : 0;
				int tile_y2 = (entity_cell_y+1 < map_height ) ? entity_cell_y+1 : map_height-1;
				
				//check for collisions within the block of tles
				//(narrow-phase collision detection)
				for (int x=tile_x1; x<=tile_x2; x++) {
					for (int y=tile_y1; y<=tile_y2; y++) {
						//check wall collisions
						TileTemplate t = tile_data.getTileTemplate(base.getPointData(x, y));
						//only check for collision if the tile is not empty
						if (t.isEmpty() == false) {
							Shape tile_shape = t.getShape();
							//set shape position as the center of the cell
							tile_shape.setPosX((x*tile_width)+(tile_width/2));
							tile_shape.setPosY((y*tile_height)+(tile_height/2));
							
							//player is colliding with a wall
							if (tile_shape.collision(e_shape)) {
								//collision response: translate entity away from the wall
								Vector2D mtv = tile_shape.getMTV(e_shape);
								e.translate(mtv.getX(), mtv.getY());
								this.col_map.updateEntity(e);
								
								//broadcast the message
								e.broadcast(new EntityCollisionMessage(e, x, y));
							}
						}
						
						//check object collisions
						//first get list of entities within the cell
						ArrayList<Entity> entity_cell_list = entity_map.get(x).get(y);
						boolean collided = false;
						for (Entity e2 : entity_cell_list) {
							//make sure the entity doesn't check collision with itself!
							//also check collision type first
							if ( (e != e2) && ((e2.getCollisionType()&e.getCollisionMask()) != 0) ) {
								Shape e2_shape = e2.getModel().getShape();
								
								//objects collide
								if (e_shape.collision(e2_shape)) {
									//collision response: translate entity away from other entity
									Vector2D mtv = e2_shape.getMTV(e_shape);
									e.translate(mtv.getX(), mtv.getY());
									collided = true;
									
									//broadcast the message
									//must broadcast two messages so each entity
									//can respond to the collision accordingly
									e.broadcast(new EntityCollisionMessage(e, e2));
									e2.broadcast(new EntityCollisionMessage(e2, e));
								}
							}
						}
						//must do it outside of the loop b/c it crashes
						//when you try to remove an element while in the loop
						if (collided) {
							this.col_map.updateEntity(e);	
						}
						
						// TODO implement raycasting
					}
				}
			}
		}
		//whew! long function, isn't it?
	}
	
	//update controllers
	//this method is called each frame
	public void update(boolean[] key_state, int mouse_x, int mouse_y) {
		//update physics
		this.updatePhysics();
		
		//check collisions
		this.checkCollisions();
		
		//update input events
		this.update_msg.setKeyState(key_state);
		this.update_msg.setMousePosition(mouse_x, mouse_y);
		
		//broadcast update message to controllers
		for (Controller c : this.ctrl_list) {
			c.onMessage(this.update_msg);
		}
		
		//broadcast the message to all the other listeners
		this.broadcast(this.update_msg);
	}
	
	//level received a message
	public void onMessage(Message msg) {}
}
//Entity.java
//Rolph Recto

package engine;

import engine.util.*;
import engine.msgtype.*;

/*
 * Entity class
 * Base class of all objects in the game (players, enemies, obstacles)
 */
public class Entity extends Dispatcher implements Listener {
	protected long id;
	protected String name;
	protected Model model;
	protected double old_pos_x, old_pos_y; //previous position on the entity
	protected double vel_x, vel_y, vel_rot; //velocities of position and rotation
	protected double accel_x, accel_y, accel_rot; //velocities of position and rotation
	protected EntityType type;
	protected boolean moved; //did the entity move this frame?
	protected boolean dynamic; //can the entity move?
	protected double friction; //constant of friction
	
	public Entity () {
		super();
	}
	
	public Entity(long a_id) {
		this(a_id, "");	
	}
	
	public Entity(long id, String name) {
		this();
		this.id=id;
		this.name=name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void setModel(Model m) {
		this.model = m;
	}

	public double getPosX() {
		return this.model.getShape().getPosX();
	}

	public double getPosY() {
		return this.model.getShape().getPosY();
	}
	
	public double getOldPosX() {
		return this.old_pos_x;
	}
	
	public double getOldPosY() {
		return this.old_pos_y;
	}
	
	public void setPosX(double pos_x) {
		this.old_pos_x = this.getPosX();
		this.model.getShape().setPosX(pos_x);
	}

	public void setPosY(double pos_y) {
		this.old_pos_y = this.getPosY();
		this.model.getShape().setPosY(pos_y);
	}
	
	public void setPosition(double x, double y) {
		this.old_pos_x = this.getPosX();
		this.old_pos_y = this.getPosY();
		this.model.getShape().setPosition(x, y);
	}

	public double getRotation() {
		return this.model.getShape().getRotation();
	}
	
	public void setRotation(double rot) {
		this.model.getShape().setRotation(rot);
	}
	
	public double getVelX() {
		return this.vel_x;
	}
	
	public double getVelY() {
		return this.vel_y;
	}
	
	public double getVelRot() {
		return this.vel_rot;
	}

	public void setVelX(double vel_x) {
		this.vel_x = vel_x;
	}
	
	public void setVelY(double vel_y) {
		this.vel_y = vel_y;
	}
	
	public void setVelRot(double vel_rot) {
		this.vel_rot = vel_rot;
	}
	
	public void setVelocity(double x, double y, double rot) {
		this.vel_x = x;
		this.vel_y = y;
		this.vel_rot = rot;
	}

	public double getAccelX() {
		return this.accel_x;
	}

	public double getAccelY() {
		return this.accel_y;
	}

	public double getAccelRot() {
		return this.accel_rot;
	}

	public void setAccelX(double accel_x) {
		this.accel_x = accel_x;
	}
	
	public void setAccelY(double accel_y) {
		this.accel_y = accel_y;
	}
	
	public void setAccelRot(double accel_rot) {
		this.accel_rot = accel_rot;
	}
	
	public void setAcceleration(double x, double y, double rot) {
		this.accel_x = x;
		this.accel_y = y;
		this.accel_rot = rot;
	}
	
	public EntityType getType() { 
		return this.type;
	}
	
	public void setType(EntityType type) {
		this.type = type;
	}
	
	public boolean getMoved() {
		return this.moved;
	}
	
	public void setMoved(boolean move) {
		this.moved = move;
	}
	
	public boolean isDynamic() {
		return this.dynamic;
	}
	
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	public double getFriction() {
		return this.friction;
	}
	
	//friction should be a value between 0.0 and 1.0
	public void setFriction(double friction) {
		if (friction < 0.0 || friction > 1.0) {
			throw new IllegalArgumentException("Entity: Friction constant must be between 0.0 and 1.0");
		}
		
		this.friction = friction;
	}

	/**
	 * Updates the velocity, then the position of the entity.
	 * 
	 * @param dt The time since the last update.
	 */
	public void move(double dt) {
		//the entity only moves if it is dynamic
		if (this.dynamic) {
			this.setVelocity(this.vel_x+(this.accel_x*dt), this.vel_y+(this.accel_y*dt), this.vel_rot+(this.accel_rot*dt));
			this.setPosition(this.getPosX()+(this.vel_x*dt), this.getPosY()+(this.vel_y*dt));
			this.setRotation(this.getRotation()+(this.vel_rot*dt));
			
			//only set moved flag if velocity is not 0
			if ((this.vel_x != 0.0) || (this.vel_y != 0.0) || (this.vel_rot != 0.0)) {
				moved = true;
				
				//broadcast message: ENTITY_MOVE
				EntityMoveMessage move_msg = new EntityMoveMessage(MsgType.ENTITY_MOVE, this,
						this.vel_x*dt, this.vel_y*dt, this.vel_rot*dt);
				this.broadcast(move_msg);
			}
		}
	}
	
	
	public void onMessage(Message msg) {
		switch(msg.getType()) {
		case ENTITY_COMMAND_MOVE:
			EntityMoveMessage move_msg = (EntityMoveMessage)msg;
			//treat values as components of force vector
			this.setAcceleration(move_msg.getX(), move_msg.getY(), move_msg.getRotation());
			break;
		default:
			break;
		}
	}
}

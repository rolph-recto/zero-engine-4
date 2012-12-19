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
	protected double pos_x, pos_y, pos_z;
	protected double vel_x, vel_y, vel_z;
	protected double accel_x, accel_y, accel_z;
	protected EntityType type;
	protected boolean moved; //did the player move this frame?
	
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

	public double getPosX() {
		return pos_x;
	}

	public void setPosX(double pos_x) {
		this.pos_x = pos_x;
	}

	public double getPosY() {
		return pos_y;
	}

	public void setPosY(double pos_y) {
		this.pos_y = pos_y;
	}

	public double getPosZ() {
		return pos_z;
	}

	public void setPosZ(double pos_z) {
		this.pos_z = pos_z;
	}

	public double getVelX() {
		return vel_x;
	}
	
	public void setPosition(double x, double y, double z) {
		this.pos_x = x;
		this.pos_y = y;
		this.pos_z = z;
	}

	public void setVelX(double vel_x) {
		this.vel_x = vel_x;
	}

	public double getVelY() {
		return vel_y;
	}

	public void setVelY(double vel_y) {
		this.vel_y = vel_y;
	}

	public double getVelZ() {
		return vel_z;
	}

	public void setVelZ(double vel_z) {
		this.vel_z = vel_z;
	}
	
	public void setVelocity(double x, double y, double z) {
		this.vel_x = x;
		this.vel_y = y;
		this.vel_z = z;
	}

	public double getAccelX() {
		return accel_x;
	}

	public void setAccelX(double accel_x) {
		this.accel_x = accel_x;
	}

	public double getAccelY() {
		return accel_y;
	}

	public void setAccelY(double accel_y) {
		this.accel_y = accel_y;
	}

	public double getAccelZ() {
		return accel_z;
	}

	public void setAccelZ(double accel_z) {
		this.accel_z = accel_z;
	}
	
	public void setAcceleration(double x, double y, double z) {
		this.accel_x = x;
		this.accel_y = y;
		this.accel_z = z;
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

	/**
	 * Updates the velocity, then the position of the entity.
	 * 
	 * @param dt The time since the last update.
	 */
	public void move(double dt) {
		this.setVelocity(vel_x + accel_x * dt, vel_y + accel_y * dt, vel_z + accel_z * dt);
		this.setPosition(pos_x + vel_x * dt, pos_y + vel_y * dt, pos_z + vel_z * dt);
		
		//only set moved flag if velocity is not 0
		if ((vel_x != 0.0) || (vel_y != 0.0) || (vel_z != 0.0)) {
			moved = true;
			
			//broadcast message: ENTITY_MOVE
			double dx, dy, dz;
			EntityMoveMessage move_msg = new EntityMoveMessage(MsgType.ENTITY_MOVE, this, vel_x*dt, vel_y*dt, vel_z*dt);
			this.broadcast(move_msg);
		}
	}
	
	public void onMessage(Message msg) {
		switch(msg.getType()) {
		case ENTITY_COMMAND_MOVE:
			EntityMoveMessage move_msg = (EntityMoveMessage)msg;
			//treat values as components of force vector
			this.setAcceleration(move_msg.getX(), move_msg.getY(), move_msg.getZ());
		}
	}
}

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
	protected double old_pos_x, old_pos_y, old_rot; //previous position on the entity
	protected double vel_x, vel_y, vel_rot; //velocities of position and rotation
	protected double accel_x, accel_y, accel_rot; //velocities of position and rotation
	protected EntityType type;
	protected boolean moved; //did the entity move this frame?
	protected boolean dynamic; //can the entity move?
	protected double friction; //constant of friction
	protected double bounce; //constant of "bounciness"
	protected double max_vel; //maximum velocity
	protected int col_mask; //what other types of entities will this collide with?
	protected int col_type; //what type of entity is this, for collision response purposes?
	protected boolean bullet;
	protected boolean dead; //should this entity be removed?
	
	public Entity () {
		super();
		this.old_pos_x = -1.0;
		this.old_pos_y = -1.0;
		this.dead = false;
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
	
	/*
	public void setPosX(double x) {
		if (x != this.getPosX()) {
			this.old_pos_x = this.getPosX();
			this.model.getShape().setPosX(x);
		}
	}
	
	public void translateX(double x) {
		this.setPosX(this.getPosX()+x);
	}

	public void setPosY(double y) {
		if (y != this.getPosY()) {
			this.old_pos_y = this.getPosY();
			this.model.getShape().setPosY(y);
		}
	}
	
	public void translateY(double y) {
		this.setPosY(this.getPosY()+y);
	}
	*/
	
	public void setPosRot(double x, double y, double rot) {
		double dx = 0.0;
		double dy = 0.0;
		double drot = 0.0;
		if (x != this.getPosX() || y != this.getPosY()) {
			//the entity has moved before
			if (this.old_pos_x != -1.0 && this.old_pos_y != -1.0) {
				this.old_pos_x = this.getPosX();
				this.old_pos_y = this.getPosY();
			}
			//the entity has not moved before; set old position as current position
			else {
				this.old_pos_x = x;
				this.old_pos_y = y;
			}
			this.model.getShape().setPosition(x, y);
			dx = x - this.old_pos_x;
			dy = y - this.old_pos_y;
			this.moved = true;
		}
		if (rot != this.getRotation()) {
			this.old_rot = this.getRotation();
			this.model.getShape().setRotation(rot);
			drot = rot - this.old_rot;
			this.moved = true;
		}
		
		if (this.moved) {
			//broadcast message: ENTITY_MOVE
			EntityMoveMessage move_msg = new EntityMoveMessage(MsgType.ENTITY_MOVE, this, dx, dy, drot);
			this.broadcast(move_msg);
		}
	}
	
	public void setPosition(double x, double y) {
		this.setPosRot(x, y, this.getRotation());
	}
	
	public void translate(double x, double y) {
		this.setPosition(this.getPosX()+x, this.getPosY()+y);
	}

	public double getRotation() {
		return this.model.getShape().getRotation();
	}
	
	public void setRotation(double rot) {
		this.setPosRot(this.getPosX(), this.getPosY(), rot);
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
		//if max_vel <= 0 then there is no max velocity
		if (this.max_vel > 0.0) {
			this.vel_x = (vel_x <= this.max_vel) ? this.vel_x : this.max_vel;
			this.vel_x = (vel_x >= -this.max_vel) ? this.vel_x : -this.max_vel;
		}
	}
	
	public void setVelY(double vel_y) {
		this.vel_y = vel_y;
		//if max_vel <= 0 then there is no max velocity
		if (this.max_vel > 0.0) {
			this.vel_y = (vel_y <= this.max_vel) ? this.vel_y : this.max_vel;
			this.vel_y = (vel_y >= -this.max_vel) ? this.vel_y : -this.max_vel;
		}
	}
	
	public void setVelRot(double vel_rot) {
		this.vel_rot = vel_rot;
	}
	
	public void setVelocity(double x, double y, double rot) {
		this.setVelX(x);
		this.setVelY(y);
		this.setVelRot(rot);
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
		if (friction < 0.0) {
			this.friction = 0.0;
		}
		else if (friction > 1.0) {
			this.friction = 1.0;
		}
		else {
			this.friction = friction;
		}
	}
	
	public double getBounce() {
		return this.bounce;
	}
	
	//friction should be a value between 0.0 and 1.0
	public void setBounce(double bounce) {
		if (bounce < 0.0) {
			this.bounce = 0.0;
		}
		else if (bounce > 1.0) {
			this.bounce = 1.0;
		}
		else {
			this.bounce = bounce;
		}
	}
	
	public double getMaxVelocity() {
		return this.max_vel;
	}
	
	public void setMaxVelocity(double max) {
		this.max_vel = max;
	}
	
	public int getCollisionMask() {
		return this.col_mask;
	}
	
	public void setCollisionMask(int mask) {
		this.col_mask = mask;
	}
	
	public int getCollisionType() {
		return this.col_type;
	}
	
	public void setCollisionType(int type) {
		this.col_type = type;
	}
	
	public boolean isBullet() {
		return this.bullet;
	}
	
	public void setBullet(boolean bullet) {
		this.bullet = bullet;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
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

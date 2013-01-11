package engine;

import java.util.BitSet;

import engine.util.*;

/*
 * NullEntityType class
 * Blank template for Entities
 */
public class NullEntityType implements EntityType {
	public static final NullEntityType instance = new NullEntityType();
	private static NullController controller = new NullController();
	
	//set the constructor to private so an instance of NullEntityType can't be created
	private NullEntityType() {}
	
	public String getName() {
		return "null_entity";
	}
	
	public String getModelName() {
		return "null";
	}
	
	public Controller createController() {
		return NullEntityType.controller;
	}
	
	//returns a plain Entity
	//other subclasses of EntityType can use this function
	//to create subclasses of Entity
	//ex: PlayerType creates Player entities
	public Entity createEntity() {
		return new Entity();
	}
	
	public boolean isDynamic() {
		return false;
	}
	
	public boolean isBullet() {
		return false;
	}
	
	public double getFriction() {
		return 0.0;
	}
	
	public double getBounce() {
		return 0.0;
	}
	
	public double getMaxVelocity() {
		return 0.0;
	}
	
	public int getCollisionMask() {
		//do not collide with anything!
		//mask: 0000 0000 0000 0000
		return 0x0000;
	}
	
	public int getCollisionType() {
		return 0x0001;
	}
}

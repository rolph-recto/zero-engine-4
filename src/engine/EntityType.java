//EntityType.java
//Rolph Recto

package engine;

import java.util.*;

/*
 * EntityType class
 * Template and factory for entities
 * Contains data hashmap default arguments
 * EntityType subclasses are NOT instantiated,
 * but rather used as static classes / Singletons
 */
public interface EntityType {
	public abstract String getName();
	
	//controller
	public abstract Controller createController();
	//NOTE:
	//if the controller is "hivemind" (i.e., one controller for many entities)
	//the subclass should implement that by
	//having an isntance of the controller as a static class member
	
	//creates an entity
	//subclasses override this method to create
	//instances of Entity subclasses
	public abstract Entity createEntity();
	
	//entity properties
	//is the entity dynamic (i.e., can it move?)
	public abstract boolean isDynamic();
	//does the entity move fast?
	//(this turns on ray casting)
	public abstract boolean isBullet();
	//constant of friction
	//this is a value from 1 to 0
	public abstract double getFriction();
	//sets the maximum  absolute value of the entity's velocity
	//does not include rotation
	public abstract double getMaxVelocity();
	//determines collision response of the entity
	//to other types of entities
	public abstract int getCollisionMask();
	//determines the collision type of the entity
	//the collision mask uses this to determine collision response
	public abstract int getCollisionType();
	/* KEY OF COLLISION TYPES
	 * 0000 0000 0000 0001 (0x0001) - NullEntityType
	 * 0000 0000 0000 0010 (0x0002) - Player
	 * 0000 0000 0000 0100 (0x0004) - Enemies
	 * 0000 0000 0000 1000 (0x0008) - Bullets
	 * 0000 0000 0001 0000 (0x0010) - Items
	 * 0000 0000 0010 0000 (0x0020) - Installations (turrets, etc.)
	 */
}

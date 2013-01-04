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
	//constant of friction
	//this is a value from 1 to 0
	public abstract double getFriction();
	
}

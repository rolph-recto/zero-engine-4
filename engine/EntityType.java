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
	public abstract Class<? extends Controller> getControllerClass();
	public abstract boolean isHivemind();
	
	//creates an entity
	//subclasses override this method to create
	//instances of Entity subclasses
	public abstract Entity createEntity();
	
	//returns the template model for entities of this EntityType
	public abstract Model getModel();
}

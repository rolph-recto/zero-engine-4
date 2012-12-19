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

public abstract class EntityType {
	protected String name = "NullEntityType";
	protected Class<? extends Controller> controller_class;
	protected boolean hivemind; //if true, Entities share the same controller
	protected EntityType instance; //singleton instance
	
	public String getName() {
		return this.name;
	}
	
	public Class<? extends Controller> getControllerClass() {
		return this.controller_class;
	}
	
	public boolean isHivemind() {
		return this.hivemind;
	}
	
	//creates an entity
	//subclasses override this method to create
	//instances of Entity subclasses
	public abstract Entity createEntity();
}

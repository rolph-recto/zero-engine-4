package engine;

import engine.util.*;

/*
 * NullEntityType class
 * Blank template for Entities
 */
public class NullEntityType implements EntityType {
	public static final NullEntityType instance = new NullEntityType();
	private static Model model;
	
	//set the constructor to private so an instance of NullEntityType can't be created
	private NullEntityType() {
		Shape s = new Circle(0.0);
		
		NullEntityType.model = new Model(s, null);
	}
	
	public String getName() {
		return "NullEntityType";
	}
	
	public Class<? extends Controller> getControllerClass() {
		return NullController.class;
	}
	
	public boolean isHivemind() {
		return true;
	}
	
	//returns a plain Entity
	//other subclasses of EntityType can use this function
	//to create subclasses of Entity
	//ex: PlayerType creates Player entities
	public Entity createEntity() {
		return new Entity();
	}
	
	public Model getModel() {
		return NullEntityType.model;
	}
	
	public boolean isDynamic() {
		return false;
	}
}

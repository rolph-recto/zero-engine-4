package engine;

/*
 * NullEntityType class
 * Blank template for Entities
 */
public class NullEntityType extends EntityType {	
	//set the constructor to private so an instance of NullEntityType can't be created
	private NullEntityType() {
		this.name = "NullEntityType";
		this.controller_class = NullController.class;
		this.hivemind = true;  //all entities who do nothing should share just one controller
	}

	//return the ONE instance of the object
	//no other instances can be created
	public EntityType getInstance() {
		if (this.instance == null) {
			this.instance = new NullEntityType();
		}
		return this.instance;
	}
	
	//returns a plain Entity
	//other subclasses of EntityType can use this function
	//to create subclasses of Entity
	//ex: PlayerType creates Player entities
	public Entity createEntity() {
		return new Entity();
	}

}

//HeavyType.java

package zero;

import engine.Entity;
import engine.EntityType;

class Heavy extends Player {
	 public Heavy() {
		 super();
		 this.weapon = new FastShotgun(this);
	 }
}

class HeavyType extends PlayerType {
	public static EntityType instance = new HeavyType();
	
	private HeavyType(){
		super();
	}
	
	public Entity createEntity() {
		return new Heavy();
	}
	
	public String getName() {
		return "heavy";
	}
	
	public String getModelName() {
		return "player";
	}
	
	public double getMaxVelocity(){
		return 6;//arbitrary max velocity for now
	}
}

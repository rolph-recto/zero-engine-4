//SniperType.java
package zero;

import engine.Entity;
import engine.EntityType;

class Ranger extends Player {
	//define health, weapon, power based on structure of player
	public Ranger(){
		super();
		this.weapon = new MachineGun(this);
	}
}

class RangerType extends PlayerType {
	public static EntityType instance = new RangerType();

	private RangerType(){
		super();
	}

	public Entity createEntity() {
		return new Ranger();
	}

	public String getName() {
		return "sniper";
	}

	public String getModelName() {
		return "player";
	}

	public double getMaxVelocity(){
		return 12;//arbitrary max velocity for now	
	}
	
	public int getMaxHealth() {
		return 100;
	}
}

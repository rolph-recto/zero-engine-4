//PlayerType.java

package zero;

import engine.Controller;
import engine.Entity;
import engine.EntityType;

class Player extends Entity {
	public Player() {}
}

class PlayerType implements EntityType {
	public static final PlayerType instance = new PlayerType();

	protected PlayerType() {}
	
	public String getName() {
		return "player";
	}
	
	public String getModelName() {
		return "player";
	}
	
	public Controller createController() {
		return new PlayerController();
	}
	
	public Entity createEntity() {
		return new Player();
	}
	
	public boolean isDynamic() {
		return true;
	}
	
	public boolean isBullet() {
		return false;
	}
	
	public double getFriction() {
		return 0.8;
	}
	
	public double getBounce() {
		return 0.0;
	}
	
	public double getMaxVelocity() {
		return 7.5;
	}
	
	public int getCollisionMask() {
		//collide with other players and enemies
		//mask: 0000 0000 0000 0110
		return 0x0006;
	}
	
	public int getCollisionType() {
		//player type
		return 0x0002;
	}
}

//PlayerType.java

package zero;

import engine.Controller;
import engine.Entity;
import engine.EntityType;

/* Player class
 * Base class for all player objects
 * TODO make Player class abstract
 */
abstract class Player extends Entity {
	protected int health;
	protected int kills;
	protected int deaths;
	protected boolean enabled;
	
	//weapon used by the player
	//since there is one weapon per class,
	//make the weapon immutable
	// TODO make Weapon attribute final
	protected Weapon weapon;
	
	protected Player() {
		this.enabled = true;
	}
	
	//getters and setters
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		//set the lower bound of health to 0
		this.health = (health < 0) ? 0 : health;
	}
	
	public void changeHealth(int health) {
		this.setHealth(this.health+health);
	}
	
	public int getKills() {
		return this.kills;
	}
	
	public void setKills(int kills) {
		//set lower bound to 0
		this.kills = (this.kills < 0) ? 0 : kills;
	}
	
	public void incrementKills() {
		this.setKills(this.kills+1);
	}
	
	public int getDeaths() {
		return this.deaths;
	}
	
	public void setDeaths(int deaths) {
		//set lower bound to 0
		this.deaths = (deaths < 0) ? 0 : deaths;
	}
	
	public void incrementDeaths() {
		this.setDeaths(this.deaths+1);
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	
	//don't make an accessor for weapon
	//because there is only one weapon per class
	public Weapon getWeapon() {
		return this.weapon;
	}
}

/* PlayerType class
 * EntityType of player
 */
abstract class PlayerType implements EntityType {
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
	
	abstract public Entity createEntity();
	
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

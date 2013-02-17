package zero;

import engine.Controller;
import engine.Entity;
import engine.EntityType;

/* Bullet class
 * A generic bullet
 */
class Bullet extends Entity {

	public Bullet() {}
	/*
	 * changes done by Brenden
	 * 
	 * because there will be more than one bullet type there should be a value given to the constructor to determine what type correct?
	 * 
	 * I am asuming the bullet type will be determined by the weapon, but for now it will just use a string to represent 
	 * the weapon instead of assuming there is a weapon class 
	 */

	protected double range; // distance bullet will travel before it sends a message to be destroyed (if it hasn't collided yet)
	protected double dmgMultiplier; // don't know if multipler will be only int values so for now it will be a double
	protected Weapon sourceWeapon; // no weapon class yet but will be set by the input given to the constructor method

	public Bullet(Weapon source) {
		this.sourceWeapon = source;
	}

	//setters and getters
	public void setRange(double range){
		this.range = range;
	}
	
	public double getRange(){
		return this.range;
	}

	public void setDmgMultiplyer(double mul) {
		this.dmgMultiplier = mul;
	}
	
	public double getDmgMultiplier(){
		return this.dmgMultiplier;
	}
}

public class BulletType implements EntityType {
	public static final BulletType instance = new BulletType();
	private static final BulletController controller = new BulletController();

	protected BulletType() {}
	
	public String getName() {
		return "bullet";
	}
	
	public String getModelName() {
		return "bullet";
	}
	
	public Controller createController() {
		return BulletType.controller;
	}
	
	public Entity createEntity() {
		return new Bullet();
	}
	
	public boolean isDynamic() {
		return true;
	}
	
	public boolean isBullet() {
		return true;
	}
	
	public double getFriction() {
		return 1.0;
	}
	
	public double getBounce() {
		return 0.25;
	}
	
	public double getMaxVelocity() {
		return -1.0;
	}
	
	public int getCollisionMask() {
		//collide with no entities
		//mask: 0000 0000 0000 0000
		return 0x0000;
	}
	
	public int getCollisionType() {
		//bullet type
		return 0x0008;
	}
}

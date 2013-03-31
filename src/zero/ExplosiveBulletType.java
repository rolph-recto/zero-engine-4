package zero;

import engine.Controller;
import engine.Entity;

/* ExplosiveBullet class
 * Bullets that explode (i.e., damage an area) on contact
 */
class ExplosiveBullet extends Bullet {
	//detonate after 60 frames (2sec)
	public static final int explosionTime = 5;
	//how fast the "shrapnel" bullets fire after the bullet explodes
	public static final double shrapnelSpeed = 25.0;
	//number of shrapnel bullets to fire from the explosion
	public static final double shrapnelNum = 18;
	
	 //time until bullet explodes
	private int explosionElapsed;
	
	public ExplosiveBullet() {
		this.explosionElapsed = ExplosiveBullet.explosionTime;
	}

	public int getExplosionElapsed() {
		return this.explosionElapsed;
	}

	public void setExplosionElapsed(int explosionElapsed) {
		this.explosionElapsed = explosionElapsed;
	}
	
	public void decrementExplosionElapsed(int dec) {
		this.setExplosionElapsed(this.explosionElapsed - dec);
	}
}

/* ExplosiveBulletType class
 * EntityType of explosive bullet
 * Explosive bullets shoot out a spray of bullets after
 * a certain time period or after colliding with an object/wall
 */
public class ExplosiveBulletType extends BulletType {
	public static final BulletType instance = new ExplosiveBulletType();
	private static final ExplosiveBulletController controller =
			new ExplosiveBulletController();
	
	@Override
	public String getName() {
		return "explosive_bullet";
	}
	
	@Override
	public String getModelName() {
		return "bullet";
	}
	
	@Override
	public Controller createController() {
		return ExplosiveBulletType.controller;
	}
	
	@Override
	public Entity createEntity() {
		return new ExplosiveBullet();
	}
}

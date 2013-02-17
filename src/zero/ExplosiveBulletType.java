package zero;

import engine.Controller;
import engine.Entity;

/* ExplosiveBullet class
 * Bullets that explode (i.e., damage an area) on contact
 */
class ExplosiveBullet extends Bullet {
	double radius; // the radius of the bullet's explosion
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setRadius(double r) {
		this.radius = r;
	}
}

/* ExplosiveBulletType class
 * EntityType of explosive bullet
 */
public class ExplosiveBulletType extends BulletType {
	public static final BulletType instance = new BouncyBulletType();
	private static final BouncyBulletController controller =
			new BouncyBulletController();
	
	@Override
	public String getName() {
		return "explosive_bullet";
	}
	
	@Override
	public String getModelName() {
		return "explosive_bullet";
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

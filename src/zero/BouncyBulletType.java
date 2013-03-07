package zero;

import engine.Controller;
import engine.Entity;

class BouncyBullet extends Bullet {
	public BouncyBullet() {}
}

public class BouncyBulletType extends BulletType {
	public static final BouncyBulletType instance = new BouncyBulletType();
	protected static final BouncyBulletController controller 
		= new BouncyBulletController();
	
	protected BouncyBulletType() {
		super();
	}

	@Override
	public String getName() {
		return "bouncy_bullet";
	}
	
	@Override
	public String getModelName() {
		return "bullet";
	}
	
	@Override
	public Controller createController() {
		return BouncyBulletType.controller;
	}
	
	@Override
	public Entity createEntity() {
		return new Bullet();
	}
	
	@Override
	public double getBounce() {
		return 1.0;
	}
}

package zero;

import engine.Controller;
import engine.Entity;

public class BouncyBulletType extends BulletType {
	public static final BulletType instance = new BouncyBulletType();
	private static final BouncyBulletController controller =
			new BouncyBulletController();
	
	protected BouncyBulletType() {
		super();
	}

	@Override
	public String getName() {
		return "bouncy_bullet";
	}
	
	@Override
	public String getModelName() {
		return "bouncy_bullet";
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

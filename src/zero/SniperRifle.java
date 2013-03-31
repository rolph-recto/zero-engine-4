package zero;

public class SniperRifle extends Weapon{
	protected SniperRifle(Player p) {
		super(p);
		this.setMaxAmmo(20);
		this.setAmmo(20);
		this.setClipSize(5);
		this.setClipAmmo(5);
		this.setNextBulletTime(6);
		this.setReloadTime(50);
		this.setAccuracy(1);
		this.setBulletSpeed(90.0);
		this.setBulletType("bullet");
	}
}

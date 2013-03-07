package zero;

public class MachineGun extends Weapon {
	protected MachineGun(Player p) {
		super(p);
		this.setMaxAmmo(90);
		this.setAmmo(90);
		this.setClipSize(30);
		this.setClipAmmo(30);
		this.setNextBulletTime(6);
		this.setReloadTime(30);
		this.setAccuracy(0.97);
		this.setBulletSpeed(90.0);
		this.setBulletType("bouncy_bullet");
	}
}

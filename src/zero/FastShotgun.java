package zero;


public class FastShotgun extends Shotgun {
	
	public FastShotgun(Player p) {
		super(p);
		this.setMaxAmmo(60);
		this.setClipSize(120);
		this.setAmmo(60);
		this.setClipAmmo(60);
		this.setReloadTime(25);
		this.setNextBulletTime(5);
		this.setAccuracy(0.90);
		this.setBulletSpeed(80.0);
		this.setBulletType("bullet");
		this.setBulletsPerShot(4);
	}
	
	//getters and setter
	//end of getters and setters
	

}

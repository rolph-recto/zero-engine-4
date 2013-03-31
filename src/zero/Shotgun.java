package zero;

import engine.Entity;
import engine.Level;
import engine.util.Vector2D;

public class Shotgun extends Weapon {
	protected int bulletsPerShot; //number of bullets to fire per shot
	
	public Shotgun(Player p) {
		super(p);
		this.setMaxAmmo(30);
		this.setClipSize(30);
		this.setAmmo(30);
		this.setClipAmmo(30);
		this.setReloadTime(90);
		this.setNextBulletTime(40);
		this.setAccuracy(0.92);
		this.setBulletSpeed(45.0);
		this.setBulletType("explosive_bullet");
		this.bulletsPerShot = 3;
	}
	
	//getters and setters
	public int getBulletsPerShot() {
		return this.bulletsPerShot;
	}
	
	public void setBulletsPerShot(int num) {
		this.bulletsPerShot = (num > 0) ? num : 1;
	}
	
	//end of getters and setters
	
	@Override
	public void fire(Level level, double angle) {		
		//only fire if the weapon is not waiting or reloading
		if (!this.waiting && !this.reloading
				&& this.clipAmmo >= this.bulletsPerShot) {
			
			//fire 3 bullets at once
			for (int i = 0; i < this.bulletsPerShot; i++) {
				double radians = Math.toRadians(this.calculateFiringAngle(angle));
				Vector2D bullet_pos = new Vector2D(Math.cos(radians),
						Math.sin(radians));
				bullet_pos.setMagnitude(50.0);
				
		        Entity bullet = level.createEntity(this.getBulletType(),
		        		this.player.getPosX()+bullet_pos.getX(),
		        		this.player.getPosY()+bullet_pos.getY());
		        
		        bullet_pos.setMagnitude(this.bulletSpeed);
		        bullet.setAcceleration(bullet_pos.getX(),
		        		bullet_pos.getY(), 0.0);
			}
			
			this.setClipAmmo(this.clipAmmo - this.bulletsPerShot);
			this.waiting = true;
		}
	}

}

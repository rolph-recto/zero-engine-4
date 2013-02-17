package zero;

import engine.Level;
import engine.util.Vector2D;

/* Weapon class
 * Contains data about the weapon used by players
 */
public class Weapon {
	//maximum amount of ammo in weapon
	protected int maxAmmo;
	//current amount of ammo
	protected int ammo;
	//amount of ammo that can be held in the clip
	protected int clipSize;
	//current amount of ammo in clip
	protected int clipAmmo;
	//determines how narrow is the spread of fire
	// 1.0 means perfect accuracy (no spread, straight line of fire)
	// 0.0 means no accuracy (bullet can be fired at any angle)
	protected double accuracy;
	//how many frames until the next bullet can be fired
	protected int nextBulletTime;
	//counter for next bullet
	protected int nextBulletCounter;
	//is the weapon waiting for the next bullet? (i.e., can't fire)
	protected boolean waiting;
	//how many frames to complete reloading
	protected int reloadTime;
	//counter for reload
	protected int reloadCounter;
	//is the weapon reloading? (i.e., can't fire)
	protected boolean reloading;
	//the player who owns this weapon
	protected Player player;
	
	public Weapon(Player p) {
		this.player = p;
	}
	
	//getters and setters
	
	public int getMaxAmmo() {
		return this.maxAmmo;
	}
	
	public void setMaxAmmo(int maxAmmo) {
		//make sure max ammo is at least 1
		this.maxAmmo = (maxAmmo < 1) ? 1 : maxAmmo;
	}
	
	public int getAmmo() {
		return this.ammo;
	}
	
	public void setAmmo(int ammo) {
		//make sure ammo is at least 0
		this.ammo = (ammo < 0) ? 0 : ammo;
	}
	
	public void decreaseAmmo(int usedAmmo) {
		this.setAmmo(this.ammo-usedAmmo);
	}
	
	public int getClipSize() {
		return this.clipSize;
	}
	
	public void setClipSize(int clipSize) {
		this.clipSize = clipSize;
	}
	
	public int getClipAmmo() {
		return this.clipAmmo;
	}
	
	public void setClipAmmo(int clipAmmo) {
		this.clipAmmo = (clipAmmo < 0 ) ? 0 : clipAmmo;
	}
	
	public void decreaseClipAmmo() {
		this.setClipAmmo(this.clipAmmo - 1);
	}
	
	public double getAccuracy() {
		return this.accuracy;
	}
	
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public int getNextBulletTime() {
		return this.nextBulletTime;
	}
	
	public void setNextBulletTime(int nextBulletTime) {
		this.nextBulletTime = nextBulletTime;
	}
	
	public int getNextBulletCounter() {
		return this.nextBulletCounter;
	}
	
	public boolean isWaiting() {
		return this.waiting;
	}
	
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	public int getReloadTime() {
		return this.reloadTime;
	}
	
	public void setReloadTime(int reloadTime) {
		this.reloadTime = reloadTime;
	}
	
	public int getReloadCounter() {
		return this.reloadCounter;
	}
	
	public boolean isReloading() {
		return this.reloading;
	}
	
	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	//end of getters and setters
	
	//reload the weapon
	protected void reload() {
		this.reloading = false;
		this.reloadCounter = 0;
		this.setClipAmmo((this.ammo < this.clipSize) ?
				this.ammo : this.clipSize);
		this.decreaseAmmo(this.clipSize);
	}
	
	//update one time step
	//sets reloading, etc.
	public void update() {
		//weapon is reloading
		if (this.reloading) {
			this.reloadCounter++;
			
			//weapon has reloaded
			if (this.reloadCounter >= this.reloadTime) {
				this.reload();
			}
		}
		
		//waiting to fire next bullet
		if (this.waiting) {
			this.nextBulletCounter++;
			
			//now the gun can fire
			if (this.nextBulletCounter >= this.nextBulletTime) {
				this.waiting = false;
				this.nextBulletCounter = 0;
			}
		}
	}
	
	//fire a bullet
	protected void expendBullet() {
		this.decreaseClipAmmo();
		this.waiting = true;
	}
	
	//fire bullets
	public void fire(Level level, double angle) {
		//only fire if the weapon is not waiting or reloading
		if (!this.waiting && !this.reloading && this.clipAmmo > 0) {
			Vector2D bullet_pos = new Vector2D(Math.cos(Math.toRadians(angle)),
					Math.sin(Math.toRadians(angle)));
			bullet_pos.setMagnitude(50.0);
			
	        long id = level.createEntity("bullet",
	        		this.player.getPosX()+bullet_pos.getX(),
	        		this.player.getPosY()+bullet_pos.getY());
	        
	        bullet_pos.setMagnitude(60.0);
	        level.getEntityById(id).setAcceleration(bullet_pos.getX(),
	        		bullet_pos.getY(), 0.0);
	        
	        this.expendBullet();
		}
	}
}

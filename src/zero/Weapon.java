package zero;

/* Weapon class
 * Contains data about the weapon used by players
 */
public abstract class Weapon {
	//determines how narrow is the spread of fire
	// 0.0 means perfect accuracy (no spread, straight line of fire)
	// 1.0 means no accuracy (bullet can be fired at any angle)
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
	
	public void setNextBulletCounter(int nextBulletCounter) {
		this.nextBulletCounter = nextBulletCounter;
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
	
	public void setReloadCounter(int reloadCounter) {
		this.reloadCounter = reloadCounter;
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
	
	
}

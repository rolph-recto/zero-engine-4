package zero;

import engine.Entity;
import engine.msgtype.EntityCollisionMessage;
import engine.util.Message;
import engine.util.Vector2D;

public class ExplosiveBulletController extends BulletController {
	
	private void explode(ExplosiveBullet b) {
		//the bullet dies
		b.setDead(true);
		//fire a radiating circle of bullets out of the exploding bullet
		for (int i = 0 ; i < ExplosiveBullet.shrapnelNum-1; i++) {
			double radians =
					Math.toRadians( (360.0/ExplosiveBullet.shrapnelNum) * i);
			Vector2D bullet_pos = new Vector2D(Math.cos(radians),
					Math.sin(radians));
			bullet_pos.setMagnitude(50.0);
			
	        Entity shrapnel = this.level.createEntity("bullet",
	        		b.getPosX()+bullet_pos.getX(),
	        		b.getPosY()+bullet_pos.getY());
	        
	        //little hack needed so that bullets won't be considered "dead"
	        //since any bullet that is stationary is considered dead
	        shrapnel.setVelocity(0.1, 0.1, 0.0);
	        
	        bullet_pos.setMagnitude(ExplosiveBullet.shrapnelSpeed);
	        shrapnel.setAcceleration(bullet_pos.getX(),
	        		bullet_pos.getY(), 0.0);
		}
	}
	
	//check if bullet needs to explosive
	@Override
	public void onMessage(Message msg) {
		switch (msg.getType()) {
		//if any of the bullets are not moving, they are dead
		case LEVEL_UPDATE:
			for (Entity e : this.entity_list) {
				ExplosiveBullet b = (ExplosiveBullet)e;
				b.decrementExplosionElapsed(1);
				
				//explode!
				if (b.getExplosionElapsed() == 0) {
					this.explode(b);
				}
			}
			break;
		//the bullet explodes when it collides with something
		case ENTITY_COLLIDE_ENTITY:
		case ENTITY_COLLIDE_WALL:
			EntityCollisionMessage colMsg = (EntityCollisionMessage)msg;
			this.explode( (ExplosiveBullet)colMsg.getEntity() );
		default:
			break;
		}
	}
}

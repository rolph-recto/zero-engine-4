package zero;

import engine.Controller;
import engine.Entity;
import engine.Level;
import engine.msgtype.EntityCollisionMessage;
import engine.util.Message;

public class BulletController extends Controller {
	public BulletController() {
		super();
	}
	
	public BulletController(Level l, Entity bullet) {
		super(l, bullet);
	}
	
	@Override
	public void onMessage(Message msg) {
		switch (msg.getType()) {
		//if any of the bullets are not moving, they are dead
		case LEVEL_UPDATE:
			for (Entity e : this.entity_list) {
				if (e.getVelX() == 0.0 && e.getVelY() == 0) {
					e.setDead(true);
				}
			}
			break;
		//if a bullet collides with a wall, the bullet is dead
		case ENTITY_COLLIDE_WALL:
			EntityCollisionMessage col_msg = (EntityCollisionMessage)msg;
			col_msg.getEntity().setDead(true);
			break;
		default:
			break;
		}
	}

}

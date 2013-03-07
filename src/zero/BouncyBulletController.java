package zero;

import engine.Entity;
import engine.Level;
import engine.msgtype.EntityCollisionMessage;
import engine.util.Message;

public class BouncyBulletController extends BulletController {
	public BouncyBulletController() {
		super();
	}
	
	public BouncyBulletController(Level l, Entity bullet) {
		super(l, bullet);
	}
	
	//this needs to be overriden because in BulletController the bullet
	//dies when it hits a wall, which should not happen
	//for BouncyBulletControler
	@Override
	public void onMessage(Message msg) {
		switch (msg.getType()) {
		//if any of the bullets are not moving, they are dead
		case LEVEL_UPDATE:
			for (Entity e : this.entity_list) {
				if (e.getVelX() == 0.0 && e.getVelY() == 0.0) {
					e.setDead(true);
				}
			}
			break;
		default:
			break;
		}
	}
}

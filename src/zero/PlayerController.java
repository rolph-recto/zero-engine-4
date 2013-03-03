package zero;

import java.awt.event.KeyEvent;

import engine.Controller;
import engine.Entity;
import engine.Level;
import engine.MsgType;
import engine.msgtype.EntityCollisionMessage;
import engine.msgtype.EntityMoveMessage;
import engine.msgtype.LevelMessage;
import engine.util.Message;

class PlayerController extends Controller {
	private Player player;
	
	public PlayerController() {
		super();
	}
	
	public PlayerController(Level l, Entity player) {
		super(l, player);
		this.player = (Player)player;
	}
	
	/* Override addEntity method
	 * set added entity as this.player
	 */
	@Override
	public boolean addEntity(Entity e) {
		boolean added = super.addEntity(e);
		
		if (added) {
			this.player = (Player)e;
			return true;
		}
		else return false;
	}
	
	public void onMessage(Message msg) {
		switch (msg.getType()) {
		case LEVEL_UPDATE:
			LevelMessage level_msg = (LevelMessage)msg;
			//update the weapon
			//needed for reload times, etc.
			this.player.getWeapon().update();
			
			if (this.player.isEnabled()) {
				//move the player
				boolean[] key_state = level_msg.getKeyState();
				double move_x, move_y;
				move_x = 0.0;
				move_y = 0.0;
				
				double force = 1.5;
				if (key_state[KeyEvent.VK_W]) {
					move_y = -force;		
				}
				else if (key_state[KeyEvent.VK_S]) {
					move_y = force;
				}
				if (key_state[KeyEvent.VK_A]) {
					move_x = -force;			
				}
				else if (key_state[KeyEvent.VK_D]) {
					move_x = force;
				}
				else if (key_state[KeyEvent.VK_R]) {
					this.player.getWeapon().startReload();
				}
	
				//change player orientation
				double move_rot = 0.0;
				int mouse_x = level_msg.getMouseX();
				int mouse_y = level_msg.getMouseY();
				double comp_x = mouse_x - this.player.getPosX();
				double comp_y = mouse_y - this.player.getPosY();
				//find unit vector (||v||cos(t)i, ||v||sin(t)jd), solve for theta
				double magnitude = Math.sqrt(Math.pow(comp_x, 2) + Math.pow(comp_y, 2));
				double cos = comp_x/magnitude;
				double angle = Math.toDegrees(Math.acos(cos));
				if (comp_y > 0) {
					angle = 360.0 - angle;
				}
				angle = (angle >= 360.0) ? 0 : angle;
				move_rot = angle - this.player.getRotation();
				
				EntityMoveMessage move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player,
						move_x, move_y, move_rot);				
				
				this.player.onMessage(move_msg);
				
				if (key_state[KeyEvent.VK_SPACE]) {
					this.player.getWeapon().fire(this.level, 360.0 - angle);
				}
			}
			break;
		case ENTITY_COLLIDE_ENTITY:
			EntityCollisionMessage col_msg = (EntityCollisionMessage)msg;
			Entity e2 = col_msg.getEntity2();
			if (e2.getType().getName().equals("bullet")) {
				e2.setDead(true);
			}
			break;
		default:
			break;
		}
	}
}
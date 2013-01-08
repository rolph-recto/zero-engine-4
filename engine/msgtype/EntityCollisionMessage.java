package engine.msgtype;

import engine.Entity;
import engine.MsgType;

public class EntityCollisionMessage extends EntityMessage {
	private int wall_x, wall_y;
	private Entity entity2;

	public EntityCollisionMessage(Entity e, int x, int y) {
		super(MsgType.ENTITY_COLLIDE_WALL, e);
		this.wall_x = x;
		this.wall_y = y;
	}
	
	public EntityCollisionMessage(Entity e, Entity e2) {
		super(MsgType.ENTITY_COLLIDE_ENTITY, e);
		this.entity2 = e2;
	}
	
	public int getWallX() {
		return this.wall_x;
	}
	
	public int getWallY() {
		return this.wall_y;
	}
	
	public void setWallPosition(int x, int y) {
		this.wall_x = x;
		this.wall_y = y;
	}
	
	public Entity getEntity2() {
		return this.entity2;
	}
	
	public void setEntity2(Entity e) {
		this.entity2 = e;
	}
}

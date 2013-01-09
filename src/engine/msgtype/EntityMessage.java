package engine.msgtype;

import engine.*;
import engine.util.*;

/*
 * EntityMessage class
 * messages concerning an entity
 */
public class EntityMessage extends Message {
	private Entity entity;
	
	public EntityMessage(MsgType type, Entity e) throws IllegalArgumentException {
		super(type);
		if (!type.inRange(MsgType.MSG_ENTITY)) {
			throw new IllegalArgumentException("Message type must be in range MSG_ENTITY");
		}
		this.setEntity(e);
	}
	
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
}

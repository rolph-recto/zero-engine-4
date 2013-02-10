//EntityMoveMessage.java

package engine.msgtype;

import engine.*;
import engine.util.*;

/*
 * EntityMoveMessage
 * message that represents ENTITY_MOVE and ENTITY_COMMAND_MOVE
 */
public class EntityMoveMessage extends EntityMessage {
	private double x, y, rot;
	public EntityMoveMessage(MsgType type, Entity e, double x, double y, double rot) {
		super(type, e);
		if ((type != MsgType.ENTITY_MOVE) && (type != MsgType.ENTITY_COMMAND_MOVE)) {
			throw new IllegalArgumentException("Message type must be ENTITY_MOVE or ENTITY_COMMAND_MOVE");
		}
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
	
	//only instantiate getters, not setters
	//we don't want the message values changed once it's been sent
	public double getX() { return this.x; }
	
	public double getY() { return this.y; }
	
	public double getRotation() { return this.rot; }
}

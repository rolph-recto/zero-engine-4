//Message.java
//Rolph Recto

package engine.util;

import java.util.*;
import engine.MsgType;

/*
 * Message class
 * Base class for all message objects
 */
public class Message {
	protected MsgType type;
	
	public Message(MsgType type) {
		this.setType(type);
	}

	public MsgType getType() {
		return this.type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}
}
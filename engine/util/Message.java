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
	protected HashMap<String, Object> data;
	
	public Message() {
		data = new HashMap<String, Object> ();
	}
	
	public Message(MsgType type) {
		this();
		this.setType(type);
	}
	
	public Message(MsgType type, HashMap<String, Object> data) {
		this.setType(type);
		this.setData(data);
	}

	public MsgType getType() {
		return this.type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}

	public HashMap<String, Object> getData() {
		return this.data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
}
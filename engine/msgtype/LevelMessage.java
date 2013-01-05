package engine.msgtype;

import engine.*;
import engine.util.*;

/*
 * LevelMessage class
 * messages concerning a level
 */
public class LevelMessage extends Message {
	private Level level;
	private boolean[] key_state;
	
	public LevelMessage(MsgType type, Level l, boolean[] key_state) throws IllegalArgumentException {
		super(type);
		if (!type.inRange(MsgType.MSG_LEVEL)) {
			throw new IllegalArgumentException("Message type must be in range MSG_LEVEL");
		}
		this.setLevel(l);
	}
	
	public void setLevel(Level l) {
		this.level = l;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public void setKeyState(boolean[] key) {
		this.key_state = key;
	}
	
	public boolean[] getKeyState() {
		return this.key_state;
	}
}

//LevelMessage.java

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
	private int mouse_x, mouse_y;
	
	public LevelMessage(MsgType type, Level l, boolean[] key_state, int mouse_x, int mouse_y) 
	throws IllegalArgumentException {
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
	
	//note that mouse position is relative to
	//level coords, NOT screen coords
	public void setMousePosition(int x, int y) {
		this.mouse_x = x;
		this.mouse_y = y;
	}
	
	public int getMouseX() {
		return this.mouse_x;
	}
	
	public int getMouseY() {
		return this.mouse_y;
	}
}

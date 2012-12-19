//MsgType.java
//Rolph Recto

package engine;

import java.util.BitSet;

import engine.util.Message;

/*
 * Enum MsgType
 * Enumerates all possible message types used in the engine
 */
public enum MsgType {
	MSG_NONE				(-1),
	MSG_ALL					(0, MsgType.NUM_MSG-1),
	ENTITY_CREATE			(0),
	ENTITY_DESTROY			(1),
	ENTITY_MOVE				(2),
	ENTITY_COLLIDE			(3),
	ENTITY_COMMAND_MOVE		(4),
	MSG_ENTITY				(0, 5),
	LEVEL_UPDATE			(5),
	MSG_LEVEL				(5, 6);
	
	public final static int NUM_MSG = 20;
	private final BitSet filter;
	
	MsgType() {
		this.filter = new BitSet(MsgType.NUM_MSG);
		this.filter.clear();
	}
	
	MsgType(int index) {
		this();
		//if index < 0, it is MSG_NONE
		if (index >= 0) {
			this.filter.set(index, true);	
		}
	}
	
	MsgType(int start, int end) {
		this();
		this.filter.set(start, end, true);
	}
	
	public BitSet getFilter() {
		return this.filter;
	}
	
	public boolean inRange(MsgType range) {
		BitSet b = (BitSet)range.getFilter().clone();
		b.and(this.getFilter());
		return !b.isEmpty();
	}
}




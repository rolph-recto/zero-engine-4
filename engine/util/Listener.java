//Listener.java
//Rolph Recto

package engine.util;


/*
 * Listener interface
 * Implemented by classes that receive messages
 */
public interface Listener {
	public abstract void onMessage(Message msg);
}

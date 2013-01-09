//NullController.java
//Rolph Recto

package engine;

import engine.util.*;

/*
 * NullController class
 * As the name implies, this controller does nothing.
 */
public class NullController extends Controller {
	public NullController() {
		super();
	}
	
	public NullController(long id, Level l) {
		super(id, l);
	}

	public NullController(long id, Level l, Entity e) {
		super(id, l, e);
	}

	//Do nothing when the controller gets a message
	public void onMessage(Message msg) {}
}

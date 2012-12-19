//Dispatcher.java
//Rolph Recto

package engine.util;

import java.util.*;

import engine.MsgType;


/*
 * Subscription
 * Ties a subscriber (Listener) with a message filter
 * to which it is listening
 */
final class Subscription {
	private Listener subscriber;
	private BitSet msg_filter;
	
	Subscription(Listener subscriber, BitSet filter) {
		this.subscriber = subscriber;
		this.msg_filter = filter;
	}
	
	Subscription(Listener subscriber, MsgType msg_type) {
		this.subscriber = subscriber;
		this.msg_filter = msg_type.getFilter();
	}

	public Listener getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Listener subscriber) {
		this.subscriber = subscriber;
	}

	public BitSet getMsgFilter() {
		return msg_filter;
	}

	public void setMsgFilter(BitSet msg_filter) {
		this.msg_filter = msg_filter;
	}
	
}

/*
 * Dispatcher class
 * Broadcasts messages to subscribers
 */
public class Dispatcher {
	protected ArrayList<Subscription> sub_list;
	
	public Dispatcher() {
		this.sub_list = new ArrayList<Subscription> ();
	}
	
	//Add a new subscriber
	public void addSubscriber(Listener subscriber, MsgType msg_type) {
		this.addSubscriber(subscriber, msg_type.getFilter());
	}
	
	//Add a new subscriber
	public void addSubscriber(Listener subscriber, BitSet filter) {
		Subscription s = new Subscription(subscriber, filter);
		this.sub_list.add(s);
	}
	
	//Remove an existing subscriber
	public void removeSubscriber(Listener subscriber) {
		for (Subscription s : this.sub_list) {
			if (s.getSubscriber() == subscriber) {
				this.sub_list.remove(s);
				break;
			}
		}
	}
	
	//Sends a message to all subscribers with the correct filter
	public void broadcast(Message m) {
		for (Subscription s : this.sub_list) {
			BitSet s_filter = (BitSet) s.getMsgFilter().clone();
			s_filter.and(m.getType().getFilter());
			
			//there is a match in the filter; send the message!
			if (!s_filter.isEmpty()) {
				s.getSubscriber().onMessage(m);
			}
		}
	}
}

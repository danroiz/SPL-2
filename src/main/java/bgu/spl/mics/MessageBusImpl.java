package bgu.spl.mics;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	// *check if maps need to be concurrent maps*
	private final ConcurrentHashMap<Event, Future> eventToFutureMap; // Each event has a specific Future object related to him.

	// TO DO: CONSIDER CHANGING TO BLOCKING QUEUE
	private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServiceToMsgQueueMap; // Each Micro Service has a messages queue

	private final ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<MicroService>> eventToMicroServicesQueueMap; // Each type of event has matching subscribers queue
	private final ConcurrentHashMap<Class<? extends Broadcast>, Vector<MicroService>> broadcastToMicroServiceQueueMap; // Each type of broadcast has matching subscribers list

	// TO DO: Change it to Vector
	private final ConcurrentHashMap<MicroService, Vector<Class<? extends Event>>> microServiceToEvent; // given a micro-service, get all the types of events it registered to.
	private final ConcurrentHashMap<MicroService, Vector<Class<? extends Broadcast>>> microServiceToBroadcast; // given a micro-service, get all the types of broadcasts it registered to.

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	public static MessageBusImpl getInstance(){
		return MessageBusHolder.instance;
	}

	private MessageBusImpl(){
		eventToFutureMap = new ConcurrentHashMap<>();
		microServiceToMsgQueueMap = new ConcurrentHashMap<>();
		eventToMicroServicesQueueMap = new ConcurrentHashMap<>();
		broadcastToMicroServiceQueueMap = new ConcurrentHashMap<>();
		microServiceToEvent = new ConcurrentHashMap<>();
		microServiceToBroadcast = new ConcurrentHashMap<>();
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// check if m is registered
		// check if already subscribed to that type of event
		if (eventToMicroServicesQueueMap.get(type) == null) // first micro service to subscribe for this type of event
			eventToMicroServicesQueueMap.put(type,new ConcurrentLinkedQueue<>()); // create new round robin queue for this type

		// Maybe to synchronize eventToMicroServicesQueueMap.get(type) because what if add m
		// in the middle of another ms is polled out of the queue in round robin
		synchronized (eventToMicroServicesQueueMap.get(type)) { // round robin sync
			eventToMicroServicesQueueMap.get(type).add(m); // add the micro service to the queue
		}
		microServiceToEvent.get(m).add(type); // add the type to the list of types which m subscribed to

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (broadcastToMicroServiceQueueMap.get(type) == null) // first micro service to subscribe for this type of broadcast
			broadcastToMicroServiceQueueMap.putIfAbsent(type,new Vector<>()); // create new list for this type
		synchronized (broadcastToMicroServiceQueueMap.get(type)) {
			microServiceToBroadcast.get(m).add(type); // add the type to the list of types which m subscribed to
			broadcastToMicroServiceQueueMap.get(type).add(m); // add the micro service to the list
		}

    }

	@Override
	public <T> void complete(Event<T> e, T result) {
		if (eventToFutureMap.containsKey(e)) {
			eventToFutureMap.get(e).resolve(result);
			eventToFutureMap.remove(e);
		}
		else // DEBUG PURPOSES
			System.out.println("didn't found matching future for event: " + e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Vector<MicroService> subscribers = broadcastToMicroServiceQueueMap.get(b.getClass());
		if (subscribers != null) {
			synchronized (broadcastToMicroServiceQueueMap.get(b.getClass())) {
				for (Iterator<MicroService> iterator = broadcastToMicroServiceQueueMap.get(b.getClass()).iterator(); iterator.hasNext(); ) {
					MicroService value = iterator.next();
					microServiceToMsgQueueMap.get(value).add(b);
				}
//				for (MicroService ms : broadcastToMicroServiceQueueMap.get(b.getClass())) { // check if need to be synchronized
//					//synchronized (microServiceToMsgQueueMap.get(ms)) {
//						microServiceToMsgQueueMap.get(ms).add(b);
				//}
				//}
			}
		}
	}

	
//	//@Override
//	public <T> Future<T> sendEvent2(Event<T> e) {
//		// check if theres a subscriber for this type
//		// if there is, take the next subscriber by robinhood
//		// 		add the event to that subscriber messgage queue
//		// 		put back the subs to the robinhood
//		//		return the future
//		// else return null
//		Future<T> future = null;
//		ConcurrentLinkedQueue<MicroService> subscribersQueue = eventToMicroServicesQueueMap.get(e.getClass()); // queue of the micro-services that subscribed to this type of event
//		if (subscribersQueue != null) { // there's such queue
//			synchronized (eventToMicroServicesQueueMap.get(e.getClass())) { // first thread to get to round-robin queue, no other thread will mess with that queue until he finish
//				MicroService ms = subscribersQueue.poll(); // get next micro-service in the queue (by round-robin)
//				if (ms != null) { // there's a subscriber in the queue @INV: subscribersQueue never empty
//					boolean successAdd = microServiceToMsgQueueMap.get(ms).offer(e); // add that event the proper micro-service message queue
//					if (!successAdd){ // DEBUG PURPOSES
//						System.out.println("WTF WHY FAILED TO ADD EVENT: " + e + " TO MICROSERVICE:" + ms);
//					}
//					subscribersQueue.add(ms); // add back the micro-service to the subscriber-queue (by round-robin)
//					future = new Future<T>();
//					eventToFutureMap.put(e, future);
//				}
//			}
//		}
//		return future;
//	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// check if theres a subscriber for this type
		// if there is, take the next subscriber by robinhood
		// 		add the event to that subscriber messgage queue
		// 		put back the subs to the robinhood
		//		return the future
		// else return null
		Future<T> future = null;
		MicroService nextToReceiveEvent = null;
		ConcurrentLinkedQueue<MicroService> subscribersQueue = eventToMicroServicesQueueMap.get(e.getClass()); // queue of the micro-services that subscribed to this type of event
		if (subscribersQueue != null) { // there's such queue - if no means no one is subscribe to this type of event
			synchronized (eventToMicroServicesQueueMap.get(e.getClass())) { // first thread to get to round-robin queue, no other thread will mess with that queue until he finish
				nextToReceiveEvent = subscribersQueue.poll(); // get next micro-service in the queue (by round-robin)
				if (nextToReceiveEvent == null) // queue is empty
					return null; // no micro-service to receive the event, (the sender will handle properly)
				subscribersQueue.add(nextToReceiveEvent); // add back the micro-service to the subscriber-queue (by round-robin)
			}

			if (microServiceToMsgQueueMap.get(nextToReceiveEvent) != null){ // ad
				future = new Future<T>();
				eventToFutureMap.put(e, future);
				if (!microServiceToMsgQueueMap.get(nextToReceiveEvent).offer(e))
					System.out.println("WTF WHY FAILED TO ADD EVENT: " + e + " TO MICROSERVICE:" + nextToReceiveEvent.getName()); // DEBUG PURPOSES
			}
			else // DEBUG PURPOSES
				System.out.println("WTF WHY FAILED TO GET THE MESSAGE QUEUE OF " + nextToReceiveEvent.getName());
		}
		return future;
	}


	@Override
	public void register(MicroService m) {
		// check if already registered
		microServiceToEvent.putIfAbsent(m,new Vector<>());
		microServiceToBroadcast.putIfAbsent(m,new Vector<>());
		microServiceToMsgQueueMap.putIfAbsent(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) { // TO DO: might need to be synchronized
		// delete m from round robin queue and from broadcast map
		// if after unregister the queue of subscribers for some type of event is empty --> delete the queue

		Vector<Class<? extends Event>> eventTypes  = microServiceToEvent.get(m);
		Vector<Class<? extends Broadcast>> broadcastsTypes = microServiceToBroadcast.get(m);
		for (Class<? extends Event> type: eventTypes )
			synchronized (eventToMicroServicesQueueMap.get(type)){
				eventToMicroServicesQueueMap.get(type).remove(m);
			}
		for (Class<? extends Broadcast> type: broadcastsTypes)
			synchronized (broadcastToMicroServiceQueueMap.get(type)) {
				broadcastToMicroServiceQueueMap.get(type).remove(m);
			}
		microServiceToMsgQueueMap.remove(m); // what to do if m has messages in its queue and it unregistered itself?
		microServiceToBroadcast.remove(m);
		microServiceToEvent.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		//synchronized (microServiceToMsgQueueMap.get(m)) {
			return microServiceToMsgQueueMap.get(m).take(); // blocking method until new message is received
		//}
	}
}

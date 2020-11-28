package bgu.spl.mics;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	// *check if maps need to be concurrent maps*
	private ConcurrentHashMap<Event<?>, Future> eventToFutureMap; // Each event has a specific Future object related to him.

	// TO DO: CONSIDER CHANGING TO BLOCKING QUEUE
	private ConcurrentHashMap<MicroService,ConcurrentLinkedQueue<Message>> microServiceToMsgQueueMap; // Each Micro Service has a messages queue

	private ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<MicroService>> eventToMicroServicesQueueMap; // Each type of event has matching subscribers queue
	private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<MicroService>> broadcastToMicroServiceQueueMap; // Each type of broadcast has matching subscribers list

	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Class<? extends Event>>> microServiceToEvent; // given a micro-service, get all the types of events it registered to.
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Class<? extends Broadcast>>> microServiceToBroadcast; // given a micro-service, get all the types of broadcasts it registered to.

	private static MessageBusImpl messageBus = null;

	private MessageBusImpl(){
		eventToFutureMap = new ConcurrentHashMap<>();
		microServiceToMsgQueueMap = new ConcurrentHashMap<>();
		eventToMicroServicesQueueMap = new ConcurrentHashMap<>();
		broadcastToMicroServiceQueueMap = new ConcurrentHashMap<>();
		microServiceToEvent = new ConcurrentHashMap<>();
		microServiceToBroadcast = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance(){
		if (messageBus == null)
			messageBus = new MessageBusImpl();
		return messageBus;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// check if m is registered
		// check if already subscribed to that type of event
		if (eventToMicroServicesQueueMap.get(type) == null) // first micro service to subscribe for this type of event
			eventToMicroServicesQueueMap.put(type,new ConcurrentLinkedQueue<>()); // create new round robin queue for this type
		eventToMicroServicesQueueMap.get(type).add(m); // add the micro service to the queue
		microServiceToEvent.get(m).add(type); // add the type to the list of types which m subscribed to

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (broadcastToMicroServiceQueueMap.get(type) == null) // first micro service to subscribe for this type of broadcast
			broadcastToMicroServiceQueueMap.put(type,new ConcurrentLinkedQueue<>()); // create new list for this type
		broadcastToMicroServiceQueueMap.get(type).add(m); // add the micro service to the list
		microServiceToBroadcast.get(m).add(type); // add the type to the list of types which m subscribed to
    }

	@Override
	public <T> void complete(Event<T> e, T result) {
		if (eventToFutureMap.containsKey(e))
			eventToFutureMap.get(e).resolve(result);
		else // debug purpose
			System.out.println("didn't found matching future for event: " + e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<MicroService> subscribersQueue = broadcastToMicroServiceQueueMap.get(b.getClass());
		if (subscribersQueue != null){
			for(MicroService ms : subscribersQueue){ // check if need to be synchronized
				microServiceToMsgQueueMap.get(ms).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// check if theres a subscriber for this type
		// if there is, take the next subscriber by robinhood
		// 		add the event to that subscriber messgage queue
		// 		put back the subs to the robinhood
		//		return the future
		// else return null
		Future<T> future = null;
		ConcurrentLinkedQueue<MicroService> subscribersQueue = eventToMicroServicesQueueMap.get(e.getClass()); // queue of the micro-services that subscribed to this type of event
		if (subscribersQueue != null) { // there's such queue
			synchronized (subscribersQueue) { // first thread to get to round-robin queue, no other thread will mess with that queue until he finish
				MicroService ms = subscribersQueue.poll(); // get next micro-service in the queue (by round-robin)
				if (ms != null) { // there's a subscriber in the queue @INV: subscribersQueue never empty
					microServiceToMsgQueueMap.get(ms).add(e); // add that event the proper micro-service message queue
					subscribersQueue.add(ms); // add back the micro-service to the subscriber-queue (by round-robin)
					future = new Future<T>();
					eventToFutureMap.put(e, future);
				}
			}
		}
		return future;
	}

	@Override
	public void register(MicroService m) {
		// check if already registered
		microServiceToEvent.putIfAbsent(m,new ConcurrentLinkedQueue<>());
		microServiceToBroadcast.putIfAbsent(m,new ConcurrentLinkedQueue<>());
		microServiceToMsgQueueMap.putIfAbsent(m,new ConcurrentLinkedQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		// delete m from round robin queue and from broadcast map
		// if after unregister the queue of subscribers for some type of event is empty --> delete the queue

		ConcurrentLinkedQueue<Class<? extends Event>> queueEvents = microServiceToEvent.get(m);
		ConcurrentLinkedQueue<Class<? extends Broadcast>> queueBroadcasts = microServiceToBroadcast.get(m);
		for (Class<? extends Event> element: queueEvents)
			eventToMicroServicesQueueMap.get(element).remove(m);
		for (Class<? extends Broadcast> element: queueBroadcasts)
			broadcastToMicroServiceQueueMap.get(element).remove(m);
		microServiceToMsgQueueMap.remove(m); // what to do if m has messages in its queue and it unregistered itself?
		microServiceToBroadcast.remove(m);
		microServiceToEvent.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return microServiceToMsgQueueMap.get(m).poll();
	}
}

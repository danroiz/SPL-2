package bgu.spl.mics;
import java.util.HashMap;
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

	private final HashMap<Event, Future> eventToFutureMap; // Each event has a specific Future object related to him.
	private final ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServiceToMsgQueueMap; // Each Micro Service has a messages queue
	private final ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<MicroService>> eventToMicroServicesQueueMap; // Each type of event has matching subscribers queue
	private final ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<MicroService>> broadcastToMicroServiceQueueMap; // Each type of broadcast has matching subscribers list
	private final ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Class<? extends Event>>> microServiceToEvent; // given a micro-service, get all the types of events it registered to.
	private final ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Class<? extends Broadcast>>> microServiceToBroadcast; // given a micro-service, get all the types of broadcasts it registered to.

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	public static MessageBusImpl getInstance(){
		return MessageBusHolder.instance;
	}

	private MessageBusImpl(){
		eventToFutureMap = new HashMap<>();
		microServiceToMsgQueueMap = new ConcurrentHashMap<>();
		eventToMicroServicesQueueMap = new ConcurrentHashMap<>();
		broadcastToMicroServiceQueueMap = new ConcurrentHashMap<>();
		microServiceToEvent = new ConcurrentHashMap<>();
		microServiceToBroadcast = new ConcurrentHashMap<>();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventToMicroServicesQueueMap.putIfAbsent(type,new ConcurrentLinkedQueue<>()); // if absent create new round robin queue for this type
		eventToMicroServicesQueueMap.get(type).add(m); // add the micro service to the queue of micro-services subscribed to type event
		microServiceToEvent.get(m).add(type); // add the event type to the list of types which m subscribed to
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcastToMicroServiceQueueMap.putIfAbsent(type,new ConcurrentLinkedQueue<>()); // create new list for this type if absent
		microServiceToBroadcast.get(m).add(type); // add the type to the list of types which m subscribed to
		broadcastToMicroServiceQueueMap.get(type).add(m); // add the micro service to the list
    }

	@Override
	public <T> void complete(Event<T> e, T result) {
		if (eventToFutureMap.containsKey(e)) {
			eventToFutureMap.get(e).resolve(result); // getting future related to event e and resolve it to result
			eventToFutureMap.remove(e);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (broadcastToMicroServiceQueueMap.get(b.getClass()) != null) // there's micro-service subscribed to b type of broadcast
			for (MicroService microService : broadcastToMicroServiceQueueMap.get(b.getClass())) // adding broadcast to each micro-service in the concurrent list
				microServiceToMsgQueueMap.get(microService).add(b);
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = null;
		MicroService nextToReceiveEventMicroService;
		if (eventToMicroServicesQueueMap.get(e.getClass()) != null) { // there's such queue - if no means no one is subscribe to this type of event
			nextToReceiveEventMicroService = getNextEventSubscriber(e);
			future = addEventToMicroService(nextToReceiveEventMicroService,e);
		}
		return future;
	}

	// get next subscriber of the event type (by round robin)
	private <T> MicroService getNextEventSubscriber (Event<T> e) {
		synchronized (eventToMicroServicesQueueMap.get(e.getClass())) { // first thread to get to round-robin queue, no other thread will mess with that queue until he finish
			ConcurrentLinkedQueue<MicroService> subscribersQueue;
			subscribersQueue = eventToMicroServicesQueueMap.get(e.getClass());
			MicroService nextToReceiveEvent = subscribersQueue.poll(); // get next micro-service in the queue (by round-robin)
			if (nextToReceiveEvent != null)
				subscribersQueue.add(nextToReceiveEvent); // add back the micro-service to the subscriber-queue (by round-robin)
			return nextToReceiveEvent;
		}
	}

	// add the event to nexToToReceive event's queue
	private <T> Future<T> addEventToMicroService (MicroService nextToReceiveEvent, Event<T> e) {
		Future<T> future = null;
		if ((nextToReceiveEvent != null) && (microServiceToMsgQueueMap.get(nextToReceiveEvent) != null)) { // ad
			future = new Future<>();
			eventToFutureMap.put(e, future);
			microServiceToMsgQueueMap.get(nextToReceiveEvent).offer(e);
		}
		return future;
	}

	@Override
	public void register(MicroService m) {
		microServiceToEvent.putIfAbsent(m,new ConcurrentLinkedQueue<>());
		microServiceToBroadcast.putIfAbsent(m,new ConcurrentLinkedQueue<>());
		microServiceToMsgQueueMap.putIfAbsent(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		for (Class<? extends Event> eventType: microServiceToEvent.get(m) ) // each event type that m is subscribed to
			eventToMicroServicesQueueMap.get(eventType).remove(m); // remove m from the type's micro-services queue
		for (Class<? extends Broadcast> broadcastType: microServiceToBroadcast.get(m)) // each broadcast type that m is subscribed to
			broadcastToMicroServiceQueueMap.get(broadcastType).remove(m); // remove m from the type's micro-services queue
		microServiceToMsgQueueMap.remove(m);
		microServiceToBroadcast.remove(m);
		microServiceToEvent.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return microServiceToMsgQueueMap.get(m).take(); // blocking method until new message is received
	}
}

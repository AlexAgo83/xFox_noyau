package com.aura.base.manager.event;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.event.AbstractEvent;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.thread.EventDispatchThread;
import com.aura.base.utils.AuraLogger;

public class AbstractEventManager<A extends Aura> extends AbstractAuraManager<A> {
	private final EventCM<A> containerManager;
	public EventCM<A> getContainerManager() {
		return containerManager;
	}
	
	private List<AbstractEvent> events;
	private synchronized List<AbstractEvent> getEvents() {
		if (events == null)
			events = new ArrayList<AbstractEvent>();
		return events;
	}
	
	public void addEvent(AbstractEvent e) {
		getEvents().add(e);
		if (!e.isSilent())
			AuraLogger.finest(getAura().getSide(), 
				"Enregistrement de l'event [" 
					+ getAura().getEvent(e.getId()) 
					+ "].");
	}
	public void removeEvent(AbstractEvent e) {
		getEvents().remove(e);
	}
	public AbstractEvent[] getAll() {
		return getEvents().toArray(new AbstractEvent[0]);
	}
	
	private EventDispatchThread dispatchThread;
	public EventDispatchThread getDispatchThread() {
		if (dispatchThread == null) {
			dispatchThread = new EventDispatchThread(getAura(), this);
		}
		return dispatchThread;
	}
	
	public AbstractEventManager(A aura) {
		super(aura);
		this.containerManager = new EventCM<A>(aura);
		getDispatchThread();
	}
	@Override
	public boolean load() {
		try {
			containerManager.load();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getAura().getSide());
			return false;
		}
		getDispatchThread().start();
		return true;
	}
}
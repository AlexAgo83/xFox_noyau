package com.aura.base.thread;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.manager.event.AbstractEventManager;
import com.aura.base.manager.event.EventCE;
import com.aura.base.utils.AuraLogger;

public class EventDispatchThread extends AuraThread {
	@SuppressWarnings("rawtypes")
	private final AbstractEventManager eventManager;
	
	private List<AuraEventListener> listeners;
	private synchronized List<AuraEventListener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<AuraEventListener>();
		}
		return listeners;
	}
	public AuraEventListener[] getListenerTab() {
		return getListeners().toArray(new AuraEventListener[0]);
	}
	
	public void addListener(AuraEventListener listener) {
		getListeners().add(listener);
	}
	public void removeListener(AuraEventListener listener) {
		getListeners().remove(listener);
	}
	public void clearListener() {
		getListeners().clear();
	}
	
	@SuppressWarnings("rawtypes")
	public EventDispatchThread(Aura aura, AbstractEventManager eventManager) {
		super(aura, "EventDispatchThread", 10);
		this.eventManager = eventManager;
	}
	@Override
	public boolean condition() {
		return eventManager.getAura().isRunning();
	}
	@Override
	public void doOnClose() {
		getListeners().clear();
	}
	@Override
	public void action() {
		AbstractEvent[] aes = eventManager.getAll();
		for (AbstractEvent ae: aes) {
			AuraEventListener[] listeners = getListenerTab();
			for (AuraEventListener e: listeners) {
				for (EventCE c: e.getContainer()) {
					if (ae != null && ae.getId() == c.getId()) {
						if (!ae.isSilent())
							AuraLogger.finest(getMainAura().getSide(), "Listener trouvé pour [" 
								+ ae.getId() + ":" 
								+ c.getLabel()+"].");
						e.execute(ae);
					}
				}
			}
			eventManager.removeEvent(ae);
		}
	}
}
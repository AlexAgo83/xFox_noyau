package com.aura.base.event;

import com.aura.base.manager.event.EventCE;

public abstract class AuraEventListener {
	private EventCE[] container;
	public EventCE[] getContainer() {
		return container;
	}
	
	private int count;
	public int getCount() {
		return count;
	}
	
	public AuraEventListener(EventCE... container) {
		this.container = container;
		this.count = 0;
	}
	
	public void execute(AbstractEvent event) {
		implExecute(event);
		this.count += 1;
	}
	
	protected abstract void implExecute(AbstractEvent event);
}
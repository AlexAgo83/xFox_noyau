package com.aura.base.event;

import com.aura.base.utils.Validate;

public abstract class AbstractEvent {
	private final int id;
	private boolean silent;
	
	public AbstractEvent(int id) {
		Validate.notNull(id);
		this.id = id;
		this.silent = false;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isSilent() {
		return silent;
	}
	public void setSilent(boolean silent) {
		this.silent = silent;
	}
}
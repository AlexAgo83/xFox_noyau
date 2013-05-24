package com.aura.base.event.impl;

import com.aura.base.event.AbstractEvent;

public class DefaultSilentEvent extends AbstractEvent {
	public DefaultSilentEvent(int id) {
		super(id);
		setSilent(true);
	}
}
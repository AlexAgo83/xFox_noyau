package com.aura.base.event.impl;

import com.aura.base.event.AbstractEvent;
import com.aura.base.manager.AbstractPacketManager;

public class PacketManagerDeconnectionEvent extends AbstractEvent {
	private AbstractPacketManager pm;
	
	public PacketManagerDeconnectionEvent(int id) {
		super(id);
	}
	
	public AbstractPacketManager getPacketManager() {
		return pm;
	}
	public void setPacketManager(AbstractPacketManager pm) {
		this.pm = pm;
	}
}
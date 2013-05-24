package com.aura.base.thread;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.packet.AbstractPacket;

public abstract class AbstractPacketManagerExecutorThread extends AuraThread {
	private final AbstractPacketManager pm;
	
	public AbstractPacketManagerExecutorThread(String name, AbstractPacketManager pm) {
		super(pm.getAura(), name, 100);
		this.pm = pm;
	}
	
	public AbstractPacketManager getPacketManager() {
		return pm;
	}
	
	@Override
	public void action() {
		AbstractPacket[] aps = pm.getReceived().toArray(new AbstractPacket[0]);
		for (AbstractPacket ap : aps) {
			if (ap != null) {
				execute(ap);
				for (Integer i : pm.getAura().getPlugins().keySet())
					pm.getAura().getPlugins().get(i).onPacketReceive(pm, ap);
			}
			pm.getReceived().remove(ap);
		}
		routine();
	}
	@Override
	public boolean condition() {
		return pm.isOnline();
	}
	@Override
	public void doOnClose() {}
	
	public abstract void execute(AbstractPacket ap);
	public abstract void routine();
}
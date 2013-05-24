package com.aura.base;

import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.event.EventCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractAuraPlugin<A extends Aura> {

	private final int id;
	private final String name;
	private final A aura;
	
	public AbstractAuraPlugin(int id, String name, A aura) {
		this.id = id;
		this.name = name;
		this.aura = aura;
	}
	
	public A getAura() {
		return aura;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getTag() {
		return "plugin."+getName();
	}
	
	public boolean load() {
		try {
			if (!getAura().getCfgManager().getLoader().loadBoolean(getTag()+".active", true)) {
				AuraLogger.config(getAura().getSide(), "Le plugin '"+getName()+"' est présent mais désactivé.");
				return true;
			}
		} catch (PropertiesLoaderException e) {
			AuraLogger.severe(getAura().getSide(), "Un problème avec le context du plugin: "+getName(), e);
		}
		boolean result = false;
		try {
			result = implLoad();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getAura().getSide());
			return false;
		}
		if (result) {
			getAura().getEventManager().getDispatchThread().addListener(
				new AuraEventListener(aura.getEvent(EventCM.SYSTEM_LOAD)) {
				@Override
				protected void implExecute(AbstractEvent event) {
					onSystemLoad();
				}
			});
			AuraLogger.config(getAura().getSide(), "Le plugin '"+getName()+"' est activé.");
		}
		return result;
	}
	protected abstract boolean implLoad() throws KeyContainerAlreadyUsed ;
	
	public abstract void onSystemLoad();
	public abstract void onPacketReceive(AbstractPacketManager pm, AbstractPacket ap);
	public abstract void onCommand(String[] cmd);
	public abstract Class<?>[] onLoadDbClass();
}
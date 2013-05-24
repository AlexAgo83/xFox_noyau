package com.aura.exemple.plugin;

import com.aura.base.Aura;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.impl.DefaultEvent;
import com.aura.base.manager.event.EventCE;
import com.aura.base.manager.event.EventCM;
import com.aura.base.utils.Curseur;

public class ExemplePluginEventContainerManager<A extends Aura> extends EventCM<A> {
	public static Curseur CURSEUR = new Curseur(ExemplePluginEventContainerManager.class);
	
	public final static int TEST_PLUGIN_EVENT = CURSEUR.nextVal();
	
	public ExemplePluginEventContainerManager(A aura) {
		super(aura);
	}
	public ExemplePluginEventContainerManager(EventCM<A> selfManager) {
		super(selfManager);
	}
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new EventCE(TEST_PLUGIN_EVENT, "TEST_PLUGIN_EVENT") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
	}
}

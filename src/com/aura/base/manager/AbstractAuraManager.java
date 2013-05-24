package com.aura.base.manager;

import java.util.logging.Level;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractAuraManager<A extends Aura> {
	private boolean dumped = false;
	private final A aura;
	public AbstractAuraManager(A aura) {
		if (dumped)
			throw new RuntimeException("Manager déjà chargé !");
		this.aura = aura;
		this.dumped = true;
	}
	public A getAura() {
		return aura;
	}
	public TypeSide getSide() {
		if (getAura() == null) // lazy loading
			return null;
		return getAura().getSide();
	}
	public abstract boolean load();
	
	public void log(Level info, int id, String args) {
		log(info, id, args, null);		
	}
	public void log(Level info, int id, String args, Exception exc) {
		AuraLogger.log(getSide(), info, getAura().getI18nMessage(id, args), exc);		
	}
}
package com.aura.base.event.impl;

import com.aura.base.event.AbstractEvent;
import com.aura.base.packet.TypeKick;

public class KickEvent extends AbstractEvent {
	private TypeKick typeKick;
	
	public KickEvent(int id) {
		super(id);
	}
	
	public TypeKick getTypeKick() {
		return typeKick;
	}
	public void setTypeKick(TypeKick typeKick) {
		this.typeKick = typeKick;
	}
}
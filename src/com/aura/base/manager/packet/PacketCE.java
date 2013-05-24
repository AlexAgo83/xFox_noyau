package com.aura.base.manager.packet;

import com.aura.base.container.AbstractContainerElement;
import com.aura.base.packet.AbstractPacket;

public abstract class PacketCE extends AbstractContainerElement {
	public PacketCE(int id, String label) {
		super(id, label);
	}
	public abstract AbstractPacket create(int id);
}
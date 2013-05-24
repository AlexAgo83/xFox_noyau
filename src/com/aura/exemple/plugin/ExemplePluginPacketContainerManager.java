package com.aura.exemple.plugin;

import com.aura.base.Aura;
import com.aura.base.manager.packet.PacketCE;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.PacketSimpleTask;
import com.aura.base.utils.Curseur;

public class ExemplePluginPacketContainerManager<A extends Aura> extends PacketCM<A> {
	public static Curseur CURSEUR = new Curseur(ExemplePluginPacketContainerManager.class);
	
	public final static int TEST_PLUGIN_PACKET = CURSEUR.nextVal();
	
	public ExemplePluginPacketContainerManager(PacketCM<A> selfManager) {
		super(selfManager);
	}
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new PacketCE(TEST_PLUGIN_PACKET, "TEST_PLUGIN_PACKET") {@Override public AbstractPacket create(int id) {return new PacketSimpleTask(getId());}});
	}
}
package com.aura.client.thread;

import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.impl.PacketPing;
import com.aura.base.thread.AuraThread;
import com.aura.client.manager.ClientNetworkManager;
import com.aura.server.NetworkStatut;

public class ClientNetworkPingThread extends AuraThread {
	private final ClientNetworkManager nManager;
	
	public ClientNetworkPingThread(ClientNetworkManager nManager) {
		super(nManager.getAura(), "NetworkPingThread", 1000);
		this.nManager = nManager;
	}

	@Override
	public boolean condition() {
		return nManager.getNetworkStatut() == NetworkStatut.ONLINE && nManager.getServer().isOnline();
	}
	
	@Override
	public void doOnClose() {
	}
	
	@Override
	public void action() {
		PacketPing ping = (PacketPing) getMainAura().getNetworkManager().getContainerManager().createBy(PacketCM.PING);
		nManager.envoyerPaquet(ping);
	}
}
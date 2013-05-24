package com.aura.base.manager.packet;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.server.NetworkStatut;

public abstract class AbstractNetworkManager<E extends Aura> extends AbstractAuraManager<E> {
	private final PacketCM<E> containerManager;
	public PacketCM<E> getContainerManager() {
		return containerManager;
	}
	
	private NetworkStatut networkStatut;
	public NetworkStatut getNetworkStatut() {
		return networkStatut;
	}
	protected void setNetworkStatut(NetworkStatut statutServeur) {
		this.networkStatut = statutServeur;
	}
	
	public AbstractNetworkManager(E aura) {
		super(aura);
		this.networkStatut = NetworkStatut.OFFLINE;
		this.containerManager = new PacketCM<E>(getAura());
	}
	
	public void online() {
		setNetworkStatut(NetworkStatut.ONLINE);
	}
	public void offline() {
		setNetworkStatut(NetworkStatut.OFFLINE);
	}
	
	@Override
	public boolean load() {
		try {
			getContainerManager().load();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getAura().getSide());
			return false;
		}
		offline();
		return true;
	}
}
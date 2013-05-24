package com.aura.server.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.TypeKick;
import com.aura.base.packet.impl.PacketKick;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraLogger;
import com.aura.server.NetworkStatut;
import com.aura.server.manager.ServerNetworkManager;

public class ServerPacketManagerTransactionThread extends AuraThread {
	private final ServerNetworkManager manager;
	
	private List<AbstractPacketManager> toAdd;
	private synchronized List<AbstractPacketManager> getToAdd() {
		if (toAdd == null)
			toAdd = new ArrayList<AbstractPacketManager>();
		return toAdd;
	}
	public void aAdd(AbstractPacketManager toAdd) {
		getToAdd().add(toAdd);
	}
	
	private List<AbstractPacketManager> toRemove;
	private synchronized List<AbstractPacketManager> getToRemove() {
		if (toRemove == null)
			toRemove = new ArrayList<AbstractPacketManager>();
		return toRemove;
	}
	public void aRemove(AbstractPacketManager toRemove) {
		getToRemove().add(toRemove);
	}
	
	private Map<AbstractPacketManager, String> toConnect;
	private synchronized Map<AbstractPacketManager, String> getToConnect() {
		if (toConnect == null)
			toConnect = new HashMap<AbstractPacketManager, String>();
		return toConnect;
	}
	public void aConnecter(AbstractPacketManager pm, String u) {
		getToConnect().put(pm , u);
	}
	
	private List<AbstractPacketManager> toDisconnect;
	private synchronized List<AbstractPacketManager> getToDisconnect() {
		if (toDisconnect == null)
			toDisconnect = new ArrayList<AbstractPacketManager>();
		return toDisconnect;
	}
	public void aDisconnect(AbstractPacketManager pm) {
		getToDisconnect().add(pm);
	}
	
	private List<AbstractPacket> toBroadCast;
	public synchronized List<AbstractPacket> getToBroadCast() {
		if (toBroadCast == null)
			toBroadCast = new ArrayList<AbstractPacket>();
		return toBroadCast;
	}
	public void aBroadCast(AbstractPacket ap) {
		getToBroadCast().add(ap);
	}
	
	public ServerPacketManagerTransactionThread(ServerNetworkManager manager) {
		super(manager.getAura(), "PacketManagerTransactionThread", 10);
		this.manager = manager;
	}

	@Override
	public boolean condition() {
		return manager.getAura().isRunning();
	}
	@SuppressWarnings("deprecation")
	@Override
	public void doOnClose() {
		AbstractPacketManager[] toRemoves = manager.getPacketManagerTab();
		for (AbstractPacketManager pm: toRemoves) {
			pm.deconnection(false);
			manager.getPacketManagerData().remove(pm);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void action() {
		boolean lNotify = false;
		
		AbstractPacketManager[] toRemoves = getToRemove().toArray(new AbstractPacketManager[0]);
		for (AbstractPacketManager pm: toRemoves) {
			manager.getPacketManagerData().remove(pm);
			getToRemove().remove(pm);
			lNotify = true;
		}
		
		AbstractPacketManager[] toAdds = getToAdd().toArray(new AbstractPacketManager[0]);
		for (AbstractPacketManager pm: toAdds) {
			manager.getPacketManagerData().add(pm);
			getToAdd().remove(pm);
			manager.envoyerPacket(pm, getMainAura().createPacket(PacketCM.WELCOME));
			lNotify = true;
		}
		
		AbstractPacketManager[] toDisconnect = getToDisconnect().toArray(new AbstractPacketManager[0]);
		for (AbstractPacketManager pm: toDisconnect) {
//			manager.getAura().getDataChannelManager().leaveChannel(pm);
			pm.setUser(null);
			getToDisconnect().remove(pm);
			lNotify = true;
		}
		
		AbstractPacketManager[] toConnect = getToConnect().keySet().toArray(new AbstractPacketManager[0]);
		for (AbstractPacketManager pm: toConnect) {
			String u = getToConnect().get(pm);
			if (u != null) {
				AbstractPacketManager[] allPms = manager.getPacketManagerTab();
				String uLoginFound = null;
				for (AbstractPacketManager pmOnline: allPms) {
					String uOnline = pmOnline.getUser();
					if (uOnline != null && uOnline.equals(u)) {
						uLoginFound = uOnline;
						break;
//						manager.getAura().getDataChannelManager().leaveChannel(pmOnline);
//						PacketKick kick = (PacketKick) AbstractPacketContainer.USER_KICK.create();
//						kick.setTypeKick(TypeKick.DEJA_CONNECTE);
//						pmOnline.getToSend().add(kick);
//						pmOnline.setUser(null);
//						AuraLogger.info(getMainAura().getSide(), "Utilisateur [" + uOnline.getLogin() + "] kick pour la socket " + pmOnline.getInfo());
					}
				}
				if (uLoginFound == null) {
					pm.setUser(getToConnect().get(pm));
				} else {
					PacketKick kick = (PacketKick) getMainAura().createPacket(PacketCM.USER_LOGIN_KICK);
					kick.setTypeKick(TypeKick.DEJA_CONNECTE);
					pm.getToSend().add(kick);
					pm.setUser(null);
					AuraLogger.info(getMainAura().getSide(), "Utilisateur [" 
							+ uLoginFound + "] kick pour la socket " 
							+ pm.getInfo());
				}
				lNotify = true;
			}
			getToConnect().remove(pm);
		}
		
		if (lNotify) 
			manager.getAura().getEventManager().addEvent(getMainAura()
				.createEvent(EventCM.USER_NOTIFY));
		
		AbstractPacket[] toBroadCast = getToBroadCast().toArray(new AbstractPacket[0]);
		for (AbstractPacket ap: toBroadCast) {
			AbstractPacketManager[] allPms = manager.getPacketManagerTab();
			for (AbstractPacketManager pmOnline: allPms) 
				manager.envoyerPacket(pmOnline, ap);
			getToBroadCast().remove(ap);
		}
		
		if (manager.getNetworkStatut() == NetworkStatut.OFFLINE) {
			if (manager.getPacketManagerData().size() > 0) {
				AbstractPacketManager[] allPms = manager.getPacketManagerTab();
				for (AbstractPacketManager pm: allPms) 
					pm.deconnection(true);
			}
		}
	}
}

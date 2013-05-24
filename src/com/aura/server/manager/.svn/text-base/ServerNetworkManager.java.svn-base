package com.aura.server.manager;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.packet.AbstractNetworkManager;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;
import com.aura.server.AuraServer;
import com.aura.server.thread.ServerNetworkConnectionAttemptThread;
import com.aura.server.thread.ServerPacketManagerExecutorThread;
import com.aura.server.thread.ServerPacketManagerTransactionThread;

public class ServerNetworkManager extends AbstractNetworkManager<AuraServer> {
	
	private ServerNetworkConnectionAttemptThread connectionAttemptThread;
	protected ServerNetworkConnectionAttemptThread getConnectionAttemptThread() {
		if (connectionAttemptThread == null)
			connectionAttemptThread = new ServerNetworkConnectionAttemptThread(getAura(), this) {
				@Override
				public void register(AbstractPacketManager pm) {
					ServerPacketManagerExecutorThread executor = new ServerPacketManagerExecutorThread(pm);
					pm.loadExecutor(executor);
					getTransactionThread().aAdd(pm);
				}
			};
		return connectionAttemptThread;
	}
	
	private ServerPacketManagerTransactionThread transactionThread;
	public ServerPacketManagerTransactionThread getTransactionThread() {
		if (transactionThread == null)
			transactionThread = new ServerPacketManagerTransactionThread(this);
		return transactionThread;
	}
	
	private Set<AbstractPacketManager> packetManager;
	@Deprecated
	public synchronized Set<AbstractPacketManager> getPacketManagerData() {
		if (packetManager == null) {
			packetManager = new HashSet<AbstractPacketManager>();
		}
		return packetManager;
	}
	
	public AbstractPacketManager[] getPacketManagerTab() {
		return getPacketManagerData().toArray(new AbstractPacketManager[0]);
	}
	public String getConnectedUser(AbstractPacketManager pm) {
		return pm.getUser();
	}
	public boolean isConnectedUser(String user) {
		for (AbstractPacketManager pm: getPacketManagerTab()) {
			if (user.equals(pm.getUser()))
				return true;
		}
		return false;
	}
	
	public void envoyerPacket(AbstractPacketManager pm, AbstractPacket packet) {
		if (pm.isOnline()) {
			pm.getToSend().add(packet);
		} else {
			AuraLogger.config(getAura().getSide(), "Packet non envoyé, client déconnecté.");
		}
	}
	
	public ServerNetworkManager(AuraServer auraServer) {
		super(auraServer);
		getTransactionThread();
		getConnectionAttemptThread();
	}
	
	@Override
	public boolean load() {
		if (!super.load())
			return false;
		getTransactionThread().start();
		getConnectionAttemptThread().start();
		return true;
	}
	
	/**
	 * Action sur connection d'un client au serveur.
	 * @return connection acceptée oui/non, doit valide si la connection du client est autorisée.
	 */
	public synchronized boolean onConnection(Socket client) {
		AuraLogger.info(getAura().getSide(), "Connection d'un client ["+
				client.getInetAddress().getHostAddress()
				+":"
				+client.getPort()+"]");
		return getAura().isLoaded();
	}
}
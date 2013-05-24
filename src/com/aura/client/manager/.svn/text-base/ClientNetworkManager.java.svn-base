package com.aura.client.manager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.aura.base.event.impl.PacketManagerDeconnectionEvent;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.AbstractNetworkManager;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.impl.PacketBuildRevision;
import com.aura.base.packet.impl.TypeLoginResult;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.client.ClientConnection;
import com.aura.client.thread.ClientNetworkPingThread;
import com.aura.client.thread.ClientPacketManagerExecutorThread;
import com.aura.server.NetworkStatut;

public class ClientNetworkManager extends AbstractNetworkManager<AuraClient> {
	private final ClientConnection clientConnection;
	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	private AbstractPacketManager server;
	public AbstractPacketManager getServer() {
		return server;
	}

	public ClientNetworkManager(AuraClient auraClient) {
		super(auraClient);
		this.clientConnection = new ClientConnection(auraClient);
	}
	
	private ClientNetworkPingThread pingThread;
	public void connect() throws ConnectException {
		connect(null);
	}
	public void connect(String password) throws ConnectException {
		setNetworkStatut(NetworkStatut.ONLINE);
		getClientConnection().setPassword(password);
		getClientConnection().setLoginInfo(TypeLoginResult.KO, null);
		try {
			if (pingThread != null)
				pingThread.forceTerminate();
			
			if (server != null && server.isOnline()) 
				deconnect(true);
			
			server = new AbstractPacketManager(
				getAura(), 
				new Socket(
					getAura().getCfgManager().getConfigStringValue(ConfigCM.SOCKET_HOSTNAME),
					getAura().getCfgManager().getConfigIntegerValue(ConfigCM.SOCKET_PORT))) {
				@Override
				public void onDeconnection(AbstractPacketManager pm, boolean isErrorOrConnectionLost) {
					if (isErrorOrConnectionLost)
						getAura().getEventManager().addEvent(getAura().createEvent(EventCM.USER_CONNECT_ERR));
					deconnect(isErrorOrConnectionLost);
				}
				@Override
				public void onKick() {} // On kick pas le serveur
			};
			ClientPacketManagerExecutorThread executor = new ClientPacketManagerExecutorThread(server);
			server.loadExecutor(executor);
			PacketBuildRevision checkBuildRev = (PacketBuildRevision) getContainerManager().createBy(PacketCM.BUILD_REVISION);
			checkBuildRev.setBuildRevision(getAura().getBuildRevision());
			envoyerPaquet(checkBuildRev);
			pingThread = new ClientNetworkPingThread(this);
			pingThread.start();
			
			return;
		} catch (ConnectException e) {
			getAura().getEventManager().addEvent(getAura().createEvent(EventCM.USER_CONNECT_ERR));
			throw e;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setNetworkStatut(NetworkStatut.OFFLINE);
	}
	
	public void deconnect(boolean isError) {
		if (getServer() != null && getServer().isOnline()) {
			getServer().deconnection(isError);
			while (getServer().isOnline()) 
				AuraLogger.config(getAura().getSide(), "En attente de déconnection...");
		} else {
			AuraLogger.config(getAura().getSide(), "Aucun serveur à déconnecter...");
		}
		
		getClientConnection().setTimeServer(null, null);
		getClientConnection().setLoginInfo(TypeLoginResult.KO, null);
		
		setNetworkStatut(NetworkStatut.OFFLINE);
		
		PacketManagerDeconnectionEvent e = (PacketManagerDeconnectionEvent) getAura().createEvent(EventCM.USER_CONNECT_KO);
		e.setPacketManager(getServer());
		getAura().getEventManager().addEvent(e);
	}
	
	public void envoyerPaquet(AbstractPacket ap) {
		if (getNetworkStatut() == NetworkStatut.ONLINE 
				&& ap != null 
				&& ap.isValid()
				&& getServer() != null 
				&& getServer().isOnline()) 
			getServer().getToSend().add(ap);
	}
}
package com.aura.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.aura.base.Aura;
import com.aura.base.event.impl.PacketManagerDeconnectionEvent;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.TypeKick;
import com.aura.base.packet.impl.PacketKick;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraLogger;
import com.aura.server.NetworkStatut;
import com.aura.server.manager.ServerNetworkManager;

public abstract class ServerNetworkConnectionAttemptThread extends AuraThread {
	private final ServerNetworkManager cManager;
	private ServerSocket serverSocket;
	
	public ServerNetworkConnectionAttemptThread(Aura aura, ServerNetworkManager connectionManager) {
		super(aura, "NetworkConnectionAttemptThread", 0);
		this.cManager = connectionManager;
	}
	
	@Override
	public boolean condition() {
		return cManager.getAura().isRunning();
	}
	
	@Override
	public void doOnClose() {
		AuraLogger.config(getMainAura().getSide(), "Fermeture du thread d'écoute de connection.");		
	}
	
	@Override
	public void action() {
		if (serverSocket == null) {
			try {
				AuraLogger.config(getMainAura().getSide(), "Initialisation de thread d'écoute de connection ...");
				serverSocket = new ServerSocket(getMainAura().getCfgManager().getConfigIntegerValue(ConfigCM.SOCKET_PORT));
			} catch (IOException e) {
				AuraLogger.severe(getMainAura().getSide(), "Echec de l'initialisation du thread d'écoute de connection !" , e);
				cManager.getAura().turnOff(true);
			}
		}
		try {
			try {
				serverSocket.setSoTimeout(cManager.getNetworkStatut() == NetworkStatut.ONLINE ? 0 : 1000);
				Socket client = serverSocket.accept();
				if (cManager.getNetworkStatut() != NetworkStatut.ONLINE) {
					AuraLogger.warning(getMainAura().getSide(), "Connection refusée, le serveur est offline.");
					client.close();
				} else {
					// Connection autorisée
					if (client != null && cManager.onConnection(client)) {
						register(new AbstractPacketManager(cManager.getAura(), client) {
							@Override
							public void onDeconnection(AbstractPacketManager pm, boolean isErrorOrConnectionLost) {
								cManager.getTransactionThread().aRemove(pm);
								AuraLogger.config(getMainAura().getSide(), "Deconnection de l'utilisateur '"+pm.getInfo()+"'.");
								PacketManagerDeconnectionEvent e = (PacketManagerDeconnectionEvent) getAura().createEvent(EventCM.USER_CONNECT_KO);
								e.setPacketManager(pm);
								getAura().getEventManager().addEvent(e);
							}
							@Override
							public void onKick() {
								PacketKick kick = (PacketKick) cManager.getContainerManager().createBy(PacketCM.USER_LOGIN_KICK);
								kick.setTypeKick(TypeKick.FORCE);
								getToSend().add(kick);
								cManager.getTransactionThread().aDisconnect(this);
							}
						});
					} else {
						AuraLogger.warning(getMainAura().getSide(), "Connection refusée, le serveur n'est pas chargé.");
						client.close();
					}
				}
			} catch (SocketTimeoutException e) {}
		} catch (SocketException e) {
			AuraLogger.severe(getMainAura().getSide(), "Connection échouée (Socket) !" , e);
		} catch (IOException e) {
			AuraLogger.severe(getMainAura().getSide(), "Connection échouée (IO) !" , e);
		}
	}
	
	public abstract void register(AbstractPacketManager pm);
}
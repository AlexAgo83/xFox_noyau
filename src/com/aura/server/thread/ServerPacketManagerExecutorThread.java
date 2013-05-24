package com.aura.server.thread;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.PacketSimpleTask;
import com.aura.base.packet.impl.PacketBuildRevision;
import com.aura.base.packet.impl.PacketLoginAttempt;
import com.aura.base.packet.impl.PacketLoginResult;
import com.aura.base.packet.impl.PacketPing;
import com.aura.base.packet.impl.PacketPong;
import com.aura.base.packet.impl.TypeLoginResult;
import com.aura.base.thread.AbstractPacketManagerExecutorThread;
import com.aura.base.utils.AuraLogger;
import com.aura.server.AuraServer;

public class ServerPacketManagerExecutorThread extends AbstractPacketManagerExecutorThread {
	private boolean connectionAllowed = false;
	
	public ServerPacketManagerExecutorThread(AbstractPacketManager pm) {
		super("ServerPacketManagerExecutorThread", pm);
		this.socketTimeout = getMainAura().getCfgManager().getConfigIntegerValue(ConfigCM.SOCKET_TIMEOUT);
	}
	public AuraServer getAuraServer() {
		return (AuraServer) getPacketManager().getAura();
	}
	
	private int socketTimeout;
	@Override
	public void routine() {
		// Si le client est en timeout (plus de ping depuis un moment)
		if (socketTimeout > 0 
				&& getPacketManager().getLastPing() > -1 
				&& getPacketManager().getLastPing() + socketTimeout < System.currentTimeMillis()) {
			AuraLogger.warning(getAuraServer().getSide(), "Utilisateur ["+getPacketManager().getInfo()+"] en plein timeout. \n Fermeture forcé de la socket.");
			getPacketManager().deconnection(true);
		}
	}
	
	@Override
	public void execute(final AbstractPacket ap) {
		if (ap.getId() == PacketCM.BUILD_REVISION) {
			PacketBuildRevision buildRevCheck = (PacketBuildRevision) ap;
			connectionAllowed = (buildRevCheck.getBuildRevision()
					.compareTo(getAuraServer().getBuildRevision())) == 0;
			if (!connectionAllowed) { // Si la version est différente. 
				PacketSimpleTask outDated = (PacketSimpleTask) getAuraServer().createPacket(PacketCM.BUILD_REVISION_OUTDATED);
				getPacketManager().getToSend().add(outDated);
			}
		} else if (connectionAllowed) {
			// On accept les packet que si et seulement 
			// si la connection est autorisé.
			getPacketManager().setLastPing(System.currentTimeMillis());
			if (ap.getId() == PacketCM.PING) {
				PacketPong packetPong = (PacketPong) getAuraServer().createPacket(PacketCM.PONG);
				packetPong.setTimeRequest((PacketPing) ap); 
				getAuraServer().getNetworkManager().envoyerPacket(getPacketManager(), packetPong);
			} else {
				executeWhileConnected(ap);
			}
		} else {
			// FIXME XF(AA) [ConnectionNotAllowed] trop de paquet envoyé encore..
//			getPacketManager().deconnection(true);
		}
	}
	
	/**
	 * Traitement d'un paquet entrant d'une connection autorisée. 
	 */
	private void executeWhileConnected(final AbstractPacket ap) {
		String user = getAuraServer().getNetworkManager().getConnectedUser(getPacketManager());
		if (user != null) { // Si l'utilisateur est logged
			executeWhileLogged(user, ap);
		} else if (ap.getId() == PacketCM.USER_LOGIN_ATTEMPT) {
			AuraLogger.info(getAuraServer().getSide(), "Tentative de login de la par de "+getPacketManager().getInfo()+".");
			PacketLoginAttempt pLogin = (PacketLoginAttempt) ap;

			boolean lPass = false;
			
			// ///
//			user = getAuraServer().getDataBaseManager().getUserService().getUserByLogin(pLogin.getLogin());
//			lPass = user != null ? user.getPassword().trim().toUpperCase()
//					.equals(pLogin.getPassword().trim().toUpperCase()) : false;
//			if (user == null && getMainAura().getCfgManager().getConfigBooleanValue(ConfigCM.ADMIN_ALLOW_NEW_USER)) {
//				User u = new User();
//				u.setLogin(pLogin.getLogin());
//				u.setPassword(pLogin.getPassword());
//				getAuraServer().getDataBaseManager().getUserService().add(u);
//				
//				user = getAuraServer().getDataBaseManager().getUserService().getUserByLogin(pLogin.getLogin());
//				lPass = true;
//			}

			// < TEMPORAIRE
			user = pLogin.getLogin();
			String avecPwd = getAuraServer().getCfgManager().getConfigStringValue(ConfigCM.SOCKET_PASSWORD);
			if (avecPwd != null 
				&& avecPwd.trim().length() > 0) {
				if (avecPwd.trim().toUpperCase().equals(pLogin.getPassword().trim().toUpperCase())) {
					lPass = true;
				} else {
					lPass = false;
				}
			} else {
				lPass = true;
			}
			// > TEMPORAIRE
			
			PacketLoginResult result = (PacketLoginResult) getAuraServer().createPacket(PacketCM.USER_LOGIN_RESULT);
			if (lPass) {
				getAuraServer().getNetworkManager().getTransactionThread().aConnecter(getPacketManager(), user);
				result.setTypeResult(TypeLoginResult.OK);
				result.setLogin(user);
				AuraLogger.info(getAuraServer().getSide(), "Utilisateur '" + user + "' connecté.");
			} else {
				if (user != null) {
					result.setTypeResult(TypeLoginResult.BAD_PASSWORD);
					AuraLogger.info(getAuraServer().getSide(), "Utilisateur refusé, password err.");
				} 
//				else {
//					result.setTypeResult(TypeLoginResult.NOT_EXIST);
//					AuraLogger.info(getAuraServer().getSide(), "Utilisateur n'existe pas.");
//				}
			}
			getAuraServer().getNetworkManager().envoyerPacket(getPacketManager(), result);
		}
	}
	
	/**
	 * Traitement d'un paquet entrant d'un utilisateur sous session.
	 */
	private void executeWhileLogged(final String user, final AbstractPacket ap) {
//		if (ap.getId() == TypePacket.DATA_CHANNEL_JOIN_REQUEST.getId()) {
//			String name = ((PacketSimpleStringTask) ap).getString();
//			ServerDataChannel dc = getAuraServer().getDataChannelManager().getBy(name);
//			if (dc == null) // Si le channel n'existe pas on le crée
//				dc = getAuraServer().getDataChannelManager().creerCanal(name);
//			if (!dc.isContain(user))  // Si n'existe pas déjà dans le channel
//				dc.join(user);
//		} else if (ap.getId() == TypePacket.DATA_CHANNEL_LEAVE_REQUEST.getId()) {
//			String name = ((PacketSimpleStringTask) ap).getString();
//			ServerDataChannel dc = getAuraServer().getDataChannelManager().getBy(name);
//			if (dc != null && dc.isContain(user)) { // Si le channel exist et que l'utilisateur est déjà dedans
//				dc.leave(user);
//				if (dc.getUserCount() == 0) // si plus personne on supprime le canal
//					getAuraServer().getDataChannelManager().supprimerCanal(dc);
//			}
//		}
	}
}
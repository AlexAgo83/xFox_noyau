package com.aura.client.thread;

import com.aura.base.event.impl.KickEvent;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.impl.PacketKick;
import com.aura.base.packet.impl.PacketLoginResult;
import com.aura.base.packet.impl.PacketPong;
import com.aura.base.packet.impl.TypeLoginResult;
import com.aura.base.thread.AbstractPacketManagerExecutorThread;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;

public class ClientPacketManagerExecutorThread extends AbstractPacketManagerExecutorThread {
	public ClientPacketManagerExecutorThread(AbstractPacketManager pm) {
		super("ClientPacketManagerExecutorThread", pm);
	}
	
	private AuraClient getAuraClient() {
		return (AuraClient) getPacketManager().getAura();
	}
	
	@Override
	public void routine() {}
	
	@Override
	public void execute(AbstractPacket ap) {
		if (ap.getId() == PacketCM.PONG) {
			PacketPong packetPong = (PacketPong) ap;
			getAuraClient().getNetworkManager().getClientConnection()
				.setTimeServer(
					packetPong.getDelta(), 
					packetPong.getTimeResult());
			getAuraClient().getEventManager().addEvent(
				getAuraClient().createEvent(EventCM.PONG));
		} else if (ap.getId() == PacketCM.WELCOME) {
			AuraLogger.info(getPacketManager().getAura().getSide(), "Connection réussie.");
			getAuraClient().getEventManager().addEvent(getAuraClient().createEvent(EventCM.USER_CONNECT_OK));
		} else if (ap.getId() == PacketCM.BUILD_REVISION_OUTDATED) {
			AuraLogger.severe(getPacketManager().getAura().getSide(), "Version client ou serveur différente !");
			getAuraClient().getEventManager().addEvent(getAuraClient().createEvent(EventCM.USER_OUTDATED));
			getAuraClient().getNetworkManager().deconnect(false);
		} else if (ap.getId() == PacketCM.USER_LOGIN_RESULT) {
			PacketLoginResult p = (PacketLoginResult) ap;
			getAuraClient().getNetworkManager().getClientConnection().setLoginInfo(p.getTypeResult(), p.getLogin());
		} else if (ap.getId() == PacketCM.USER_LOGIN_KICK) {
			PacketKick p = (PacketKick) ap;
			AuraLogger.info(getPacketManager().getAura().getSide(), "Kick du serveur, raison: "+p.getTypeKick().toString());
			KickEvent e = (KickEvent) getMainAura().createEvent(EventCM.USER_LOGIN_KICK);
			e.setTypeKick(p.getTypeKick());
			getAuraClient().getEventManager().addEvent(e);
			getAuraClient().getNetworkManager().getClientConnection().setLoginInfo(TypeLoginResult.KO, null);
		}
		
		if (getAuraClient().getNetworkManager().getServer().isOnline() 
				&& getAuraClient().getNetworkManager().getClientConnection().getResult() == TypeLoginResult.OK)
			executeWhileLogged(ap);
	}
	
	private void executeWhileLogged(AbstractPacket ap) {
//		if (ap.getId() == TypePacket.DATA_CHANNEL_JOIN_RESULT.getId()) {
//			String name = ((PacketSimpleStringTask) ap).getString();
//			ClientDataChannel dc = getAuraClient().getDataChannelManager().getBy(name);
//			if (dc == null) 
//				dc = getAuraClient().getDataChannelManager().creerCanal(name);
//			dc.join(getAuraClient().getNetworkManager().getLogin());
//		}
	}
}
package com.aura.exemple.plugin;
import com.aura.base.AbstractAuraPlugin;
import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.PacketCE;
import com.aura.base.packet.AbstractPacket;
import com.aura.client.AuraClient;

public class ExempleClientPlugin extends AbstractAuraPlugin<AuraClient> {
	public final static int PLUGIN_ID = -1;
	
	public ExempleClientPlugin(AuraClient client) {
		super(PLUGIN_ID, "ExempleClientPlugin", client);
	}
	
	@Override
	public boolean implLoad() throws KeyContainerAlreadyUsed {
		new ExemplePluginConsoleContainerManager<AuraClient>(getAura().getConsoleManager().getContainerManager()).load();
		new ExemplePluginPacketContainerManager<AuraClient>(getAura().getNetworkManager().getContainerManager()).load();
		new ExemplePluginEventContainerManager<AuraClient>(getAura().getEventManager().getContainerManager()).load();
		return true;
	}
	
	@Override
	public void onSystemLoad() {
		// après le chargement envoyer l'event
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getAura().getEvent(ExemplePluginEventContainerManager.TEST_PLUGIN_EVENT)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				System.out.println(getAura().getSide()+":"+getAura().getEvent(event.getId()));
			}
		});
		getAura().getEventManager().addEvent(getAura().createEvent(ExemplePluginEventContainerManager.TEST_PLUGIN_EVENT));
		getAura().getEventManager().addEvent(getAura().createEvent(ExemplePluginEventContainerManager.TEST_PLUGIN_EVENT));		
		
		// à la connection envoyer le nouveau packet
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getAura().getEvent(EventCM.USER_CONNECT_OK)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				AbstractPacket ap = getAura().createPacket(ExemplePluginPacketContainerManager.TEST_PLUGIN_PACKET);
				getAura().getNetworkManager().envoyerPaquet(ap);
			}
		});
		
		getAura().getConsoleManager().onConsoleCommand(new String[]{
			getAura().getConsoleManager().getCommande(ExemplePluginConsoleContainerManager.TEST_PLUGIN_CONSOLE).getBalise()});
	}
	
	@Override
	public void onPacketReceive(AbstractPacketManager pm, AbstractPacket ap) {
		// Réponse du serveur après l'envoi du packet
		if (ap.getId() == ExemplePluginPacketContainerManager.TEST_PLUGIN_PACKET) {
			PacketCE e = getAura().getPacket(ap.getId());
			System.out.println(getAura().getSide() + ":" + e);
		}
	}
	
	@Override
	public void onCommand(String[] cmd) {
		ConsoleCE cp = getAura().getConsoleManager().getContainerManager().dispatcher(TypeSide.SERVER, cmd, false);
		if (cp == null)
			return;
		
		if (cp.getId() == ExemplePluginConsoleContainerManager.TEST_PLUGIN_CONSOLE) {
			System.out.println(getAura().getSide() + ":" + cp.getId() + ":"+ cp.getLabel());
		} 
	}
	
	@Override public Class<?>[] onLoadDbClass() { return null; }
}
package com.aura.exemple.plugin;
import com.aura.base.AbstractAuraPlugin;
import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.packet.PacketCE;
import com.aura.base.packet.AbstractPacket;
import com.aura.server.AuraServer;

public class ExempleServerPlugin extends AbstractAuraPlugin<AuraServer> {
	public final static int PLUGIN_ID = -1;
	
	public ExempleServerPlugin(AuraServer server) {
		super(PLUGIN_ID, "ExempleServerPlugin", server);
	}
	
	@Override
	public boolean implLoad() throws KeyContainerAlreadyUsed {
		new ExemplePluginConsoleContainerManager<AuraServer>(getAura().getConsoleManager().getContainerManager()).load();
		new ExemplePluginPacketContainerManager<AuraServer>(getAura().getNetworkManager().getContainerManager()).load();
		new ExemplePluginEventContainerManager<AuraServer>(getAura().getEventManager().getContainerManager()).load();
		return true;
	}
	
	@Override
	public void onSystemLoad() {
		getAura().getConsoleManager().onConsoleCommand(new String[]{
			getAura().getConsoleManager().getCommande(ExemplePluginConsoleContainerManager.TEST_PLUGIN_CONSOLE).getBalise()});
	}
	
	@Override
	public void onPacketReceive(AbstractPacketManager pm, AbstractPacket ap) {
		// réception du packet de test du client, et retourne un packet de test.
		if (ap.getId() == ExemplePluginPacketContainerManager.TEST_PLUGIN_PACKET) {
			PacketCE e = getAura().getPacket(ap.getId());
			System.out.println(getAura().getSide() + ":" + e);
			
			AbstractPacket newAp = getAura().createPacket(ExemplePluginPacketContainerManager.TEST_PLUGIN_PACKET);
			getAura().getNetworkManager().envoyerPacket(pm, newAp);
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
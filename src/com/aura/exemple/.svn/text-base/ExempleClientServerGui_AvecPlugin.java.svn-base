package com.aura.exemple;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.exemple.client.ExempleClientConnectionView;
import com.aura.exemple.plugin.ExempleClientPlugin;
import com.aura.exemple.plugin.ExempleServerPlugin;
import com.aura.exemple.server.ExempleServerMonitorView;
import com.aura.server.AuraServer;

public class ExempleClientServerGui_AvecPlugin {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setContentPane(panel);
		{
			AuraServer server = new AuraServer() {
				@Override
				public void initPlugin(final Aura self) {
					addPlugin(new ExempleServerPlugin(this));
				}
			};
			new ExempleServerMonitorView(server, panel);
			
			AuraClient client_1 = new AuraClient() {
				@Override
				public void initPlugin(final Aura self) {
					addPlugin(new ExempleClientPlugin(this));
				}
			};
			new ExempleClientConnectionView(client_1, panel);
			
			AuraClient client_2 = new AuraClient() {
				@Override
				public void initPlugin(final Aura self) {
					addPlugin(new ExempleClientPlugin(this));
				}
			};
			new ExempleClientConnectionView(client_2, panel);
			
			AuraLogger.debug(false);
		}
		frame.setVisible(true);
	}
}
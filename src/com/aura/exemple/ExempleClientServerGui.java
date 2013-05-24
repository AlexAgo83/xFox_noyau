package com.aura.exemple;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aura.base.utils.AuraLogger;
import com.aura.exemple.client.ExempleClientConnectionView;
import com.aura.exemple.server.ExempleServerMonitorView;


public class ExempleClientServerGui {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setContentPane(panel);
		{
			new ExempleServerMonitorView(null, panel);
			new ExempleClientConnectionView(null, panel);
			new ExempleClientConnectionView(null, panel);
			AuraLogger.debug(false);
		}
		frame.setVisible(true);
	}
}

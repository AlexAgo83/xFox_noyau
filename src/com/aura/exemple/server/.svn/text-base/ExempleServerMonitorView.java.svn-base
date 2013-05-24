package com.aura.exemple.server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.CallableAction;
import com.aura.exemple.ExempleAuraGui;
import com.aura.server.AuraServer;

public class ExempleServerMonitorView extends ExempleAuraGui<AuraServer> {
	public ExempleServerMonitorView(AuraServer server, Component parentFrame) {
		super(parentFrame, server != null ? server : new AuraServer() {
			@Override
			public void initPlugin(final Aura self) {
				// à impl...
			}
		});
		
		initialize();
		getLInfoServeur().setText("Serveur démarré, Statut: Offline");
	}

	private void initialize() {
		GridBagConstraints gbcStart = new GridBagConstraints();
		gbcStart.gridx = 0; gbcStart.gridy = 0;
		gbcStart.weightx = 1; gbcStart.weighty = 0;
		gbcStart.fill = GridBagConstraints.HORIZONTAL;
		getPContent().add(getPStart(), gbcStart);
		
		GridBagConstraints gbcConsole = new GridBagConstraints();
		gbcConsole.gridx = 0; gbcConsole.gridy = 1;
		gbcConsole.weightx = 1; gbcConsole.weighty = 1;
		gbcConsole.insets = new Insets(0, 5, 0, 5);
		gbcConsole.fill = GridBagConstraints.BOTH;
		getPContent().add(getSpConsole(), gbcConsole);
	}
	
	private JPanel pStart;
	public JPanel getPStart() {
		if (pStart == null) {
			pStart = new JPanel(new GridBagLayout());
			pStart.setBackground(Color.GRAY);
			
			GridBagConstraints gbcStart = new GridBagConstraints();
			gbcStart.gridx = 0; gbcStart.gridy = 0;
			gbcStart.weightx = 1;
			gbcStart.fill = GridBagConstraints.HORIZONTAL;
			pStart.add(getBtOnline(), gbcStart);
			
			GridBagConstraints gbcStop = new GridBagConstraints();
			gbcStop.gridx = 1; gbcStop.gridy = 0;
			gbcStop.weightx = 1;
			gbcStop.fill = GridBagConstraints.HORIZONTAL;
			pStart.add(getBtOffline(), gbcStop);
			
			GridBagConstraints gbcInfoServeur = new GridBagConstraints();
			gbcInfoServeur.gridx = 0; gbcInfoServeur.gridy = 1;
			gbcInfoServeur.weightx = 1;
			gbcInfoServeur.gridwidth = 3;
			gbcInfoServeur.insets = new Insets(0, 5, 0, 5);
			gbcInfoServeur.fill = GridBagConstraints.HORIZONTAL;
			pStart.add(getLInfoServeur(), gbcInfoServeur);
		}
		return pStart;
	}
	
	private JButton btOnline;
	public JButton getBtOnline() {
		if (btOnline == null) {
			btOnline = new JButton("Online");
			btOnline.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					aura.getNetworkManager().online();
					getLInfoServeur().setText("Serveur online.");
					
					getBtOffline().setEnabled(true);
					getBtOnline().setEnabled(false);
				}
			});
		}
		return btOnline;
	}
	
	private JButton btOffline;
	public JButton getBtOffline() {
		if (btOffline == null) {
			btOffline = new JButton("Offline");
			btOffline.setEnabled(false);
			btOffline.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					aura.getNetworkManager().offline();
					getLInfoServeur().setText("Serveur offline.");
					
					getBtOnline().setEnabled(true);
					getBtOffline().setEnabled(false);
				}
			});
		}
		return btOffline;
	}
	
	private JScrollPane spConsole;
	public JScrollPane getSpConsole() {
		if (spConsole == null) {
			spConsole = new JScrollPane(getTableConsole());
		}
		return spConsole;
	}
	
	private JXTable taConsole;
	private final List<String> consoleSwap = new ArrayList<String>();
	public JXTable getTableConsole() {
		if (taConsole == null) {
			taConsole = new JXTable(getModel());
			taConsole.setPreferredSize(new Dimension(200, 100));
			taConsole.setShowGrid(false);
			AuraLogger.addAction(new CallableAction() {
				@Override
				public void execute(TypeSide side, Level info, String msg) {
					consoleSwap.add(info + ": " + msg + "\n");
					getModel().fireTableDataChanged();
				}
			});
		}
		return taConsole;
	}
	private DefaultTableModel model;
	public DefaultTableModel getModel() {
		if (model == null) {
			model = new DefaultTableModel() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object getValueAt(int row, int column) {
					return consoleSwap.get((getRowCount()-1)-row);
				}
				@Override
				public int getColumnCount() {
					return 1;
				}
				@Override
				public int getRowCount() {
					return consoleSwap.size();
				}
				@Override
				public String getColumnName(int column) {
					return null;
				}
			};
		}
		return model;
	}
	
	@Override
	public void doOnThreadCycle() {
		runServerInfo();		
	}
	
	private void runServerInfo() {
//		Date tickServeur = aura.getNetworkManager().getTickServeur();
//		if (tickServeur != null) {
//			getLInfoServeur().setText("Date serveur: "+SDF.format(tickServeur) + " Ping: " + aura.getNetworkManager().getPing());
//		}
	}
}
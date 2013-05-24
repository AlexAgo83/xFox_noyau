package com.aura.exemple.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.aura.base.Aura;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.event.impl.KickEvent;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.impl.PacketLoginAttempt;
import com.aura.client.AuraClient;
import com.aura.exemple.ExempleAuraGui;

public class ExempleClientConnectionView extends ExempleAuraGui<AuraClient> {
	
	public ExempleClientConnectionView(AuraClient client, Component parentFrame) {
		super(parentFrame, client != null ? client : new AuraClient() {
			@Override
			public void initPlugin(final Aura self) {}
		});
		initialize();
		
		getPConnection().setVisible(true);
		getPLogin().setVisible(true);
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_CONNECT_OK)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Client connecté");
				
				getBtConnect().setEnabled(false);
				getBtDeconnect().setEnabled(true);
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_CONNECT_KO)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Client déconnecté");
				
				getBtConnect().setEnabled(true);
				getBtDeconnect().setEnabled(false);
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_CONNECT_ERR)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Client connection Err.");
				
				getBtConnect().setEnabled(true);
				getBtDeconnect().setEnabled(false);
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_OUTDATED)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoServeur().setText("Version outdated");
		
				getBtConnect().setEnabled(true);
				getBtDeconnect().setEnabled(false);
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_LOGIN_OK)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Utilisateur connecté");
				
				getPLogin().setVisible(false);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_LOGIN_KO)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Utilisateur déconnecté");
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_LOGIN_KICK)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Kick par le serveur: "+((KickEvent) event).getTypeKick());
				
				getPLogin().setVisible(true);
			}
		});
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(aura.getEvent(EventCM.USER_LOGIN_BAD_PASSWORD)) {
			@Override
			public void implExecute(AbstractEvent event) {
				getLInfoLogin().setText("Combinaison login/mdp incorrect. Ou déjà connecté ?");

				getPLogin().setVisible(true);
			}
		});
	}

	private void initialize() {
		GridBagConstraints gbcConnection = new GridBagConstraints();
		gbcConnection.gridx = 0; gbcConnection.gridy = 0;
		gbcConnection.weightx = 1; gbcConnection.weighty = 0;
		gbcConnection.fill = GridBagConstraints.BOTH;
		getPContent().add(getPConnection(), gbcConnection);
		
		GridBagConstraints gbcSplash = new GridBagConstraints();
		gbcSplash.gridx = 0; gbcSplash.gridy = 1;
		gbcSplash.weightx = 1; gbcSplash.weighty = 1;
		gbcSplash.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints gbcLogin = new GridBagConstraints();
		gbcLogin.gridx = 0; gbcLogin.gridy = 2;
		gbcLogin.weightx = 1; gbcLogin.weighty = 1;
		gbcLogin.fill = GridBagConstraints.BOTH;
		getPContent().add(getPLogin(), gbcLogin);
		
		GridBagConstraints gbcGame = new GridBagConstraints();
		gbcGame.gridx = 0; gbcGame.gridy = 3;
		gbcGame.weightx = 1; gbcGame.weighty = 1;
		gbcGame.fill = GridBagConstraints.BOTH;
	}
	
	private JPanel pConnection;
	public JPanel getPConnection() {
		if (pConnection == null) {
			pConnection = new JPanel(new GridBagLayout());
			pConnection.setBackground(Color.GRAY);
			
			GridBagConstraints gbcConnect = new GridBagConstraints();
			gbcConnect.gridx = 0; gbcConnect.gridy = 0;
			gbcConnect.weightx = 1;
			gbcConnect.fill = GridBagConstraints.HORIZONTAL;
			pConnection.add(getBtConnect(), gbcConnect);
			
			GridBagConstraints gbcDeconnect = new GridBagConstraints();
			gbcDeconnect.gridx = 1; gbcDeconnect.gridy = 0;
			gbcDeconnect.weightx = 1;
			gbcDeconnect.fill = GridBagConstraints.HORIZONTAL;
			pConnection.add(getBtDeconnect(), gbcDeconnect);
			
			GridBagConstraints gbcInfoServeur = new GridBagConstraints();
			gbcInfoServeur.gridx = 0; gbcInfoServeur.gridy = 1;
			gbcInfoServeur.weightx = 1;
			gbcInfoServeur.gridwidth = 3;
			gbcInfoServeur.insets = new Insets(0, 5, 0, 5);
			gbcInfoServeur.fill = GridBagConstraints.HORIZONTAL;
			pConnection.add(getLInfoServeur(), gbcInfoServeur);
		}
		return pConnection;
	}
	
	private JButton btConnect;
	public JButton getBtConnect() {
		if (btConnect == null) {
			btConnect = new JButton("Connect");
			btConnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						getLInfoServeur().setText("En attente de connection avec le serveur...");
						aura.getNetworkManager().connect();
					} catch (ConnectException e) {
						getLInfoServeur().setText("Connection au serveur impossible. Serveur OFF ?");
					}
				}
			});
		}
		return btConnect;
	}
	
	private JButton btDeconnect;
	public JButton getBtDeconnect() {
		if (btDeconnect == null) {
			btDeconnect = new JButton("Deconnect");
			btDeconnect.setEnabled(false);
			btDeconnect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getLInfoServeur().setText("Deconnection du serveur.");
					aura.getNetworkManager().deconnect(false);
				}
			});
		}
		return btDeconnect;
	}
	
	private JPanel pLogin;
	public JPanel getPLogin() {
		if (pLogin == null) {
			pLogin = new JPanel(new GridBagLayout());
			pLogin.setVisible(false);
			pLogin.setBackground(Color.LIGHT_GRAY);
			
			GridBagConstraints gbcLLogin = new GridBagConstraints();
			gbcLLogin.gridx = 0; gbcLLogin.gridy = 0;
			gbcLLogin.anchor = GridBagConstraints.WEST;
			gbcLLogin.insets = new Insets(0, 2, 0, 5);
			pLogin.add(new JLabel("Login"), gbcLLogin);
			
			GridBagConstraints gbcTfLogin = new GridBagConstraints();
			gbcTfLogin.gridx = 1; gbcTfLogin.gridy = 0;
			gbcTfLogin.weightx = 1; gbcTfLogin.fill = GridBagConstraints.HORIZONTAL;
			gbcTfLogin.gridwidth = 2;
			pLogin.add(getTfLogin(), gbcTfLogin);
			
			GridBagConstraints gbcLPassword = new GridBagConstraints();
			gbcLPassword.gridx = 0; gbcLPassword.gridy = 1;
			gbcLPassword.anchor = GridBagConstraints.WEST;
			gbcLPassword.insets = new Insets(0, 2, 0, 5);
			pLogin.add(new JLabel("Password"), gbcLPassword);
			
			GridBagConstraints gbcTfPassword = new GridBagConstraints();
			gbcTfPassword.gridx = 1; gbcTfPassword.gridy = 1;
			gbcTfPassword.weightx = 1; gbcTfPassword.fill = GridBagConstraints.HORIZONTAL;
			gbcTfPassword.gridwidth = 2;
			pLogin.add(getTfPassword(), gbcTfPassword);
			
			GridBagConstraints gbcBtLogin = new GridBagConstraints();
			gbcBtLogin.gridx = 1; gbcBtLogin.gridy = 2;
			gbcBtLogin.weightx = 1; 
			gbcBtLogin.fill = GridBagConstraints.HORIZONTAL;
			pLogin.add(getBtLogin(), gbcBtLogin);
			
//			GridBagConstraints gbcBtNew = new GridBagConstraints();
//			gbcBtNew.gridx = 2; gbcBtNew.gridy = 2;
//			gbcBtNew.weightx = 1; 
//			gbcBtNew.fill = GridBagConstraints.HORIZONTAL;
//			pLogin.add(getBtNew(), gbcBtNew);
			
			GridBagConstraints gbcLInfo = new GridBagConstraints();
			gbcLInfo.gridx = 0; gbcLInfo.gridy = 3;
			gbcLInfo.gridwidth = 3; gbcLInfo.insets = new Insets(0, 5, 0, 5);
			pLogin.add(getLInfoLogin(), gbcLInfo);
			
			checkLoginPassword();
		}
		return pLogin;
	}
	
	private JTextField tfLogin;
	public JTextField getTfLogin() {
		if (tfLogin == null) {
			tfLogin = new JTextField(aura.getCfgManager().getConfigStringValue(ConfigCM.SOCKET_LOGIN));
			tfLogin.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
			});
		}
		return tfLogin;
	}

	private JTextField tfPassword;
	public JTextField getTfPassword() {
		if (tfPassword == null) {
			tfPassword = new JTextField();
			tfPassword.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					checkLoginPassword();
				}
			});
		}
		return tfPassword;
	}
	
	private void checkLoginPassword() {
		boolean check = true;
		try {
//			User.V_LOGIN.validate(getTfLogin().getText());
//			User.V_PASSWORD.validate(getTfPassword().getText());
		} catch (Exception e) {
			check = false;
		}
		getBtLogin().setEnabled(check);
//		getBtNew().setEnabled(check);
	}
	
	private JButton btLogin;
	public JButton getBtLogin() {
		if (btLogin == null) {
			btLogin = new JButton("Login");
			btLogin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					PacketLoginAttempt packetLogin = (PacketLoginAttempt) aura.createPacket(PacketCM.USER_LOGIN_ATTEMPT);
					packetLogin.setLogin(getTfLogin().getText().trim());
					packetLogin.setPassword(getTfPassword().getText().trim());
					if (packetLogin.isValid()) {
						getLInfoLogin().setText("En attente de login.");
						aura.getNetworkManager().envoyerPaquet(packetLogin);
					} else {
						getLInfoLogin().setText("Combinaison login/mdp invalide.");
					}
				}
			});
		}
		return btLogin;
	}
	
//	private JButton btNew;
//	public JButton getBtNew() {
//		if (btNew == null) {
//			btNew = new JButton("New");
//			btNew.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					PacketLoginCreate packetNew = (PacketLoginCreate) AbstractPacketContainer.USER_LOGIN_CREATE.create();
//					packetNew.setLogin(getTfLogin().getText().trim());
//					packetNew.setPassword(getTfPassword().getText().trim());
//					if (packetNew.isValid()) {
//						getLInfoLogin().setText("En attente de création du login.");
//						aura.getNetworkManager().envoyerPaquet(packetNew);
//					} else {
//						getLInfoLogin().setText("Combinaison login/mdp invalide.");
//					}
//				}
//			});
//		}
//		return btNew;
//	}
	
	private JLabel lInfoLogin;
	public JLabel getLInfoLogin() {
		if (lInfoLogin == null) {
			lInfoLogin = new JLabel();
		}
		return lInfoLogin;
	}

	@Override
	public void doOnThreadCycle() {
		runServerInfo();
	}
	
	private void runServerInfo() {
		Long tickServeur = aura.getNetworkManager().getClientConnection().getTimeDeltaServer();
		if (tickServeur != null) {
			getLInfoServeur().setText("Date serveur: "+SDF.format(new Date(tickServeur)) + " Ping: " + aura.getNetworkManager().getClientConnection().getTimePingServer());
		}
	}
}
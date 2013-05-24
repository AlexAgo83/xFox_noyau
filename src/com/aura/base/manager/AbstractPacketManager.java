package com.aura.base.manager;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.thread.AbstractPacketManagerExecutorThread;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractPacketManager {
	private final Aura aura;
	public Aura getAura() {
		return aura;
	}
	
	private String user;
	public String getUser() {
		return user;
	}
	
	public void setUser(String u) {
		user = u;
	}
	
	private final Socket socket;
	public Socket getSocket() {
		return socket;
	}
	
	private final String hostNameInfo;
	private final int portInfo;
	public String getInfo() {
		return "["+hostNameInfo
				+":"+portInfo
				+(getUser() != null ? "@"
//						+ "{" + getUser().getId() + "}"
						+ getUser()
//						.getLogin() 
						: "")
				+"]";
	}
	
	private long lastPing;
	public long getLastPing() {
		return lastPing;
	}
	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}
	
	private boolean online;
	public boolean isOnline() {
		return online;
	}
	
	private DataInputStream input;
	private DataOutputStream output;
	
	private NetworkManagerReader reader;
	private NetworkManagerWriter writer;
	
	private AbstractPacketManagerExecutorThread executor;
	public void loadExecutor(AbstractPacketManagerExecutorThread executor) {
		this.executor = executor;
		this.executor.start();
	}
	
	private List<AbstractPacket> toSend;
	public synchronized List<AbstractPacket> getToSend() {
		if (toSend == null) {
			toSend = new ArrayList<AbstractPacket>();
		}
		return toSend;
	}
	
	private List<AbstractPacket> received = new ArrayList<AbstractPacket>();
	public synchronized List<AbstractPacket> getReceived() {
		if (received == null) {
			received = new ArrayList<AbstractPacket>();
		}
		return received;
	}
	
	public AbstractPacketManager(Aura aura, Socket socket) {
		this.aura = aura;
		this.socket = socket;
		this.hostNameInfo = socket.getInetAddress().getHostName();
		this.portInfo = socket.getPort();
		this.online = true;
		this.lastPing = -1l;
		
		try {
			socket.setTrafficClass(24);
		} catch (SocketException e) {
			AuraLogger.severe(getAura().getSide(), "Ce type de traffic, n'est pas géré sur ce système.", e);
		}
		try {
			socket.setSoTimeout(0);
			this.input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream(), 5120));
		} catch (java.io.IOException e) {
			AuraLogger.severe(getAura().getSide(), "La connection avec le socket " + getInfo() + " ne réponds plus.", e);
		}
		
		this.reader = new NetworkManagerReader(input);
			reader.start();
		this.writer = new NetworkManagerWriter(output);
			writer.start();
	}
	
	class NetworkManagerReader extends AuraThread {
		private final DataInputStream input;
		public NetworkManagerReader(DataInputStream input) {
			super(getAura(), "NetworkManagerReader", 0);
			this.input = input;
		}
		@Override
		public boolean condition() {
			return aura.isRunning() && isOnline();
		}
		@Override
		public void doOnClose() {
			try {
				input.close();
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Echec de la fermeture du stream de " + getInfo() + ".", e);
			}
		}
		@Override
		public void action() {
			if (socket.isClosed() || !socket.isConnected()) {
				deconnection(true);
			}
			try {
				AbstractPacket p = null;
				int curseur = input.readInt();
				p = getAura().createPacket(curseur);
				if (p != null) {
					p.read(getAura(), input);
					AuraLogger.finest(getAura().getSide(), "Lecture du paquet [" + getAura().getPacket(p.getId()) + "] en provenance de " + getInfo() + " détecté.");
					if (p.isValid()) {
						getReceived().add(p);
						AuraLogger.finest(getAura().getSide(), "Paquet [" + getAura().getPacket(p.getId()) + "] recu en provenance de " + getInfo() + ".");
					} else {
						AuraLogger.severe(getAura().getSide(), "Echec de l'enregistrement d'un paquet en provenance de " + getInfo() + ".");
					}
				} else {
					AuraLogger.severe(getAura().getSide(), "Paquet recu non conforme en provenance de " + getInfo() + ".");
				}
			} catch (EOFException e){
				AuraLogger.severe(getAura().getSide(), "Echec de lecture du paquet (EOF) en provenance de " + getInfo() + ".", e);
				deconnection(true);
			} catch (SocketException e) {
				AuraLogger.severe(getAura().getSide(), "Echec de lecture du paquet (Socket) en provenance de " + getInfo() + ".", e);
				deconnection(true);
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Echec de lecture du paquet (IO) en provenance de " + getInfo() + ".", e);
			}
		}
	}
	
	class NetworkManagerWriter extends AuraThread {
		private final DataOutputStream output;
		public NetworkManagerWriter(DataOutputStream output) {
			super(getAura(), "NetworkManagerWriter", 5);
			this.output = output;
		}
		@Override
		public boolean condition() {
			return aura.isRunning() && isOnline();
		}
		@Override
		public void doOnClose() {
			try {
				output.close();
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Echec de la fermeture du stream de " + getInfo() + ".");
			}
		}
		@Override
		public void action() {
			if (socket.isClosed() || !socket.isConnected()) {
				deconnection(true);
			}
			try {
				AbstractPacket[] ps = getToSend().toArray(new AbstractPacket[0]);
				for (AbstractPacket p : ps) {
					if (!isOnline()) {
						break;
					} else if (p != null && p.isValid()) {
						AuraLogger.finest(getAura().getSide(), "Paquet [" + getAura().getPacket(p.getId()) + "] en cours d'envoi à " + getInfo() + ".");
						if (p.write(getAura(), output)) {
							AuraLogger.finest(getAura().getSide(), "Paquet [" + getAura().getPacket(p.getId()) + "] envoyé à " + getInfo() + ".");
						} else {
							AuraLogger.severe(getAura().getSide(), "Paquet [" + getAura().getPacket(p.getId()) + "] non envoyé à " + getInfo() + ".");
						}
					} else {
						AuraLogger.severe(getAura().getSide(), "Paquet "
							+ (p != null ? "[" + getAura().getPacket(p.getId())+"]":"")
							+ " à envoyé non conforme pour " + getInfo() + ".");
					}
					AuraLogger.finest(getAura().getSide(), "Paquet [" + (p != null ? getAura().getPacket(p.getId())
							: "null?") + "] supprimé.");
					getToSend().remove(p);
				}
			} catch (SocketException e) {
				AuraLogger.severe(getAura().getSide(), "Echec de l'envoi du paquet (Socket) à " + getInfo() + ".", e);
				deconnection(true);
			}
		}
	}
	
	public void deconnection(boolean isErrorOrConnectionLost) {
		if (isOnline()) {
			online = false;
			try {
				getSocket().close();
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Exception levé lors de la déconnection du client " + getInfo(), e);
			}
			onDeconnection(this, isErrorOrConnectionLost);
		}
	}
	
	public abstract void onDeconnection(AbstractPacketManager pm, boolean isError);
	public abstract void onKick();
}
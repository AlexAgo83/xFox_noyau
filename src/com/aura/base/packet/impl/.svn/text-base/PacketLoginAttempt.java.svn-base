package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketLoginAttempt extends AbstractPacket {
	private String login;
	private String password;
	
	public PacketLoginAttempt(int id) {
		super(id);
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeUTF(getLogin());
			output.flush();
			output.writeUTF(getPassword());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du login/mdp [+" 
					+ getLogin() + "/" + getPassword() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setLogin(input.readUTF());
			setPassword(input.readUTF());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du login/mdp sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getLogin() != null && getPassword() != null;
	}
}
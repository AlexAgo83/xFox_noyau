package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketLoginResult extends AbstractPacket {
	private TypeLoginResult typeResult;
	private String login;
	
	public PacketLoginResult(int id) {
		super(id);
		this.typeResult = TypeLoginResult.KO;
	}

	public TypeLoginResult getTypeResult() {
		return typeResult;
	}
	public void setTypeResult(TypeLoginResult typeResult) {
		this.typeResult = typeResult;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeInt(getTypeResult().getId());
			output.flush();
			output.writeUTF(getLogin() != null ? getLogin() : "");
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du flag(Accepted) et/ou login [+" 
					+ getTypeResult() + ","
					+ getLogin() + ""
					+ "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTypeResult(TypeLoginResult.getById(input.readInt()));
			setLogin(input.readUTF());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du flag(Accepted) et/ou login sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getTypeResult() != null;
	}
}
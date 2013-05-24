package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketBuildRevision extends AbstractPacket {
	private String buildRevision; 
	public PacketBuildRevision(int id) {
		super(id);
	}
	
	public String getBuildRevision() {
		return buildRevision;
	}
	public void setBuildRevision(String revision) {
		this.buildRevision = revision;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeUTF(getBuildRevision());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du buildRevision [+" 
					+ getBuildRevision() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setBuildRevision(input.readUTF());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du buildRevision sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	@Override
	public boolean isValid() {
		return getBuildRevision() != null;
	}
}
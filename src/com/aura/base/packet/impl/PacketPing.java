package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketPing extends AbstractPacket {
	private long timeRequest = -1l;
	
	public PacketPing(int id) {
		super(id);
	}
	
	public long getTimeRequest() {
		return timeRequest;
	}
	private void setTimeRequest(long timeRequest) {
		this.timeRequest = timeRequest;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(System.currentTimeMillis());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du time request [+" 
					+ getTimeRequest() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTimeRequest(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du ping sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
}
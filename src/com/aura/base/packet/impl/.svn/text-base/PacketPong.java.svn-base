package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketPong extends AbstractPacket {
	private long timeRequest = -1l;
	private long timeResult = -1l;
	
	public PacketPong(int id) {
		super(id);
	}
	
	public long getTimeRequest() {
		return timeRequest;
	}
	public void setTimeRequest(PacketPing packetPing) {
		this.timeRequest = packetPing.getTimeRequest();
	}
	public void setTimeRequest(long timeRequest) {
		this.timeRequest = timeRequest;
	}

	public long getTimeResult() {
		return timeResult;
	}
	private void setTimeResult(long timeResult) {
		this.timeResult = timeResult;
	}
	
	public long getDelta() {
		return getTimeResult() - getTimeRequest();
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(getTimeRequest());
			output.flush();
			output.writeLong(System.currentTimeMillis());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du time request/result [+" 
					+ getTimeRequest() + "/" 
					+ getTimeResult() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTimeRequest(input.readLong());
			setTimeResult(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du pong sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getTimeRequest() != -1l;
	}
}
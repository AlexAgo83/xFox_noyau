package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class PacketServerHeartTick extends AbstractPacket {
	private long time;
	
	public PacketServerHeartTick(int id) {
		super(id);
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(getTime());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du temps [+" 
					+ getTime() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTime(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du temps sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return time != 0l;
	}
}
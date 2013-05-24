package com.aura.base.packet.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.TypeKick;
import com.aura.base.utils.AuraLogger;

public class PacketKick extends AbstractPacket {
	private TypeKick typeKick;
	public PacketKick(int id) {
		super(id);
	}
	
	public TypeKick getTypeKick() {
		return typeKick;
	}
	public void setTypeKick(TypeKick typeKick) {
		this.typeKick = typeKick;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeInt(getTypeKick().getId());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du TypeKick [+" 
					+ typeKick.getId() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTypeKick(TypeKick.getById(input.readInt()));
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du flag(Accepted) sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getTypeKick() != null;
	}
}
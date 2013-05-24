package com.aura.base.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractPacketObject<E> extends AbstractPacket {
	private E value;
	
	public AbstractPacketObject(int id) {
		super(id);
	}

	public E getValue() {
		return value;
	}
	public void setValue(E value) {
		this.value = value;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			if (getValue() instanceof String)
				output.writeUTF((String) getValue());
			if (getValue() instanceof Integer)
				output.writeInt((Integer) getValue());
			if (getValue() instanceof Boolean)
				output.writeBoolean((Boolean) getValue());
			if (getValue() instanceof Long)
				output.writeLong((Long) getValue());
			if (getValue() instanceof Float)
				output.writeFloat((Float) getValue());
			if (getValue() instanceof Double)
				output.writeDouble((Double) getValue());
			if (getValue() instanceof Short)
				output.writeShort((Short) getValue());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture de l'objet [+" 
					+ getValue() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
//	@Override
//	protected boolean implRead(DataInputStream input) {
//		try {
//			if (E instanceof String)
//				setValue(input.readUTF());
//			if (getValue() instanceof Integer)
//				setValue(input.readInt());
//			if (getValue() instanceof Boolean)
//				setValue(input.readBoolean());
//			if (getValue() instanceof Long)
//				setValue(input.readLong());
//			if (getValue() instanceof Float)
//				setValue(input.readFloat());
//		} catch (IOException e) {
//			AuraLogger.severe("Echec de lecture du text sur le stream du paquet ["
//					+ this.getClass() + "].", e);
//			return false;
//		}
//		return true;
//	}
	@Override
	public boolean isValid() {
		return getValue() != null;
	}
}
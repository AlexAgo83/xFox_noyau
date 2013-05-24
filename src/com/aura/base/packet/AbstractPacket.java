package com.aura.base.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractPacket {
	public AbstractPacket(int id) {
		this.id = id;
	}

	private final int id;
	public int getId() {
		return id;
	}
	
	public boolean write(Aura aura, DataOutputStream output) throws SocketException {
		if (selfWrite(aura, output)) {
			return implWrite(aura, output);
		}
		return false;
	}
	
	protected boolean selfWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeInt(getId());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture de l'id [+" 
					+ getId() + ":" + aura.getPacket(getId()) +  "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	public boolean read(Aura aura, DataInputStream input) throws SocketException {
		if (selfRead(aura, input)) {
			return implRead(aura, input);
		}
		return false;
	}
	public boolean selfRead(Aura aura, DataInputStream input) {
		// rien à faire pour le moment, vue que l'id est nativement initialisé.
		return true;
	}
	
	protected abstract boolean implWrite(Aura aura, DataOutputStream output);
	protected abstract boolean implRead(Aura aura, DataInputStream input); 
	public abstract boolean isValid();
	
    public String strToHexa(String texte) { 
        StringBuffer buff = new StringBuffer(); 
        for (int i = 0; i < texte.length(); i++) { 
            buff.append(Integer.toHexString(texte.charAt(i))).append("§"); 
        } 
        return buff.toString(); 
    }
    public String hexToStr(String hex){
    	StringBuffer buff = new StringBuffer();
    	if (hex != null && hex.trim().length() > 0) { 
	    	for (String t : hex.split("§")) {
	    		buff.append((char) Integer.parseInt(t, 16));
	    	}
	    	return buff.toString();
    	}
    	return hex;
    }
}
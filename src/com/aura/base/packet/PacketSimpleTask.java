package com.aura.base.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.aura.base.Aura;

public class PacketSimpleTask extends AbstractPacket {
	public PacketSimpleTask(int id) {
		super(id);
	}
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		return true;
	}
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		return true;
	}
	@Override
	public boolean isValid() {
		return true;
	}
}

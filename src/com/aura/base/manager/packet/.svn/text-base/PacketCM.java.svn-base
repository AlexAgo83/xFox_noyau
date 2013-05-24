package com.aura.base.manager.packet;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.PacketSimpleTask;
import com.aura.base.packet.impl.PacketBuildRevision;
import com.aura.base.packet.impl.PacketKick;
import com.aura.base.packet.impl.PacketLoginAttempt;
import com.aura.base.packet.impl.PacketLoginResult;
import com.aura.base.packet.impl.PacketPing;
import com.aura.base.packet.impl.PacketPong;
import com.aura.base.utils.Curseur;

public class PacketCM<A extends Aura> extends AbstractContainerManager<A, PacketCE> {
	public static Curseur CURSEUR = new Curseur(PacketCM.class);

	public final static int PING = CURSEUR.nextVal();
	public final static int PONG = CURSEUR.nextVal();
	
	public final static int BUILD_REVISION = CURSEUR.nextVal();
	public final static int BUILD_REVISION_OUTDATED = CURSEUR.nextVal();
	
	public final static int WELCOME = CURSEUR.nextVal();
	
	public final static int USER_LOGIN_ATTEMPT = CURSEUR.nextVal();
	public final static int USER_LOGIN_RESULT = CURSEUR.nextVal();
	public final static int USER_LOGIN_KICK = CURSEUR.nextVal();
	
	public PacketCM(A aura) {
		super(aura);
	}
	public PacketCM(PacketCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new PacketCE(PING, "PING") {@Override public AbstractPacket create(int id) { return new PacketPing(getId());}});
		register(new PacketCE(PONG, "PONG") {@Override public AbstractPacket create(int id) { return new PacketPong(getId());}});
		register(new PacketCE(BUILD_REVISION, "BUILD_REVISION") {@Override public AbstractPacket create(int id) { return new PacketBuildRevision(getId());}});
		register(new PacketCE(BUILD_REVISION_OUTDATED, "BUILD_REVISION_OUTDATED") {@Override public AbstractPacket create(int id) { return new PacketSimpleTask(getId());}});
		register(new PacketCE(WELCOME, "WELCOME") {@Override public AbstractPacket create(int id) { return new PacketSimpleTask(getId());}});
		register(new PacketCE(USER_LOGIN_ATTEMPT, "USER_LOGIN_ATTEMPT") {@Override public AbstractPacket create(int id) { return new PacketLoginAttempt(getId());}});
		register(new PacketCE(USER_LOGIN_RESULT, "USER_LOGIN_RESULT") {@Override public AbstractPacket create(int id) { return new PacketLoginResult(getId());}});
		register(new PacketCE(USER_LOGIN_KICK, "USER_LOGIN_KICK") {@Override public AbstractPacket create(int id) { return new PacketKick(getId());}});
	}

	public AbstractPacket createBy(int id) {
		if (getElement(id) == null)
			throw new NullPointerException("Key: "+id);
		return getElement(id).create(id);
	}
	
	@Override
	public PacketCE getElement(int id) {
		return super.getElement(id);
	}
	
	public String getLabel(int id) {
		if (getElement(id) == null)
			throw new NullPointerException("Key: "+id);
		return getElement(id).getLabel();
	}
}
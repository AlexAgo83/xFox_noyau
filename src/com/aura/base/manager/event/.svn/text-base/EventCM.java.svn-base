package com.aura.base.manager.event;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.impl.DefaultEvent;
import com.aura.base.event.impl.KickEvent;
import com.aura.base.event.impl.PacketManagerDeconnectionEvent;
import com.aura.base.utils.Curseur;

public class EventCM<A extends Aura> extends AbstractContainerManager<A, EventCE> {
	public static Curseur CURSEUR = new Curseur(EventCM.class);
	
	public final static int SYSTEM_LOAD = CURSEUR.nextVal();
	public final static int PONG = CURSEUR.nextVal();
	
	public final static int USER_NOTIFY = CURSEUR.nextVal();
	public final static int USER_OUTDATED = CURSEUR.nextVal();
	
	public final static int USER_CONNECT_OK = CURSEUR.nextVal();
	public final static int USER_CONNECT_KO = CURSEUR.nextVal();
	public final static int USER_CONNECT_ERR = CURSEUR.nextVal();
	
	public final static int USER_LOGIN_KICK = CURSEUR.nextVal();
	public final static int USER_LOGIN_OK = CURSEUR.nextVal();
	public final static int USER_LOGIN_KO = CURSEUR.nextVal();
	public final static int USER_LOGIN_BAD_PASSWORD = CURSEUR.nextVal();
	public final static int USER_LOGIN_NOT_EXIST = CURSEUR.nextVal();
	
	public EventCM(A aura) {
		super(aura);
	}
	public EventCM(EventCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new EventCE(SYSTEM_LOAD, "SYSTEM_LOAD") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(PONG, "PONG") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		
		register(new EventCE(USER_NOTIFY, "USER_NOTIFY") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(USER_OUTDATED, "USER_OUTDATED") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		
		register(new EventCE(USER_CONNECT_OK, "USER_CONNECT_OK") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(USER_CONNECT_KO, "USER_CONNECT_KO") {@Override public AbstractEvent create(int id) {return new PacketManagerDeconnectionEvent(getId());}});
		register(new EventCE(USER_CONNECT_ERR, "USER_CONNECT_ERR") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		
		register(new EventCE(USER_LOGIN_KICK, "USER_LOGIN_KICK") {@Override public AbstractEvent create(int id) {return new KickEvent(getId());}});
		register(new EventCE(USER_LOGIN_OK, "USER_LOGIN_OK") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(USER_LOGIN_KO, "USER_LOGIN_KO") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(USER_LOGIN_BAD_PASSWORD, "USER_LOGIN_BAD_PASSWORD") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		register(new EventCE(USER_LOGIN_NOT_EXIST, "USER_LOGIN_NOT_EXIST") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
	}

	public AbstractEvent createBy(int id) {
		return getElement(id).create(id);
	}
	public String getLabel(int id) {
		return getElement(id).getLabel();
	}
	@Override
	public EventCE getElement(int id) {
		return super.getElement(id);
	}
}
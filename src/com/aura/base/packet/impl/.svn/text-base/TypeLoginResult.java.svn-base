package com.aura.base.packet.impl;

import com.aura.base.manager.event.EventCM;

public enum TypeLoginResult {
	OK(0, EventCM.USER_LOGIN_OK),
	KO(1, EventCM.USER_LOGIN_KO),
	BAD_PASSWORD(2, EventCM.USER_LOGIN_BAD_PASSWORD),
	NOT_EXIST(10, EventCM.USER_LOGIN_NOT_EXIST);
	
	private int id;
	private int eventId;
	
	private TypeLoginResult(int id, int eventId) {
		this.id = id;
		this.eventId = eventId;
	}
	
	public int getId() {
		return id;
	}
	public int getEventId() {
		return eventId;
	}
	
	public static TypeLoginResult getById(int id) {
		for (TypeLoginResult t: TypeLoginResult.values()) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}
}

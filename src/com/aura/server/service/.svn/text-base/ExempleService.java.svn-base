package com.aura.server.service;

import com.aura.base.data.ExempleAuraData;
import com.aura.server.AuraServer;
import com.aura.server.AuraService;
import com.avaje.ebean.Query;

public class ExempleService extends AuraService<ExempleAuraData> {
	public ExempleService(AuraServer aura) {
		super(aura);
	}
	@Override
	protected Query<ExempleAuraData> implQuery() {
		return getDataBase().find(ExempleAuraData.class);
	}
	public ExempleAuraData getUserByLogin(String login) {
		Query<ExempleAuraData> q = implQuery().where("upper(trim(login)) = upper(trim(:login))");
			q.setParameter("login", login.trim().toUpperCase());
		return q.findUnique();
	}
	public ExempleAuraData createUser(String login, String password) {
		ExempleAuraData u = new ExempleAuraData();
		u.setLogin(login);
		u.setPassword(password);
		this.add(u);
		u = getUserByLogin(login);
		return  u;
	}
}
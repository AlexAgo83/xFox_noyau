package com.aura.server;

import java.util.List;

import com.aura.base.data.AuraData;
import com.avaje.ebean.Query;
import com.avaje.ebeaninternal.api.SpiEbeanServer;

public abstract class AuraService<D extends AuraData> {
	private AuraServer aura;
	public AuraService(AuraServer aura) {
		this.aura = aura;
	}
	protected AuraServer getAura() {
		return aura;
	}
	protected SpiEbeanServer getDataBase() {
		return getAura().getDataBaseManager().getDataBase();
	}
	protected abstract Query<D> implQuery();
	public List<D> getAll() {
		return implQuery().findList();
	}
	public D getById(int id) {
		return implQuery().where().idEq(id).findUnique();
	}
	public void add(D d) {
		getDataBase().insert(d);
	}
	public void upd(D d) {
		getDataBase().update(d);
	}
	public void rmv(D d) {
		getDataBase().delete(d);
	}
}
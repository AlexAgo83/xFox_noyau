package com.aura.base.container;

import com.aura.base.Aura;

public abstract class AbstractAuraContainer<A extends Aura> {
	public A a;
	public A getAura() {
		if (a == null)
			a = init();
		return a;
	}
	public abstract A init();
}

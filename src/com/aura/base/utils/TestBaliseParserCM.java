package com.aura.base.utils;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager;

public class TestBaliseParserCM<A extends Aura> extends AbstractContainerManager<A, TestBaliseParserCE<?>> {
	public static Curseur CURSEUR = new Curseur(TestBaliseParserCM.class);
	
	public static final int COLOR = CURSEUR.nextVal();

	public TestBaliseParserCM(A aura) {
		super(aura);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new TestBaliseParserCEColor(COLOR, "color", "c", "WHITE"));
	}
	
	@Override
	public TestBaliseParserCE<?> getElement(int id) {
		return super.getElement(id);
	}
}
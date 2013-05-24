package com.aura.base.manager.i18n;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.utils.Curseur;

public class I18nCM<A extends Aura> extends AbstractContainerManager<A, I18nCE>{
	public static Curseur CURSEUR = new Curseur(I18nCM.class);
	
//	public static final int MESSAGE_TEST = CURSEUR.nextVal();
	
	public I18nCM(A aura) {
		super(aura);
	}
	public I18nCM(I18nCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
//		register(new I18nCE(MESSAGE_TEST, "message.test"));
	}
	
	@Override
	public I18nCE getElement(int id) {
		return super.getElement(id);
	}
}
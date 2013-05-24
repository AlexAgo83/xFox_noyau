package com.aura.exemple.plugin;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.console.ConsoleCM;
import com.aura.base.utils.Curseur;

public class ExemplePluginConsoleContainerManager<A extends Aura> extends ConsoleCM<A> {
	public static Curseur CURSEUR = new Curseur(ExemplePluginConsoleContainerManager.class);
	
	public final static int TEST_PLUGIN_CONSOLE = CURSEUR.nextVal();
	
	public ExemplePluginConsoleContainerManager(ConsoleCM<A> selfManager) {
		super(selfManager);
	}
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new ConsoleCE(TEST_PLUGIN_CONSOLE, "TEST_PLUGIN_CONSOLE", "test", TypeSide.BOTH, 1));
	}
}
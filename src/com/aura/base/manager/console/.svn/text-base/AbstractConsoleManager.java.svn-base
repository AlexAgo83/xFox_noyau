package com.aura.base.manager.console;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.thread.ConsoleReaderThread;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractConsoleManager<A extends Aura> extends AbstractAuraManager<A> {
	private final ConsoleCM<A> containerManager;
	public ConsoleCM<A> getContainerManager() {
		return containerManager;
	}
	
	private ConsoleReaderThread<A> thread;
	public AbstractConsoleManager(A aura) {
		super(aura);
		containerManager = new ConsoleCM<A>(getAura());
		thread = new ConsoleReaderThread<A>(aura, this);
	}
	@Override
	public boolean load() {
		try {
			try {
				containerManager.load();
			} catch (KeyContainerAlreadyUsed exc) {
				exc.printLog(getAura().getSide());
				return false;
			}
			thread.start();
		} catch (IllegalThreadStateException e) {
			AuraLogger.warning(getAura().getSide(), "Thread du 'ConsoleManager' en erreur !", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Action sur saisie de commande dans la console 
	 */
	public synchronized void onConsoleCommand(String[] cmd) {
		if (getAura().isRunning() && getAura().isLoaded()) 
			implOnCommand(cmd);
	}
	public abstract void implOnCommand(String[] cmd);
	
	public String[] formatCommand(String ligne) {
		String[] cmd = ligne.split(" ");
		if (cmd[0].indexOf("/") == 0) {
			cmd[0] = cmd[0].substring(1).trim();
			return cmd;
		}
		return null;
	}
	
	public ConsoleCE getCommande(int id) {
		return getContainerManager().getElement(id);
	}
}
package com.aura.base.thread;

import java.io.IOException;
import java.io.PrintWriter;

import jline.ConsoleReader;

import com.aura.base.Aura;
import com.aura.base.manager.console.AbstractConsoleManager;
import com.aura.base.utils.AuraLogger;

public class ConsoleReaderThread<A extends Aura> extends AuraThread {
	private final AbstractConsoleManager<A> consoleManager;
	
	protected AbstractConsoleManager<A> getConsoleManager() {
		return consoleManager;
	}
	
	public ConsoleReaderThread(A aura, final AbstractConsoleManager<A> consoleManager) {
		super(aura, "ConsoleReaderThread", 100);
		this.consoleManager = consoleManager;
	}
	
	@Override
	public boolean condition() {
		return getConsoleManager().getAura().isRunning();
	}
	@Override
	public void doOnClose() {
		AuraLogger.config(getMainAura().getSide(), "Fermeture du thread d'écoute de la console.");
	}
	
	private String sCmd = null;
	private ConsoleReader bufferedReader; 
	
	@Override
	public void action() {
		try {
        	if (bufferedReader == null) {
        		AuraLogger.config(getMainAura().getSide(), "Initialisation du thread d'écoute de la console ...");
    			try {
    				bufferedReader = new ConsoleReader(
    						System.in, 
    						new PrintWriter(System.out));
    			} catch (IOException e) {
    				AuraLogger.severe(getMainAura().getSide(), "Le thread d'écoute de la console ne s'est pas initialisé.", e);
    				return;
    			}
    		}
        	sCmd = bufferedReader.readLine();
        	if (sCmd != null && sCmd.trim().length() > 0) {
        		String[] cmd = consoleManager.formatCommand(sCmd);
        		if (cmd != null) 
        			getConsoleManager().onConsoleCommand(cmd);
        	}
		} catch (IOException e) {
			AuraLogger.severe(getMainAura().getSide(), "Le thread d'écoute de la console s'est arreté.", e);
			forceTerminate();
		}
	}
}
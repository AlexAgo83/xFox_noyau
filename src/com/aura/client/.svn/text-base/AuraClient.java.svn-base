package com.aura.client;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.manager.configuration.AbstractConfigurationManager;
import com.aura.base.manager.console.AbstractConsoleManager;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.console.ConsoleCM;
import com.aura.base.manager.event.AbstractEventManager;
import com.aura.base.manager.i18n.AbstractI18nManager;
import com.aura.base.utils.AuraLogger;
import com.aura.client.manager.ClientNetworkManager;

public abstract class AuraClient extends Aura {
	private AbstractConsoleManager<AuraClient> consoleManager;
	@Override
	public AbstractConsoleManager<AuraClient> getConsoleManager() {
		if (consoleManager == null) 
			consoleManager = new AbstractConsoleManager<AuraClient>(this) {
				public void implOnCommand(String[] cmd) {
					if (!onCommand(cmd)) {
						for (Integer i : getPlugins().keySet()) 
							getPlugins().get(i).onCommand(cmd);
					}
				};
			};
		return consoleManager;
	}
	
	private AbstractI18nManager<AuraClient> i18nManager;
	@Override
	public AbstractI18nManager<AuraClient> getI18nManager() {
		if (i18nManager == null) {
			i18nManager = new AbstractI18nManager<AuraClient>(this);
		}
		return i18nManager;
	}
	
	private AbstractConfigurationManager<AuraClient> cfgManager;
	@Override
	public AbstractConfigurationManager<AuraClient> getCfgManager() {
		if (cfgManager == null) 
			cfgManager = new AbstractConfigurationManager<AuraClient>(this, 
				D_RESS + "\\" + D_CFG + "\\" + FC_CFG) {};
		return cfgManager;
	}
	
	private AbstractEventManager<AuraClient> eventManager;
	@Override
	public AbstractEventManager<AuraClient> getEventManager() {
		if (eventManager == null) 
			eventManager = new AbstractEventManager<AuraClient>(this); 
		return eventManager;
	}
	
	private ClientNetworkManager networkManager;
	@Override
	public ClientNetworkManager getNetworkManager() {
		if (networkManager == null) 
			networkManager = new ClientNetworkManager(this);
		return networkManager;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initManager(List<AbstractAuraManager> managers) {
	}
	
	public AuraClient() {
		this("XFOX_CLIENT", D_RESS + "\\" + D_LOG + "\\" + FC_LOG);
	}
	public AuraClient(String loggerInstance, String loggerFileName) {
		super(TypeSide.CLIENT, loggerInstance, loggerFileName);
		loaded();
	}
	
	@Override
	protected boolean onCommand(String[] cmd) {
		ConsoleCE cp = getConsoleManager().getContainerManager().dispatcher(TypeSide.CLIENT, cmd, true);
		if (cp == null)
			return false;
		
		if (cp.getId() == ConsoleCM.EXIT) {
			turnOff(true);
		} else if (cp.getId() == ConsoleCM.VERSION) {
			AuraLogger.info(getSide(), getBuildRevision());
		} else if (cp.getId() == ConsoleCM.USAGE) {
			promptUsageLog();
		}
		return false;
	}
	
	@Override
	public void log(Level info, int id, String args, Exception exc) {
		AuraLogger.log(getSide(), info, getI18nMessage(id, args), exc);		
	}
	
	@Override
	public void initFolder(String subFolder) {
		File dir = new File(D_RESS+"\\"+subFolder); 
		if (!dir.exists()) {
			AuraLogger.config(getSide(), "... création du dossier '" + subFolder + "'.");
			dir.mkdirs();	
		}	
	}
}
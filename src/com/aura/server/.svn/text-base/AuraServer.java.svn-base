package com.aura.server;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.configuration.AbstractConfigurationManager;
import com.aura.base.manager.console.AbstractConsoleManager;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.console.ConsoleCM;
import com.aura.base.manager.event.AbstractEventManager;
import com.aura.base.manager.i18n.AbstractI18nManager;
import com.aura.base.utils.AuraLogger;
import com.aura.server.manager.ServerDataBaseManager;
import com.aura.server.manager.ServerNetworkManager;

public abstract class AuraServer extends Aura {
	private AbstractConsoleManager<AuraServer> consoleManager;
	@Override
	public AbstractConsoleManager<AuraServer> getConsoleManager() {
		if (consoleManager == null) {
			consoleManager = new AbstractConsoleManager<AuraServer>(this) {
				public void implOnCommand(String[] cmd) {
					if (!onCommand(cmd)) {
						for (Integer i : getPlugins().keySet()) 
							getPlugins().get(i).onCommand(cmd);
					}
				};
			};
		}
		return consoleManager;
	}
	
	private AbstractConfigurationManager<AuraServer> cfgManager;
	@Override
	public AbstractConfigurationManager<AuraServer> getCfgManager() {
		if (cfgManager == null) 
			cfgManager = new AbstractConfigurationManager<AuraServer>(this, 
				D_RESS + "\\" + D_CFG + "\\" + FS_CFG) {};
		return cfgManager;
	}
	
	private AbstractI18nManager<AuraServer> i18nManager;
	@Override
	public AbstractI18nManager<AuraServer> getI18nManager() {
		if (i18nManager == null) {
			i18nManager = new AbstractI18nManager<AuraServer>(this);
		}
		return i18nManager;
	}
	
	private AbstractEventManager<AuraServer> eventManager;
	@Override
	public AbstractEventManager<AuraServer> getEventManager() {
		if (eventManager == null) 
			eventManager = new AbstractEventManager<AuraServer>(this); 
		return eventManager;
	}
	
	private ServerDataBaseManager dbManager;
	public ServerDataBaseManager getDataBaseManager() {
		if (dbManager == null) 
			dbManager = new ServerDataBaseManager(this);
		return dbManager;
	}
	
	private ServerNetworkManager networkManager;
	@Override
	public ServerNetworkManager getNetworkManager() {
		if (networkManager == null) 
			networkManager = new ServerNetworkManager(this);
		return networkManager;
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	public void initManager(List<AbstractAuraManager> managers) {
		managers.add(getDataBaseManager());
	}
	
	public AuraServer() {
		this("XFOX_SERVER", D_RESS + "\\" + D_LOG + "\\" + FS_LOG);
	}
	public AuraServer(String loggerInstance, String loggerFileName) {
		super(TypeSide.SERVER, loggerInstance, loggerFileName);
		loaded();
	}
	
	@Override
	protected boolean onCommand(String[] cmd) {
		ConsoleCE cp = getConsoleManager().getContainerManager().dispatcher(TypeSide.SERVER, cmd, true);
		if (cp == null)
			return false;
		
		if (cp.getId() == ConsoleCM.EXIT) {
			turnOff(true);
		} else if (cp.getId() == ConsoleCM.VERSION) {
			AuraLogger.info(getSide(), getBuildRevision());
		} else if (cp.getId() == ConsoleCM.USAGE) {
			promptUsageLog();
		} else if (cp.getId() == ConsoleCM.KICK_ALL) {
			AbstractPacketManager[] pmsall = getNetworkManager().getPacketManagerTab();
			for (AbstractPacketManager pm: pmsall) {
				String u = getNetworkManager().getConnectedUser(pm);
				if (u != null) {
					AuraLogger.info(getSide(), "Utilisateur [" + u + "] kick pour la socket " + pm.getInfo());
					pm.onKick();
				}
			}
		} else if (cp.getId() == ConsoleCM.KICK_USER) {
			AbstractPacketManager[] pmsku = getNetworkManager().getPacketManagerTab();
			for (AbstractPacketManager pm: pmsku) {
				String u = getNetworkManager().getConnectedUser(pm);
				if (u != null && u.equals(cmd[1].trim())) {
					AuraLogger.info(getSide(), "Utilisateur [" + u + "] kick pour la socket " + pm.getInfo());
					pm.onKick();
					break;
				}
			}
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
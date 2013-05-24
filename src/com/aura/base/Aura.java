package com.aura.base;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.aura.base.event.AbstractEvent;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.manager.configuration.AbstractConfigurationManager;
import com.aura.base.manager.configuration.ConfigCEBoolean;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.console.AbstractConsoleManager;
import com.aura.base.manager.event.AbstractEventManager;
import com.aura.base.manager.event.EventCE;
import com.aura.base.manager.event.EventCM;
import com.aura.base.manager.i18n.AbstractI18nManager;
import com.aura.base.manager.i18n.I18nMessage;
import com.aura.base.manager.packet.AbstractNetworkManager;
import com.aura.base.manager.packet.PacketCE;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Curseur;

// FIXME XF(AA) [IO] Créer les dossiers ext. automatiquement (ress/...)
// FIXME XF(AA) [Plugin] mettre en place version de plugin, pour ne plus monter le numero du noyau sans raisons

@SuppressWarnings("rawtypes")
public abstract class Aura {
	
	// DEFAULT RESS. PROPERTIES
	public final static String D_RESS = "ress";
	public final static String D_LOG = "log";
	public final static String D_CFG = "cfg";
	public final static String D_DB = "db";
	public final static String D_I18N = "i18n";
	
	public final static String FC_CFG = "client.cfg";
	public final static String FC_LOG = "client.log";
	
	public final static String FS_CFG = "server.cfg";
	public final static String FS_LOG = "server.log";
	public final static String FS_DB = "server.db";
	
	private String buildRevision = "xFox-0.6.3";
	public String getBuildRevision() {
		return buildRevision;
	}
	public void setBuildRevision(String buildRevision) {
		this.buildRevision = buildRevision;
	}
	
	private final TypeSide side;
	public TypeSide getSide() {
		return side;
	}
	
	public boolean running = false;
	public boolean isRunning() {
		return running;
	}
	public void turnOff(boolean exit) {
		this.running = false;
		AuraLogger.info(getSide(), "Fermeture du programme détectée.");
		try {
			promptUsageLog();
		} catch (Exception e) {
			AuraLogger.severe(getSide(), "Impossible d'afficher le log SYSTEM.", e);
		}
		try {
			getCfgManager().getLoader().saveFile();
		} catch (IOException e) {
			AuraLogger.severe(getSide(), "Impossible de sauvegarder le fichier config.", e);
		}
		if (exit)
			System.exit(0);
	}
	
	private boolean loaded = false;
	public boolean isLoaded() {
		return loaded;
	}
	public void loaded() {
		this.loaded = true;
	}
	
	protected abstract boolean onCommand(String[] cmd); 
	
	public abstract AbstractConfigurationManager getCfgManager();
	public abstract AbstractI18nManager getI18nManager();
	public abstract AbstractEventManager getEventManager();
	public abstract AbstractNetworkManager getNetworkManager();
	public abstract AbstractConsoleManager getConsoleManager();

	private List<AbstractAuraManager> managers;
	private List<AbstractAuraManager> getManagers() {
		if (managers == null) {
			managers = new ArrayList<AbstractAuraManager>();
			managers.add(getCfgManager());
			managers.add(getI18nManager());
			managers.add(getEventManager());
			managers.add(getNetworkManager());
			managers.add(getConsoleManager());
			initManager(managers);
		}
		return managers;
	}
	public abstract void initManager(List<AbstractAuraManager> managers);
	
	private Map<Integer, AbstractAuraPlugin> plugins;
	public Map<Integer, AbstractAuraPlugin> getPlugins() {
		if (plugins == null) {
			plugins = new HashMap<Integer, AbstractAuraPlugin>();
			initPlugin(this);
		}
		return plugins;
	}
	public abstract void initPlugin(final Aura self);
	public void addPlugin(AbstractAuraPlugin plugin) {
		getPlugins().put(plugin.getId(), plugin);
	}
	public void rmvPlugin(AbstractAuraPlugin plugin) {
		getPlugins().remove(plugin.getId());
	}
	
	public Aura(TypeSide side, String loggerInstance, String loggerFileName) {
		AuraLogger.preInitLogger(side, loggerInstance, loggerFileName);
		AuraLogger.config(getSide(), "Loading...");

		initFolder(D_CFG);
		initFolder(D_DB);
		initFolder(D_I18N);
		initFolder(D_LOG);
		
		this.running = true;
		this.side = side;
		
		for (AbstractAuraManager m : getManagers()) {
			AuraLogger.config(getSide(), "Mount '"+m.getClass().getName()+"' manager...");
			if (!m.load()) {
				AuraLogger.severe(getSide(), "Erreur durant le chargement du manager: " + m.getClass());
				turnOff(true);
			}
		}
		
		for (Integer i : getPlugins().keySet()) {
			AbstractAuraPlugin<?> p = getPlugins().get(i);
			AuraLogger.config(getSide(), "Mount '"+p.getClass().getName()+"' plugin...");
			if (!p.load()) {
				AuraLogger.severe(getSide(), "Erreur durant le chargement du plugin: " + p.getClass());
				turnOff(true);
			}
		}
		
		AuraLogger.postInitLogger(side);
		
		AuraLogger.setLevel(
				((ConfigCEBoolean) getCfgManager().getConfig(ConfigCM.VERBOSE_ACTIF)).getValue() ? 
					Level.ALL : 
					Level.SEVERE);
		
		this.loaded = true;
		getEventManager().addEvent(createEvent(EventCM.SYSTEM_LOAD));
	}
	
	public void promptUsageLog() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		
		final List<String> msg = new ArrayList<String>();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
				    value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
				    value = e;
				}
				msg.add(method.getName() + "() = " + value);
			}
		} 
		
		for (String str: msg) {
			AuraLogger.config(getSide(), "[UC]" + str);
		}
		
		AuraThread.promptAllUsages();
		Curseur.promptDebug(getSide());
	}

	/**
	 * ACTIONS RAPIDES 
	 */
	public PacketCE getPacket(int id) {
		return getNetworkManager().getContainerManager().getElement(id);
	}
	public AbstractPacket createPacket(int id) {
		return getNetworkManager().getContainerManager().createBy(id);
	}
	
	public EventCE getEvent(int id) {
		return getEventManager().getContainerManager().getElement(id);
	}
	public AbstractEvent createEvent(int id) {
		return getEventManager().getContainerManager().createBy(id);
	}
	
	public I18nMessage getI18nMessage(int id) {
		return getI18nManager().getI18nMessage(id);
	}
	public String getI18nMessage(int id, String...args) {
		return getI18nManager().getI18nMessage(id, args);
	}
	public void log(Level info, int id, String args) {
		log(info, id, args, null);
	}
	public abstract void log(Level info, int id, String args, Exception exc);

	public abstract void initFolder(String subFolder);
}
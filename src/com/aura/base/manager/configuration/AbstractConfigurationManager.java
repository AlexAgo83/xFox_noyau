package com.aura.base.manager.configuration;

import java.io.File;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.base.configuration.YamlConfiguration;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.utils.AuraLogger;

public abstract class AbstractConfigurationManager<A extends Aura> extends AbstractAuraManager<A> {
	private File fCfg;
	
	private YamlConfiguration ymlConfiguration;
	public YamlConfiguration getYmlConfiguration() {
		return ymlConfiguration;
	}
	
	private PropertiesLoader loader;
	public PropertiesLoader getLoader() {
		return loader;
	}
	
	private ConfigCM<A> containerManager;
	public ConfigCM<A> getContainerManager() {
		return containerManager;
	}
	
	public AbstractConfigurationManager(A aura, String fileName) {
		super(aura);
		containerManager = new ConfigCM<A>(getAura());
		fCfg = new File(fileName);
		try {
			ymlConfiguration = YamlConfiguration.loadConfiguration(getAura(), fCfg);
			loader = new PropertiesLoader(getAura(), fCfg, ymlConfiguration);
		} catch (VerifyError ve) {
			AuraLogger.info(getSide(), "Ce system ne supporte pas snakeYaml", ve);
			loader = new PropertiesLoader(getAura());
		}
	}
	
	@Override
	public boolean load() {
		AuraLogger.config(getSide(), "Initialisation des paramètres de configuration.");
		try {
			containerManager.load();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getSide());
			return false;
		}
		try {
			Integer[] ids = containerManager.getContainerKeyTab();
			for (Integer id: ids) {
				ConfigCE<?> ce = containerManager.getElement(id);
				if (ce.getSide() == TypeSide.BOTH || ce.getSide() == getSide())
					ce.load(getLoader());
			}
			AuraLogger.initFiltres(this);
		} catch (PropertiesLoaderException e) {
			AuraLogger.severe(getSide(), "Err. de fichier properties", e);
			return false;
		}
		return true;
	}
	
	public void save(int id) {
		save(getContainerManager().getElement(id));
	}
	public void save(ConfigCE<?> ce) {
		try {
			ce.save(getLoader());
		} catch (PropertiesLoaderException e) {
			AuraLogger.severe(getSide(), "Impossible d'enregistrer la modification sur " + ce.getLabel(), e);
		}
	}
	
	public ConfigCE<?> getConfig(int id) {
		return getContainerManager().getElement(id);
	}
	public boolean getConfigBooleanValue(int id) {
		return ((ConfigCEBoolean) getContainerManager().getElement(id)).getValue();
	}
	public void setConfigBooleanValue(int id, boolean value, boolean avecSave) {
		ConfigCEBoolean c = (ConfigCEBoolean) getContainerManager().getElement(id);
		c.setValue(value);
		if (avecSave) {
			save(c);
		}
	}
	public Integer getConfigIntegerValue(int id) {
		return ((ConfigCEInteger) getContainerManager().getElement(id)).getValue();
	}
	public void setConfigIntegerValue(int id, Integer value, boolean avecSave) {
		ConfigCEInteger c = (ConfigCEInteger) getContainerManager().getElement(id);
		c.setValue(value);
		if (avecSave) {
			save(c);
		}
	}
	public String getConfigStringValue(int id) {
		return ((ConfigCEString) getContainerManager().getElement(id)).getValue();
	}
	public void setConfigStringValue(int id, String value, boolean avecSave) {
		ConfigCEString c = (ConfigCEString) getContainerManager().getElement(id);
		c.setValue(value);
		if (avecSave) {
			save(c);
		}
	}
}
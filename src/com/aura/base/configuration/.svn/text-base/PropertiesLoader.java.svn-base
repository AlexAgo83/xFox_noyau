package com.aura.base.configuration;

import java.io.File;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;

public class PropertiesLoader {
	private final Aura aura;
	private boolean lazyMode = false;
	private final File fs; 
	private final YamlConfiguration yCfg;
	
	public PropertiesLoader(Aura aura) {
		this(aura, null, null);
		this.lazyMode = true;
	}
	public PropertiesLoader(Aura aura, File fs, YamlConfiguration yCfg) {
		this.aura = aura;
		this.fs = fs;
		this.yCfg = yCfg;
	}
	
	public Aura getAura() {
		return aura;
	}
	
	public void saveString(String tag, String value) throws PropertiesLoaderException {
		if (lazyMode) return;
		if (value != null)
			yCfg.set(tag, value);
	}
	public String loadString(String tag) throws PropertiesLoaderException {
		if (lazyMode) return null;
		return loadString(tag, null);
	}
	public String loadString(String tag, String defaultValue) throws PropertiesLoaderException {
		if (lazyMode) return defaultValue;
		if (!yCfg.isSet(tag) && defaultValue != null) {
			yCfg.set(tag, defaultValue);
			try {
				saveFile();
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Cannot save " + fs, e);
				throw new PropertiesLoaderException(tag);
			}
		}
		return yCfg.getString(tag);
	}
	
	public void saveInt(String tag, Integer value) throws PropertiesLoaderException {
		if (lazyMode) return;
		if (value != null)
			yCfg.set(tag, value.intValue());
	}
	public int loadInt(String tag) throws PropertiesLoaderException {
		if (lazyMode) return 0;
		return loadInt(tag, null);
	}
	public int loadInt(String tag, Integer defaultValue) throws PropertiesLoaderException {
		if (lazyMode) return defaultValue;
		if (!yCfg.isSet(tag) && defaultValue != null) {
			yCfg.set(tag, defaultValue.intValue());
			try {
				saveFile();
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Cannot save " + fs, e);
				throw new PropertiesLoaderException(tag);
			}
		}
		return yCfg.getInt(tag);
	}
	
	public void saveDouble(String tag, Double value) throws PropertiesLoaderException {
		if (lazyMode) return;
		if (value != null)
			yCfg.set(tag, value.intValue());
	}
	public double loadDouble(String tag) throws PropertiesLoaderException {
		if (lazyMode) return 0;
		return loadDouble(tag, null);
	}
	public double loadDouble(String tag, Double defaultValue) throws PropertiesLoaderException {
		if (lazyMode) return defaultValue;
		if (!yCfg.isSet(tag) && defaultValue != null) {
			yCfg.set(tag, defaultValue.intValue());
			try {
				saveFile();
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Cannot save " + fs, e);
				throw new PropertiesLoaderException(tag);
			}
		}
		return yCfg.getDouble(tag);
	}
	
	public void saveBoolean(String tag, Boolean value) throws PropertiesLoaderException {
		if (lazyMode) return;
		if (value != null)
			yCfg.set(tag, value.booleanValue());
	}
	public boolean loadBoolean(String tag) throws PropertiesLoaderException {
		if (lazyMode) return false;
		return loadBoolean(tag, null);
	}
	public boolean loadBoolean(String tag, Boolean defaultValue) throws PropertiesLoaderException {
		if (lazyMode) return defaultValue;
		if (!yCfg.isSet(tag) && defaultValue != null) {
			yCfg.set(tag, defaultValue.booleanValue());
			try {
				saveFile();
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Cannot save " + fs, e);
				throw new PropertiesLoaderException(tag);
			}
		}
		return yCfg.getBoolean(tag);
	}

	public static class PropertiesLoaderException extends Exception {
		private static final long serialVersionUID = 1L;
		public PropertiesLoaderException(String tag) {
			super(tag);
		}
	}
	public void saveFile() throws IOException {
		if (lazyMode) return;
		yCfg.save(fs);
	}
}
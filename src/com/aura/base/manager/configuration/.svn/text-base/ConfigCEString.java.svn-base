package com.aura.base.manager.configuration;

import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;

public class ConfigCEString extends ConfigCE<String> {
	public ConfigCEString(int id, String label, TypeSide side, String defaultValue) {
		super(id, label, side, defaultValue);
	}
	
	@Override
	public void load(PropertiesLoader loader) throws PropertiesLoaderException {
		setValue(loader.loadString(getLabel(), getDefaultValue()));
	}
	@Override
	public void save(PropertiesLoader loader) throws PropertiesLoaderException {
		loader.saveString(getLabel(), getValue());
	}
}

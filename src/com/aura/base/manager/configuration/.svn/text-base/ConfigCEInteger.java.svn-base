package com.aura.base.manager.configuration;

import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;

public class ConfigCEInteger extends ConfigCE<Integer> {
	public ConfigCEInteger(int id, String label, TypeSide side, Integer defaultValue) {
		super(id, label, side, defaultValue);
	}
	
	@Override
	public void load(PropertiesLoader loader) throws PropertiesLoaderException {
		setValue(loader.loadInt(getLabel(), getDefaultValue()));
	}
	@Override
	public void save(PropertiesLoader loader) throws PropertiesLoaderException {
		loader.saveInt(getLabel(), getValue());
	}
}

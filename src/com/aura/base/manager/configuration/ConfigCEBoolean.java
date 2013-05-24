package com.aura.base.manager.configuration;

import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;

public class ConfigCEBoolean extends ConfigCE<Boolean> {
	public ConfigCEBoolean(int id, String label, TypeSide side, boolean defaultValue) {
		super(id, label, side, defaultValue);
	}
	
	@Override
	public void load(PropertiesLoader loader) throws PropertiesLoaderException {
		setValue(loader.loadBoolean(getLabel(), getDefaultValue()));
	}
	@Override
	public void save(PropertiesLoader loader) throws PropertiesLoaderException {
		loader.saveBoolean(getLabel(), getValue());
	}
}

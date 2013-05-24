package com.aura.base.manager.configuration;

import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.base.container.AbstractContainerElement;

public abstract class ConfigCE<E> extends AbstractContainerElement {
	private TypeSide side;
	private E value;
	private E defaultValue;
	
	public ConfigCE(int id, String label, TypeSide side, E defaultValue) {
		super(id, label);
		this.side = side;
		this.defaultValue = defaultValue;
	}
	
	public TypeSide getSide() {
		return side;
	}
	
	public void setValue(E value) {
		this.value = value;
	}
	@SuppressWarnings("unchecked")
	public ConfigCE<E> setObject(Object value) {
		this.value = (E) value;
		return this;
	}
	
	public E getValue() {
		return value;
	}
	public String getValueToString() {
		return (String) value;
	}
	public Integer getValueToInteger() {
		return (Integer) value;
	}
	public Boolean getValueToBoolean() {
		return (Boolean) value;
	}
	
	public E getDefaultValue() {
		return defaultValue;
	}
	
	public abstract void load(PropertiesLoader loader) throws PropertiesLoaderException;
	public abstract void save(PropertiesLoader loader) throws PropertiesLoaderException;
}
package com.aura.base.container;

import java.util.HashMap;
import java.util.Map;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Validate;

public abstract class AbstractContainerManager<A extends Aura, E extends AbstractContainerElement> {

	private A aura;
	public A getAura() {
		return getSelf() != null ? getSelf().getAura() : aura;
	}
	
	private AbstractContainerManager<A, E> selfManager;
	public AbstractContainerManager<A, E> getSelf() {
		return selfManager;
	}
	
	private Map<Integer, E> containers;
	private Map<Integer, E> getContainers() {
		if (selfManager != null)
			return selfManager.getContainers();
		if (containers == null) 
			containers = new HashMap<Integer, E>();
		return containers;
	}
	public Integer[] getContainerKeyTab() {
		return getContainers().keySet().toArray(new Integer[0]);
	}
	
	public E getElement(int id) {
		return getContainers().get(id);
	}
	
	public AbstractContainerManager(A aura) {
		this.aura = aura;
	}
	
	public AbstractContainerManager(AbstractContainerManager<A, E> selfManager) {
		this.selfManager = selfManager;
	}

	private boolean loaded;
	public boolean isLoaded() {
		return loaded;
	}
	
	public void load() throws KeyContainerAlreadyUsed {
		String msg = "Mount '"+getClass().getName()+"' container...";
		if (getAura() != null)
			AuraLogger.config(getAura().getSide(), msg);
		else 
			System.out.println(msg);
		if (!isLoaded()) {
			implLoad();
			loaded = true;
		} else { 
			AuraLogger.severe(getAura().getSide(), getClass().getName() + " déja chargé.");
		}
	}
	
	protected abstract void implLoad() throws KeyContainerAlreadyUsed;
	
	public void override(E newElement) throws KeyContainerAlreadyUsed {
		Validate.notNull(newElement);
		getContainers().put(newElement.getId(), newElement);
	}
	public void register(E newElement) throws KeyContainerAlreadyUsed {
		Validate.notNull(newElement);
		if (getContainers().containsKey(newElement.getId())) {
			E oldElement = getElement(newElement.getId());
			throw new KeyContainerAlreadyUsed(
					newElement.getId(), 
					this.getClass().getName(), 
					oldElement.getLabel(), 
					newElement.getLabel());
		} else {
			Integer[] ids = getContainerKeyTab();
			for (Integer id: ids) {
				E oldElement = getElement(id);
				if (oldElement.getLabel().equals(newElement.getLabel())) {
					throw new KeyContainerAlreadyUsed(
							newElement.getId(), 
							this.getClass().getName(), 
							oldElement.getLabel(), 
							newElement.getLabel());
				}
			}
		}
		getContainers().put(newElement.getId(), newElement);
//		System.out.println(getClass()+ " " +newElement.getId() + " " + newElement.getLabel());
	}
	
	public static class KeyContainerAlreadyUsed extends Exception {
		private static final long serialVersionUID = 1L;
		private int id;
		private String clazzName;
		private String oldElement;
		private String newElement;
		public KeyContainerAlreadyUsed(int id, String clazzName, String oldElement, String newElement) {
			super(clazzName + " [err] Key: "+id + " , old: " + oldElement + " new: " + newElement);
			this.id = id;
			this.clazzName = clazzName;
			this.oldElement = oldElement;
			this.newElement = newElement;
		}
		public void printLog(TypeSide side) {
			AuraLogger.severe(side, clazzName + " err : "
				+ "[id: " + id + ", old: " + oldElement + " label: " + newElement 
				+ "] valeur déjà utilisée.", this);
		}
	}
}
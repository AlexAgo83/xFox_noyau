package com.aura.base.utils;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.TypeSide;

public class Curseur {
	private final static List<Curseur> curseursMemory = new ArrayList<Curseur>();
	
	public final Class<?> clazz;
	public Integer key = 0;
	
	public Curseur(Class<?> clazz) {
		this.clazz = clazz;
		curseursMemory.add(this);
	}
	public Integer nextVal() {
		key = key + 1;
		return key;
	}
	
	public static void promptDebug(TypeSide side) {
		AuraLogger.config(side, "Cursor Mem. , count: " + curseursMemory.size());
		for (Curseur cc: curseursMemory) {
			AuraLogger.config(side, "Class: " + cc.clazz.getName() + " lastKey: " + cc.key);
		}
	}
}
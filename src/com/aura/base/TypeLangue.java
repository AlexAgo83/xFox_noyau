package com.aura.base;

public enum TypeLangue {
	DEFAULT(0, "fr");
	
	private int id;
	private String tag;
	
	private TypeLangue(int id, String tag) {
		this.id = id;
		this.tag = tag;
	}
	
	public int getId() {
		return id;
	}
	public String getTag() {
		return tag;
	}
	
	public static TypeLangue getById(int id) {
		for (TypeLangue l: TypeLangue.values()) {
			if (l.getId() == id)
				return l;
		}
		return null;
	}
}
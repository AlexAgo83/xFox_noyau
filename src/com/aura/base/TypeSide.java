package com.aura.base;

public enum TypeSide {
	CLIENT("C"),
	SERVER("S"),
	BOTH("#");
	
	private String tag;
	private TypeSide(String tag) {
		this.tag = tag;
	}
	public String getTag() {
		return tag;
	}
}

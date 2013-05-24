package com.aura.base.packet;

public enum TypeKick {
	DEFAULT(0),
	FORCE(1),
	DEJA_CONNECTE(2);
	
	private int id;
	private TypeKick(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public static TypeKick getById(int id) {
		for (TypeKick type : TypeKick.values()) {
			if (type.getId() == id)
				return type;
		}
		return null;
	}
}

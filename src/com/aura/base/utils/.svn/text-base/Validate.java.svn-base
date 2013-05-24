package com.aura.base.utils;

public class Validate {
	public static void notNull(Object o) {
		if (o == null)
			throw new NullPointerException();
	}
	public static Class<?> notNull(Class<?> o) {
		if (o == null)
			throw new NullPointerException();
		return o;
	}
	public static Class<?> notNull(Class<?> o, String prop) {
		if (o == null)
			throw new NullPointerException(prop);
		return o;
	}
}

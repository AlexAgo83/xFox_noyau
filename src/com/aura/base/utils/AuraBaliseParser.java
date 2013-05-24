package com.aura.base.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIXME XF(AA) [BaliseParser] à faire en container ici aussi
public enum AuraBaliseParser {
	COLOR("c", "WHITE");

	private String baliseCode;
	private String defaultBaliseInfo;

	private AuraBaliseParser(String baliseCode, String defaultBaliseInfo) {
		this.baliseCode = baliseCode;
		this.defaultBaliseInfo = defaultBaliseInfo;
	}
	
	public String getBaliseCode() {
		return baliseCode;
	}
	public String getDefaultBaliseInfo() {
		return defaultBaliseInfo;
	}
	
	public String getBalise(String baliseInfo) {
		return getBaliseStart()+baliseInfo+getBaliseEnd();
	}
	public String getBaliseStart() {
		return "<"+getBaliseCode()+"=";
	}
	public String getBaliseEnd() {
		return "/>";
	}
	
	private static Map<String, Color> colors = new HashMap<String, Color>();
	public static Color getColor(String name) {
		Color c = colors.get(name);
		if (c == null) {
			try {
			    Field field = Class.forName("java.awt.Color").getField(name);
			    c = (Color) field.get(null);
			    colors.put(name, c);
			} catch (Exception e) {
			    e.printStackTrace();
			    colors.put(name, getColor(COLOR.getDefaultBaliseInfo()));
			}
		}
		return c;
	}
	
	public ColorText[] formatText(String text) {
		String[] splitStart = text.split(getBaliseStart());
		List<ColorText> newText = new ArrayList<ColorText>();
		if (splitStart.length > 1) {
			newText.add(new ColorText(null, splitStart[0]));
			for (int i=1; i<splitStart.length; i++) {
				String[] splitEnd = splitStart[i].split(getBaliseEnd());
				if (splitEnd.length > 1) {
					newText.add(new ColorText(getColor(splitEnd[0]), splitEnd[1]));
//				} else {
//					newText.add(new ColorText(null, splitEnd[1]));
				}
			}
		} else {
			newText.add(new ColorText(null, text));
		}
		return newText.toArray(new ColorText[0]);
	}
	
	public class ColorText {
		public Color color;
		public String text;
		public ColorText(Color color, String text) {
			this.color = color;
			this.text = text;
		}
	}
}
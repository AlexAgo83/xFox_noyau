package com.aura.base.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBaliseParserCEColor extends TestBaliseParserCE<Color> {
	
	private String baliseCode;
	private String defaultBaliseInfo;
	
	public TestBaliseParserCEColor(int id, String label, String baliseCode, String defaultBaliseInfo) {
		super(id, label, baliseCode, defaultBaliseInfo);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public TextBox<Color>[] formatText(String text) {
		String[] splitStart = text.split(getBaliseStart());
		List<TextBox<Color>> newText = new ArrayList<TextBox<Color>>();
		if (splitStart.length > 1) {
			newText.add(new TextBox<Color>(null, splitStart[0]));
			for (int i=1; i<splitStart.length; i++) {
				String[] splitEnd = splitStart[i].split(getBaliseEnd());
				if (splitEnd.length > 1) {
					newText.add(new TextBox<Color>(getColor(splitEnd[0]), splitEnd[1]));
				}
			}
		} else {
			newText.add(new TextBox<Color>(null, text));
		}
		return newText.toArray(new TextBox[0]);
	}
	
	private static final Map<String, Color> colors = new HashMap<String, Color>();
	public Color getColor(String name) {
		Color c = colors.get(name);
		if (c == null) {
			try {
			    Field field = Class.forName("java.awt.Color").getField(name);
			    c = (Color) field.get(null);
			    colors.put(name, c);
			} catch (Exception e) {
			    e.printStackTrace();
			    colors.put(name, getColor(getDefaultBaliseInfo()));
			}
		}
		return c;
	}
}
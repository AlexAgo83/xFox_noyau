package com.aura.base.utils;

import com.aura.base.container.AbstractContainerElement;

public abstract class TestBaliseParserCE<E> extends AbstractContainerElement {
	
	private String baliseCode;
	private String defaultBaliseInfo;
	
	public TestBaliseParserCE(int id, String label, String baliseCode, String defaultBaliseInfo) {
		super(id, label);
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
	
	public abstract TextBox<E>[] formatText(String text);
	public static class TextBox<F> {
		public F value;
		public String text;
		public TextBox(F value, String text) {
			this.value = value;
			this.text = text;
		}
	}
}
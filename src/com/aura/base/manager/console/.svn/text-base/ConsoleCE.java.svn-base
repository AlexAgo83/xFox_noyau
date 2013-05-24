package com.aura.base.manager.console;

import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerElement;

public class ConsoleCE extends AbstractContainerElement {
	
	private final String balise;
	private final TypeSide side;
	private final int minArgs;
	private final String description;
	
	public ConsoleCE(int id, String label, String balise, TypeSide side, int minArgs) {
		this(id, label, balise, side, minArgs, "");
	}
	public ConsoleCE(int id, String label, String balise, TypeSide side, int minArgs, String description) {
		super(id, label);
		this.balise = balise;
		this.side = side;
		this.minArgs = minArgs;
		this.description = description;
	}
	
	public String getBalise() {
		return balise;
	}
	public TypeSide getSide() {
		return side;
	}
	public int getMinArgs() {
		return minArgs;
	}
	public String getDescription() {
		return description;
	}
}
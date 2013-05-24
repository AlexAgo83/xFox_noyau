package com.aura.base.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractValidator {
	private Pattern pattern;
	private Matcher matcher;
	public AbstractValidator(String schemPattern){
		pattern = Pattern.compile(schemPattern);
	}
	public void validate(final String value){
		matcher = pattern.matcher(value);
		if (!matcher.matches())
			throw new IllegalArgumentException();
	}
}
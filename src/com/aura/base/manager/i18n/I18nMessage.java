package com.aura.base.manager.i18n;


public class I18nMessage {
	private final String resultMessage;

	public I18nMessage(String resultMessage) throws I18nIdNotExistException {
		if (resultMessage == null)
			throw new I18nIdNotExistException();
		this.resultMessage = resultMessage;
	}
	
	public String getMessage(String...args) {
		String msg = resultMessage;
		if (args != null && args.length > 0) {
			for (int i=0; i<args.length; i++) {
				msg = msg.replaceAll("{"+i+"}", args[i]);
			}
		}
		return msg;
	}
	
	public static class I18nIdNotExistException extends Exception {
		private static final long serialVersionUID = 1L;
		public I18nIdNotExistException() {
			super();
		}
	}
}
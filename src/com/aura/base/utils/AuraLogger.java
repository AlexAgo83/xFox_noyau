package com.aura.base.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.aura.base.TypeSide;
import com.aura.base.manager.configuration.AbstractConfigurationManager;
import com.aura.base.manager.configuration.ConfigCEBoolean;
import com.aura.base.manager.configuration.ConfigCM;

public class AuraLogger {
	private static SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss,SSS");
	
	private static boolean append;
	private static Map<TypeSide, Logger> logger;
	
	private static CallableAction actionPreInit = new CallableAction() {
		@Override
		public void execute(TypeSide side, Level info, String msg) {
			System.out.println((info != null ? info.toString()+": " : "") + msg);
		}
	};
	
	private static Map<Level, Boolean> FILTRES = new HashMap<Level, Boolean>();
	protected static Level[] getFiltreLevel() {
		return FILTRES.keySet().toArray(new Level[0]);
	}
	protected static Boolean getFiltre(Level l) {
		return FILTRES.get(l);
	}
	private static void setFiltre(Level l, Boolean value) {
		FILTRES.put(l, value);
	}
	
	private static void preInitLogger(TypeSide side, String name) {
		if (logger == null) {
			logger = new HashMap<TypeSide, Logger>();
			initFiltres();
		} else if (!logger.containsKey(side))
			logger.put(side, Logger.getLogger(name));
	}
	
	public static void preInitLogger(final TypeSide side, final String name, final String fileName) {
		try {
			Validate.notNull(side);
			
			if (logger == null) {
				logger = new HashMap<TypeSide, Logger>();
				initFiltres();
			} 
			
			append = true;
		    FileHandler handler = new FileHandler(fileName, append);
		    handler.setFormatter(new Formatter() {
		    	public String format(LogRecord rec) {
		    		StringBuffer buf = new StringBuffer(1000);
		            buf.append("["+LOG_DATE_FORMAT.format(new Date())+"]");
		            buf.append(" [");
		            buf.append(side.getTag());
		            buf.append("] ");
		            buf.append(" [");
		            buf.append(rec.getLevel());
		            buf.append("] ");
		            buf.append(formatMessage(rec));
		            buf.append('\n');
		            return buf.toString();
		         }
		    });
		    
		    preInitLogger(side, name);
		    logger.get(side).addHandler(handler);
		    
		    if (LOG_ACTION == null) {
		    	addAction(actionPreInit);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void postInitLogger(TypeSide side) {
		if (LOG_ACTION != null) {
			AuraLogger.config(side, "Pré-chargement terminé.");
	    	LOG_ACTION.remove(actionPreInit);
		}
	}
	public static void initFiltres(AbstractConfigurationManager<?> cfg) {
		if (cfg != null) {
			setFiltre(Level.FINEST, ((ConfigCEBoolean) cfg.getContainerManager().getElement(ConfigCM.VERBOSE_FINEST)).getValue());
			setFiltre(Level.FINE, ((ConfigCEBoolean) cfg.getContainerManager().getElement(ConfigCM.VERBOSE_FINE)).getValue());
			setFiltre(Level.CONFIG, ((ConfigCEBoolean) cfg.getContainerManager().getElement(ConfigCM.VERBOSE_CONFIG)).getValue());
			setFiltre(Level.INFO, ((ConfigCEBoolean) cfg.getContainerManager().getElement(ConfigCM.VERBOSE_INFO)).getValue());
			setFiltre(Level.SEVERE, ((ConfigCEBoolean) cfg.getContainerManager().getElement(ConfigCM.VERBOSE_SEVERE)).getValue());
		}
	}
	
	public static void initFiltres() {
		setFiltre(Level.FINEST, true);
		setFiltre(Level.FINE, true);
		setFiltre(Level.CONFIG, true);
		setFiltre(Level.INFO, true);
		setFiltre(Level.SEVERE, true);
	}
	private static Logger getLogger(TypeSide side) {
		if (logger == null || logger.get(side) == null) {
			preInitLogger(side, "zFox_"+side);
		}
		return logger.get(side);
	}
	
	protected static void log(TypeSide side, String string) {
		log(side, null, string, null);
	}
	protected static void log(TypeSide side, Level info, String string) {
		log(side, info, string, null);
	}
	public static void log(TypeSide side, Level info, String string, Object ex) {
		if (side != TypeSide.BOTH) {
			try {
				// Filtres actifs
				if (info != null) {
					Boolean value = FILTRES.get(info);
					if (value != null && !value)
						return;
				}
				
				if (side == null)
					side = TypeSide.BOTH;
				getLogger(side).log(info != null ? info : Level.INFO, string, ex);
				if (LOG_ACTION != null) {
					for (CallableAction ca: LOG_ACTION) 
						ca.execute(side, info, string);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			log(TypeSide.CLIENT, info, string, ex);
			log(TypeSide.SERVER, info, string, ex);
		}
	}
	
	public static void finest(TypeSide side, String string) {
		log(side, Level.FINEST, string);
	}
	public static void fine(TypeSide side, String string) {
		log(side, Level.FINE, string);
	}
	public static void config(TypeSide side, String string) {
		log(side, Level.CONFIG, string);
	}
	public static void info(TypeSide side, String string) {
		log(side, Level.INFO, string);
	}
	public static void info(TypeSide side, String string, VerifyError ve) {
		log(side, Level.INFO, string, ve);
	}
	public static void severe(TypeSide side, String string) {
		log(side, Level.SEVERE, string);
	}
	public static void severe(TypeSide side, String string, Exception e) {
		log(side, Level.SEVERE, string, e);
	}
	public static void warning(TypeSide side, String string) {
		log(side, Level.WARNING, string);
	}
	public static void warning(TypeSide side, String string, Exception e) {
		log(side, Level.WARNING, string, e);
	}
	
	public static void chat(TypeSide side, String string) {
		log(side, string);
	}
	
	private static List<CallableAction> LOG_ACTION;
	public static void setLevel(Level level) {
		for (TypeSide s: TypeSide.values()) 
			getLogger(s).setLevel(level);
	}
	
	public static void addAction(CallableAction action) {
		if (LOG_ACTION == null)
			LOG_ACTION = new ArrayList<CallableAction>();
		LOG_ACTION.add(action);
	}
	
	public static void debug(boolean clear) {
		initFiltres();
		setLevel(Level.ALL);
		if (LOG_ACTION != null && clear)
			LOG_ACTION.clear();
		addAction(new CallableAction() {
			@Override
			public void execute(TypeSide side, Level info, String msg) {
				System.out.println(
						(side != null || info != null ? 
							"[" 
							+ (side != null ? side.getTag() : "") 
							+ (info != null ? (":" + info) : "") 
							+ "] " : "")
						+ msg);
			}
		});
	}
}
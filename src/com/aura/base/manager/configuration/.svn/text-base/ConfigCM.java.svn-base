package com.aura.base.manager.configuration;

import com.aura.base.Aura;
import com.aura.base.TypeLangue;
import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.utils.Curseur;

public class ConfigCM<A extends Aura> extends AbstractContainerManager<A, ConfigCE<?>>{
	public static Curseur CURSEUR = new Curseur(ConfigCM.class);
	
	public static final int VERBOSE_ACTIF = CURSEUR.nextVal();
	public static final int VERBOSE_FINEST = CURSEUR.nextVal();
	public static final int VERBOSE_FINE = CURSEUR.nextVal();
	public static final int VERBOSE_CONFIG = CURSEUR.nextVal();
	public static final int VERBOSE_INFO = CURSEUR.nextVal();
	public static final int VERBOSE_SEVERE = CURSEUR.nextVal();
	
	public static final int LANG = CURSEUR.nextVal();
	
	public static final int SOCKET_PORT = CURSEUR.nextVal();
	
	// CLIENT
	public static final int SOCKET_LOGIN = CURSEUR.nextVal();
	public static final int SOCKET_HOSTNAME = CURSEUR.nextVal();
	
	// SERVEUR
	public static final int SOCKET_PASSWORD = CURSEUR.nextVal();
	public static final int SOCKET_MAX_CONNECTION = CURSEUR.nextVal();
	public static final int SOCKET_TIMEOUT = CURSEUR.nextVal();
	
	public static final int DB_ACTIF = CURSEUR.nextVal();
	public static final int DB_NAME = CURSEUR.nextVal();
	public static final int DB_DRIVER = CURSEUR.nextVal();
	public static final int DB_URL = CURSEUR.nextVal();
	public static final int DB_USERNAME = CURSEUR.nextVal();
	public static final int DB_PASSWORD = CURSEUR.nextVal();
	public static final int DB_BASE_ISO = CURSEUR.nextVal();
	public static final int DB_HEART_BEAT_SQL = CURSEUR.nextVal();
	
	public ConfigCM(A aura) {
		super(aura);
	}
	public ConfigCM(ConfigCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new ConfigCEBoolean(VERBOSE_ACTIF, "verbose.actif", TypeSide.BOTH, true));
		register(new ConfigCEBoolean(VERBOSE_FINEST, "verbose.finest", TypeSide.BOTH, false));
		register(new ConfigCEBoolean(VERBOSE_FINE, "verbose.fine", TypeSide.BOTH, false));
		register(new ConfigCEBoolean(VERBOSE_CONFIG, "verbose.config", TypeSide.BOTH, true));
		register(new ConfigCEBoolean(VERBOSE_INFO, "verbose.info", TypeSide.BOTH, true));
		register(new ConfigCEBoolean(VERBOSE_SEVERE, "verbose.severe", TypeSide.BOTH, true));
		
		register(new ConfigCEInteger(LANG, "lang", TypeSide.BOTH, TypeLangue.DEFAULT.getId()));
		
		register(new ConfigCEInteger(SOCKET_PORT, "socket.port", TypeSide.BOTH, 25642));
		
		register(new ConfigCEString(SOCKET_LOGIN, "socket.login", TypeSide.CLIENT, "user"));
		register(new ConfigCEString(SOCKET_HOSTNAME, "socket.hostname", TypeSide.CLIENT, "localhost"));
		register(new ConfigCEString(SOCKET_PASSWORD, "socket.password", TypeSide.SERVER, ""));
		register(new ConfigCEInteger(SOCKET_MAX_CONNECTION, "socket.maxConnection", TypeSide.SERVER, 0));
		register(new ConfigCEInteger(SOCKET_TIMEOUT, "socket.timeOut", TypeSide.SERVER, 15000));
		
		register(new ConfigCEBoolean(DB_ACTIF, "db.actif", TypeSide.SERVER, false));
		register(new ConfigCEString(DB_NAME, "db.name", TypeSide.SERVER, "server"));
		register(new ConfigCEString(DB_DRIVER, "db.driver", TypeSide.SERVER, "org.sqlite.JDBC"));
		String url = "jdbc:sqlite:" + Aura.D_RESS + "\\" + Aura.D_DB + "\\" + Aura.FS_DB;
		register(new ConfigCEString(DB_URL, "db.url", TypeSide.SERVER, url));
		register(new ConfigCEString(DB_USERNAME, "db.username", TypeSide.SERVER, "server_username"));
		register(new ConfigCEString(DB_PASSWORD, "db.password", TypeSide.SERVER, "server_password"));
		register(new ConfigCEString(DB_BASE_ISO, "db.baseIso", TypeSide.SERVER, "TRANSACTION_SERIALIZABLE"));
		register(new ConfigCEString(DB_HEART_BEAT_SQL, "db.heartBeatSql", TypeSide.SERVER, ""));
	}
	
	@Override
	public ConfigCE<?> getElement(int id) {
		return super.getElement(id);
	}
}
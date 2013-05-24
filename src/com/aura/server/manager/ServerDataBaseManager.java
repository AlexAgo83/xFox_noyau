package com.aura.server.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.utils.AuraLogger;
import com.aura.server.AuraServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;

public class ServerDataBaseManager extends AbstractAuraManager<AuraServer> {
	public static String DB_URL_DEBUG;
	
	private SpiEbeanServer ebean;
	
	private ServerConfig db;
	private DataSourceConfig ds;
	private List<Class<?>> clazz;
	
	private List<Class<?>> getAuraClass() {
		if (clazz == null) {
			clazz = new ArrayList<Class<?>>();
		}
		return clazz;
	}
	
	public ServerDataBaseManager(AuraServer auraServer) {
		super(auraServer);
		
		// ServerConfig
		db = new ServerConfig();
		db.setDefaultServer(false);
		db.setRegister(false);
		
		db.setDatabasePlatform(new SQLitePlatform());
        db.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
		
		// DataSourceConfig
		ds = new DataSourceConfig();
	}

	@Override
	public boolean load() {
		if (!getAura().getCfgManager().getConfigBooleanValue(ConfigCM.DB_ACTIF)) {
			AuraLogger.config(getAura().getSide(), "Abort ... 'dataBase manager'.");
			return true;
		}
		
		db.setName(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_NAME));
        ds.setDriver(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_DRIVER));
        ds.setUrl(DB_URL_DEBUG != null ? DB_URL_DEBUG : getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_URL));
        ds.setUsername(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_USERNAME));
        ds.setPassword(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_PASSWORD));
        ds.setHeartbeatSql(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_HEART_BEAT_SQL));
        ds.setIsolationLevel(TransactionIsolation.getLevel(getAura().getCfgManager().getConfigStringValue(ConfigCM.DB_BASE_ISO)));
        db.setDataSourceConfig(ds);

        try {
        	AuraLogger.config(getAura().getSide(), "Initialisation de la base de donnée.");
        	
        	// FIXME XF(AA) [Login] déplacer l'init User.class dans un futur plugin login
//        	getAuraClass().add(User.class); 
        	
        	for (Integer i: getAura().getPlugins().keySet()) {
        		Class<?>[] cz = getAura().getPlugins().get(i).onLoadDbClass();
        		if (cz != null && cz.length > 0)
        			getAuraClass().addAll(Arrays.asList(cz));
        	}
    		db.setClasses(getAuraClass());
        	
        	ebean = (SpiEbeanServer) EbeanServerFactory.create(db);
        	try {
        		// Class
        		
	        	for (Class<?> c : getAuraClass()) {
	        		ebean.find(c).findList().size();
	        	}
        	} catch (Exception e) {
        		AuraLogger.severe(
        				getAura().getSide(), 
        				"La base de donnée n'est pas à jour, le manager va tenter de l'adapter.", 
        				e);
	        	DdlGenerator gen = ebean.getDdlGenerator();
	        	gen.runScript(false, gen.generateCreateDdl());
        	}
        } catch (Exception e) {
        	AuraLogger.severe(getAura().getSide(), "Un problème est survenu pendant l'initialisation de la base.", e);
        	return false;
        }
        
		return true;
	}
	
	public SpiEbeanServer getDataBase() {
		return ebean;
	}
}
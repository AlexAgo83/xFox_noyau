package com.aura.test.utils;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.aura.base.Aura;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.manager.event.EventCE;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.server.AuraServer;
import com.aura.server.manager.ServerDataBaseManager;

public abstract class AuraTestCase extends TestCase {
	private static AuraServer SERVER;
	public AuraServer getServer() {
		return SERVER;
	}
	
	private static AuraClient CLIENT_1;
	public AuraClient getClient1() {
		return CLIENT_1;
	}
	
	private static AuraClient CLIENT_2;
	public AuraClient getClient2() {
		return CLIENT_2;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		listeners.clear();
		System.out.println("SETUP");
		
		if (SERVER != null) {
			SERVER.getEventManager().getDispatchThread().clearListener();
			SERVER.getNetworkManager().offline();
			SERVER.setBuildRevision("REV-UT");
		} else {
			ServerDataBaseManager.DB_URL_DEBUG = "jdbc:sqlite:ress\\db\\server_testCase.db";
			SERVER = new AuraServer() {
				@Override
				public void initPlugin(final Aura self) {}
			};
		}
//		SERVER.getCfgManager().setConfigBooleanValue(ConfigCM.ADMIN_ALLOW_NEW_USER, false, false);
		Thread.sleep(2000);
		
		if (CLIENT_1 != null) {
			CLIENT_1.getEventManager().getDispatchThread().clearListener();
			CLIENT_1.getNetworkManager().deconnect(false);
			CLIENT_1.setBuildRevision("REV-UT");
		} else {
			CLIENT_1 = new AuraClient() {
				@Override
				public void initPlugin(final Aura self) {}
			};
		}
		if (CLIENT_2 != null) {
			CLIENT_2.getEventManager().getDispatchThread().clearListener();
			CLIENT_2.getNetworkManager().deconnect(false);
			CLIENT_2.setBuildRevision("REV-UT");
		} else {
			CLIENT_2 = new AuraClient() {
				@Override
				public void initPlugin(final Aura self) {}
			};
		}
		Thread.sleep(2000);
		
		AuraLogger.debug(true);
		
		SERVER.getNetworkManager().online();
		Thread.sleep(1000);
		System.out.println("START");
	}
	
	private Map<String, AuraEventListener> listeners = new HashMap<String, AuraEventListener>();
	public void addListener(Aura aura, EventCE event, String code) {
		AuraEventListener listener = new AuraEventListener(event) {
			@Override 
			public void implExecute(AbstractEvent event) {
				// ...
			}
		};
		aura.getEventManager().getDispatchThread().addListener(listener);
		listeners.put(code, listener);
	}
	public boolean isSuccess(String code) {
		return listeners.get(code).getCount() > 0;
	}
	
	public AbstractPacket createPacket(Aura aura, int id) {
		return aura.createPacket(id);
	}
	public AbstractEvent createEvent(Aura aura, int id) {
		return aura.createEvent(id);
	}
}
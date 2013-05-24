package com.aura.test;

import com.aura.base.manager.event.EventCM;
import com.aura.test.utils.AuraTestCase;

public class ConnectionTest extends AuraTestCase {
	public void testConnect_OK() throws Exception {
		getClient1().getNetworkManager().connect();
		addListener(getClient1(), getClient1().getEvent(EventCM.USER_CONNECT_OK), "OK");
		Thread.sleep(2000);
		assertTrue(isSuccess("OK"));
	}
	
	public void testConnect_KO_Offline() throws Exception {
		getServer().getNetworkManager().offline();
		addListener(getClient1(), getClient1().getEvent(EventCM.USER_CONNECT_KO), "KO");
		getClient1().getNetworkManager().connect();
		Thread.sleep(2000);
		assertTrue(isSuccess("KO"));
	}
	
	public void testConnect_KO_Outdated() throws Exception {
		getClient1().setBuildRevision("<!>");
		addListener(getClient1(), getClient1().getEvent(EventCM.USER_OUTDATED), "OUT");
		getClient1().getNetworkManager().connect();
		Thread.sleep(2000);
		assertTrue(isSuccess("OUT"));
	}
}
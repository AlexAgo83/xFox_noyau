package com.aura.client;

import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.packet.impl.TypeLoginResult;

public class ClientConnection {
	private final AuraClient aura;
	
	private String login;
	private String password;
	
	private TypeLoginResult result;
	
	private Long timePingServer;
	private Long timeDeltaServer;
	
	public ClientConnection(AuraClient aura) {
		this.aura = aura;
		
		this.login = null;
		this.password = null;
		this.result = TypeLoginResult.KO;
	}
	
	public AuraClient getAura() {
		return aura;
	}

	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TypeLoginResult getResult() {
		return result;
	}
	
	public void setLoginInfo(TypeLoginResult result, String login) {
		if (this.result == result)
			return;
		this.result = result;
		this.login = login;
		
		if (getLogin() != null && !getLogin().equals(getAura().getCfgManager().getConfigStringValue(ConfigCM.SOCKET_LOGIN))) 
			getAura().getCfgManager().setConfigStringValue(ConfigCM.SOCKET_LOGIN, getLogin(), true);
		
		getAura().getEventManager().addEvent(getAura().createEvent(getResult().getEventId()));
	}

	public Long getTimePingServer() {
		return timePingServer;
	}
	public Long getTimeDeltaServer() {
		return timeDeltaServer;
	}
	public synchronized void setTimeServer(Long ping, Long tick) {
		this.timePingServer = ping;
		this.timeDeltaServer = tick;
	}
}
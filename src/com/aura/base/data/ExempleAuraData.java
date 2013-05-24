package com.aura.base.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.validator.AbstractValidator;
import com.aura.base.validator.LoginValidator;
import com.aura.base.validator.PasswordValidator;
import com.avaje.ebean.validation.NotEmpty;

@Entity
@Table(name = "tbl_user")
@UniqueConstraint(columnNames = "login")
public class ExempleAuraData extends AuraData {
	private AbstractPacketManager pm;
	public AbstractPacketManager getPm() {
		return pm;
	}
	public void setPm(AbstractPacketManager pm) {
		this.pm = pm;
	}
	
	private String login;
	public static String PROP_LOGIN = "login";
	public static AbstractValidator V_LOGIN = new LoginValidator();
	@Column(name = "login")
	@Basic(optional = false)
	@NotEmpty
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		V_LOGIN.validate(login);
		this.login = login;
	}
	
	private String password;
	public static String PROP_PASSWORD = "password";
	public static AbstractValidator V_PASSWORD = new PasswordValidator();
	@Column(name = "password")
	@Basic(optional = false)
	@NotEmpty
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		V_PASSWORD.validate(password);
		this.password = password;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExempleAuraData other = (ExempleAuraData) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}
}
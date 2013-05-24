package com.aura.base.manager.i18n;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.aura.base.Aura;
import com.aura.base.TypeLangue;
import com.aura.base.configuration.PropertiesLoader;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.base.configuration.YamlConfiguration;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.manager.AbstractAuraManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.i18n.I18nMessage.I18nIdNotExistException;
import com.aura.base.utils.AuraLogger;

public class AbstractI18nManager<A extends Aura> extends AbstractAuraManager<A> {
	private final Map<I18nCE, I18nMessage> messages;
	
	private TypeLangue lang;
	public TypeLangue getLang() {
		return lang;
	}
	
	private final I18nCM<A> containerManager;
	public I18nCM<A> getContainerManager() {
		return containerManager;
	}
	
	public AbstractI18nManager(A aura) {
		super(aura);
		this.containerManager = new I18nCM<A>(aura);
		this.messages = new HashMap<I18nCE, I18nMessage>();
	}
	
	@Override
	public boolean load() {
		this.lang = TypeLangue.getById(getAura().getCfgManager().getConfigIntegerValue(ConfigCM.LANG));
		
		try {
			containerManager.load();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getAura().getSide());
			return false;
		}
		
		File fLang = new File(Aura.D_RESS + "\\" + Aura.D_I18N + "\\" + lang.getTag() + ".properties");
		YamlConfiguration yml = null;
		PropertiesLoader loader = null;
		try {
			yml = YamlConfiguration.loadConfiguration(getAura(), fLang);
			loader = new PropertiesLoader(getAura(), fLang, yml);
		} catch (VerifyError ve) {
			AuraLogger.info(getSide(), "Ce system ne supporte pas snakeYaml", ve);
			return false;
		}
		
		for (Integer id: containerManager.getContainerKeyTab()) {
			I18nCE ce = containerManager.getElement(id);
			try {
				messages.put(ce, new I18nMessage(loader.loadString(ce.getLabel())));
			} catch (I18nIdNotExistException exc) {
				AuraLogger.severe(getSide(), "Message i18n [" 
					+ ce.getLabel() + "] introuvable dans le fichier ["
					+ fLang.getName() + "].", exc);
				return false;
			} catch (PropertiesLoaderException exc) {
				AuraLogger.severe(getSide(), "Erreur de lecture du fichier lang.", exc);
				return false;
			}
		}
		
		return true;
	}

	public I18nMessage getI18nMessage(int id) {
		return messages.get(getContainerManager().getElement(id));
	}
	public String getI18nMessage(int id, String...args) {
		return getI18nMessage(id).getMessage(args);
	}
}
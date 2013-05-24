package com.aura.base.manager.console;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Curseur;

public class ConsoleCM<A extends Aura> extends AbstractContainerManager<A, ConsoleCE> {
	public static Curseur CURSEUR = new Curseur(ConsoleCM.class);
	
	public final static int HELP = CURSEUR.nextVal();
	public final static int EXIT = CURSEUR.nextVal();
	public final static int VERSION = CURSEUR.nextVal();
	public final static int USAGE = CURSEUR.nextVal();
	
	public final static int KICK_USER = CURSEUR.nextVal();
	public final static int KICK_ALL = CURSEUR.nextVal();
	
	public ConsoleCM(A aura) {
		super(aura);
	}
	public ConsoleCM(ConsoleCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new ConsoleCE(HELP, "HELP", "help", TypeSide.BOTH, 1));
		register(new ConsoleCE(EXIT, "EXIT", "exit", TypeSide.BOTH, 1));
		register(new ConsoleCE(VERSION, "VERSION", "version", TypeSide.BOTH, 1));
		register(new ConsoleCE(USAGE, "USAGE", "usage", TypeSide.BOTH, 1));
		register(new ConsoleCE(KICK_USER, "KICK_USER", "kick", TypeSide.SERVER, 2, "Ex: /kick player"));
		register(new ConsoleCE(KICK_ALL, "KICK_ALL", "kickall", TypeSide.SERVER, 1));
	}
	
	public ConsoleCE dispatcher(TypeSide side, String[] cmd, boolean helpAuto) {
		if (cmd != null && cmd.length > 0) {
			Integer[] ids = getContainerKeyTab();
			for (int i : ids) {
				ConsoleCE cp = getElement(i);
				if (cmd[0].trim().toLowerCase().equals(cp.getBalise().trim().toLowerCase())
						&& cmd.length >= cp.getMinArgs()
						&& (cp.getSide() == TypeSide.BOTH || side == TypeSide.BOTH || cp.getSide() == side)) {
					if (helpAuto && cp.getId() == ConsoleCM.HELP) {
						for (String str: printHelp(side))
							AuraLogger.info(side, str);
					}
					return cp;
				}
			}
		}
		return null;
	}
	
	public String[] printHelp(TypeSide side) {
		List<String> a = new ArrayList<String>();
		Integer[] ids = getContainerKeyTab();
		for (int i : ids) {
			ConsoleCE cp = getElement(i);
			if (cp.getSide() == TypeSide.BOTH || side == TypeSide.BOTH || cp.getSide() == side)
				a.add("/"+cp.getBalise() 
						+ (cp.getMinArgs() > 1 ? " args=" + (cp.getMinArgs()-1) : "")
						+ (cp.getDescription() != null ? " " + cp.getDescription() : ""));
		}
		return a.toArray(new String[0]);
	}

	@Override
	public ConsoleCE getElement(int id) {
		return super.getElement(id);
	}
}
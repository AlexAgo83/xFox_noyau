package com.aura.base.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;

public abstract class AuraThread extends Thread {
	private static List<Usage> usages = new ArrayList<Usage>();
	private boolean lock = false;
	public synchronized static List<Usage> getUsages() {
		return usages;
	}
	
	private final Aura aura;
	private boolean forceTerminate = false;
	
	private Usage usage = null;
	
	public AuraThread(Aura a, String name, int timer, int max) {
		super(name);
		
		this.aura = a;
		this.usage = new Usage(getId(), name);
		this.usage.timer = timer;
		this.usage.max = max;
	}
	public AuraThread(Aura a, String name, int timer) {
		this(a, name, timer, 0);
	}
	
	public Aura getMainAura() {
		return aura;
	}
	
	@Override
	public void run() {
		while(!forceTerminate
				&& getMainAura().isRunning()
				&& condition() 
				&& (usage.max == 0 || (usage.max > 0 && usage.whileTurn < usage.max))) {
			usage.whileTurn += 1;
			usage.currentTime = System.currentTimeMillis();
			if (!usage.pause)
				action();
			usage.delta = System.currentTimeMillis() - usage.currentTime;
			if (usage.minTimeExec > usage.delta || usage.minTimeExec <= 0)
				usage.minTimeExec = usage.delta;
			if (usage.maxTimeExec < usage.delta || usage.maxTimeExec <= 0)
				usage.maxTimeExec = usage.delta;
			try {
				usage.sleepTime = usage.timer - usage.delta;
				if (usage.sleepTime > 0)
					sleep(usage.sleepTime);
				usage.deltaWithTimer = System.currentTimeMillis() - usage.currentTime;
			} catch (InterruptedException e) {
				AuraLogger.warning(getMainAura().getSide(), "Thread perdu dans un wait.", e);
			}
		}
		doOnClose();
		this.usage.terminate = true;
//		getUsages().remove(usage); 
	}
	public abstract void action();
	public abstract boolean condition();
	public abstract void doOnClose();
	
	public void pauseThread() {
		this.usage.pause = true;
	}
	public void resumeThread() {
		this.usage.pause = false;
	}
	public void resumeThread(int newTimer) {
		this.usage.timer = newTimer;
		resumeThread();
	}
	public boolean isPaused() {
		return this.usage.pause;
	}
	
	public void forceTerminate() {
		this.forceTerminate = true;
	}
	public boolean isTerminate() {
		return this.usage.terminate;
	}
	public long getCurrentTime() {
		return this.usage.currentTime;
	}
	public long getTurn() {
		return this.usage.whileTurn;
	}
	public double getFps() {
		if (this.usage.deltaWithTimer > 0)
			return 1000 / this.usage.deltaWithTimer;
		return 0;
	}
	public long getTimer() {
		return usage.timer;
	}
	public long getTargetFps() {
		return getTimer() > 0 ? 1000 / getTimer() : -1;
	}
	public long getSleepTime() {
		return usage.sleepTime;
	}
	public long getDeltaTime() {
		return usage.delta;
	}
	
	class Usage {
		long id;
		String name;
		long timer;
		long max;
		
		boolean terminate = false;
		boolean pause = false;

		long currentTime = 0;
		long whileTurn = 0;
		long minTimeExec = -1;
		long maxTimeExec = -1;
		long delta = 0;
		long deltaWithTimer = 0;
		long sleepTime = 0;
		
		public Usage(long id, String name) {
			this.id = id;
			this.name = name;
			while(lock){}
			lock = true;
			getUsages().add(this);
			lock = false;
		}
		
		public void prompt() {
			AuraLogger.config(getMainAura().getSide(), "Thread > " 
				+ name + "[" + id + "]>"
				+ " actif: " + !terminate 
				+ " pause: " + pause
				+ " turn: " + whileTurn 
				+ " timer: " + timer
				+ "  min/max: " + minTimeExec + "/" + maxTimeExec);
		}
	}
	
	public static void promptAllUsages() {
		Usage[] us = getUsages().toArray(new Usage[0]);
		for (Usage u: us) {
			if (u != null) u.prompt();
		}
	}
	
	public void log(Level info, int id, String args) {
		log(info, id, args, null);		
	}
	public void log(Level info, int id, String args, Exception exc) {
		AuraLogger.log(getMainAura().getSide(), info, getMainAura().getI18nMessage(id, args), exc);		
	}
}
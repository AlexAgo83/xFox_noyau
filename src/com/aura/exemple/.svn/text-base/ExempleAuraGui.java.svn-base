package com.aura.exemple;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.aura.base.Aura;
import com.aura.base.thread.AuraThread;

public abstract class ExempleAuraGui<E extends Aura> implements WindowListener {
//	private static int gridY = 0; 
	
	public static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	protected final E aura;
	
	protected final Component component;
	
	protected boolean threadLock = false;
	
	public ExempleAuraGui(Component parentFrame, E a) {
		this.aura = a;
		this.threadLock = true;
		// Frame
		if (parentFrame == null) {
			final JFrame frame = new JFrame(a.getBuildRevision());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().add(getPContent());
			frame.getContentPane().setBackground(Color.BLACK);
			component = frame;
		} else { 
			component = parentFrame;
			if (parentFrame instanceof Window) {
				Window w = (Window) parentFrame;
				w.add(getPContent());
			} else if (parentFrame instanceof Canvas) {
				Canvas c = (Canvas) parentFrame;
				c.getParent().add(getPContent());
			} else if (parentFrame instanceof Container) {
				Container c = (Container) parentFrame;
				if (parentFrame instanceof JPanel) {
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.weightx = 1; gbc.weighty = 1;
					gbc.fill = GridBagConstraints.BOTH;
					c.add(getPContent(), gbc);
				} else
					c.add(getPContent());
			}
		}
		if (component instanceof JFrame) {
			Window window = (JFrame) component;
			window.setSize(new Dimension(400, 400));
			window.addWindowListener(this);
			window.setLocationRelativeTo(null);
		} else if (component instanceof JPanel) {
			Container panel = (Container) component;
			for (int i=0; i<100; i++) {
				panel = panel.getParent();
				boolean lFound = panel instanceof Window;
				if (lFound)
					break;
			}
			Window window = (Window) panel;
			window.setSize(new Dimension(400, 400));
			window.addWindowListener(this);
			window.setLocationRelativeTo(null);
		}
		component.setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUIThread(aura).start();
			}
		});
	}
	
	private JPanel pContent;
	public JPanel getPContent() {
		if (pContent == null) {
			pContent = new JPanel(new GridBagLayout());
		}
		return pContent;
	}
	
	private JLabel lInfoServeur;
	public JLabel getLInfoServeur() {
		if (lInfoServeur == null) {
			lInfoServeur = new JLabel();
		}
		return lInfoServeur;
	}
	
	public Component getComponent() {
		return component;
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		this.threadLock = false;
		aura.turnOff(true);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
	
	class GUIThread extends AuraThread {
		public GUIThread(Aura aura) {
			super(aura, "GUIThread", 100);
		}
		@Override
		public boolean condition() {
			return threadLock;
		}
		@Override
		public void doOnClose() {}
		@Override
		public void action() {
			doOnThreadCycle();
		}
	}
	public abstract void doOnThreadCycle();
}
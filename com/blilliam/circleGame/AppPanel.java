package com.blilliam.circleGame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class AppPanel extends JPanel implements Runnable {
	static Toolkit tk = Toolkit.getDefaultToolkit();
	
	public static final int WIDTH = ((int) tk.getScreenSize().getWidth());
	public static final int HEIGHT = ((int)tk.getScreenSize().getHeight()) - 38;
	public Dimension d = new Dimension(WIDTH, HEIGHT);
	public Thread t = new Thread(this);
	KeyboardInput keyHandler = new KeyboardInput();
	GameObject gameObj = new GameObject(keyHandler);
	
	// Constructor
	public AppPanel() {
		setPreferredSize(d);
		addKeyListener(keyHandler);
		setFocusable(true);
		t.start();
	}
	
	// Overriding functions
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		gameObj.draw(g2);
	}

	@Override
	public void run() {
		while (true) {
			repaint();
			gameObj.update();
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
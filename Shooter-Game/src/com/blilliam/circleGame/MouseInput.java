package com.blilliam.circleGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    public static int mouseX, mouseY;
    public static boolean mousePressed = false;
    public static boolean mouseClicked = false;

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
        mouseClicked = true; // single-frame click
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /** Call once per frame at the end of your game loop */
    public static void update() {
        mouseClicked = false;
    }
}

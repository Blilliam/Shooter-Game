package com.blilliam.circleGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    public boolean isLeft = false;
    public int x = 0;
    public int y = 0;

    public MouseInput() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        
        //gameObj.upgrades.mouseClick(x, y); FIX THIS
        isLeft = true;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	if (e.getButton() == MouseEvent.BUTTON1) {
    		isLeft = false;
    	}
    }

}

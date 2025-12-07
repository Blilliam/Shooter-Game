package com.blilliam.circleGame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    private GameObject gameObj;

    public MouseInput(GameObject gameObj) {
        this.gameObj = gameObj;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        gameObj.upgrades.mouseClick(x, y); // call method in GameObject
    }

}

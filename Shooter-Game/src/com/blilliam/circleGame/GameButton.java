package com.blilliam.circleGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A clickable button with hover and click functionality.
 */
public class GameButton {

    public int x, y, w, h;
    private String buttonText;
    private Runnable clickFunc;

    public GameButton(int x, int y, int w, int h, String text, Runnable clickFunc) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.buttonText = text;
        this.clickFunc = clickFunc;
    }

    /** Returns true if mouse is hovering over this button */
    public boolean isHovering() {
        return MouseInput.mouseX >= x && MouseInput.mouseX <= x + w &&
               MouseInput.mouseY >= y && MouseInput.mouseY <= y + h;
    }

    /** Call this each frame to handle clicks */
    public void update() {
        if (isHovering() && MouseInput.mouseClicked) {
            clickFunc.run();
            MouseInput.update(); // consume click so it doesn't trigger again
        }
    }

    /** Draw the button with hover tint */
    public void draw(Graphics2D g2) {

        // Base color
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(x, y, w, h);

        // Hover overlay
        if (isHovering()) {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(x, y, w, h);
        }

        // Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, w, h);

        // Text
        g2.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (w - fm.stringWidth(buttonText)) / 2;
        int textY = y + (h + fm.getAscent()) / 2 - 4;

        g2.drawString(buttonText, textX, textY);
    }

    /** Optional helper for rectangle bounds */
    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }
}

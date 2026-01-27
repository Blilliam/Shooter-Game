package com.blilliam.circleGame;

class TrailPoint {
    double x, y;
    double vx, vy;
    float alpha;
    boolean boosting;

    public TrailPoint(double x, double y, double vx, double vy, boolean boosting) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.boosting = boosting;
        this.alpha = 1.0f;
    }

    public void fade() {
        alpha -= 0.045f;
    }

    public boolean isDead() {
        return alpha <= 0;
    }
}

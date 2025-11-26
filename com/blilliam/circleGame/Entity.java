package com.blilliam.circleGame;

public abstract class Entity {
	private double x;
	private double y;
	
	public boolean isDead = false;
	
	public int radius = 10;
	public double dx;
	public double dy;
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public void setX(double x) { 
		this.x = x; 
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public static boolean circleCollision(Entity e1, Entity e2) {
		double dx = e1.getX() + e1.radius - e2.getX() - e2.radius;
		double dy = e1.getY() + e1.radius - e2.getY() - e2.radius;
		double distance = Math.sqrt(dx*dx + dy*dy);
		return distance <= e1.radius + e2.radius;
	}
}

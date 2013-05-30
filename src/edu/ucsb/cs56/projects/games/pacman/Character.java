package edu.ucsb.cs56.projects.games.pacman;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;

public abstract class Character {
	public int startX, startY;
	public int x, y, dx, dy, viewdx, viewdy, reqdx, reqdy, speed;
	
	public Character(int x, int y){
        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
	}
	
	public void reset(){
        this.x = startX;
        this.y = startY;
        reqdx = 0;
        reqdy = 0;
        viewdx = -1;
        viewdy = 0;
	}

	public abstract void draw(Graphics2D g, JComponent canvas);
	
	public abstract void loadImages();
    
        public abstract Image getLifeImage();

	public void move(){
        x = x + speed * dx;
        y = y + speed * dy;
	}
}

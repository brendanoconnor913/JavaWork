import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;


public class EggDrop extends JFrame {
	Grid grid;
	Graphics2D g2;
	Vector<Egg> eggVec = new Vector();
	Basket basket;
	int width;
	int height;
	int score = 0;
	int drops = 0;
	
	EggDrop(String name,Dimension d) {
		// Setup frame initialization details
		super(name);
		setSize(d);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		g2 = (Graphics2D)this.getGraphics();
		width = getWidth();
		height = getHeight();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Initialize game objects
		grid = new Grid(this,7,7);	
		eggVec.addElement(new Egg(g2, grid)); 
		basket = new Basket(g2, grid);
		
		// left arrow = move basket left, right arrow = move basket right 
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					basket.slide(-1);
					repaint();
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					basket.slide(1);
					repaint();
				}
			}
			public void keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){}
		});
		
		repaint();
	}
	// Add another egg to gameplay
	void addEgg() {
		eggVec.addElement(new Egg(g2,grid));
	}
	
	synchronized public void paint(Graphics g) {	
		g2.setPaint(Color.DARK_GRAY);
		g2.fillRect(0, 0, width, height);
		// Draw all the egs and the basket
		for(Egg egg : eggVec) {
			egg.draw();
		}
		basket.draw();
		
		// If an egg reaches the bottom row update score/drop counters
		// and repaint the egg back at the top row
		for(Egg egg : eggVec) {
			if(egg.isOnGround()) {
				if(basket.catchesEgg(egg)) score++;
				else drops++;
				try {Thread.sleep(300);}
				catch (InterruptedException e) {e.printStackTrace();}
				egg.reset();
				g2.setPaint(Color.DARK_GRAY);
				g2.fillRect(0, 0, width, height);
				for(Egg egg2 : eggVec) {
					egg2.draw();
				}
				basket.draw();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int count = 1; // number of eggs
		int time = 1; // time counter
		EggDrop ed = new EggDrop("EGGGER", new Dimension(420,500));
		JOptionPane.showMessageDialog(ed,
				"Save the egg from hitting the ground!\n"
				+ "You can only drop 5 eggs before losing.");
		while(ed.drops < 5) {
			Thread.sleep(300);
			for(Egg egg : ed.eggVec) {
				egg.drop();
				time++;
			}
			ed.repaint();
			// Let some time elapse before adding other eggs
			if(count < 3 && time % 5 == 0) {
				synchronized(ed) {
					ed.addEgg();
					count++;
				}
			}
		}
		JOptionPane.showMessageDialog(ed,
				"GAME OVER\n"
				+"Your score was " + ed.score);
		System.exit(0);
	}
}

class Grid {
	int rowHeight;
	int colWidth;
	int mrows;
	int ncols;
	Grid(Component c, int mrows, int ncols	) {
		rowHeight = c.getHeight() / mrows;
		colWidth = c.getWidth() / ncols;
		this.mrows = mrows;
		this.ncols = ncols;
	}
	
	int getRowHeight() {
		return rowHeight;
	}
	
	int getColWidth() {
		return colWidth;
	}
	
	int getNumRows() {
		return mrows;
	}
	
	int getNumCols() {
		return ncols;
	}
	
	// takes grid coordinates and returns a coordinate object at the center of the square
	Coordinates toAbsltCoords(Coordinates c) {
		int x;
		int y;
		x = c.getX() * colWidth;
		x += (colWidth / 2);
		y = c.getY() * rowHeight; 
		y += (rowHeight / 2);
		// x,y = center of square at row,col
		return new Coordinates(x,y);
	}
	
	// Takes the current coordinates of object and returns the square it is in
	Coordinates toGridCoords(Coordinates c) {
		int x = c.getX() - (colWidth / 2);
		x = x / colWidth;
		int y = c.getY() - (rowHeight / 2);
		y = y / rowHeight;
		return new Coordinates(x,y);
	}
	// displays white lines where the grid columns and rows are
	void drawGrid(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(Color.white);
		for(int m = 0; m < mrows; m++) {
			g2.drawLine(0, m*rowHeight, colWidth*ncols, m*rowHeight);
		}
		for(int n = 0; n < ncols; n++) {
			g2.drawLine(n*colWidth, 0, n*colWidth, rowHeight*mrows);
		}
	}
}

class Coordinates {
	private int x;
	private int y;
	Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	int getX() {
		return x;
	}
	int getY() {
		return y;
	}
	
	void setX(int x) {
		this.x = x;
	}
	
	void setY(int y) {
		this.y = y;
	}
}
// abstract class for the eggs and basket
abstract class GameObject {
	Coordinates c;
	Graphics2D g2;
	Grid grid;
	
	GameObject(Graphics g, Grid grid){
		g2 = (Graphics2D)g;
		this.grid = grid;
	}
	
	abstract void draw();
	
	// Check to see if object is on the screen
	boolean isOnGrid(Coordinates c) {
		c = grid.toGridCoords(c);
		if (c.getX() >= 0 && c.getX() < grid.getNumRows()){
			if(c.getY() >= 0 && c.getY() < grid.getNumCols()) {
				return true; 
			}
		}
		return false;
	}
	// Move the object on the screen x,y
	// takes grid coordinates
	void move(int dx, int dy) {
		c = grid.toGridCoords(c);
		c.setX(c.getX()+dx);
		c.setY(c.getY()+dy);
		c = grid.toAbsltCoords(c);
	}
}

class Egg extends GameObject {
	static final int WIDTH = 35;
	static final int HEIGHT = 45;
	static Random rand = new Random();
	int n;
	
	Egg(Graphics g, Grid grid){
		super(g, grid);
		n = rand.nextInt(7);
		c = grid.toAbsltCoords(new Coordinates(n,0));
	}
	
	void draw() {
		g2.setPaint(Color.white);
		g2.fillOval(c.getX()-(WIDTH/2), c.getY()-(HEIGHT/2), WIDTH, HEIGHT);
		g2.setStroke(new BasicStroke(2));
		g2.setPaint(Color.gray);
		g2.drawOval(c.getX()-(WIDTH/2), c.getY()-(HEIGHT/2), WIDTH, HEIGHT);
	}
	// move the egg down one row
	void drop() {
		Coordinates tmpc = c;
		move(0,1);
		if(!isOnGrid(c)){
			c = tmpc;
		}
	}
	// checks to see if egg is on the bottom row
	boolean isOnGround() {
		if(grid.toGridCoords(c).getY() == grid.getNumRows()-1) return true;
		else return false;
	}
	// resets the coordinates to the top row
	void reset() {
		n = rand.nextInt(7);
		c = grid.toAbsltCoords(new Coordinates(n,0));
	}
}

class Basket extends GameObject {
	static final int WIDTH = 50;
	static final int HEIGHT = 50;
	Basket(Graphics g, Grid grid) {
		super(g, grid);
		c = grid.toAbsltCoords(new Coordinates(3,6));
	}
	
	void draw() {
		g2.setPaint(Color.yellow);
		Shape oldClip = g2.getClip();
		Shape s = new Rectangle2D.Float(c.getX()-(WIDTH/2),c.getY(),50,25);
		g2.clip(s);
		g2.fillOval(c.getX()-(WIDTH/2), c.getY()-(HEIGHT/2), WIDTH, HEIGHT);
		g2.setClip(oldClip);
	}
	// function for moving the basket left and right
	void slide(int dir) {
		Coordinates tmpc = c;
		if (dir == -1) {
			move(-1,0);
			if (!isOnGrid(c)){
				c = tmpc;
			}
		}
		else if (dir == 1) {
			move(1,0);
			if(!isOnGrid(c)){
				c = tmpc;
			}
		}
	}
	// checks to see if the basket is in the same column with 
	// an egg, to be called when egg is on bottom row
	boolean catchesEgg(Egg egg) {
		if (c.getX() == egg.c.getX()) return true;
		else return false;
	}
}
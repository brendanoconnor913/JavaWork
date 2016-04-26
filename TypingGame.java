import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

import javax.swing.*;

// never got this working properly.... don't upload

public class TypingGame extends JFrame {
	int mistakes = 0;
	final int NUMKEYS = 2;
	static final int TIMEINIT = 100;
	static final int TIMEDEC = 3;
	HashMap<Key,Integer> keys = new HashMap();
	//Key [] keyArray = new Key[NUMKEYS];
	//int [] tRecords = new int[NUMKEYS];
	
	TypingGame() {
		
	}
		
	Boolean allTimesPositive() {
		Boolean isPositive = true;
		for(Key k : keys.keySet()) {
			if(keys.get(k) <= 0) {
				isPositive = false;
			}
		}
		return isPositive;
	}
	
	public void lostGame() {
		JOptionPane.showMessageDialog(this, "YOU LOST :(");
		System.exit(0);
	}
	
	void reset(Key keyButton) {
		keys.put(keyButton, TIMEINIT);
		repaint();
	}
	
	void decrementAll() {
		for(Key k : keys.keySet()) {
			int tmpTime = keys.get(k);
			tmpTime -= TIMEDEC;
			if(keys.get(k) <= 0) {
				lostGame();
			}
			keys.put(k, tmpTime);
		}
	}
	
	void decrementKey(Key keyButton) {
		int tmpTime = keys.get(keyButton);
		tmpTime -= TIMEDEC;
		keys.put(keyButton, tmpTime);
	}
	
	char [] activeButtons = {'k','l'};
	Boolean isActive(char e) {
		Boolean inArray = false;
		for (char c : activeButtons) {
			if (e == c) {
				inArray = true;
				break;
			}
		}
		return inArray;
	}
	
	final KeyListener kl = new KeyListener() {
		public void keyPressed(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {
			if (isActive(e.getKeyChar())) {
				System.out.print("Reset Called");
				for(Key k : keys.keySet()) {
					if (k.getName() == e.getKeyChar()) {
						reset(k);
					}
				}
			}
			else {
				System.out.print("Strike called");
				decrementAll();
			}
		}
	};
	
	void makeLayout(int centerx, int centery) {
		Key keyButton;
		Integer time;
		
		char[] letters = {'k', 'l'};
		for(int i = 0; i < NUMKEYS; i++) {
			if ((keys.keySet().size()-1) <= i) {
				time = TIMEINIT;
			}
			else {
				time = (Integer)keys.get(keys.keySet().toArray()[i]); //Get the value of the i'th key
			}
			keyButton = new Key(this,letters[i], centerx+(30*i), centery, time);
			keys.put(keyButton, time);
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		int centerx = getWidth() / 2;
		int centery = getHeight() / 2;
		
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, centerx*2, centery*2);
		
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, centerx*2, centery*2);
		
		makeLayout(centerx, centery);
	}
	
	public static void main(String [] args) {
		TypingGame game = new TypingGame();
		game.addKeyListener(game.kl);
		game.setSize(new Dimension(800,400));
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		
		JOptionPane.showMessageDialog(game, "Press OK when you're ready to begin");
		
		while(game.allTimesPositive()) {
			try {
				game.decrementAll();
				Thread.sleep(2000);
				game.repaint();
			} catch (InterruptedException e) {}
		}
		game.lostGame();
	}
}

class Key {
	TypingGame tg;
	Graphics2D g2;
	Key tkey = this;
	int time;
	static final int btnW = 25;
	static final int btnH = 25;
	final char namef;
	
	Key(TypingGame tg, char name, int x, int y, int time) {
		this.tg = tg;
		this.g2 = (Graphics2D)tg.getGraphics();
		this.time = time;
		this.namef = name;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);	

		makeButton(x, y);
		makeLine(x, y);
	}
	
	char getName() {
		return namef;
	}
	
	void makeButton(int x, int y) {
		final int xf = x;
		final int yf = y;
		Font fnt = new Font("Helvetica", Font.PLAIN, 20);
		g2.setFont(fnt);
		
		Shape b = new RoundRectangle2D.Float(x,y,btnW,btnH,10,10);
		g2.setStroke(new BasicStroke(2));
		g2.setPaint(Color.blue);
		g2.draw(b);
		g2.drawString(Character.toString(namef), x+btnW/4+2, y+20);
		

	}
	
	// x,y is the starting point for the key button
	void makeLine(int x, int y) {
		int xpos = x + (btnW/2) + 1;
		int ypos = y;
		g2.setPaint(Color.red);
		g2.drawLine(xpos, ypos-2, xpos, ypos-time);
		System.out.println(time);
	}
}


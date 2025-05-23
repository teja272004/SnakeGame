package Snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
		x[0] = UNIT_SIZE * 4; // 100px from left
	    y[0] = UNIT_SIZE * 2;
	}
	public void newApple() {
		appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;

	    // Avoid top header (40px = 1.6 units → use offset of 2 units)
	    int headerOffset = 2;
	    appleY = (headerOffset + random.nextInt((SCREEN_HEIGHT / UNIT_SIZE) - headerOffset)) * UNIT_SIZE;
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(!running) gameOver(g);
		/*for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
			g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
		}*/
		g.setColor(Color.red);
		g.fillOval(appleX,appleY, UNIT_SIZE,  UNIT_SIZE);
		
		// For the snake
		for(int i = 0;i<bodyParts;i++) {
			if(i==0) {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}else {
				//g.setColor(new Color(45,180,0));
				g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, SCREEN_WIDTH, 40); // Header bar
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 30));
		g.drawString("Score: " + applesEaten, 10, 30);

	}
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0]==appleX) && (y[0]==appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		  for (int i = bodyParts; i > 0; i--) {
		        if ((x[0] == x[i]) && (y[0] == y[i])) {
		            running = false;
		        }
		    }

		    // Wrap around screen (instead of dying on walls)
		    if (x[0] < 0) {
		        x[0] = SCREEN_WIDTH - UNIT_SIZE;
		    } else if (x[0] >= SCREEN_WIDTH) {
		        x[0] = 0;
		    }

		    if (y[0] < 0) {
		        y[0] = SCREEN_HEIGHT - UNIT_SIZE;
		    } else if (y[0] >= SCREEN_HEIGHT) {
		        y[0] = 0;
		    }

		    // Stop game only if the snake hits itself
		    if (!running) {
		        timer.stop();
		    }
		
	}
	public void gameOver(Graphics g) {
		// Game Over
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Wasted", (SCREEN_WIDTH - metrics.stringWidth("Wasted"))/2, SCREEN_HEIGHT/2);
		
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// For moving snake
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
	

}

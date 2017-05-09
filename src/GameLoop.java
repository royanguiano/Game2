import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameLoop extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 700; 
	public static final int HEIGHT = 500; 
	private Thread thread; 
	private boolean running; 
	private BufferedImage image = null; 
	private Graphics2D g;
	private int FPS = 30; 
	private int targetTime = 1000 / FPS; 
	
	private TileMap tileMap; 
	private Player player;
	private BufferedImage[] background; 
	private Animation animation; 
	
	
	public GameLoop() {
		// TODO Auto-generated constructor stub
		super();
		//background(g);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		requestFocus();
		setFocusable(true);
		
	}
	
	public void background(Graphics2D g){
		//background = new BufferedImage[1]; 
		
		background = new BufferedImage[1];  
		try{
			
			background[0] = ImageIO.read(new File("src/Resources/Background.png")); 
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		animation = new Animation(); 
		animation.setFrame(background);
		animation.setDelay(-1);
		animation.update();
	
		g.drawImage(animation.getImage(), 
					0, 
					0, 
					WIDTH, 
					HEIGHT, 
					this); 
		super.paintComponent(g);
		
	}
	
	
	public void addNotify() {
		super.addNotify();
		//starting thread
		if(thread == null){
			thread = new Thread(this); 
			thread.start();
		}
		addKeyListener(this);
	}
	
	@Override
	public void run() {
		init(); 
		
		long starTime; 
		long urdTime; 
		long waitTime; 
		
		while(running){
			starTime = System.nanoTime(); 
			update();
			render();
			draw(); 
			
			urdTime = (System.nanoTime() - starTime) / 1000000; 
			waitTime = targetTime - urdTime; 
			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//init() servers as the initializer of all objects in this game
	public void init(){
		running = true; 
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); 
		g = (Graphics2D) image.getGraphics();
		tileMap = new TileMap("Resources\\testmap.txt", 32); 
		tileMap.loadTiles();
		player = new Player(tileMap);
		//players starting position
		player.setX(290);
		player.setY(180);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	//update objects
	public void update() {
		tileMap.update();
		player.update();
	}
	
	private void render(){
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		tileMap.draw(g);//setup tiles
		
		player.draw(g);
		
	}
	//fix the draw method to draw the graphics object into the jpanel 
	//might have to crate a new draw object or something

	public void draw(){
		Graphics2D g2 = (Graphics2D) getGraphics();
		
		g2.drawImage(image, 0, 0, null);
		g2.dispose();//acts like garbage collection
		
	}


	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(code == KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(code == KeyEvent.VK_W){
			player.setJumping(true);
		}
	}


	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(code == KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(code == KeyEvent.VK_UP){
			player.setJumping(false);
		}
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

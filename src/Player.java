import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Player {

	private double x; 
	private double y; 
	private double dx; 
	private double dy; 
	private int width; 
	private int height; 
	private boolean left; 
	private boolean right; 
	private boolean jumping; 
	private boolean falling; 
	
	private double moveSpeed; 
	private double maxSPeed; 
	private double maxFallingSpeed; 
	private double StopSpeed; 
	private double jumpStart; 
	private double gravity; 
	
	private TileMap tileMap; 
	
	private boolean topLeft; 
	private boolean topRight; 
	private boolean bottomLeft; 
	private boolean bottomRight; 
	
	private Animation animation;
	private BufferedImage[] standingSprite;
	private BufferedImage[] squishedSprite;
	private BufferedImage[] jumpingLeft;
	private BufferedImage[] jumpingRight;
	private BufferedImage[] moveRight; 
	private BufferedImage[] moveLeft; 
	private boolean facingLeft; 
	
	public Player(TileMap tm) {
		// TODO Auto-generated constructor stub
		tileMap = tm; 
		width = 40;	//pixels
		height = 40;//pixels
		moveSpeed = 0.6; 
		maxSPeed = 4.2;
		maxFallingSpeed = 12; 
		StopSpeed = 0.30;
		jumpStart = 0.64;
		gravity = 0.64; 
		
		try{
			standingSprite = new BufferedImage[1]; 
			squishedSprite = new BufferedImage[11]; 
			jumpingLeft = new BufferedImage[7]; 
			jumpingRight = new BufferedImage[7]; 
			moveRight = new BufferedImage[7]; 
			moveLeft = new BufferedImage[7]; 
			
			//read in
			standingSprite[0] = ImageIO.read(new File("src/Resources/Lazarus_stand.png")); 
			BufferedImage squishedImage = ImageIO.read(new File("src/Resources/Lazarus_squished_strip11.png")); 
			BufferedImage jumpleftImage = ImageIO.read(new File("src/Resources/Lazarus_jump_left_strip7.png")); 
			BufferedImage jumprightImage = ImageIO.read(new File("src/Resources/Lazarus_jump_right_strip7.png"));
			BufferedImage moveRightImage = ImageIO.read(new File("src/Resources/Lazarus_right_strip7.png"));
			BufferedImage moveLeftImage = ImageIO.read(new File("src/Resources/Lazarus_left_strip7.png"));
			
			for(int i = 0; i < jumpingLeft.length; i++){
				jumpingLeft[i] = jumpleftImage.getSubimage(
														i * width, 
														0, 
														width, 
														height);
			}
			for(int i = 0; i < jumpingRight.length; i++){
				jumpingRight[i] = jumprightImage.getSubimage(
														i * width, 
														0, 
														width, 
														height);
			}
			for(int i = 0; i < squishedSprite.length; i++){
				squishedSprite[i] = squishedImage.getSubimage(
														i * width, 
														0, 
														width, 
														height);
			}
			for(int i = 0; i < moveLeft.length; i++){
				moveLeft[i] = moveLeftImage.getSubimage(
														i * width, 
														0, 
														width, 
														height);
			}
			for(int i = 0; i < moveRight.length; i++){
				moveRight[i] = moveRightImage.getSubimage(
														i * width, 
														0, 
														width, 
														height);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//create animation object
		animation = new Animation(); 
		facingLeft = false; 
	}
	
	//setting players x and y position with i
	public void setX(int i){
		x = i; 
	}
	public void setY(int i){
		y = i; 
	}
	
	public void setLeft(boolean b){
		//System.out.println("moving left");
		left = b; 
	}
	
	public void setRight(boolean b){
		//System.out.println("moving right");
		right = b;
	}
	public void setJumping(boolean b){
		//System.out.println(" jumping");
			jumping = true; 
	}
	
	private void calculateCorners(double x, double y){
		int leftTile = tileMap.getColTile((int) (x - width / 2)); 
		int rightTile = tileMap.getColTile((int) ( x + width / 2) - 1); 
		int topTile = tileMap.getRowTile((int) (y - height / 2)); 
		int bottomTile = tileMap.getRowTile((int) (y + height / 2) - 1); 
		topLeft = tileMap.isBlocked(topTile, leftTile);
		topRight = tileMap.isBlocked(topTile, rightTile); 
		bottomLeft = tileMap.isBlocked(bottomTile, leftTile); 
		bottomRight = tileMap.isBlocked(bottomTile, rightTile); 
	}
	
	public void update() {
		//determine next position
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSPeed){
				dx = -maxSPeed;
			}
		} else if(right){
			dx += moveSpeed; 
			if(dx > maxSPeed){
				dx = maxSPeed;
			}
		} else {
			//if still moving towards the right
			if(dx > 0){
				dx -= StopSpeed;	//friction
				if(dx < 0){
					dx = 0; 
				}
			} else if( dx < 0){
				dx += StopSpeed;
				if(dx > 0){
					dx = 0; 
				}
			}
		}
		
		//if jumping
		if(jumping){
			//System.out.println("jumping");
			dy -= jumpStart;
			falling = true; 
			jumping = false; 
		}
		
		//if falling
		if(falling){
			//System.out.println("falling");
			dy += gravity; 
			if(dy > maxFallingSpeed){
				dy = maxFallingSpeed;
			}
		}
		else {
			//not falling, we are on the ground here
			dy = 0; 
		}
		
		//check collisions
		int currentCol = tileMap.getColTile((int) x);
		int currentRow = tileMap.getRowTile((int) y); 
		
		double toX = x + dx; 
		double toY = y + dy; 
		
		double tempX = x; 
		double tempY = y; 
		
		calculateCorners(x, toY); 
		if(dy > 0){	//if we are moving up
			if(topLeft || topRight){
				dy = 0;
				tempY = currentRow * tileMap.getTileSize() + height / 2;
			} else {
				tempY += dy;
			}
		}
		
		if(dy > 0){	//doing downward
			if(bottomLeft || bottomRight){
				dy = 0; 
				falling = false; 
				tempY = (currentRow + 1) * tileMap.getTileSize() - height / 2; 
			} else {
				tempY += dy;
			}
		}
		
		calculateCorners(toX,y);
		if(dx < 0){	//if we are moving to the left
			if(topLeft || bottomLeft){
				dx = 0; 
				tempX = currentCol * tileMap.getTileSize() + width / 2; //reached a wall
			} else {
				tempX += dx; 
			}
		}
		
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0; 
				tempX = (currentCol + 1) * tileMap.getTileSize() - width / 2; 
			} else {
				tempX += dx; 
			}
		}
		
		if(!falling){
			calculateCorners(x, y + 1);	//check if we are falling from a cliff
			if(!bottomLeft && !bottomRight){
				falling = true; 
			}
		}
		
		x = tempX;
		y = tempY; 
		
		//move the map with player
		tileMap.setX((int) (GameLoop.WIDTH / 2 - x));
		tileMap.setY((int)(GameLoop.HEIGHT / 2 - y ));
		
		
		//do animation
		//sprite animation
		//jumpleft, jumpright, squish
		
		if(left){
			animation.setFrame(moveLeft);
			animation.setDelay(100);
		} else if(right){
			animation.setFrame(moveRight);
			animation.setDelay(100);
		} else{
			animation.setFrame(standingSprite);
			animation.setDelay(-1);
		}
			
		if(dy < 0){//jumping
			animation.setFrame(jumpingLeft);
			animation.setDelay(100);
		} else if(dy > 0){
			animation.setFrame(squishedSprite);
			animation.setDelay(100);
		}
		
		animation.update();
		
		//if moving towards the left
		if(dx < 0){
			facingLeft = true; 
		}
		
		if(dx > 0){
			facingLeft = false; 
		}
	}
	
	public void draw(Graphics2D g){
		//offset of tile map
		int tx = tileMap.getX(); 
		int ty = tileMap.getY(); 
		
		if(facingLeft){
			g.drawImage(animation.getImage(), 
						(int) (tx + x -  width / 2), 
						(int) (ty + y - height / 2), 
						null); 
		} else {
			g.drawImage(animation.getImage(),
						(int) (tx + x - width / 2 + width), 
						(int) (ty + y - height / 2), 
						-width,
						height, 
						null);
		}
	}
}

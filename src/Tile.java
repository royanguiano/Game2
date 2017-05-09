import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image; 
	private boolean blocked; 
	

	public Tile(BufferedImage image, boolean blocked) {
		// TODO Auto-generated constructor stub
		this.image = image;
		this.blocked = blocked; 
	}
	
	public void setBlocked(boolean block){
		this.blocked = block; 
	}
	
	public BufferedImage getImage(){
		return image; 
	}
	public boolean isBlocked(){
		return blocked; 
	}
}

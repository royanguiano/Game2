import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame window = new JFrame("game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GameLoop());
		window.pack();
		window.setVisible(true);
	}
}

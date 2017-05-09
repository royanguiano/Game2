import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TileMap {

	private int x;
	private int y;
	private int tileSize;
	private int[][] map;
	private int mapWidth;
	private int mapHeight;

	private BufferedImage tileset; // the tileset.gif we are using
	private Tile[][] tiles; //
	private int minx;
	private int miny;
	private int maxx = 0;
	private int maxy = 0;
	private BufferedImage[] wall;
	private BufferedImage[] woodBox;
	private BufferedImage[] rock;
	private BufferedImage[] mesh;
	private BufferedImage[] stoneBox;
	private BufferedImage[] stopButton;
	private BufferedImage[] cardBox;
	private BufferedImage[] white;
	private BufferedImage[] background;
	private BufferedImage[] transparentBackground;

	private Animation animation;

	public TileMap(String s, int tileSize) {
		this.tileSize = tileSize;

		try {
			BufferedReader bReader = new BufferedReader(new FileReader("src/Resources/testmap.txt"));
			mapWidth = Integer.parseInt(bReader.readLine());
			mapHeight = Integer.parseInt(bReader.readLine());
			map = new int[mapHeight][mapWidth];

			minx = (GameLoop.WIDTH - mapWidth) * tileSize;
			miny = (GameLoop.HEIGHT - mapHeight) * tileSize;

			// delimiters
			String delimeters = "\\s+"; // any white space
			for (int row = 0; row < mapHeight; row++) { // loop 15 times
				String line = bReader.readLine();
				String[] tokens = line.split(delimeters);
				for (int col = 0; col < mapWidth; col++) { // loop 20 times
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		wall = new BufferedImage[1];
		woodBox = new BufferedImage[1];
		rock = new BufferedImage[1];
		mesh = new BufferedImage[1];
		stoneBox = new BufferedImage[1];
		stopButton = new BufferedImage[1];
		cardBox = new BufferedImage[1];
		white = new BufferedImage[1];
		background = new BufferedImage[1];
		transparentBackground = new BufferedImage[1];

		try {
			wall[0] = ImageIO.read(new File("src/Resources/Wall.png"));
			woodBox[0] = ImageIO.read(new File("src/Resources/WoodBox.png"));
			rock[0] = ImageIO.read(new File("src/Resources/Rock.png"));
			mesh[0] = ImageIO.read(new File("src/Resources/Mesh.png"));
			stoneBox[0] = ImageIO.read(new File("src/Resources/StoneBox.png"));
			stopButton[0] = ImageIO.read(new File("src/Resources/Button.png"));
			cardBox[0] = ImageIO.read(new File("src/Resources/CardBox.png"));
			white[0] = ImageIO.read(new File("src/Resources/tre.gif"));
			background[0] = ImageIO.read(new File("src/Resources/Background.png"));
			transparentBackground[0] = ImageIO.read(new File("src/Resources/tre.gif"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		animation = new Animation();
	}

	public void loadTiles() {
		try {
			tileset = ImageIO.read(new File("src/Resources/tileset.gif")); // read
																			// images
			int numTilesAcross = (tileset.getWidth() + 1) / (tileSize + 1); // finding
																			// out
																			// how
																			// wide
																			// is
																			// the
																			// image
			tiles = new Tile[2][numTilesAcross]; // create a new tiles array

			BufferedImage suBufferedImage;

			for (int col = 0; col < numTilesAcross; col++) { // loop 13 times
				suBufferedImage = tileset.getSubimage(col * tileSize + col, 0, tileSize, tileSize);

				tiles[0][col] = new Tile(suBufferedImage, false); // create a
																	// new tile
																	// array for
																	// first row

				if (tiles[0][col].equals(0)) {
					tiles[0][col].setBlocked(false);
				}

				suBufferedImage = tileset.getSubimage(col * tileSize + col, tileSize + 1, tileSize, tileSize);

				tiles[1][col] = new Tile(suBufferedImage, true); // blocked tile

				if (!tiles[1][col].equals(0)) {
					tiles[1][col].setBlocked(true);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getColTile(int x) {
		// find out what tile the player is on
		return x / tileSize;

	}

	public int getTile(int row, int col) {
		return map[row][col];
	}

	public int getRowTile(int y) {
		return y / tileSize;
	}

	public int getTileSize() {
		return tileSize;
	}

	public boolean isBlocked(int row, int col) {
		int rc = map[row][col]; // get position of tile from txt file from 2D
								// array
		int r = rc / tiles[0].length; // row of tile image
		int c = rc % tiles[0].length; // column of tile image
		return tiles[r][c].isBlocked();
	}

	public void setX(int x) {
		this.x = x;
		if (x < minx) {
			x = minx;
		}
		if (x > maxx) {
			x = maxx;
		}
	}

	public void setY(int y) {
		this.y = y;
		if (y < miny) {
			y = miny;
		}
		if (y > maxy) {
			y = maxy;
		}
	}

	public void update() {
		// System.out.println("calling update");

	}

	public void draw(Graphics2D g) {
		for (int row = 0; row < mapHeight; row++) {// loop 15 times
			for (int col = 0; col < mapWidth; col++) { // loop 20 times
				int rc = map[row][col]; // get index at this position

				int r = rc / tiles[0].length; // connect # in map with index
												// number in image array
				int c = rc % tiles[0].length;

				if (row == 0 && col == 0) {
					animation.setFrame(background);
					animation.setDelay(-1);
					animation.update();

					g.drawImage(animation.getImage(), // get image from gif at
														// specified index
							0, // draw at this position
							0, // draw at this position
							null);

					g.drawRect(row, col, r, c);
				} else {

					if (rc == 0) {
						// draw another image buffer
						animation.setFrame(transparentBackground);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 13) {
						// draw another image buffer
						animation.setFrame(wall);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 14) {
						// draw another image buffer
						animation.setFrame(woodBox);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 7) {
						// draw another image buffer
						animation.setFrame(stopButton);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 8) {
						// draw another image buffer
						animation.setFrame(mesh);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 6) {
						// draw another image buffer
						animation.setFrame(stoneBox);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 5) {
						// draw another image buffer
						animation.setFrame(rock);
						animation.setDelay(-1);
						animation.update();
					} else if (rc == 3) {
						// draw another image buffer
						animation.setFrame(cardBox);
						animation.setDelay(-1);
						animation.update();
					}
					 animation.update();

					g.drawImage(animation.getImage(), // get image from gif at
														// specified index
							x + col * tileSize, // draw at this position
							y + row * tileSize, // draw at this position
							null);

					g.fillRect(col, row, tileSize, tileSize);
				}
			}
		}
	}
}

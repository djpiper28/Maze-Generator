package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.scene.paint.Color;

public class mazegen implements Runnable {
	
	@SuppressWarnings("exports")
	public static BufferedImage mazeImage;
	
	public static int width;
	public static int height;
	private static int entranceY;;
	private static int exitY;
	public static float scale;
	public static int max;
	private final static int white = 0xFFFFFF;
	public static final int maxRand = 500;	
	public static int[][] adjMat;

	public static int[] pivotColumns;
	public static int[] deletedRows;

	public static int pivotColumnsLength;
	public static int deletedRowsLength;
	
	private static renderer renderObject;
	private static Thread renderThread;
	
	private static File file;	

	private static Random rand;
	
	private void randInt(int x, int y) {
		int random = rand.nextInt();
		if (random < 0) {
			random *= -1;			
		} 
		adjMat[x][y] = random % maxRand;
	}
	
	public static void drawArc(int adjMatX, int adjMatY) {
		
		int x1 = ( (adjMatX % width) * 2 ) + 1;
		int y1 = ( (adjMatX / width) * 2 ) + 1;
	
		int x2 = ( (adjMatY % width) * 2 ) + 1;
		int y2 = ( (adjMatY / width) * 2 ) + 1;
		
		int x3 = (x1 + x2) / 2;
		int y3 = (y1 + y2) / 2;
		
		mazeImage.setRGB(x1, y1, white);
		mazeImage.setRGB(x2, y2, white);
		mazeImage.setRGB(x3, y3, white);
	}

	private void primms() {
		primms.executePrimms();
	}

	private void populateAdjMat() {
		max = height*width;
		
		loadingScreen();
		
		adjMat = new int[max][max];
		for(int x = 0; x < max; x++) {
			for(int y =0; y < max; y++) {
				adjMat[x][y] = -1;
			}
		}
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int Coord = (width * y) + x;
				
				if(x < width - 1) {
					randInt(Coord + 1, Coord);
					randInt(Coord, Coord + 1);
				}
				if(x > 0) {
					randInt(Coord - 1, Coord);
					randInt(Coord, Coord - 1);
				}
				if(y < height - 1) {
					randInt(Coord, Coord + width);
					randInt(Coord, Coord + width);
				}
				if(y > 0) {
					randInt(Coord, Coord - width);
					randInt(Coord, Coord - width);
				}
				
			}
		}
	}

	private void addEntranceAndExit() {
		mazeImage.setRGB(0, entranceY*2+1, white);
		mazeImage.setRGB(width*2, exitY*2+1, white);
	}

	private void loadingScreen() {
		gui.graphicsContext.fillText("A Really Good Loading Screen!", 1920/2 , 1050/2 );
	}
	
	private void setBGToGrey() {
		gui.graphicsContext.setFill(Color.DARKGREY);
		gui.graphicsContext.fillRect(0, 0, gui.XMAX, gui.YMAX);
	}

	private void setImageToBlack() {
		for(int x = 0; x< width * 2; x++) {
			for(int y = 0; y< height * 2; y++) {
				mazeImage.setRGB(x,y,1);
			}
		}
	}
	
	private void generate() {

		System.out.println("Setting image to black");		
		setImageToBlack();
		System.out.println("Set image to black");

		System.out.println("Adding entrance and exit");		
		addEntranceAndExit();
		System.out.println("Added entrance and exit");
		
		System.out.println("Populating adjacency matrix");
		populateAdjMat();
		System.out.println("Populated adjacency matrix");
		
		setBGToGrey();
		
		long time = System.currentTimeMillis();
		System.out.println("Applying Primms...");
		primms();
		System.out.println("Applied Primms in "+(System.currentTimeMillis() - time)+"ms");
		
		saveImage();
		
		System.exit(0);
	}
	
	private void saveImage(){
		try {
			ImageIO.write(mazeImage, "png", file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void render() {
		renderThread = new Thread(renderObject, "Render Thread");
		renderThread.start();
	}

	public mazegen(int widthIn, int heightIn, float scaleIn, File imageFile, int entranceYIn, int exitYIn) {
		this.file = imageFile;
		this.width = widthIn;
		this.height = heightIn;
		this.scale = scaleIn;
		this.entranceY = entranceYIn;
		this.exitY = exitYIn;
		
		assert(height>0);
		assert(width>0);
		assert(scale>0);
		
		this.rand = new Random();
		
		System.out.println("Graph Width: "+this.width+" Graph Height: "+this.height+" Entrance Y: "+this.entranceY
				+" Exit Y: "+this.exitY+ " Scale: "+this.scale+" Filename: "+this.file.getName());
		
		mazeImage = new BufferedImage(width*2 +1, height*2 +1, BufferedImage.TYPE_INT_RGB);
		
		System.out.println("Image Width: "+mazeImage.getWidth()+" Image Height: "+mazeImage.getHeight());
		
		mazeImage.setAccelerationPriority(1);
		
		this.renderObject = new renderer(width*2 +1, height*2 +1, scale);	
	}
	
	@Override
	public void run() {
		generate();		
	}
	
}

package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;

import dannypiper.mazegenerator.kuskals.kruskals;
import dannypiper.mazegenerator.kuskals.sorting.sortType;
import dannypiper.mazegenerator.primms.primmsAdjMat;
import dannypiper.mazegenerator.primms.primmsProcedual;
import dannypiper.mazegenerator.primms.primmsUtils;
import javafx.scene.paint.Color;

public class mazegen implements Runnable {
	
	@SuppressWarnings("exports")
	private boolean primms;
	private sortType type;
	
	public static BufferedImage mazeImage;
	
	public static int width;
	public static int height;
	public static int entranceY;;
	private static int exitY;
	public static float scale;
	public static int max;
	private static boolean procedual;
	
	private static final int white = 0xFFFFFF;
	public static final long frameRate = 16;
	public static final int procedualThreshold = 100;
	public static final short maxRand = 500;	
	public static final short halfMaxRand = maxRand / 2;
	
	private static renderer renderObject;
	private static Thread renderThread;
	
	private static File file;	

	private static Random rand;
	
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

	private void primmsAdjMat() {
		primmsAdjMat.executePrimms();
	}
	
	private void primmsProcedual() {
		primmsProcedual.executePrimms();
	}

	public static short randInt() {
		int random = rand.nextInt();
		if (random < 0) {
			random *= -1;			
		} 
		return (short) (random % maxRand);
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
		for(int x = 0; x< width * 2 + 1; x++) {
			for(int y = 0; y< height * 2 + 1; y++) {
				mazeImage.setRGB(x,y,0);
			}
		}
	}
	
	private void generate() {
		long time = System.currentTimeMillis();
		System.out.println("Setting image to black");		
		setImageToBlack();
		System.out.println("Set image to black");
		System.out.println("Set image to black in "+(System.currentTimeMillis() - time)+"ms");
		
		time = System.currentTimeMillis();
		System.out.println("Adding entrance and exit");		
		addEntranceAndExit();
		System.out.println("Added entrance and exit");
		System.out.println("Added entrance and exit in "+(System.currentTimeMillis() - time)+"ms");

		max = height*(width+1);
		
		loadingScreen();
		if(this.primms) {
			if(Runtime.getRuntime().totalMemory()/1024/1024 <= 500) {
				mazegen.procedual = true;
			}
			
			if( (mazegen.width < mazegen.procedualThreshold && mazegen.height < mazegen.procedualThreshold) 
					&& !(mazegen.procedual)) {				
				time = System.currentTimeMillis();
				System.out.println("Populating adjacency matrix");
				primmsUtils.populateAdjMat();
				System.out.println("Populated adjacency matrix");
				System.out.println("Populated adjacency matrix in "+(System.currentTimeMillis() - time)+"ms");
				System.out.println("Memory usage: " + (Runtime.getRuntime().totalMemory() 
						- Runtime.getRuntime().freeMemory()) /1024 /1024 + "MB in use.");
							
				time = System.currentTimeMillis();
				System.out.println("Applying Primms adj mat ...");
				primmsAdjMat();
				System.out.println("Applied Primms adj mat in "+(System.currentTimeMillis() - time)+"ms");
			} else {
				
				time = System.currentTimeMillis();
				System.out.println("Applying Primms procedual...");
				primmsProcedual();
				System.out.println("Applied Primms procedual in "+(System.currentTimeMillis() - time)+"ms");
			}
		} else {
			System.out.println("Applying Kruskals...");
			kruskals Kruskals = new kruskals(this.type);
			Kruskals.run();
			System.out.println("Applied Kruskals in "+(System.currentTimeMillis() - time)+"ms");
		}
		
		render();
		
		imageFile.saveImage(mazegen.mazeImage, mazegen.file);
				
		System.exit(0);
	}

	public static void render() {
		renderThread = new Thread(renderObject, "Render Thread");
		renderThread.setPriority(10);
		renderThread.start();
	}

	public mazegen(int widthIn, int heightIn, float scaleIn, File imageFile, int entranceYIn,
			int exitYIn, boolean procedualIN, int screenWidth, int screenHeight, boolean primms, sortType type) throws Exception {
		mazegen.file = imageFile;
		mazegen.width = widthIn;
		mazegen.height = heightIn;
		mazegen.scale = scaleIn;
		mazegen.entranceY = entranceYIn;
		mazegen.exitY = exitYIn;
		mazegen.procedual = procedualIN;
		this.primms = primms;
		this.type = type;
		
		assert(height>1);
		assert(imageFile != null);
		assert(width>1);
		assert(scale>0);
		
		mazegen.rand = new Random();
		 
		System.out.println("Graph Width: " + mazegen.width + " Graph Height: " + mazegen.height+
				" Entrance Y: " + mazegen.entranceY + " Exit Y: " + mazegen.exitY + " Scale: "
				+ mazegen.scale + " Filename: " + mazegen.file.getName());
		
		if(width > screenWidth * 2 || height > screenHeight * 2) {
			mazegen.renderObject = new renderless(width*2 +1, height*2 +1, scale);
			gui.graphicsContext.fillText("Maze too big to be displayed", 10, 10);
		} else {
			mazegen.renderObject = new renderer(width*2 +1, height*2 +1, scale);
			setBGToGrey();
		}
		try {
			mazeImage = new BufferedImage(width*2 +1, height*2 +1, BufferedImage.TYPE_INT_RGB);

			System.out.println("Image Width: "+mazeImage.getWidth()+" Image Height: "+mazeImage.getHeight());
			
			mazeImage.setAccelerationPriority(1);
			
		} catch(Exception e) {
			gui.showError("Window is too large!");
			throw new Exception("Window too large");
		}			
	}
	
	@Override
	public void run() {
		try {
			generate();
		} catch (Exception e) {
			e.printStackTrace ( );
			System.out.println("Critical error.");
			String stackTraceMsg = "";
			for(StackTraceElement error : e.getStackTrace()) {
				stackTraceMsg += error.toString();
			}
			gui.showError("Critical error:\nParameters Width: " + mazegen.width+" Height: " + mazegen.height 
					+ "\nError Details:\n" + stackTraceMsg);
		}
	}
	
}

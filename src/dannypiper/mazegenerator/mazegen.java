package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class mazegen implements Runnable {
	
	@SuppressWarnings("exports")
	public static BufferedImage mazeImage;
	
	private int width;
	private int height;
	private int entranceY;;
	private int exitY;
	private int max;
	private float scale;
	private final static int white = 0xFFFFFF;
	private final static int maxRand = 50000;	
	
	private renderer renderObject;
	private Thread renderThread;
	
	private File file;	

	private int[][] adjMat;
	private Random rand;
	
	private void randInt(int x, int y) {
		int random = rand.nextInt();
		if (random < 0) {
			random *= -1;			
		} 
		adjMat[x][y] = random % maxRand;
	}
	
	private void primms() {
		long frameControlTime = System.currentTimeMillis();
		
		int nodesFound = 0;
		int nodesNeeded = height * width - 1;
		
		int[] pivotColumns = new int[nodesNeeded+1];
		int[] deletedRows = new int[nodesNeeded+1];
	
		pivotColumns[0] = height * width / 2;
		deletedRows[0] = pivotColumns[0];
		
		int pivotColumnsLength = 1;
		int deletedRowsLength = 1;		
		
		while(nodesFound < nodesNeeded) {
			
			//PRIMMS
			int minValue = maxRand+1;
			int column = 0;
			int row = 0;
			
			for(int i = 0; i < pivotColumnsLength; i++) {
				int x = pivotColumns[i];
				for(int y = 0; y < max; y++) {
					boolean deleted = false;
					int j = 0; 
					while(j < deletedRowsLength && !deleted) {	
						if (y==deletedRows[j]) {
							deleted = true;
						}
						j++;
					}
					
					if(!deleted) {
						if((adjMat[x][y] < minValue) && adjMat[x][y]!=-1) {
							minValue = adjMat[x][y];
							column = x;
							row = y;		
							
							if(minValue==0) {
								break;			//TODO: Remove break
							}
						}
					}
				}
	
				if(minValue==0) {
					break;						//TODO: Remove break
				}
			}
	
			deletedRows[deletedRowsLength] = row;
			deletedRowsLength++;
	
			pivotColumns[pivotColumnsLength] = row;
			pivotColumnsLength++;
	
			drawArc(column, row);
			
			if(System.currentTimeMillis() - frameControlTime >=16) {				
				frameControlTime = System.currentTimeMillis();
				render();
			}
			nodesFound++;
		}
	}

	private void drawArc(int adjMatX, int adjMatY) {
		
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
				}
				if(x > 0) {
					randInt(Coord - 1, Coord);
				}
				if(y < height - 1) {
					randInt(Coord, Coord + width);
				}
				if(y > 0) {
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
		gui.graphicsContext.fillText("Loading", 1920/2 , 1050/2 );
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

	public void render() {
		this.renderThread = new Thread(renderObject, "Render Thread");
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

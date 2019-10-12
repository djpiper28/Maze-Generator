package dannypiper.mazegenerator;

import java.util.Random;

public class generator {
	public boolean cancelled;
	
	private int width;
	private int height;
	private int[][] adjMat;
	private Random rand;
	private renderer renderOBJ;
	
	private void randInt(int x, int y) {
		adjMat[x][y] = rand.nextInt();
	}
	
	private void populateAdjMat() {
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				if(y==x-1 || y==x+1 || y==x-width-1 || y==x-width+1) {
					randInt(x,y);
				}
			}
		}
	}
	
	private void primms() {
		Thread renderThread = new Thread(this.renderOBJ);
		long time = System.currentTimeMillis();
		while(!cancelled) {
			
			
			
			if(System.currentTimeMillis() - time >=16) {				
				time = System.currentTimeMillis();
				renderThread.run();
			}
		}
	}
	
	public int[][] generate() {
		populateAdjMat();
		
		primms();
		
		if(cancelled) {
			return null;
		}
		
		return adjMat;
	}
	
	public generator(int x, int y, renderer renderOBJ) {
		this.width = x;
		this.height = y;
		this.renderOBJ = renderOBJ;
		this.cancelled = false;
	}
	
}

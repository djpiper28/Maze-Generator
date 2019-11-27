package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import dannypiper.mazegenerator.kuskals.kruskals;
import dannypiper.mazegenerator.kuskals.sorting.sortType;
import dannypiper.mazegenerator.primms.primmsAdjMat;
import dannypiper.mazegenerator.primms.primmsProcedual;
import dannypiper.mazegenerator.primms.primmsUtils;
import javafx.scene.paint.Color;

public class mazegen implements Runnable {

	public static BufferedImage mazeImage;
	public static int width;

	public static int height;

	public static int entranceY;
	private static int exitY;
	public static float scale;;
	public static int max;
	private static boolean procedual;
	private static final int white = 0xFFFFFF;
	public static final long frameRate = 16;

	public static final int procedualThreshold = 100;
	public static final short maxRand = 500;
	public static final short halfMaxRand = mazegen.maxRand / 2;
	private static renderer renderObject;
	private static Thread renderThread;

	private static File file;
	private static Random rand;

	public static void drawArc ( final int adjMatX, final int adjMatY ) {

		final int x1 = ( ( adjMatX % mazegen.width ) * 2 ) + 1;
		final int y1 = ( ( adjMatX / mazegen.width ) * 2 ) + 1;

		final int x2 = ( ( adjMatY % mazegen.width ) * 2 ) + 1;
		final int y2 = ( ( adjMatY / mazegen.width ) * 2 ) + 1;

		final int x3 = ( x1 + x2 ) / 2;
		final int y3 = ( y1 + y2 ) / 2;

		mazegen.mazeImage.setRGB ( x1, y1, mazegen.white );
		mazegen.mazeImage.setRGB ( x2, y2, mazegen.white );
		mazegen.mazeImage.setRGB ( x3, y3, mazegen.white );
	}

	public static short randInt ( ) {
		int random = mazegen.rand.nextInt ( );

		if ( random < 0 ) {
			random *= - 1;
		}

		return ( short ) ( random % mazegen.maxRand );
	}

	public static void render ( ) {
		mazegen.renderThread = new Thread ( mazegen.renderObject, "Render Thread" );
		mazegen.renderThread.setPriority ( 10 );
		mazegen.renderThread.start ( );
	}

	@SuppressWarnings ( "exports" )
	private final boolean primms;

	private final sortType type;

	public mazegen ( final int widthIn, final int heightIn, final float scaleIn, final File imageFile,
	        final int entranceYIn, final int exitYIn, final boolean procedualIN, final int screenWidth,
	        final int screenHeight, final boolean primms, final sortType type ) throws Exception {
		mazegen.file = imageFile;
		mazegen.width = widthIn;
		mazegen.height = heightIn;
		mazegen.scale = scaleIn;
		mazegen.entranceY = entranceYIn;
		mazegen.exitY = exitYIn;
		mazegen.procedual = procedualIN;
		this.primms = primms;
		this.type = type;

		assert ( mazegen.height > 1 );
		assert ( imageFile != null );
		assert ( mazegen.width > 1 );
		assert ( mazegen.scale > 0 );

		mazegen.rand = new Random ( );

		System.out.println ( "Graph Width: " + mazegen.width + " Graph Height: " + mazegen.height + " Entrance Y: "
		        + mazegen.entranceY + " Exit Y: " + mazegen.exitY + " Scale: " + mazegen.scale + " Filename: "
		        + mazegen.file.getName ( ) );

		if ( ( mazegen.width > ( screenWidth * 2 ) ) || ( mazegen.height > ( screenHeight * 2 ) ) ) {
			mazegen.renderObject = new renderless ( ( mazegen.width * 2 ) + 1, ( mazegen.height * 2 ) + 1,
			        mazegen.scale );
			gui.graphicsContext.fillText ( "Maze too big to be displayed", 10, 10 );
		}
		else {
			mazegen.renderObject = new renderer ( ( mazegen.width * 2 ) + 1, ( mazegen.height * 2 ) + 1,
			        mazegen.scale );
			this.setBGToGrey ( );
		}

		try {
			mazegen.mazeImage = new BufferedImage ( ( mazegen.width * 2 ) + 1, ( mazegen.height * 2 ) + 1,
			        BufferedImage.TYPE_INT_RGB );

			System.out.println ( "Image Width: " + mazegen.mazeImage.getWidth ( ) + " Image Height: "
			        + mazegen.mazeImage.getHeight ( ) );

			mazegen.mazeImage.setAccelerationPriority ( 1 );

		}
		catch ( final Exception e ) {
			gui.showError ( "Window is too large!" );
			throw new Exception ( "Window too large" );
		}

	}

	private void addEntranceAndExit ( ) {
		mazegen.mazeImage.setRGB ( 0, ( mazegen.entranceY * 2 ) + 1, mazegen.white );
		mazegen.mazeImage.setRGB ( mazegen.width * 2, ( mazegen.exitY * 2 ) + 1, mazegen.white );
	}

	private void generate ( ) {
		long time = System.currentTimeMillis ( );
		System.out.println ( "Setting image to black" );
		this.setImageToBlack ( );
		System.out.println ( "Set image to black" );
		System.out.println ( "Set image to black in " + ( System.currentTimeMillis ( ) - time ) + "ms" );

		time = System.currentTimeMillis ( );
		System.out.println ( "Adding entrance and exit" );
		this.addEntranceAndExit ( );
		System.out.println ( "Added entrance and exit" );
		System.out.println ( "Added entrance and exit in " + ( System.currentTimeMillis ( ) - time ) + "ms" );

		mazegen.max = mazegen.height * ( mazegen.width + 1 );

		this.loadingScreen ( );

		if ( this.primms ) {

			if ( ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) <= 500 ) {
				mazegen.procedual = true;
			}

			if ( ( ( mazegen.width < mazegen.procedualThreshold ) && ( mazegen.height < mazegen.procedualThreshold ) )
			        && ! ( mazegen.procedual ) ) {
				time = System.currentTimeMillis ( );
				System.out.println ( "Populating adjacency matrix" );
				primmsUtils.populateAdjMat ( );
				System.out.println ( "Populated adjacency matrix" );
				System.out
				        .println ( "Populated adjacency matrix in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
				System.out.println ( "Memory usage: "
				        + ( ( Runtime.getRuntime ( ).totalMemory ( ) - Runtime.getRuntime ( ).freeMemory ( ) ) / 1024
				                / 1024 )
				        + "MB in use." );

				time = System.currentTimeMillis ( );
				System.out.println ( "Applying Primms adj mat ..." );
				this.primmsAdjMat ( );
				System.out.println ( "Applied Primms adj mat in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
			}
			else {

				time = System.currentTimeMillis ( );
				System.out.println ( "Applying Primms procedual..." );
				this.primmsProcedual ( );
				System.out.println ( "Applied Primms procedual in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
			}

		}
		else {
			System.out.println ( "Applying Kruskals..." );
			final kruskals Kruskals = new kruskals ( this.type );
			Kruskals.run ( );
			System.out.println ( "Applied Kruskals in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
		}

		mazegen.render ( );

		imageFile.saveImage ( mazegen.mazeImage, mazegen.file );

		System.exit ( 0 );
	}

	private void loadingScreen ( ) {
		gui.graphicsContext.fillText ( "A Really Good Loading Screen!", 1920 / 2, 1050 / 2 );
	}

	private void primmsAdjMat ( ) {
		primmsAdjMat.executePrimms ( );
	}

	private void primmsProcedual ( ) {
		primmsProcedual.executePrimms ( );
	}

	@Override
	public void run ( ) {

		try {
			this.generate ( );
		}
		catch ( final Exception e ) {
			e.printStackTrace ( );
			System.out.println ( "Critical error." );
			String stackTraceMsg = "";

			for ( final StackTraceElement error : e.getStackTrace ( ) ) {
				stackTraceMsg += error.toString ( );
			}

			gui.showError ( "Critical error:\nParameters Width: " + mazegen.width + " Height: " + mazegen.height
			        + "\nError Details:\n" + stackTraceMsg );
		}

	}

	private void setBGToGrey ( ) {
		gui.graphicsContext.setFill ( Color.DARKGREY );
		gui.graphicsContext.fillRect ( 0, 0, gui.XMAX, gui.YMAX );
	}

	private void setImageToBlack ( ) {

		for ( int x = 0; x < ( ( mazegen.width * 2 ) + 1 ); x ++ ) {

			for ( int y = 0; y < ( ( mazegen.height * 2 ) + 1 ); y ++ ) {
				mazegen.mazeImage.setRGB ( x, y, 0 );
			}

		}

	}

}

package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import dannypiper.mazegenerator.kuskals.Kruskals;
import dannypiper.mazegenerator.kuskals.sorting.sortType;
import dannypiper.mazegenerator.primms.PrimmsAdjMat;
import dannypiper.mazegenerator.primms.PrimmsProcedural;
import dannypiper.mazegenerator.primms.PrimmsUtils;
import javafx.scene.paint.Color;

public class Mazegen implements Runnable {

	public static BufferedImage mazeImage;
	public static int width;

	public static int height;

	public static int entranceY;
	private static int exitY;
	public static float scale;;
	public static int max;
	private static boolean procedural;
	private static final int white = 0xFFFFFF;
	public static final long frameRate = 16;

	public static final int proceduralThreshold = 100;
	public static final short maxRand = 500;
	public static final short halfMaxRand = Mazegen.maxRand / 2;
	private static Renderer renderObject;
	private static Thread renderThread;

	private static File file;
	private static Random rand;

	public static void drawArc ( final int adjMatX, final int adjMatY ) {

		final int x1 = ( ( adjMatX % Mazegen.width ) * 2 ) + 1;
		final int y1 = ( ( adjMatX / Mazegen.width ) * 2 ) + 1;

		final int x2 = ( ( adjMatY % Mazegen.width ) * 2 ) + 1;
		final int y2 = ( ( adjMatY / Mazegen.width ) * 2 ) + 1;

		final int x3 = ( x1 + x2 ) / 2;
		final int y3 = ( y1 + y2 ) / 2;

		Mazegen.mazeImage.setRGB ( x1, y1, Mazegen.white );
		Mazegen.mazeImage.setRGB ( x2, y2, Mazegen.white );
		Mazegen.mazeImage.setRGB ( x3, y3, Mazegen.white );
	}

	public static short randInt ( ) {
		int random = Mazegen.rand.nextInt ( );

		if ( random < 0 ) {
			random *= - 1;
		}

		return ( short ) ( random % Mazegen.maxRand );
	}

	public static void render ( ) {
		Mazegen.renderThread = new Thread ( Mazegen.renderObject, "Render Thread" );
		Mazegen.renderThread.setPriority ( 10 );
		Mazegen.renderThread.start ( );
	}

	@SuppressWarnings ( "exports" )
	private final boolean primms;

	private final sortType type;

	public Mazegen ( final int widthIn, final int heightIn, final float scaleIn, final File imageFile,
	        final int entranceYIn, final int exitYIn, final boolean procedualIN, final int screenWidth,
	        final int screenHeight, final boolean primms, final sortType type ) throws Exception {
		Mazegen.file = imageFile;
		Mazegen.width = widthIn;
		Mazegen.height = heightIn;
		Mazegen.scale = scaleIn;
		Mazegen.entranceY = entranceYIn;
		Mazegen.exitY = exitYIn;
		Mazegen.procedural = procedualIN;
		this.primms = primms;
		this.type = type;

		assert ( Mazegen.height > 1 );
		assert ( imageFile != null );
		assert ( Mazegen.width > 1 );
		assert ( Mazegen.scale > 0 );

		Mazegen.rand = new Random ( );

		System.out.println ( "Graph Width: " + Mazegen.width + " Graph Height: " + Mazegen.height + " Entrance Y: "
		        + Mazegen.entranceY + " Exit Y: " + Mazegen.exitY + " Scale: " + Mazegen.scale + " Filename: "
		        + Mazegen.file.getName ( ) );

		if ( ( Mazegen.width > ( screenWidth * 2 ) ) || ( Mazegen.height > ( screenHeight * 2 ) ) ) {
			Mazegen.renderObject = new Renderless ( ( Mazegen.width * 2 ) + 1, ( Mazegen.height * 2 ) + 1,
			        Mazegen.scale );
			Gui.graphicsContext.fillText ( "Maze too big to be displayed", 10, 10 );
		}
		else {
			Mazegen.renderObject = new Renderer ( ( Mazegen.width * 2 ) + 1, ( Mazegen.height * 2 ) + 1,
			        Mazegen.scale );
			this.setBGToGrey ( );
		}

		try {
			Mazegen.mazeImage = new BufferedImage ( ( Mazegen.width * 2 ) + 1, ( Mazegen.height * 2 ) + 1,
			        BufferedImage.TYPE_INT_RGB );

			System.out.println ( "Image Width: " + Mazegen.mazeImage.getWidth ( ) + " Image Height: "
			        + Mazegen.mazeImage.getHeight ( ) );

			Mazegen.mazeImage.setAccelerationPriority ( 1 );

		}
		catch ( final Exception e ) {
			Gui.showError ( "Window is too large!" );
			throw new Exception ( "Window too large" );
		}

	}

	private void addEntranceAndExit ( ) {
		Mazegen.mazeImage.setRGB ( 0, ( Mazegen.entranceY * 2 ) + 1, Mazegen.white );
		Mazegen.mazeImage.setRGB ( Mazegen.width * 2, ( Mazegen.exitY * 2 ) + 1, Mazegen.white );
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

		Mazegen.max = Mazegen.height * ( Mazegen.width + 1 );

		this.loadingScreen ( );

		if ( this.primms ) {

			if ( ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) <= 500 ) {
				Mazegen.procedural = true;
			}

			if ( ( ( Mazegen.width < Mazegen.proceduralThreshold ) && ( Mazegen.height < Mazegen.proceduralThreshold ) )
			        && ! ( Mazegen.procedural ) ) {
				time = System.currentTimeMillis ( );
				System.out.println ( "Populating adjacency matrix" );
				PrimmsUtils.populateAdjMat ( );
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
				this.primmsProcedural ( );
				System.out.println ( "Applied Primms procedual in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
			}

		}
		else {
			System.out.println ( "Applying Kruskals..." );
			final Kruskals Kruskals = new Kruskals ( this.type );
			Kruskals.run ( );
			System.out.println ( "Applied Kruskals in " + ( System.currentTimeMillis ( ) - time ) + "ms" );
		}

		Mazegen.render ( );

		ImageFile.saveImage ( Mazegen.mazeImage, Mazegen.file );

		System.exit ( 0 );
	}

	private void loadingScreen ( ) {
		Gui.graphicsContext.fillText ( "A Really Good Loading Screen!", 1920 / 2, 1050 / 2 );
	}

	private void primmsAdjMat ( ) {
		PrimmsAdjMat.executePrimms ( );
	}

	private void primmsProcedural ( ) {
		PrimmsProcedural.executePrimms ( );
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

			Gui.showError ( "Critical error:\nParameters Width: " + Mazegen.width + " Height: " + Mazegen.height
			        + "\nError Details:\n" + stackTraceMsg );
		}

	}

	private void setBGToGrey ( ) {
		Gui.graphicsContext.setFill ( Color.DARKGREY );
		Gui.graphicsContext.fillRect ( 0, 0, Gui.XMAX, Gui.YMAX );
	}

	private void setImageToBlack ( ) {

		for ( int x = 0; x < ( ( Mazegen.width * 2 ) + 1 ); x ++ ) {

			for ( int y = 0; y < ( ( Mazegen.height * 2 ) + 1 ); y ++ ) {
				Mazegen.mazeImage.setRGB ( x, y, 0 );
			}

		}

	}

}

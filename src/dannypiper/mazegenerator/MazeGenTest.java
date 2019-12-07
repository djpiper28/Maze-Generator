package dannypiper.mazegenerator;

import java.io.File;

public class MazeGenTest extends MazeGen {

	public MazeGenTest ( int widthIn, int heightIn, File imageFile, boolean primms ) throws Exception {
		super ( widthIn, heightIn, 1f, imageFile, 2, 2, true, 0, 0, primms );
	}

	public long runTest ( ) {
		long time = System.currentTimeMillis ( );

		super.run ( );

		return System.currentTimeMillis ( ) - time;
	}

}

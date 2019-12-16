package dannypiper.mazegenerator;

import java.io.File;

public class MazeGenTest extends MazeGen {

	public MazeGenTest ( final int widthIn, final int heightIn, final File imageFile, final boolean primms )
	        throws Exception {
		super ( widthIn, heightIn, 1f, imageFile, 2, 2, true, 0, 0, primms );
	}

	public long runTest ( ) {
		final long time = System.currentTimeMillis ( );

		super.run ( );

		return System.currentTimeMillis ( ) - time;
	}

}

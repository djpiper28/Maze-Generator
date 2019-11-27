package dannypiper.mazegenerator;

public class renderless extends renderer {

	public renderless ( final int width, final int height, final float scale ) {
		super ( width, height, scale ); // Call the super classes constructor
	}

	@Override
	public void run ( ) {
		System.gc ( );
	}

}

package dannypiper.mazegenerator;

public class Validation {

	public static boolean enoughMemory ( final int width, final int height, final float scale ) {
		return ( ( Runtime.getRuntime ( ).freeMemory ( ) - ( width * height * scale * scale * 64 ) ) < 128000 );
	}

	// Methods
	public static boolean validNumericalParameter ( final String parameterStr ) {
		final String validChars = "0123456789";
		final String [ ] parameterStrArray = parameterStr.split ( "" ); // split after each char

		for ( int i = 0; i < parameterStrArray.length; i ++ ) {

			if ( ! validChars.contains ( parameterStrArray [ i ] ) ) {
				return false;
			}

		}

		return true && ( parameterStr.length ( ) < 8 );
	}

	private Validation ( ) {

	}

}

package dannypiper.mazegenerator;

public class validation {

	// Methods
	public static boolean validNumericalParameter ( final String parameterStr ) {
		final String validChars = "0123456789";
		final String [ ] parameterStrArray = parameterStr.split ( "" ); // split after each char

		for ( int i = 0; i < parameterStrArray.length; i ++ ) {

			if ( ! validChars.contains ( parameterStrArray [ i ] ) ) {
				return false;
			}

		}

		return true;
	}
}

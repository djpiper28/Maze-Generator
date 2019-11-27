package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class imageFile {
	public static void saveImage ( final BufferedImage mazeImage, final File file ) {

		try {
			ImageIO.write ( mazeImage, "png", file );
		}
		catch ( final Exception e ) {
			e.printStackTrace ( );
		}

	}
}

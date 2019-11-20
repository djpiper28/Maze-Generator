package dannypiper.mazegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class imageFile {
	public static void saveImage(BufferedImage mazeImage, File file){
		try {
			ImageIO.write(mazeImage, "png", file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

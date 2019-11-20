package dannypiper.mazegenerator;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class renderless extends renderer {

	public renderless(int width, int height, float scale) {
		super(width, height, scale);	//Call the super classes constructor
	}
	
	@Override
	public void run() {
		System.gc();
	}

}

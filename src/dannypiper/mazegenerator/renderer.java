package dannypiper.mazegenerator;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class renderer implements Runnable {

	public BufferedImage renderImage;
	private Graphics graphics;
	private JPanel observer;
	
	public void updateGraphics(Graphics graphics) {
		this.graphics = graphics;
	}
	
	public renderer(int width, int height, Graphics graphics, JPanel observer) {
		this.observer = observer;
		updateGraphics(graphics);
		this.renderImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	@Override
	public void run() {
		graphics.drawImage(this.renderImage, 0, 0, this.observer);
	}

}

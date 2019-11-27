package dannypiper.mazegenerator;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class renderer implements Runnable {

	public int width;
	public int height;
	public float scale;
	protected BufferedImage after;
	protected AffineTransform affineTransform;
	protected AffineTransformOp scaleOp;

	public renderer ( final int width, final int height, final float scale ) {
		this.width = width;
		this.height = height;
		this.scale = scale;

		if ( this.scale != 1 ) {
			this.after = new BufferedImage ( ( int ) Math.ceil ( width * scale ), ( int ) Math.ceil ( height * scale ),
			        BufferedImage.TYPE_INT_RGB );
			this.after.setAccelerationPriority ( 1 );
			this.affineTransform = new AffineTransform ( );
			this.affineTransform.scale ( scale, scale );
			this.scaleOp = new AffineTransformOp ( this.affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
		}

	}

	@Override
	public void run ( ) {
		final long time = System.currentTimeMillis ( );

		if ( this.scale != 1 ) {
			this.after = this.scaleOp.filter ( mazegen.mazeImage, this.after );

			Image image = SwingFXUtils.toFXImage ( this.after, null );

			gui.graphicsContext.drawImage ( image, 0, 0 );

			image = null;
			this.after = null;
		}
		else {
			Image image = SwingFXUtils.toFXImage ( mazegen.mazeImage, null );

			gui.graphicsContext.drawImage ( image, 0, 0 );

			image = null;
		}

		System.out.println ( "Render call - " + ( System.currentTimeMillis ( ) - time ) + "ms" );
		System.gc ( );
	}

}

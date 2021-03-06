package dannypiper.mazegenerator;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Renderer implements Runnable {
    public float scale;
    protected BufferedImage after;
    protected AffineTransform affineTransform;
    protected AffineTransformOp scaleOp;

    public Renderer(final int width, final int height, final float scale) {
        this.scale = scale;

        if (this.scale != 1) {
            this.after = new BufferedImage((int) Math.ceil(width * this.scale), (int) Math.ceil(height * this.scale),
                    BufferedImage.TYPE_INT_RGB);
            this.after.setAccelerationPriority(1);
            this.affineTransform = new AffineTransform();
            this.affineTransform.scale(scale, scale);
            this.scaleOp = new AffineTransformOp(this.affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        }

    }

    public void renderFinishedScreen() {
        Gui.graphicsContext.drawImage(new Image("savingScreenImage.png"), 0, 0);
        System.out.println("Rendered finished message");
    }

    @Override
    public void run() {
        final long time = System.currentTimeMillis();

        if (this.scale != 1) {
            this.after = this.scaleOp.filter(MazeGen.mazeImage, this.after);

            final Image image = SwingFXUtils.toFXImage(this.after, null);

            Gui.graphicsContext.drawImage(image, 0, 0);

        } else {
            final Image image = SwingFXUtils.toFXImage(MazeGen.mazeImage, null);

            Gui.graphicsContext.drawImage(image, 0, 0);
        }

        System.out.println("Render call - " + (System.currentTimeMillis() - time) + "ms");
    }

}

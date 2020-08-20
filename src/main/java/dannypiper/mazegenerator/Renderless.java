package dannypiper.mazegenerator;

public class Renderless extends Renderer {

    public Renderless(final int width, final int height, final float scale) {
        super(width, height, scale); // Call the super classes constructor
    }

    @Override
    public void run() {
        System.gc();
    }

}

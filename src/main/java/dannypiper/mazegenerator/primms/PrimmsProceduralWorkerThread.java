package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.MazeGen;

public class PrimmsProceduralWorkerThread implements Runnable {

    public boolean finished;
    public boolean delete;
    public int columnInput;
    public int row;
    public int column;
    public short minValue;

    public PrimmsProceduralWorkerThread() {
        this.columnInput = -1;
    }

    @Override
    public void run() {
        // this.finished = false;
        this.delete = false;
        this.row = 0;
        this.column = this.columnInput;

        short minValue = MazeGen.maxRand + 1;

        final int x = this.columnInput % MazeGen.width;
        final int y = this.columnInput / MazeGen.width;
        final int Coord = this.columnInput;

        boolean yCurrentXPlusDeleted = true;
        boolean yCurrentXMinusDeleted = true;
        boolean yDownDeleted = true;
        boolean yUpDeleted = true;

        if (x < (MazeGen.width - 1)) {
            yCurrentXPlusDeleted = PrimmsUtils.deletedRows[Coord + 1];
        }

        if (x > 0) {
            yCurrentXMinusDeleted = PrimmsUtils.deletedRows[Coord - 1];
        }

        if (y < (MazeGen.height - 1)) {
            yUpDeleted = PrimmsUtils.deletedRows[Coord + MazeGen.width];
        }

        if (y > 0) {
            yDownDeleted = PrimmsUtils.deletedRows[Coord - MazeGen.width];
        }

        if (!yCurrentXPlusDeleted) {
            final short value = MazeGen.randInt();

            if (value < minValue) {
                minValue = value;
                this.row = Coord + 1;
            }

        }

        if (!yCurrentXMinusDeleted) {
            final short value = MazeGen.randInt();

            if (value < minValue) {
                minValue = value;
                this.row = Coord - 1;
            }

        }

        if (!yUpDeleted) {
            final short value = MazeGen.randInt();

            if (value < minValue) {
                minValue = value;
                this.row = Coord + MazeGen.width;
            }

        }

        if (!yDownDeleted) {
            final short value = MazeGen.randInt();

            if (value < minValue) {
                minValue = value;
                this.row = Coord - MazeGen.width;
            }

        }

        this.delete = yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted;

        this.finished = true;
        this.minValue = minValue;
    }

}

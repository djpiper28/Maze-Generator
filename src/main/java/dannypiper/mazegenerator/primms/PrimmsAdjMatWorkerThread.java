package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.MazeGen;

public class PrimmsAdjMatWorkerThread implements Runnable {

    public boolean finished;
    public boolean delete;
    public int columnInput;
    public int row;
    public int column;
    public short minValue;

    public PrimmsAdjMatWorkerThread() {
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

        boolean yCurrentXPlusDeleted = false;
        boolean yCurrentXMinusDeleted = false;
        boolean yDownDeleted = false;
        boolean yUpDeleted = false;

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

        if (!(yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted)) {

            if ((x < (MazeGen.width - 1)) && !yCurrentXPlusDeleted) {

                if (PrimmsUtils.adjMat[Coord][Coord + 1] < minValue) {
                    minValue = PrimmsUtils.adjMat[Coord][Coord + 1];
                    this.row = Coord + 1;
                }

            }

            if ((x > 0) && !yCurrentXMinusDeleted) {

                if (PrimmsUtils.adjMat[Coord][Coord - 1] < minValue) {
                    minValue = PrimmsUtils.adjMat[Coord][Coord - 1];
                    this.row = Coord - 1;
                }

            }

            if ((y < (MazeGen.height - 1)) && !yUpDeleted) {

                if (PrimmsUtils.adjMat[Coord][Coord + MazeGen.width] < minValue) {
                    minValue = PrimmsUtils.adjMat[Coord][Coord + MazeGen.width];
                    this.row = Coord + MazeGen.width;
                }

            }

            if ((y > 0) && !yDownDeleted) {

                if (PrimmsUtils.adjMat[Coord][Coord - MazeGen.width] < minValue) {
                    minValue = PrimmsUtils.adjMat[Coord][Coord - MazeGen.width];
                    this.row = Coord - MazeGen.width;
                }

            }

        } else {
            this.delete = true;
        }

        this.finished = true;
        this.minValue = minValue;
    }

}

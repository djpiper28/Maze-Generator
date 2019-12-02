package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.Mazegen;

public class PrimmsProceduralWorkerThread implements Runnable {

	public boolean finished;
	public boolean delete;
	public int columnInput;
	public int row;
	public int column;
	public short minValue;

	public PrimmsProceduralWorkerThread ( ) {
		this.columnInput = - 1;
	}

	@Override
	public void run ( ) {
		// this.finished = false;
		this.delete = false;
		this.row = 0;
		this.column = this.columnInput;

		short minValue = Mazegen.maxRand + 1;

		final int x = this.columnInput % Mazegen.width;
		final int y = this.columnInput / Mazegen.width;
		final int Coord = this.columnInput;

		boolean yCurrentXPlusDeleted = true;
		boolean yCurrentXMinusDeleted = true;
		boolean yDownDeleted = true;
		boolean yUpDeleted = true;

		if ( x < ( Mazegen.width - 1 ) ) {
			yCurrentXPlusDeleted = PrimmsUtils.deletedRows [ Coord + 1 ];
		}

		if ( x > 0 ) {
			yCurrentXMinusDeleted = PrimmsUtils.deletedRows [ Coord - 1 ];
		}

		if ( y < ( Mazegen.height - 1 ) ) {
			yUpDeleted = PrimmsUtils.deletedRows [ Coord + Mazegen.width ];
		}

		if ( y > 0 ) {
			yDownDeleted = PrimmsUtils.deletedRows [ Coord - Mazegen.width ];
		}

		if ( ! yCurrentXPlusDeleted ) {
			final short value = Mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord + 1;
			}

		}

		if ( ! yCurrentXMinusDeleted ) {
			final short value = Mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord - 1;
			}

		}

		if ( ! yUpDeleted ) {
			final short value = Mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord + Mazegen.width;
			}

		}

		if ( ! yDownDeleted ) {
			final short value = Mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord - Mazegen.width;
			}

		}

		this.delete = yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted;

		this.finished = true;
		this.minValue = minValue;
	}

}

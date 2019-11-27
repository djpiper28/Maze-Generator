package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.mazegen;

public class primmsProcedualWorkerThread implements Runnable {

	public boolean finished;
	public boolean delete;
	public int columnInput;
	public int row;
	public int column;
	public short minValue;

	public primmsProcedualWorkerThread ( ) {
		this.columnInput = - 1;
	}

	@Override
	public void run ( ) {
		// this.finished = false;
		this.delete = false;
		this.row = 0;
		this.column = this.columnInput;

		short minValue = mazegen.maxRand + 1;

		final int x = this.columnInput % mazegen.width;
		final int y = this.columnInput / mazegen.width;
		final int Coord = this.columnInput;

		boolean yCurrentXPlusDeleted = true;
		boolean yCurrentXMinusDeleted = true;
		boolean yDownDeleted = true;
		boolean yUpDeleted = true;

		if ( x < ( mazegen.width - 1 ) ) {
			yCurrentXPlusDeleted = primmsUtils.deletedRows [ Coord + 1 ];
		}

		if ( x > 0 ) {
			yCurrentXMinusDeleted = primmsUtils.deletedRows [ Coord - 1 ];
		}

		if ( y < ( mazegen.height - 1 ) ) {
			yUpDeleted = primmsUtils.deletedRows [ Coord + mazegen.width ];
		}

		if ( y > 0 ) {
			yDownDeleted = primmsUtils.deletedRows [ Coord - mazegen.width ];
		}

		if ( ! yCurrentXPlusDeleted ) {
			final short value = mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord + 1;
			}

		}

		if ( ! yCurrentXMinusDeleted ) {
			final short value = mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord - 1;
			}

		}

		if ( ! yUpDeleted ) {
			final short value = mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord + mazegen.width;
			}

		}

		if ( ! yDownDeleted ) {
			final short value = mazegen.randInt ( );

			if ( value < minValue ) {
				minValue = value;
				this.row = Coord - mazegen.width;
			}

		}

		this.delete = yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted;

		this.finished = true;
		this.minValue = minValue;
	}

}

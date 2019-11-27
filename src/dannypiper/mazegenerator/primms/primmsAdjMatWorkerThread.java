package dannypiper.mazegenerator.primms;

import dannypiper.mazegenerator.mazegen;

public class primmsAdjMatWorkerThread implements Runnable {

	public boolean finished;
	public boolean delete;
	public int columnInput;
	public int row;
	public int column;
	public short minValue;

	public primmsAdjMatWorkerThread ( ) {
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

		boolean yCurrentXPlusDeleted = false;
		boolean yCurrentXMinusDeleted = false;
		boolean yDownDeleted = false;
		boolean yUpDeleted = false;

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

		if ( ! ( yCurrentXPlusDeleted && yCurrentXMinusDeleted && yUpDeleted && yDownDeleted ) ) {

			if ( ( x < ( mazegen.width - 1 ) ) && ! yCurrentXPlusDeleted ) {

				if ( primmsUtils.adjMat [ Coord ] [ Coord + 1 ] < minValue ) {
					minValue = primmsUtils.adjMat [ Coord ] [ Coord + 1 ];
					this.row = Coord + 1;
				}

			}

			if ( ( x > 0 ) && ! yCurrentXMinusDeleted ) {

				if ( primmsUtils.adjMat [ Coord ] [ Coord - 1 ] < minValue ) {
					minValue = primmsUtils.adjMat [ Coord ] [ Coord - 1 ];
					this.row = Coord - 1;
				}

			}

			if ( ( y < ( mazegen.height - 1 ) ) && ! yUpDeleted ) {

				if ( primmsUtils.adjMat [ Coord ] [ Coord + mazegen.width ] < minValue ) {
					minValue = primmsUtils.adjMat [ Coord ] [ Coord + mazegen.width ];
					this.row = Coord + mazegen.width;
				}

			}

			if ( ( y > 0 ) && ! yDownDeleted ) {

				if ( primmsUtils.adjMat [ Coord ] [ Coord - mazegen.width ] < minValue ) {
					minValue = primmsUtils.adjMat [ Coord ] [ Coord - mazegen.width ];
					this.row = Coord - mazegen.width;
				}

			}

		}
		else {
			this.delete = true;
		}

		this.finished = true;
		this.minValue = minValue;
	}

}

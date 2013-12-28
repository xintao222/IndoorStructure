package indoor.data.structure;

import java.util.ArrayList;

public class Piece {

	public static void main(String[] argArray) {
		System.out.println("This is an attempt to create the structural foundation for 3d navigating system." + "\n" +
				"And thanks you for reading. Idea that inspired this solution is - recursion" + "\n" +
				"Building is a number of floors that contain volumes, while building is a piece of larger complex." + "\n" +
				"This structure have to be useful for graphical rendering, associating user and navigation data while making" + "\n" +
				"picking object and identifying corresponding information not that big pain in the a%!.");

		System.out.println("Boring tests:");

		Piece.testInBetweenZeroAnd();

		Piece.testAddPieceWithRelation();
		Piece.testAddSafelyPieceWithRelation();

		Piece.testAddThenReturnPieceWithRelation();
		Piece.testAddThenReturnSafelyPieceWithRelation();

		Piece.testAddCoord();
		Piece.testAddSafelyCoord();
	}


	public String tag; //complex {street, building {floor {volume}}}

	public int completeWidth, completeDepth, completeHeight; //parallelepiped described by this piece


	public Piece[] pieceArray;

	public Coord[] pieceCoordRelationArray;


	public ArrayList<Coord> pointList; //points for users, qr-marks, navigational waypoints 


	public BindingAtlas bindingAtlas;


	public Piece(String tag, int width, int depth, int height) {
		this.tag = tag;
		completeWidth = width;
		completeDepth = depth;
		completeHeight = height;

		pieceArray = new Piece[0];
		pieceCoordRelationArray = new Coord[0];

		pointList = new ArrayList<Coord>();
		
		bindingAtlas = new BindingAtlas();
	}


	public static boolean inBetweenZeroAnd(int what, int boundary) {
		if (boundary > 0) {
			return (what >= 0 && what < boundary) ? true : false;
		}
		else if (boundary < 0) {
			return (what < 0 && what >= boundary) ? true : false;
		}
		else {
			return false;
		}
	}

	public static void testInBetweenZeroAnd() {
		System.out.println("testInBetweenZeroAnd");

		System.out.println(" 3 in [0, 5) =  " + inBetweenZeroAnd(3, 5));
		System.out.println("33 in [0, 5) =  " + inBetweenZeroAnd(33, 5));
		System.out.println("-3 in [0, 5) =  " + inBetweenZeroAnd(-3, 5));

		System.out.println("-3 in [-5, 0) =  " + inBetweenZeroAnd(-3, -5));
		System.out.println("-8 in [-5, 0) =  " + inBetweenZeroAnd(-8, -5));
		System.out.println(" 3 in [-5, 0) =  " + inBetweenZeroAnd(3, -5));

		System.out.println(" 0 in [0, 0) =  " + inBetweenZeroAnd(0, 0));
	}

	public Piece add(Piece piece, Coord pieceCoordRelation) {
		Piece[] newPieceArray = new Piece[this.pieceArray.length + 1];

		System.arraycopy(this.pieceArray, 0, newPieceArray, 0, this.pieceArray.length);
		newPieceArray[newPieceArray.length - 1] = piece;
		
		this.pieceArray = newPieceArray;


		Coord[] newCoordArray = new Coord[this.pieceCoordRelationArray.length + 1];

		System.arraycopy(this.pieceCoordRelationArray, 0, newCoordArray, 0, this.pieceCoordRelationArray.length);
		newCoordArray[newCoordArray.length - 1] = pieceCoordRelation;

		this.pieceCoordRelationArray = newCoordArray;


		return this;
	}

	public static void testAddPieceWithRelation() {
		System.out.println("testAddPieceWithRelation");


		Piece floorPiece = new Piece("floor", 100, 100, 100);
		Piece volumePiece = new Piece("volume", 10, 10, 10);
		Coord pieceCoordRelation = new Coord(10, 0, 0);


		System.out.println("BEFORE>>"
				+ "  pieceArray.length = " + floorPiece.pieceArray.length
				+ "; pieceCoordRelationArray.length = " + floorPiece.pieceCoordRelationArray.length);

		floorPiece.add(volumePiece, pieceCoordRelation);
		System.out.println("Added piece with relation");

		System.out.println("AFTER >>"
				+ "  pieceArray.length = " + floorPiece.pieceArray.length
				+ "; pieceCoordRelationArray.length = " + floorPiece.pieceCoordRelationArray.length);
	}

	public Piece addSafely(Piece piece, Coord pieceCoordRelation) throws IllegalArgumentException {
		int width = this.completeWidth, depth = this.completeDepth, height = this.completeHeight;

		if (inBetweenZeroAnd(piece.completeWidth, width)
			&& inBetweenZeroAnd(piece.completeDepth, depth)
			&& inBetweenZeroAnd(piece.completeHeight, height)
			&& inBetweenZeroAnd(piece.completeHeight + pieceCoordRelation.x, height)
			&& inBetweenZeroAnd(piece.completeDepth + pieceCoordRelation.y, depth)
			&& inBetweenZeroAnd(piece.completeHeight +  + pieceCoordRelation.z, height)) {
			return add(piece, pieceCoordRelation);
		}
		else {
			throw new IllegalArgumentException("Piece with tag = \"" + piece.tag + "\" doesn't fit in a piece with tag = \"" + this.tag + "\"");
		}
	}

	public static void testAddSafelyPieceWithRelation() {
		System.out.println("testAddSafelyPieceWithRelation");


		Piece floorPiece = new Piece("floor", 100, 100, 100);
		Piece volumePiece = new Piece("volume", 10, 10, 10);
		Coord pieceCoordRelation = new Coord(10, 0, 0);
		Coord pieceCoordRelationHUGE = new Coord(100, 0, 0);


		System.out.println("BEFORE>>"
				+ "  pieceArray.length = " + floorPiece.pieceArray.length
				+ "; pieceCoordRelationArray.length = " + floorPiece.pieceCoordRelationArray.length);

		floorPiece.addSafely(volumePiece, pieceCoordRelation);
		System.out.println("Added piece with relation coordinates.\n" + ToJSON.transform(floorPiece));

		System.out.println("AFTER >>"
				+ "  pieceArray.length = " + floorPiece.pieceArray.length
				+ "; pieceCoordRelationArray.length = " + floorPiece.pieceCoordRelationArray.length);


		try {
			floorPiece.addSafely(volumePiece, pieceCoordRelationHUGE);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Exception message = " + e.getMessage());
		}
		System.out.println("Tried to add piece with HUGE relation coordinates");

		System.out.println("AFTER >>"
				+ "  pieceArray.length = " + floorPiece.pieceArray.length
				+ "; pieceCoordRelationArray.length = " + floorPiece.pieceCoordRelationArray.length);

	}


	public Piece addThenReturn(Piece piece, Coord pieceCoordRelation) {
		this.add(piece, pieceCoordRelation);

		return this.pieceArray[this.pieceArray.length - 1];
	}

	public static void testAddThenReturnPieceWithRelation() {
		System.out.println("testAddThenReturnPieceWithRelation");


		Piece floorPiece = new Piece("floor", 100, 100, 100);
		Piece volumePiece = new Piece("volume", 10, 10, 10);
		Coord pieceCoordRelation = new Coord(10, 10, 10);


		System.out.println("BEFORE>>  mainPiece.toString() = " + floorPiece);

		Piece result = floorPiece.addThenReturn(volumePiece, pieceCoordRelation);
		System.out.println("Added one piece");

		System.out.println("AFTER >>  returnedPiece.toString() = " + result);
	}


	public Piece addThenReturnSafely(Piece piece, Coord pieceCoordRelation) throws IllegalArgumentException {
		this.addSafely(piece, pieceCoordRelation);

		return this.pieceArray[this.pieceArray.length - 1];
	}

	public static void testAddThenReturnSafelyPieceWithRelation() {
		System.out.println("testAddThenReturnSafelyPieceWithRelation");


		Piece floorPiece = new Piece("floor", 100, 100, 100);
		Piece volumePiece = new Piece("volume", 10, 10, 10);
		Coord pieceCoordRelation = new Coord(10, 0, 0);
		Coord pieceCoordRelationHUGE = new Coord(100, 0, 0);

		
		System.out.println("BEFORE>>  mainPiece.toString() = " + floorPiece);

		Piece result = floorPiece.addThenReturnSafely(volumePiece, pieceCoordRelation);
		System.out.println("Added one piece");

		System.out.println("AFTER >>  returnedPiece.toString() = " + result);


		try {
			result = floorPiece.addThenReturnSafely(volumePiece, pieceCoordRelationHUGE);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Exception message = " + e.getMessage());
		}
		System.out.println("Tried to add piece with HUGE relation coordinates");

		System.out.println("AFTER >>  returnedPiece.toString() = " + result);
	}


	public Piece add(Coord point) {
		this.pointList.add(point);

		return this;
	}

	public static void testAddCoord() {
		System.out.println("testAddCoord");


		Piece volumePiece = new Piece("volume", 100, 100, 100);
		Coord point = new Coord(0, 0, 0);


		System.out.println("BEFORE>>  pointList.size = " + volumePiece.pointList.size());

		volumePiece.add(point);
		System.out.println("Added one point");

		System.out.println("AFTER >>  pointList.size = " + volumePiece.pointList.size());
	}


	public Piece addSafely(Coord point)  throws IllegalArgumentException {
		if (inBetweenZeroAnd(point.x, this.completeWidth)
			&& inBetweenZeroAnd(point.y, this.completeDepth)
			&& inBetweenZeroAnd(point.z, this.completeHeight)) {
			return add(point);
		}
		else {
			throw new IllegalArgumentException("Coord{" + point.x + ", " + point.y + ", " + point.z + "} doesn't fit in a piece with tag = \"" + this.tag + "\"");
		}
	}

	public static void testAddSafelyCoord() {
		System.out.println("testAddSafelyCoord");


		Piece volumePiece = new Piece("volume", 100, 100, 100);
		Coord point = new Coord(0, 0, 0);
		Coord pointFAR = new Coord(1000, 0, 0);


		System.out.println("BEFORE>>\n" + ToJSON.transform(volumePiece));

		volumePiece.addSafely(point);
		System.out.println("Added one point");

		System.out.println("AFTER >>\n" + ToJSON.transform(volumePiece));

		try {
			volumePiece.addSafely(pointFAR);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Tried to add really far point");

		System.out.println("AFTER >>  pointList.size = " + volumePiece.pointList.size());
	}

}
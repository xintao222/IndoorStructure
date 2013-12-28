package indoor.data.structure;

import java.util.ArrayList;

public class ToJSON {

	public static void main(String[] argArray) {
		System.out.println("This is an attempt to create the structural foundation for 3d navigating system." + "\n" +
				"And thanks you for reading. Idea that inspired this solution is - recursion" + "\n" +
				"Building is a number of floors that contain volumes, while building is a piece of larger complex." + "\n" +
				"This structure have to be useful for graphical rendering, associating user and navigation data while making" + "\n" +
				"picking object and identifying corresponding information not that big pain in the a%!.");

		System.out.println("Boring tests:");

		ToJSON.testTransformPiece();
	}


	public static void addTab(StringBuilder stringBuilder, int count) {
		for (int i = 0; i < count; i++) {
			stringBuilder.append("\t");
		}
	}

	public static void addField(StringBuilder stringBuilder, int level, String name, String value, String ending) {
		stringBuilder.append("\n");
		addTab(stringBuilder, level+1);
		stringBuilder.append("\"");
		stringBuilder.append(name);
		stringBuilder.append("\"");
		stringBuilder.append(": \"");
		stringBuilder.append(value);
		stringBuilder.append("\"");
		stringBuilder.append(ending);
	}

	public static void addField(StringBuilder stringBuilder, int level, String name,  int value, String ending) {
		stringBuilder.append("\n");
		addTab(stringBuilder, level+1);
		stringBuilder.append("\"");
		stringBuilder.append(name);
		stringBuilder.append("\"");
		stringBuilder.append(": ");
		stringBuilder.append(value);
		stringBuilder.append(ending);
	}

	public static void addObject(StringBuilder stringBuilder, int level, String name,  String value, String ending) {
		stringBuilder.append("\n");
		addTab(stringBuilder, level+1);
		stringBuilder.append("\"");
		stringBuilder.append(name);
		stringBuilder.append("\"");
		stringBuilder.append(": ");
		stringBuilder.append(value);
		stringBuilder.append(ending);
	}

	public static void addCoordArray(StringBuilder stringBuilder, int level, String name,  Coord[] coordArray, String ending) {
		stringBuilder.append("\n");
		addTab(stringBuilder, level+1);
		stringBuilder.append("\"");
		stringBuilder.append(name);
		stringBuilder.append("\"");
		stringBuilder.append(": [");
		for (int i = 0; i < coordArray.length; i++) {
			stringBuilder.append("\n");
			addTab(stringBuilder, level+2);
			Coord coordRelation = coordArray[i];
			stringBuilder.append("[");
			stringBuilder.append(coordRelation.x);
			stringBuilder.append(", ");
			stringBuilder.append(coordRelation.y);
			stringBuilder.append(", ");
			stringBuilder.append(coordRelation.z);
			stringBuilder.append("]");
			if (i != coordArray.length - 1) {
				stringBuilder.append(",");
			}
			else {
				stringBuilder.append("\n");
			}
		}
		if (coordArray.length > 0) {
			addTab(stringBuilder, level+1);
		}
		stringBuilder.append("]");
		stringBuilder.append(ending);
	}

	public static void addCoordList(StringBuilder stringBuilder, int level, String name,  ArrayList<Coord> coordList, String ending) {
		addCoordArray(stringBuilder, level, name, coordList.toArray(new Coord[coordList.size()]), ending);
	}

	public static void addEdgeArray(StringBuilder stringBuilder, int level, String name,  Pair<Integer, Integer>[] pairArray, String ending) {
		stringBuilder.append("\n");
		addTab(stringBuilder, level+1);
		stringBuilder.append("\"");
		stringBuilder.append(name);
		stringBuilder.append("\"");
		stringBuilder.append(": [");
		for (int i = 0; i < pairArray.length; i++) {
			stringBuilder.append("\n");
			addTab(stringBuilder, level+2);
			Pair<Integer, Integer> pair = pairArray[i];
			stringBuilder.append("[");
			stringBuilder.append(pair.head);
			stringBuilder.append(", ");
			stringBuilder.append(pair.tail);
			stringBuilder.append("]");
			if (i != pairArray.length - 1) {
				stringBuilder.append(",");
			}
			else {
				stringBuilder.append("\n");
			}
		}
		if (pairArray.length > 0) {
			addTab(stringBuilder, level+1);
		}
		stringBuilder.append("]");
		stringBuilder.append(ending);
	}

	public static void addEdgeList(StringBuilder stringBuilder, int level, String name,  ArrayList<Pair<Integer, Integer>> pairList, String ending) {
		addEdgeArray(stringBuilder, level, name, pairList.toArray(new Pair[pairList.size()]), ending);
	}

	public static String transform(BindingAtlas bindingAtlas, int level) {
		StringBuilder resultBuilder = new StringBuilder();


		resultBuilder.append("{");


		addCoordList(resultBuilder, level, "pointList", bindingAtlas.pointList, ",\n");

		addEdgeList(resultBuilder, level, "edgeList", bindingAtlas.edgeList, ",\n");

		addTab(resultBuilder, level);
		resultBuilder.append("}");


		return resultBuilder.toString();
	}

	public static String transform(BindingAtlas bindingAtlas) {
		return transform(bindingAtlas, 0);
	}

	public static String transform(Piece piece, int level) {
		StringBuilder resultBuilder = new StringBuilder();

		addTab(resultBuilder, level);
		resultBuilder.append("{");


		addField(resultBuilder, level, "tag", piece.tag, ",");
		addField(resultBuilder, level, "completeWidth", piece.completeWidth, ",");
		addField(resultBuilder, level, "completeDepth", piece.completeDepth, ",");
		addField(resultBuilder, level, "completeHeight", piece.completeHeight, ",");


		resultBuilder.append("\n\n");
		addTab(resultBuilder, level+1);
		resultBuilder.append("\"pieceArray\": [");
		for (int i = 0; i < piece.pieceArray.length; i++) {
			resultBuilder.append("\n");
			resultBuilder.append(transform(piece.pieceArray[i], level+2));

			if (i != piece.pieceArray.length - 1) {
				resultBuilder.append(",");
			}
			else {
				resultBuilder.append("\n");
			}
		}
		if (piece.pieceArray.length > 0) {
			addTab(resultBuilder, level+1);
		}
		resultBuilder.append("],\n");


		addCoordArray(resultBuilder, level, "pieceCoordRelationArray", piece.pieceCoordRelationArray, ",\n");

		addCoordList(resultBuilder, level, "pointList", piece.pointList, ",\n");


		addObject(resultBuilder, level, "bindingAtlas", ToJSON.transform(piece.bindingAtlas, level+1),"\n");


		addTab(resultBuilder, level);
		resultBuilder.append("}");


		return resultBuilder.toString();
	}

	public static String transform(Piece piece) {
		return transform(piece, 0);
	}

	public static void testTransformPiece() {
		System.out.println("testTransformPiece");

		Piece buildingPiece = new Piece("building", 100, 100, 100);
		Piece floorPiece = new Piece("floor", 100, 100, 100);
		Coord floorPieceRelationCoord = new Coord(30, 0, 0);
		Piece volumePiece = new Piece("volume", 20, 10, 10);
		Coord volumePieceRelationCoord = new Coord(10, 0, 0);
		Coord point = new Coord(5, 5, 5);


		buildingPiece.addThenReturn(floorPiece, floorPieceRelationCoord)
					 .addThenReturn(volumePiece, volumePieceRelationCoord)
					 .add(point)
					 .add(new Coord(12,12,12));
		
		buildingPiece.bindingAtlas.pointList.add(new Coord(5,5,5));
		buildingPiece.bindingAtlas.pointList.add(new Coord(15,15,15));
		buildingPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(1,1));
		buildingPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(1,1));


		System.out.println("Search result = " + transform(buildingPiece));
	}

}
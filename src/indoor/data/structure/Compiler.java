package indoor.data.structure;

import java.util.ArrayList;

public class Compiler {

	public static void main(String[] args) {
		System.out.println("Class responsoble for organizing BindingAtlas elements in a Piece-tree");

		System.out.println("Boring tests:");

		Compiler.testOrganize();
//		Compiler.testGetPointList();
	}


	public static int getCoordIndex(ArrayList<Coord> coordList, Coord coord) {
		int result = -1;

		for (int i = 0; i < coordList.size(); i++) {
			if (coordList.get(i).equals(coord)) {
				result = i;
				break;
			}
		}
		
		return result;
	}

	/**
	 * CAUTION! MODIFIES STATE! 
	 */
	public static void filterByEdgeDuplication(ArrayList<Pair<Integer, Integer>> edgeList) {
		if (edgeList.size() <= 1) return;

		for (int i = 0; i < edgeList.size() - 1; i++) {
			Pair<Integer, Integer> searchedEdge = edgeList.get(i);

			for (int j = edgeList.size() - 1; j > i; j--) {
				Pair<Integer, Integer>  testedEdge = edgeList.get(j);

				if (testedEdge.head.intValue() == searchedEdge.head.intValue()
						&& testedEdge.tail.intValue() == searchedEdge.tail.intValue()) {
					edgeList.remove(j);
				}
				else if (testedEdge.head.intValue() == searchedEdge.tail.intValue()
						&& testedEdge.tail.intValue() == searchedEdge.head.intValue()) {
					edgeList.remove(j);
				}
			}
		}
	}

	/**
	 * CAUTION! MODIFIES STATE! Removes duplicating elements from the end to beginning.
	 */
	public static void filterByPointDuplication(ArrayList<Coord> coordList) {
		if (coordList.size() <= 1) return;

		for (int i = 0; i < coordList.size() - 1; i++) {
			Coord searchedCoord = coordList.get(i);
			
			for (int j = coordList.size() - 1; j > i; j--) {
				Coord  testedCoord = coordList.get(j);

				if (testedCoord.equals(searchedCoord)) {
					coordList.remove(j);
				}
			}
		}
	}

	/**
	 * CAUTION! MODIFIES STATE! 
	 */
	public static void filterOrImproveByPointValues(ArrayList<Pair<Integer, Integer>> edgeList, ArrayList<Coord> oldPointList, ArrayList<Coord> newPointList) {
		System.out.println(" !!! - " + edgeList.size());
		for (int i = edgeList.size() - 1; i >= 0; i--) {
			Pair<Integer, Integer> edge = edgeList.get(i);
			
			int oldStart = edge.head;
			int oldEnd = edge.tail;

			Coord start = oldPointList.get(oldStart);
			Coord end = oldPointList.get(oldEnd);

			int newStart = getCoordIndex(newPointList, start);
			int newEnd = getCoordIndex(newPointList, end);

			if (newStart == -1 || newEnd == -1) {
				edgeList.remove(i);
			}
			else if (oldStart != newStart || oldEnd != newEnd) {
				edgeList.set(i, new Pair<Integer, Integer>(newStart, newEnd));
			}
		}
	}

	/**
	 * CAUTION! MODIFIES STATE!
	 */
	public static void addCoord(ArrayList<Coord> coordList, Coord coord) {
		for (int i = 0; i < coordList.size(); i++) {
			int x = coordList.get(i).x + coord.x;
			int y = coordList.get(i).y + coord.y;
			int z = coordList.get(i).z + coord.z;

			coordList.set(i, new Coord(x, y, z));
		}
	}

	/**
	 * CAUTION! MODIFIES STATE!
	 * 
	 * @param piece hierarchical representation of 3d-map
	 * @param relationCoord coordinates that will be used to calculate result pointList 
	 * @return special pair of data for BindingAtlas
	 */
	public static Pair<ArrayList<Coord>, ArrayList<Pair<Integer, Integer>>> organize(Piece piece, Coord relationCoord) {
		Pair<ArrayList<Coord>, ArrayList<Pair<Integer, Integer>>> result = 
				new Pair<ArrayList<Coord>, ArrayList<Pair<Integer, Integer>>>(new ArrayList<Coord>(), new ArrayList<Pair<Integer, Integer>>());


		for (int i = 0; i < piece.pieceArray.length; i++) {
			Pair<ArrayList<Coord>, ArrayList<Pair<Integer, Integer>>> innerResult =
					organize(piece.pieceArray[i], piece.pieceCoordRelationArray[i]);

			ArrayList<Pair<Integer, Integer>> currentEdgeList = new ArrayList<Pair<Integer, Integer>>();

			for (int j = 0; j < innerResult.tail.size(); j++) {
				int head = innerResult.tail.get(j).head + result.head.size();
				int tail = innerResult.tail.get(j).tail  + result.head.size();

				currentEdgeList.add(new Pair<Integer, Integer>(head, tail));
			}
						result.head.addAll(innerResult.head);
			result.tail.addAll(currentEdgeList);
		}


		result.head.addAll(piece.pointList);
		filterByPointDuplication(result.head);

		//MODIFYING STATE START!!!
		filterOrImproveByPointValues(piece.bindingAtlas.edgeList, piece.bindingAtlas.pointList, result.head);
		//MODIFYING STATE END!!!
		result.tail.addAll(piece.bindingAtlas.edgeList);
		filterByEdgeDuplication(result.tail);


		//MODIFYING STATE START!!!
		piece.bindingAtlas.pointList.clear();
		piece.bindingAtlas.edgeList.clear();

		piece.bindingAtlas.pointList.addAll(result.head);
		piece.bindingAtlas.edgeList.addAll(result.tail);
		//MODIFYING STATE END!!!

		addCoord(result.head, relationCoord);


		return result;
	}

	public static void organize(Piece piece) {
		organize(piece, new Coord(0, 0, 0));
	}

	public static void testOrganize() {
		System.out.println("testOrganize");

		Piece buildingPiece = new Piece("building", 10000, 10000, 10000);
		Piece floorPiece = new Piece("floor", 1000, 1000, 1000);
		Piece volumePiece1 = new Piece("volume1", 100, 100, 100);
		Piece volumePiece2 = new Piece("volume2", 100, 100, 100);
		Coord point1 = new Coord(0, 0, 0);
		Coord point2 = new Coord(5, 0, 0);


		buildingPiece.addThenReturn(floorPiece, new Coord(1000,0,0))
					 .add(volumePiece1, new Coord(100,0,0))
					 .add(volumePiece2, new Coord(200,0,0));
		buildingPiece.pointList.add(new Coord(1105,0,0));
		buildingPiece.pointList.add(new Coord(1300,0,0));
		buildingPiece.bindingAtlas.pointList.add(new Coord(1105,0,0));
		buildingPiece.bindingAtlas.pointList.add(new Coord(1300,0,0));
		buildingPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0, 1));

		floorPiece.bindingAtlas.pointList.add(new Coord(100,0,0));
		floorPiece.bindingAtlas.pointList.add(new Coord(105,0,0));
		floorPiece.bindingAtlas.pointList.add(new Coord(200,0,0));
		floorPiece.bindingAtlas.pointList.add(new Coord(205,0,0));
		floorPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(1,2));
		floorPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0,1));

		volumePiece1.add(point1)
					.add(point2);
		volumePiece1.bindingAtlas.pointList.add(point1);
		volumePiece1.bindingAtlas.pointList.add(point2);
		volumePiece1.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0,1));

		volumePiece2.add(point1)
					.add(point2);
		volumePiece2.bindingAtlas.pointList.add(point1);
		volumePiece2.bindingAtlas.pointList.add(point2);
		volumePiece2.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0,1));


		System.out.println("input = " + ToJSON.transform(buildingPiece));
		organize(buildingPiece);
		System.out.println("result = " + ToJSON.transform(buildingPiece));
	}

	public static ArrayList<Coord> getPointList(Piece piece, Coord relationCoord) {
		ArrayList<Coord> result = new ArrayList<Coord>();

		for (int i = 0; i < piece.pieceArray.length; i++) {
			result.addAll(getPointList(piece.pieceArray[i], piece.pieceCoordRelationArray[i]));
		}

		for (int i = 0; i < piece.pointList.size(); i++) {
			int x = piece.pointList.get(i).x + relationCoord.x;
			int y = piece.pointList.get(i).y + relationCoord.y;
			int z = piece.pointList.get(i).z + relationCoord.z;

			result.add(new Coord(x, y, z));
		}

		return result;
	}


	public static ArrayList<Coord> getPointList(Piece piece) {
		return getPointList(piece, new Coord(0, 0, 0));
	}

	public static void testGetPointList() {
		System.out.println("testGetPointList");


		Piece floorPiece = new Piece("floor", 1000, 1000, 1000);
		Piece volumePiece1 = new Piece("volume1", 100, 100, 100);
		Piece volumePiece2 = new Piece("volume2", 100, 100, 100);
		Coord point = new Coord(0, 0, 0);


		floorPiece.add(volumePiece1, new Coord(0,0,0))
				  .add(volumePiece2, new Coord(100,0,0));

		floorPiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(1,2));

		volumePiece1.add(point)
					.add(new Coord(5,0,0));
		volumePiece1.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0,1));

		volumePiece2.add(point)
					.add(new Coord(5,0,0));
		volumePiece2.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(0,1));


		System.out.println("input = " + ToJSON.transform(floorPiece));

		StringBuilder result = new StringBuilder();
		ToJSON.addCoordList(result, 0, "pointList", getPointList(floorPiece), "");
		System.out.println("result = " + result);
	}

}
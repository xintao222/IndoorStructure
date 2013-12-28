package indoor.data.structure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FromJSON {

	public static void main(String[] args) {
		System.out.println("Just for getting damn objects write.");

		System.out.println("Boring tests:");

		FromJSON.testTransformPiece();
	}


	public static int getInt(Object object) {
		return ((Long) object).intValue();
	}

	public static Piece transformPiece(JSONObject jsonObject) {
		String tag = (String) jsonObject.get("tag");
		int completeWidth = getInt(jsonObject.get("completeWidth"));
		int completeDepth = getInt(jsonObject.get("completeDepth"));
		int completeHeight = getInt(jsonObject.get("completeHeight"));

		Piece result = new Piece(tag, completeWidth, completeDepth, completeHeight);

		JSONArray pieceJSONArray = (JSONArray) jsonObject.get("pieceArray");
		JSONArray pieceCoordRelationArray = (JSONArray) jsonObject.get("pieceCoordRelationArray");

		for (int i = 0; i < pieceJSONArray.size(); i++) {
			JSONArray coordJSONArray = (JSONArray) pieceCoordRelationArray.get(i);
			int x = getInt(coordJSONArray.get(0));
			int y = getInt(coordJSONArray.get(1));
			int z = getInt(coordJSONArray.get(2));
			result.add(transformPiece((JSONObject) pieceJSONArray.get(i)), new Coord(x, y, z));
		}


		JSONArray pointListJSONArray = (JSONArray) jsonObject.get("pointList");

		for (int i = 0; i < pointListJSONArray.size(); i++) {
			JSONArray coordJSONArray = (JSONArray) pointListJSONArray.get(i);
			int x = getInt(coordJSONArray.get(0));
			int y = getInt(coordJSONArray.get(1));
			int z = getInt(coordJSONArray.get(2));
			result.pointList.add(new Coord(x, y, z));
		}


		JSONObject bindingAtlasJSONObject = (JSONObject) jsonObject.get("bindingAtlas");
		pointListJSONArray = (JSONArray) bindingAtlasJSONObject.get("pointList");

		for (int i = 0; i < pointListJSONArray.size(); i++) {
			JSONArray coordJSONArray = (JSONArray) pointListJSONArray.get(i);
			int x = getInt(coordJSONArray.get(0));
			int y = getInt(coordJSONArray.get(1));
			int z = getInt(coordJSONArray.get(2));
			result.bindingAtlas.pointList.add(new Coord(x, y, z));
		}


		JSONArray edgeListArray = (JSONArray) bindingAtlasJSONObject.get("edgeList");

		for (int i = 0; i < edgeListArray.size(); i++) {
			JSONArray coordJSONArray = (JSONArray) edgeListArray.get(i);
			int head = getInt(coordJSONArray.get(0));
			int tail = getInt(coordJSONArray.get(1));
			result.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(head, tail));
		}

		return result;
	}

	public static Piece transformPiece(String pieceJSON) {
		Piece result = null;

		try {
			JSONParser parser = new JSONParser();
			
			JSONObject jsonObject = (JSONObject) parser.parse(pieceJSON);
			
			result = transformPiece(jsonObject);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
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
					 .bindingAtlas.pointList.add(new Coord(0,0,0));
		volumePiece.bindingAtlas.edgeList.add(new Pair<Integer, Integer>(1, 1));

		String input = ToJSON.transform(buildingPiece);
		System.out.println("Input = \n" + input);
		String result = ToJSON.transform(transformPiece(ToJSON.transform(buildingPiece)));
		System.out.println("Result = \n" + result);
		System.out.println("Equality = " + input.equals(result));
	}

}
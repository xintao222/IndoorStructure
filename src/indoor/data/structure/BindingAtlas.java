package indoor.data.structure;

import java.util.ArrayList;

public class BindingAtlas {

	public static void main(String[] args) {
		System.out.println("Class that should be responsible for connections between points and data");
	}


	ArrayList<Coord> pointList;

	ArrayList<Pair<Integer, Integer>> edgeList;

	ArrayList<Pair<Integer, Integer>> dataBindingList;


	public BindingAtlas() {
		pointList = new ArrayList<Coord>();
		edgeList = new ArrayList<Pair<Integer, Integer>>();
		dataBindingList = new ArrayList<Pair<Integer, Integer>>();
	}

}
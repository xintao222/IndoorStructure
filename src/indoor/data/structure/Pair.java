package indoor.data.structure;

public class Pair<K, V> {

	public static void main(String[] argArray) {
		System.out.println("Pair of two objects");
	}


	public K head;
	public V tail;


	public Pair(K head, V tail) {
		this.head = head;
		this.tail = tail;
	}

}
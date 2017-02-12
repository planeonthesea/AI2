import java.util.ArrayList;

public class Node<T> {
	private T data;
	private ArrayList<Node<T>> children;

	public Node(T data) {
		this.data = data;
		this.children = new ArrayList<Node<T>>();
	}

	public ArrayList<Node<T>> getChildren() {
		return this.children;
	}

	public void setChildren(ArrayList<T> list) {
		// A deep copy of the list
	}

	public T getData() {
		return this.data;
	}
}

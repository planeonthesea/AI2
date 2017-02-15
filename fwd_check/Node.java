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

	public void addChildren(ArrayList<T> list) {
		// A deep copy of the list
		for (int i = 0; i < list.size(); i++) {
			this.children.add(new Node<T>(list.get(i)));
		}
	}

	public void setChildrenList(ArrayList<Node<T>> list) {
		// Deep copy
		this.children = new ArrayList<Node<T>>();
		for (Node<T> node : list) {
			this.children.add(new Node<T>(node.getData()));
		}
	}

	public T getData() {
		return this.data;
	}
}

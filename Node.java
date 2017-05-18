
public class Node {
	
	private int Value;
	private Node LeftNode;
	private Node RightNode;
	
	public Node(int val) {
		this.Value = val;
		this.LeftNode = null;
		this.RightNode = null;
	}
	
	public int getValue() {
		return this.Value;
	}
	
	public Node getLeft() {
		return this.LeftNode;
	}
	
	public Node getRight() {
		return this.RightNode;
	}
	
	public void linkNode (Node newNode) {
		if(newNode.getValue() <= this.Value) {
			if(this.LeftNode == null) {
				this.LeftNode = newNode;
			} else {
				this.LeftNode.linkNode(newNode);
			}
		} else {
			if(this.RightNode == null) {
				this.RightNode = newNode;
			} else {
				this.RightNode.linkNode(newNode);
			}
		}
	}

}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

public class BTree {
	private ArrayList<Integer> Values;
	private HashMap<Integer, Node> Map;
	private int NumValues;
	private int PageLength;
	public Node HeadNode;
	
	public BTree(int val, JSONObject rid, int pageLength) {
		this.Values = new ArrayList<Integer>();
		this.Map = new HashMap<Integer, Node>();
		this.HeadNode = new Node(rid);
		this.Values.add(val);
		this.Map.put(val, this.HeadNode);
		this.NumValues = 1;
		this.PageLength = pageLength;
	}
	
	public void addNode(int val, JSONObject rid) throws BTreeException {
		if(this.NumValues == this.PageLength) {
			throw new BTreeException("BTree is already full!");
		}
		if(this.Values.contains(val)) {
			Map.get(val).addNewData(rid);
		} else {
			Node tmp = new Node(rid);
			this.HeadNode.linkNode(tmp);
			this.Values.add(val);
			this.Map.put(val, tmp);
			this.NumValues++;
		}
	}
	
	public void deleteNode(int val) throws BTreeException {
		if(this.Values.contains(val)) {
			this.Values.remove(val);
			this.Map.remove(val, this.Map.get(val));
			this.NumValues--;
			this.balanceTree();
		} else {
			throw new BTreeException("Value does not exist!");
		}
	}
	
	public void printTree(Node node) {
		System.out.print(node.getIndex()/* + ":" + node.toString() */);
		if(node.getLeft() != null) {
			System.out.print(", ");
			this.printTree(node.getLeft());
		}
		if(node.getRight() != null) {
			System.out.print(", ");
			this.printTree(node.getRight());
		}
	}
	
	//Sorts the ArraList<Integer> Values by bubblesort
	private void sortVals() {
		for(int i=1; i<this.Values.size(); i++) {
			for(int j=0; j<this.Values.size() - i; j++) {
				if(this.Values.get(j) > this.Values.get(j+1)) {
					int tmp = this.Values.get(j+1);
					this.Values.set(j+1, this.Values.get(j));
					this.Values.set(j, tmp);
				}
			}
		}
	}
	
	public void balanceTree() {
		this.sortVals();
		int mid = this.NumValues/2;
		int index = this.Values.get(mid);
		Node node = this.Map.get(index);
		node.setLeft(null);
		node.setRight(null);
		this.HeadNode = node;
		List<Integer> leftHalf = this.Values.subList(0, mid);
		this.recurBuildTree(leftHalf);
		if(mid != this.NumValues - 1) {
			List<Integer> rightHalf = this.Values.subList(mid + 1, this.NumValues);
			this.recurBuildTree(rightHalf);
		}
	}
	
	private void recurBuildTree(List<Integer> array) {
		int size = array.size();
		if(size == 1) {
			int index = array.get(0);
			Node node = this.Map.get(index);
			node.setLeft(null);
			node.setRight(null);
			this.HeadNode.linkNode(node);
		} else {
			int mid = size/2;
			int index = array.get(mid);
			Node node = this.Map.get(index);
			node.setLeft(null);
			node.setRight(null);
			this.HeadNode.linkNode(node);
			List<Integer> leftArray = array.subList(0, mid);
			this.recurBuildTree(leftArray);
			if(mid != size - 1) {
				List<Integer> rightArray = array.subList(mid + 1, size);
				this.recurBuildTree(rightArray);
			}
		}
	}
	
	public boolean doesIndexExist(int val, Node subject) {
		if(subject.getIndex() == val) {
			return true;
		} else {
			if(val < subject.getIndex()) {
				if(subject.getLeft() != null) {
					return doesIndexExist(val, subject.getLeft());
				}
				else return false;
			} else {
				if(subject.getRight() != null) {
					return doesIndexExist(val, subject.getRight());
				}
				else return false;
			}
		}
	}
}

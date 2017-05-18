import java.util.ArrayList;
import java.util.List;

public class BTree {
	private ArrayList<Integer> Values;
	private int NumValues;
	public Node HeadNode;
	
	public BTree(int val) {
		this.Values = new ArrayList<Integer>();
		this.HeadNode = new Node(val);
		Values.add(val);
		NumValues = 1;
	}
	
	public void addNode(int val) {
		this.HeadNode.linkNode(new Node(val));
		this.Values.add(val);
		NumValues++;
		this.balanceTree();
	}
	
	public void deleteNode(int val) throws NonExistantValueException {
		if(this.Values.contains(val)) {
			this.Values.remove(this.Values.indexOf(val));
			this.NumValues--;
			this.balanceTree();
		} else {
			throw new NonExistantValueException();
		}
	}
	
	//Returns an ArrayList of all values in the BTree
	public ArrayList<Integer> getVals() {
		this.sortVals();
		return this.Values;		
	}
	
	public void printTree(Node node) {
		System.out.print(node.getValue());
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
		this.HeadNode = new Node(this.Values.get(mid));
		List<Integer> leftHalf = this.Values.subList(0, mid);
		this.recurBuildTree(leftHalf);
		if(mid != this.NumValues - 1) {
			List<Integer> rightHalf = this.Values.subList(mid + 1, this.NumValues);
			this.recurBuildTree(rightHalf);
		}
		this.printTree(this.HeadNode);
		System.out.println();
	}
	
	private void recurBuildTree(List<Integer> array) {
		int size = array.size();
		if(size == 1) {
			this.HeadNode.linkNode(new Node(array.get(0)));
		} else {
			int mid = size/2;
			this.HeadNode.linkNode(new Node(array.get(mid)));
			List<Integer> leftArray = array.subList(0, mid);
			this.recurBuildTree(leftArray);
			if(mid != size - 1) {
				List<Integer> rightArray = array.subList(mid + 1, size);
				this.recurBuildTree(rightArray);
			}
		}
	}
	
	public boolean doesValueExist(int val, Node subject) {
		if(subject.getValue() == val) {
			return true;
		} else {
			if(val < subject.getValue()) {
				if(subject.getLeft() != null) {
					return doesValueExist(val, subject.getLeft());
				}
				else return false;
			} else {
				if(subject.getRight() != null) {
					return doesValueExist(val, subject.getRight());
				}
				else return false;
			}
		}
	}
}

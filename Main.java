public abstract class Main {

	public static void main(String[] args) throws NonExistantValueException {
		BTree tree = new BTree(20);
		tree.addNode(25);
		tree.addNode(18);
		tree.addNode(19);
		tree.addNode(17);
		tree.addNode(27);
		tree.addNode(29);
		tree.addNode(19);
		tree.addNode(26);
		tree.addNode(28);
		tree.deleteNode(19);
		tree.deleteNode(34);
	}

}

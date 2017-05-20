import org.json.JSONObject;

public class BTree {
	private int PageLength;
	private int numVals;
	public Node HeadNode;
	
	public BTree(int size) {
		this.HeadNode = null;
		this.PageLength = size;
		this.numVals = 0;
	}
	
	/*returns whether or not the tree is empty*/
	public boolean isEmpty() {
		return this.HeadNode == null;
	}
	
	/*Empties the tree*/
	public void clear() {
		this.HeadNode = null;
	}
	
	/*Insert new node to tree*/
	public void insert(JSONObject rid) throws BTreeException {
		int val = rid.getInt("Hourly_Counts");
		this.HeadNode = insert(val, rid, this.HeadNode);
	}
	
	/*get the height of a given node*/
	private int height(Node node) {
		return node == null ? -1 : node.Height;
	}
	
	/*Returns the largest of the two given values*/
	private int max(int lhs, int rhs) {
		return lhs > rhs ? lhs : rhs;
	}
	
	/*Recursively add node to correct place in tree*/
	 private Node insert(int val, JSONObject rid, Node node) throws BTreeException
     {
         if (node == null) {
        	 if(this.numVals == this.PageLength) {
     			throw new BTreeException("BTree is already full!");
     		} else {
     			node = new Node(rid);
     			this.numVals++;
     		}
         } else if (val < node.Index) {
             node.LeftNode = insert( val, rid, node.LeftNode );
             //If left branch height is more than 2 nodes longer than right branch height
             //Balance the tree
             if (height( node.LeftNode ) - height( node.RightNode ) == 2) {
                 if (val < node.LeftNode.Index) {
                     node = rotateWithLeftChild( node );
                 } else {
                     node = doubleWithLeftChild( node );
                 }
             }
         } else if (val > node.Index) {
             node.RightNode = insert( val, rid, node.RightNode );
             //If right branch height is more than 2 nodes longer than left branch height
             //Balance the tree
             if (height( node.RightNode ) - height( node.LeftNode ) == 2) {
                 if (val > node.RightNode.Index) {
                     node = rotateWithRightChild( node );
                 } else {
                     node = doubleWithRightChild( node );
                 }
             }
         } else if(val == node.Index) {
        	 node.addNewData(rid);
         }
         node.Height = max( height( node.LeftNode ), height( node.RightNode ) ) + 1;
         return node;
     }
	
	/*Swap node with left child*/
	private Node rotateWithLeftChild(Node k2) {
		Node k1 = k2.LeftNode;
		k2.LeftNode = k1.RightNode;
		k1.RightNode = k2;
		k2.Height = max(height(k2.LeftNode), height(k2.RightNode)) + 1;
		k1.Height = max(height(k1.LeftNode), k2.Height) + 1;
		return k1;
	}
	
	/*Swap node with right child*/
	private Node rotateWithRightChild(Node k1) {
		Node k2 = k1.RightNode;
		k1.RightNode = k2.LeftNode;
		k2.LeftNode = k1;
		k1.Height = max(height(k1.LeftNode), height(k1.RightNode)) + 1;
		k2.Height = max(height(k2.RightNode), k1.Height) + 1;
		return k2;
	}
	
	/* Double rotate binary tree node; first left child
	 * with its right child; then node with new left child */
	private Node doubleWithLeftChild(Node k3) {
		k3.LeftNode = rotateWithRightChild(k3.LeftNode);
		return rotateWithLeftChild(k3);
	}
	
	/* Double rotate binary tree node; first right child
	 * with its left child; then node with new right child */
	private Node doubleWithRightChild(Node k1) {
		k1.RightNode = rotateWithLeftChild(k1.RightNode);
		return rotateWithRightChild(k1);
	}
	
	/*Returns the number of nodes in the tree*/
	public int countNodes() {
		return countNodes(this.HeadNode);
	}
	
	private int countNodes(Node node) {
		if(node == null) {
			return 0;
		} else {
			int l = 1;
			l += countNodes(node.LeftNode);
			l += countNodes(node.RightNode);
			return l;
		}
	}
	
	/*Searches tree for a node with the given index*/
	public boolean search(int target) {
		return search(this.HeadNode, target);
	}
	
	private boolean search(Node node, int target) {
		boolean found = false;
		while((node != null) && !found) {
			int nodeVal = node.Index;
			if(target < nodeVal) {
				node = node.LeftNode;
			} else if(target > nodeVal) {
				node = node.RightNode;
			} else {
				found = true;
				break;
			}
			found = search(node, target);
		}
		return found;
	}
	
	 /* Function for in order traversal */
    public void inOrder()
    {
        inOrder(this.HeadNode);
    }
    
    private void inOrder(Node node)
    {
        if (node != null)
        {
            inOrder(node.LeftNode);
            System.out.print(node.Index + " ");
            inOrder(node.RightNode);
        }
    }
    /* Function for pre-order traversal */
    public void preOrder()
    {
        preOrder(this.HeadNode);
    }
    
    private void preOrder(Node node)
    {
    	if (node != null)
        {
    		System.out.print(node.Index + " ");
            preOrder(node.LeftNode);
            preOrder(node.RightNode);
        }
    }
    /* Function for post-order traversal */
    public void postOrder()
    {
        postOrder(this.HeadNode);
    }
    
    private void postOrder(Node node)
    {
    	if (node != null)
        {
            postOrder(node.LeftNode);
            postOrder(node.RightNode);
            System.out.print(node.Index + " ");
        }
    }
    
    /*Converts the tree to a csv string for saving*/
    public String toFileString() {
    	return toFileString(this.HeadNode);
    }
    
    private String toFileString(Node node) {
		String csv = "";
		csv += node.Index + ",";
		if(node.LeftNode != null) {
			csv += this.toFileString(node.LeftNode);
		}
		if(node.RightNode != null) {
			csv += this.toFileString(node.RightNode);
		}
		return csv;
	}
    
    /*Returns int of smallest index*/
    public int getMin() {
    	Node node = this.HeadNode;
    	while(node.LeftNode != null) {
			node = node.LeftNode;
		}
		return node.Index;
    }
    
    /*Returns int of largest index*/
    public int getMax() {
    	Node node = this.HeadNode;
    	while(node.RightNode != null) {
			node = node.RightNode;
		}
		return node.Index;
    }
}

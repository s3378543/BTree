import java.util.ArrayList;

import org.json.JSONObject;

public class BTree {
	public Node HeadNode;
	
	public BTree() {
		this.HeadNode = null;
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
	public void insert(int val, ArrayList<JSONObject> rids) throws BTreeException {
		this.HeadNode = insert(val, rids, this.HeadNode);
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
	 private Node insert(int val, ArrayList<JSONObject> rids, Node node) throws BTreeException
     {
         if (node == null) {
 			node = new Node(val, rids);
         } else if (val < node.Index) {
             node.LeftNode = insert( val, rids, node.LeftNode );
         } else if (val > node.Index) {
             node.RightNode = insert( val, rids, node.RightNode );
         }
         node.Height = max( height( node.LeftNode ), height( node.RightNode ) ) + 1;
         return node;
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
	
	/*Returns the ArrayList<JSONObject> rids of given target index*/
	public ArrayList<JSONObject> getRids(int target) {
		if(search(target)) {
			return getRids(this.HeadNode, target);
		} else {
			return null;
		}
	}
	
	private ArrayList<JSONObject> getRids(Node node, int target) {
		ArrayList<JSONObject> rids = null;
		while((node != null) && (rids == null)) {
			int nodeVal = node.Index;
			if(target < nodeVal) {
				node = node.LeftNode;
			} else if(target > nodeVal) {
				node = node.RightNode;
			} else {
				rids = node.getRids();
				break;
			}
			rids = getRids(node, target);
		}
		return rids;
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
		csv = csv.substring(0, csv.length() - 1);
		csv += "\n";
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
    
    /*Returns index number of node with the highest number or rids attached*/
    public int getLargestNode() {
    	return getLargestNode(this.HeadNode, this.HeadNode).Index;
    }
    
    private Node getLargestNode(Node max, Node node) {
    	if(node != null)
    	{
    		if(node.getNumRids() > max.getNumRids()) {
    			max = node;
    		}
    		max = getLargestNode(max, node.LeftNode);
    		max = getLargestNode(max, node.RightNode);
    	}
    	return max;
    }
}

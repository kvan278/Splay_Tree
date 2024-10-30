/*
 * Name: Khanh Van
 * NetID: kxv230013
 * Section: CS 3345.0U1
 */

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {

	public static void main(String[] args) {
		String connectionString = "mongodb+srv://kvan27082002:nobita2002@testcuster.4gsqi.mongodb.net/?retryWrites=true&w=majority&appName=TestCuster";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
		SplayTree tree = new SplayTree();
		tree.insert(22);
		tree.insert(6);
		tree.insert(13);
		tree.search(22);
		tree.insert(9);
		tree.search(6);
		tree.delete(13);
		tree.insert(12);
		tree.insert(0);
		tree.delete(9);
	}
}

// Node object with pointers to its parent Node as well as left and right children Node
class Node {
	boolean isRoot;
	Node parent;
	Node leftChild;
	Node rightChild;
	int data;

	// Constructor for Node class will take in an integer as data
	// Parent, Left, and Right Child will be null by default
	public Node(int data) {
		this.data = data;
		this.parent = null;
		leftChild = null;
		rightChild = null;
	}

	// Method to set pointer to Left Child
	public void setLeft(Node n) {
		leftChild = n;
	}

	// Method to set pointer to Right Child
	public void setRight(Node n) {
		rightChild = n;
	}

	// Accessor method to return Left Child
	public Node getLeft() {
		return leftChild;
	}

	// Accessor method to return Right Child
	public Node getRight() {
		return rightChild;
	}

	// Method to set pointer to Parent Node
	public void setParent(Node n) {
		this.parent = n;
	}

	// Accessor method to return Parent Node
	public Node getParent() {
		return this.parent;
	}

	// Accessor method to get the Node's data
	public int getData() {
		return this.data;
	}

	// Method to set current Node status as Root
	public void setRoot() {
		isRoot = true;
	}

	// Method to set un-set current Node status as Root
	public void unRoot() {
		isRoot = false;
	}

	// Accessor method to get current Node Root status
	public boolean isRoot() {
		return isRoot;
	}
}

// Splay Tree Class
class SplayTree {
	Node root;

	// Default Constructor will set root to null by default
	public SplayTree() {
		this.root = null;
	}

	// Search public method
	public boolean search(int x) {
		return searchHelper(x, root);
	}

	// Search private helper method
	// Parameter: Integer, Node
	private boolean searchHelper(int x, Node root) {
		// If the current Node is null
		// Either the tree is empty or the Node doesn't exists
		if (root == null) {
			System.out.println("Node " + x + " NOT found!");
			this.printPreOrder();
			return false;
		}
		// The Node exists in the tree -> We then Splay the found Node
		if (root.getData() == x) {
			splay(root);
			System.out.println("Node " + x + " FOUND!");
			this.printPreOrder();
			return true;
		}
		// If the Node searching for is less than current Node
		// We recurse using the Left Child of current Node
		if (root.getData() > x) {
			return searchHelper(x, root.getLeft());
		}
		// If the Node searching for is larger than current Node
		// We recurse using the Right Child of current Node
		return searchHelper(x, root.getRight());
	}

	// Right Rotation method to Rotate Node (parent Node)
	// Parameter: Node (Parent Node)
	public void rightRotate(Node n) {
		// Get the Left Child of the Parent Node
		Node leftChild = n.getLeft();
		// If Parent Node is Root
		// We don't have to worry about connecting Grand-Parent Node to Left Child Node
		// Algorithm for Right Rotation:
		// - If Right Child of Left Child is not Null -> Set Left Child of Parent Node
		// as Right Child of Left Child
		// - Set Right Child of Left Child as Parent node
		// - Assign Parent Node's Parent as Left Child
		if (n.isRoot()) {
			if (leftChild.getRight() != null) {
				n.setLeft(leftChild.getRight());
				leftChild.getRight().setParent(n);
			} else {
				n.setLeft(null);
			}
			leftChild.setRight(n);
			// If Parent is Root
			// - Set Left Child as Root
			// - Set Left Child's Parent as Null
			n.setParent(leftChild);
			leftChild.setRoot();
			root = leftChild;
			n.unRoot();
		} else {
			// Same Algorithm for Right Rotation above
			if (leftChild.getRight() != null) {
				n.setLeft(leftChild.getRight());
				leftChild.getRight().setParent(n);
			} else {
				n.setLeft(null);
			}
			leftChild.setRight(n);
			// If Parent is NOT Root -> Grand-Parent Exists
			Node gParentNode = n.getParent();
			// If Parent is Right Child of Grand-Parent -> set Grand-Parent's
			// Right Child as Parent's Left Child
			if (gParentNode.getRight() != null) {
				if (gParentNode.getRight().getData() == n.getData()) {
					gParentNode.setRight(leftChild);
				}
			}
			// If Parent is Left Child of Grand-Parent -> set Grand-Parent's
			// Left Child as Parent's Left Child
			if (gParentNode.getLeft() != null) {
				if (gParentNode.getLeft().getData() == n.getData()) {
					gParentNode.setLeft(leftChild);
				}
			}
			// Set Parent's Left Child's Parent to be Grand-Parent;
			leftChild.setParent(gParentNode);
			n.setParent(leftChild);
		}
	}

	// Left Rotation Method to Rotate Node (Parent Node)
	// Parameter: Node (Parent Node)
	public void leftRotate(Node n) {
		// Get the Right Child of the Parent Node
		Node rightChild = n.getRight();
		// If Parent Node is Root
		// We don't have to worry about connecting Grand-Parent Node to Right Child Node
		// Algorithm for Left Rotation:
		// - If Left Child of Right Child is not Null -> Set Right Child of Parent Node
		// as Left Child of Right Child
		// - Set Left Child of Right Child as Parent node
		// - Assign Parent Node's Parent as Right Child
		if (n.isRoot()) {
			if (rightChild.getLeft() != null) {
				n.setRight(rightChild.getLeft());
				rightChild.getLeft().setParent(n);
			} else {
				n.setRight(null);
			}
			rightChild.setLeft(n);
			// If Parent is Root
			// - Set Right Child as Root
			// - Set Right Child's Parent as Null
			n.setParent(rightChild);
			rightChild.setRoot();
			root = rightChild;
			n.unRoot();
		} else {
			// Same Algorithm as Left Rotation above
			if (rightChild.getLeft() != null) {
				n.setRight(rightChild.getLeft());
				rightChild.getLeft().setParent(n);
			} else {
				n.setRight(null);
			}
			rightChild.setLeft(n);
			// If Parent is NOT Root -> Grand-Parent Exists
			Node parentNode = n.getParent();
			// If Parent is Right Child of Grand-Parent -> set Grand-Parent's
			// Right Child as Parent's Right Child
			if (parentNode.getRight() != null) {
				if (parentNode.getRight().getData() == n.getData()) {
					parentNode.setRight(rightChild);
				}
			}
			// If Parent is Left Child of Grand-Parent -> set Grand-Parent's
			// Left Child as Parent's Right Child
			if (parentNode.getLeft() != null) {
				if (parentNode.getLeft().getData() == n.getData()) {
					parentNode.setLeft(rightChild);
				}
			}
			// Set Parent's Right Child's Parent to be Grand-Parent;
			rightChild.setParent(parentNode);
			n.setParent(rightChild);
		}
	}

	// Insertion Method
	// Parameter: Integer
	public void insert(int x) {
		Node newNode = new Node(x);
		insertHelper(newNode, root);
		this.printPreOrder();
	}

	// Insertion Helper Method use for Recursion
	// Parameter: Integer, Node (root)
	private void insertHelper(Node n, Node currNode) {
		// If the Tree is empty -> Simply set Root as new Node
		// Set the new Node as Root status
		if (root == null) {
			root = n;
			root.setRoot();
			System.out.println("Node " + n.getData() + " is INSERTED!");
			return;
		// If Node already existed in the Tree -> Do nothing!
		} else if (currNode.getData() == n.getData()) {
			System.out.println("Node " + currNode.getData() + " Already Existed!");
		} else {
			// If new Node is less than current Node -> traverse Left
			if (n.getData() < currNode.getData()) {
				// If Left Child of current Node is Null -> Insert new Node to the spot
				if (currNode.getLeft() == null) {
					currNode.setLeft(n);
					n.setParent(currNode);
					System.out.println("Node " + n.getData() + " is INSERTED!");
					// Splay the new Node to the Root
					splay(n);
					return;
				} else {
					// If not null, recurse using Left Child of current Node
					insertHelper(n, currNode.getLeft());
				}
			// If new Node is larger than current Node -> traverse Right
			} else {
				// If Right Child of current Node is Null -> Insert new Node to the spot
				if (currNode.getRight() == null) {
					currNode.setRight(n);
					n.setParent(currNode);
					System.out.println("Node " + n.getData() + " is INSERTED!");
					// Splay the new Node to the Root
					splay(n);
					return;
				} else {
					// If not null, recurse using Right Child of current Node
					insertHelper(n, currNode.getRight());
				}
			}
		}
	}

	// Deletion Method
	// Parameter: Integer
	public void delete(int x) {
		Node deletedNode = deleteHelper(x, root);
		// If Parent Node is not Null, Splay the Parent Node
		if (deletedNode != null) {
			splay(deletedNode);
		}
		this.printPreOrder();
	}

	// Deletion Helper Method use for Recursion
	// Parameter: Integer, Node (Root)
	// Return: Parent Node of the Deleted Node || If Deleted Node is Root -> Return Null
	// ** Similar to regular BST Deletion Method but returning Parent Node
	private Node deleteHelper(int x, Node currNode) {
		// If the deleting Node is not found or tree is empty
		if (currNode == null) {
			System.out.println("Deleting Node " + x + " not found!");
			return null;
		// If deleting Node is less than current Node -> traverse Left
		} else if (x < currNode.getData()) {
			return deleteHelper(x, currNode.getLeft());
		// If deleting Node is larger than current Node -> traverse Right
		} else if (x > currNode.getData()) {
			return deleteHelper(x, currNode.getRight());
		// Deleting Node is FOUND
		} else {
			System.out.println("Node " + currNode.getData() + " is DELETED!");
			// If current Node is Leaf Node
			if (currNode.getLeft() == null && currNode.getRight() == null) {
				// If the tree only have the root -> Simply set Tree's root to null
				if (currNode.isRoot()) {
					root = null;
					return null;
				} else {
				// Set the Parent's Left or Right Child to null if the Child is Left or Right Child
					Node parentNode = currNode.getParent();
					if (parentNode.getLeft() != null && parentNode.getLeft().getData() == currNode.getData()) {
						parentNode.setLeft(null);
					} else {
						parentNode.setRight(null);
					}
					// Return Parent Node to Splay
					return parentNode;
				}
			// If current Node is NOT Leaf Node
			// If current Node Right Child is not Null -> Replace with Successor of Right Sub-Tree
			} else if (currNode.getRight() != null) {
				currNode.data = findSuc(currNode);
				// Delete Node that is replaced with
				deleteReplaceNode(currNode.getData(), currNode.getRight());
				return currNode.getParent();
			// If current Node Left Child is not Null -> Replace with Predesessor of Left Sub-Tree
			} else {
				currNode.data = findPre(currNode);
				// Deleted Node that is replaced with
				deleteReplaceNode(currNode.getData(), currNode.getLeft());
				return currNode.getParent();
			}
		}
	}

	/*
	 * - Helper method of deletion to delete the Node that is replaced with the actual deleted Node
	 * - This deletion algorithm is the same with the above deletion method -> Only difference is 
	 * that it doesn't return the parent so that when called in main deletion, it won't splay the wrong
	 * parent Node
	 */
	private void deleteReplaceNode(int x, Node currNode) {
		if (x < currNode.getData()) {
			deleteReplaceNode(x, currNode.getLeft());
		} else if (x > currNode.getData()) {
			deleteReplaceNode(x, currNode.getRight());
		} else {
			if (currNode.getLeft() == null && currNode.getRight() == null) {
				Node parentNode = currNode.getParent();
				if (parentNode.getLeft() != null && parentNode.getLeft().getData() == currNode.getData()) {
					parentNode.setLeft(null);
				} else {
					parentNode.setRight(null);
				}
			} else if (currNode.getRight() != null) {
				currNode.data = findSuc(currNode);
				deleteReplaceNode(currNode.getData(), currNode.getRight());
			} else {
				currNode.data = findPre(currNode);
				deleteReplaceNode(currNode.getData(), currNode.getLeft());
			}
		}
	}

	// Helper method to find Successor of Right Sub-Tree
	private int findSuc(Node root) {
		root = root.getRight();
		while (root.getLeft() != null) {
			root = root.getLeft();
		}
		return root.getData();
	}

	// Helper method to find Predessesor of Left Sub-Tree
	private int findPre(Node root) {
		root = root.getLeft();
		while (root.getRight() != null) {
			root = root.getRight();
		}
		return root.getData();
	}

	// Method to Splay the Desired Node to the Root
	public void splay(Node n) {
		// If Node to Splay is Root -> Do nothing
		if (n.isRoot()) {
			return;
		}
		// Traverse the Tree to get the Parent Node of the Node to Splay
		Node parentNode = searchSplay(n, root);
		// If Parent Node is NOT Root
		while (!parentNode.isRoot()) {
			// Get the Grand-Parent Node
			Node grandParent = parentNode.getParent();
			if (grandParent.getLeft() != null && grandParent.getLeft().getData() == parentNode.getData()) {
				// If Node is Left-Left Grand-Child -> Right-Rotate(g) -> Right-Rotate(p)
				if (parentNode.getLeft() != null && parentNode.getLeft().getData() == n.getData()) {
					rightRotate(grandParent);
					rightRotate(parentNode);
				// If Node is Right-Left Grand-Child -> Left-Rotate(p) -> Right-Rotate(g)
				} else {
					leftRotate(parentNode);
					rightRotate(grandParent);
				}
			} else if (grandParent.getRight() != null && grandParent.getRight().getData() == parentNode.getData()) {
				// If Node is Left-Right Grand-Child -> Right-Rotate(p) -> Left-Rotate(g)
				if (parentNode.getLeft() != null && parentNode.getLeft().getData() == n.getData()) {
					rightRotate(parentNode);
					leftRotate(grandParent);
				// If Node is Right-Right Grand-Child -> Left-Rotate(g) -> Left-Rotate(p)
				} else {
					leftRotate(grandParent);
					leftRotate(parentNode);
				}
			}
			// Re-Traverse to find the new Parent Node after rotations
			parentNode = searchSplay(n, root);
		}
		// If Parent is NOW Root
		if (parentNode.isRoot()) {
			// If Node is Left Child of Parent(Root) -> Right-Rotate(p or Root)
			if (parentNode.getLeft() != null) {
				if (parentNode.getLeft().getData() == n.getData()) {
					rightRotate(parentNode);
					this.root.parent = null;
					return;
				}
			}
			// If Node is Right Child of Parent(Root) -> Left-Rotate(p or Root)
			if (parentNode.getRight() != null) {
				if (parentNode.getRight().getData() == n.getData()) {
					leftRotate(parentNode);
					this.root.parent = null;
					return;
				}
			}
		}
	}

	// Helper method to traverse through the Splay Tree to find the Parent Node of
	// the Splaying Node to Rotate
	private Node searchSplay(Node n, Node currNode) {
		if (n.getData() < currNode.getData()) {
			if (currNode.getLeft() != null) {
				if (currNode.getLeft().getData() == n.getData()) {
					return currNode;
				} else {
					return searchSplay(n, currNode.getLeft());
				}
			}
		} else if (n.getData() > currNode.getData()) {
			if (currNode.getRight() != null) {
				if (currNode.getRight().getData() == n.getData()) {
					return currNode;
				} else {
					return searchSplay(n, currNode.getRight());
				}
			}
		}
		return currNode;
	}

	// Method to Print Splay-Tree in PreOrder
	public void printPreOrder() {
		preOrderHelper(root);
		System.out.println();
		System.out.println();
	}

	// Helper Method to Print out Splay-Tree in PreOrder
	private void preOrderHelper(Node n) {
		if (n == null) {
			return;
		}
		// Print out if the current Node is Left-Child, Right-Child, or Root
		if (n.isRoot()) {
			System.out.print(n.getData() + "RT ");
		} else if (n.getParent().getLeft() != null && n.getParent().getLeft().getData() == n.getData()) {
			System.out.print(n.getData() + "L ");
		} else {
			System.out.print(n.getData() + "R ");
		}
		// Traverse Left Sub-Tree then Right Sub-Tree
		preOrderHelper(n.getLeft());
		preOrderHelper(n.getRight());
	}
}

package padTree;

import padInterfaces.TraversableSortedContainer;
import padTree.BinTree.BinTreeNode;

/**
 * A generic class that provides an AVLTree with its basic 
 * operation contains, insert and remove. Basically the functionality
 * are inherited of the SearchTree operations. These operations 
 * are extended in order to ensure the AVL property after every 
 * invocation of a basic operation. Furthermore a check method for 
 * the AVL property is implemented, which recursively tests that the 
 * heights of the two subtrees of every node in the AVL tree is in 
 * absolute value always smaller two.
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class AVLTree<T> extends SearchTree<T> implements TraversableSortedContainer<T> {
	
	/**
	 * Inner class in order to provide AVL nodes.
	 *
	 */
	private class AVLTreeNode extends BinTree<Comparable<T>>.BinTreeNode<Comparable<T>>{
		
		/** Saves the balance of a node. */
		int balance;
		
		/**
		 * Default constructor invokes the super class constructor and 
		 * additionally sets the balance on 0.
		 */
		public AVLTreeNode() {
			
			super();
			this.balance = 0;
		}
		
		/**
		 * A standard constructor which creates a new AVL node laden 
		 * with an obj.
		 * @param obj contains a data value which is comparable.
		 */
		public AVLTreeNode(Comparable<T> obj) {
			
			super(obj);
			this.balance = 0;
		}
		
		/**
		 * Determines the balance of node v by use of
		 * its children's heights.
		 * @param v
		 * @return -1,0,1
		 */
		public int getBalance() {
			
			return this.balance;
		}
		
		/**
		 * Standard toString method which displays 
		 * all information about the node.
		 */
		public String toString() { 
			
			return "Value = " + getData() + ", Balance = " + getBalance() + ", Parent = " + ((getParent() != null) ? getParent().getData() : "doesn't exists")
                    + ", LeftChild = " + ((getLeftChild() != null) ? getLeftChild().getData() : "nicht vorhanden") + ", RightChild = "
                    + ((getRightChild() != null) ? getRightChild().getData() : "doesn't exists");
		}
	}
	
	public enum depthTrace{LL,LR,RR,RL,NP};
	
	/**
	 * Default constructor constructs an empty AVL tree. Dummy 
	 * node is created as an BinTreeNode.
	 */
	public AVLTree() {
	
		super();
	}
	
	/**
	 * Standard constructor constructs an AVL tree with a 
	 * single AVL node.
	 * 
	 * @param obj with data value which is comparable.
	 */
	public AVLTree(Comparable<T> obj) {
		
		this();
		dummy.lson = new AVLTreeNode(obj);
		dummy.lson.parent = dummy;
	}
	
	/**
	 * Getter method for the lastNode (last visited node 
	 * by contains). Returns the casted node as an 
	 * AVLTreeNode.
	 * 
	 * @return lastNode as AVLTreeNode
	 */
	public AVLTreeNode getLastNode() {
		
		AVLTreeNode getLastNode = (AVLTreeNode)super.getLastNode();
		
		return getLastNode;		
	}
	
	/**
	 * Helper variable to indicate if updateBalanceParent method is 
	 * invoked in case of a remove operation. 
	 */
	private boolean isRemove;
	
	/**
	 * Insert method as in SearchTree. Additionally 
	 * the balances of nodes in the trace of 
	 * the added node are updated.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Comparable<T> insert(Comparable<T> obj) {
		
		isRemove = false;
		
		if(this.contains(obj) == null) {
			
			AVLTreeNode child = null;
			
			if(this.isEmpty()) {
				
				this.dummy.lson = new AVLTreeNode(obj);
				this.dummy.lson.parent = this.dummy;
				
				return obj;
			}
			
			if(this.getLastNode().getData().compareTo((T)obj) == 1) {
				
				this.getLastNode().lson = new AVLTreeNode(obj);
				this.getLastNode().lson.parent = this.getLastNode();
				
				child = (AVLTreeNode) this.getLastNode().lson;
			}
			
			if(this.getLastNode().getData().compareTo((T)obj) == -1) {
			
				this.getLastNode().rson = new AVLTreeNode(obj);
				this.getLastNode().rson.parent = this.getLastNode();
				
				child = (AVLTreeNode) this.getLastNode().rson;				
			}
			
			while(child != getRoot()) {
				
				AVLTreeNode father = (AVLTreeNode) child.parent;
				
				updateBalanceParent(child);
				
				if(father.getBalance() == 0)
					break;
				
				if(Math.abs(father.getBalance()) > 1) {
					
					rebalance(father);
					break;
				}
				
				child = father;
			}
			
			return obj;
		}
		
		return this.contains(obj);
	}
	
	/**
	 * Remove method as in SearchTree. Additionally 
	 * the balances of nodes in the trace of 
	 * the removed node are updated.
	 * 
	 */
	@Override
	public Comparable<T> remove(Comparable<T> obj) {
		
		isRemove = true;
		
		Comparable<T> tempObj = super.remove(obj);
		
		if(getRoot() == null || getPredecessor().parent == this.dummy)
			return tempObj;
			
			AVLTreeNode removedNode = new AVLTreeNode();
			removedNode.parent = getPredecessor().parent;
			removedNode.lson = getPredecessor().lson;
			removedNode.rson = getPredecessor().rson;
			
			while(removedNode != getRoot()) {
				
				AVLTreeNode father = (AVLTreeNode)removedNode.parent;
				
				updateBalanceParent((AVLTreeNode)removedNode);
				
				if(Math.abs(father.getBalance()) > 1) {
					
					rebalance(father);
					father = (AVLTreeNode)(father.parent);
					
				}
				
				if(Math.abs(father.getBalance()) == 1)
					break;
				
				removedNode = father;
			}
		
		return tempObj;
	}
	
	/**
	 * Checks the balance value in every node of the 
	 * tree starting by the root.
	 * 
	 * @return True, if no balance is greater than 1 or 
	 * smaller than -1, false otherwise.
	 */
	public boolean checkBalances() {
		
		return _checkBalances((AVLTreeNode)getRoot());
	}
	
	/**
	 * Helper method for checkBalances.
	 * 
	 * @param node the root of a binary tree for which 
	 * the AVL property should be checked.
	 * 
	 * @return true, if for every two subtrees of node and their successors 
	 * the absolute value of the difference of their heights are 
	 * smaller or equal to 1. True is also returned if the passed 
	 * node is null . Otherwise false.
	 */
	private boolean _checkBalances(AVLTreeNode node) {
        
		return (node == null) ||
                (_checkBalances((AVLTreeNode)node.getLeftChild()) &&
                _checkBalances((AVLTreeNode)node.getRightChild()) &&
                (Math.abs(getHeightNode(node.getLeftChild()) - getHeightNode(node.getRightChild())) <= 1));
	}
	
	/**
	 * This method updates the balance of the passed node v 
	 * parent. In case of removing the balanc is recalculated by adding 
	 * 1, if the node is a left child or adding -1, if the node is 
	 * a right child. If inserting the recalculation is the other 
	 * way around.
	 *  
	 * @param v is a node; for remove operation it has its 
	 * parent reference on the first node of 
	 * the remove trace; for insert operation 
	 * the v is the added node itself.
	 */
	public void updateBalanceParent(AVLTreeNode v) {
		
		if(isRemove) {
			
			// For the first turn after removing a helper node
			// which points at the first node on the removed node's trace
			// saves in its reference of leftson the current root of the 
			// tree if the removed node was a leftchild. This functions as 
			// a definite hint to decide the direction of balance updating,
			// without declaring a new storage variable for this. 
			// It is unambiguous because no other AVL node in the tree 
			// has the root as its leftson.
			AVLTreeNode father = (AVLTreeNode)(v.parent);
			father.balance += (v.getLeftChild() == getRoot() || v.isLeftChild()) ? 1 : -1;
		}

		else {
			
			AVLTreeNode father = (AVLTreeNode)(v.parent);
			father.balance += v.isLeftChild() ? -1 : 1;
		}
		
	}
	
	/**
	 * Depending on the passed node's, the node's child 
	 * and the node's grandchild balances the longest way 
	 * into the depth of the corresponding subtree is 
	 * determined. Based on this calculation the appropriate 
	 * rotation is performed through invoking helper method 
	 * _rebalnace.
	 *
	 * @param v node that constitutes the rotating subtree's 
	 * root
	 */
	public void rebalance(AVLTreeNode v) {
		
		depthTrace balanceWay = depthTrace.NP;
		int balance = v.getBalance();
		
		AVLTreeNode subParent = null;
		AVLTreeNode child = null;
		
		if(Math.abs(balance) > 1) {
			
			if (balance < 0) {
				
				subParent = (AVLTreeNode)(v.getLeftChild());
				
	            balance = subParent.getBalance();
	           
	            if (balance <= 0) {
	                
	            	child = (AVLTreeNode)(subParent.getLeftChild());
	                balanceWay = depthTrace.LL;
	            } 
	            
	            else {
	                
	            	child = (AVLTreeNode)(subParent.getRightChild());
	                balanceWay = depthTrace.LR;
	            }
	        } 
			
			else {
	            
	        	subParent = (AVLTreeNode)(v.getRightChild());
	            balance = subParent.getBalance();
	            
	            if (balance < 0) {
	                
	            	child = (AVLTreeNode)(subParent.getLeftChild());
	                balanceWay = depthTrace.RL;
	            } 
	            
	            else {
	                
	            	child = (AVLTreeNode)(subParent.getRightChild());
	                balanceWay = depthTrace.RR;
	            }
	        }
			
			_rebalance(v, subParent, child, balanceWay);
		}
	}
	
	/**
	 * This methods controls which rotation is performed. After 
	 * the rotation the balances are aptly adjusted.
	 * 
	 * @param v rotation subtree's root
	 * @param subParent son of the rotation subtree's root
	 * @param child grandson of the rotation subtree's root
	 * @param way indicates the type of rotation
	 */
	private void _rebalance(AVLTreeNode v, AVLTreeNode subParent, AVLTreeNode child, depthTrace way) {
		
		switch(way) {
		
		case LL:
				
			rotRight(v);
			
			v.balance = (subParent.getBalance() == 0) ? -1 : 0;
			subParent.balance = (subParent.getBalance() == 0) ? 1 : 0;
			
			break;
			
		case LR: 
			
			dRotRight(v);
			
			v.balance = (child.getBalance() == -1) ? 1 : 0;
			subParent.balance = (child.getBalance() == 1) ? -1 : 0;
			child.balance = 0;
			
			break;
			
		case RL: 
			
			dRotLeft(v);
			
			v.balance = (child.getBalance() == 1) ? -1 : 0;
			subParent.balance = (child.getBalance() == -1) ? 1 : 0;
			
			child.balance = 0;
			
			break;
			
		case RR: 
			
			rotLeft(v);
			
			v.balance = (subParent.getBalance() == 0) ? 1 : 0;
			subParent.balance = (subParent.getBalance() == 0) ? -1 : 0;
			
			break;
		
		default :
			
			break;
		}
		
	}
	
	@Override
	public AVLTreeNode rotRight(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		return (AVLTreeNode)(super.rotRight(v));
	}
	
	@Override
	public AVLTreeNode rotLeft(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		return (AVLTreeNode)(super.rotLeft(v));
	}
	
	@Override
	public AVLTreeNode dRotRight(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		return (AVLTreeNode)(super.dRotRight(v));
	}
	
	@Override
	public AVLTreeNode dRotLeft(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		return (AVLTreeNode)(super.dRotLeft(v));
	}
}

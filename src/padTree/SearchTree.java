package padTree;

import padInterfaces.TraversableSortedContainer;
import padTree.BinTree.BinTreeNode;

/**
 * A generic class that provides an standard SearchTree with its basic 
 * operation contains, insert and remove. Also the two rotating methods 
 * to transform an Searchtree are implemented.
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class SearchTree<T> extends BinTree<Comparable<T>> implements TraversableSortedContainer<T> {
	
	/** Last node visited by the contains-method.*/
	private BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> lastNode;
	
	/** Saves the father node of an removed node and furthermore 
	 * whether the removed node is a leftson or a rightson. */
	private BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> predecessorDummy;
	
	/**
	 * Getter method of lastNode.
	 * @return lastNode
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> getLastNode() {
		
		return lastNode;
	}
	
	/**
	 * Getter method for predecessor.
	 * @return predecessor
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> getPredecessor() {
		
		return predecessorDummy;
	}
	
	@Override
	/**
	 * The methods searches for the 
	 */
	public Comparable<T> contains(Comparable<T> obj) {
		
		if(this.isEmpty())		
			return null;
		
		return _contains(this.getRoot(), obj);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Recursive helper method of "contains" method. Needs 
	 * in the worst-case scenario h(T) invocations of this 
	 * method. The reference variable "lastNode" is set on the 
	 * last visited node.
	 * 
	 * @param treeObj Currently visited node of the tree.
	 * 
	 * @param obj the passed obj with the key to compare.
	 * 
	 * @return If the comparison of the current treeObj's data with passed obj
	 * returns 0 the treeObj's data is returned. If not null is returned.
	 */
	private Comparable<T> _contains(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> treeObj, Comparable<T> obj) {
		
		if(treeObj.getData().compareTo((T)obj) == 0) {
			
			lastNode = treeObj;
			return treeObj.getData();
		}
		
		else if(treeObj.getData().compareTo((T)obj) == 1 && treeObj.getLeftChild() != null)
			return _contains(treeObj.getLeftChild(), obj);
		
		else if(treeObj.getData().compareTo((T)obj) == -1 && treeObj.getRightChild() != null)
			return _contains(treeObj.getRightChild(), obj);
		
		else {
			
			lastNode = treeObj;
			return null;
		}
			
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This method implements the insert method. It is 
	 * distinguished between four cases. 
	 * 
	 * If the SearchTree is empty a node with the data of 
	 * the passed obj is created and inserted as the 
	 * leftson of the dummy node.
	 * 
	 * If no node with data equal to the passed obj is contained 
	 * in the SearchTree a node with the data of 
	 * the passed obj is created and inserted as the 
	 * leftson of the lastNode (determinded through contains-method)
	 * if lastnode's data is bigger than obj.
	 * 
	 * If no node with data equal to the passed obj is contained 
	 * in the SearchTree a node with the data of 
	 * the passed obj is created and inserted as the 
	 * rightson of the lastNode (determinded through contains-method)
	 * if lastnode's data is smaller than obj.
	 * 
	 * In the last case a node with the same data as obj exists. No 
	 * new node is created.
	 * 
	 * @param obj
	 * 
	 * @return In the first, second and third case the obj itself is returned. 
	 * In the fourth case the data of a already existing node with the 
	 * same key is returned.
	 */
	public Comparable<T> insert(Comparable<T> obj) {
		
		if(this.contains(obj) == null) {
		
			if(this.isEmpty()) {
				
				this.dummy.lson = new BinTreeNode<Comparable<T>>(obj);
				this.dummy.lson.parent = this.dummy;
				
				return obj;
			}
			
			if(this.lastNode.getData().compareTo((T)obj) == 1) {
				
				this.lastNode.lson = new BinTreeNode<Comparable<T>>(obj);
				this.lastNode.lson.parent = this.lastNode;
				return obj;
			}
			
			else if(this.lastNode.getData().compareTo((T)obj) == -1) {
				
				this.lastNode.rson = new BinTreeNode<Comparable<T>>(obj);
				this.lastNode.rson.parent = this.lastNode;
				return obj;
			}		
		}
		
		return this.contains(obj);
	}
	
	@Override
	/**
	 * This method removes a node which contains the 
	 * passed object's data. Three cases are distinguished: 
	 * The removed node is a leaf, the removed node has 
	 * only one child and the removed node has two children. 
	 *
	 */
	public Comparable<T> remove(Comparable<T> obj) {
		
		if(this.contains(obj) != null) {
			
			if(lastNode.isLeaf()) {
				
				return _removeLeaf(lastNode);
			}
			
			else if((lastNode.getLeftChild() != null && lastNode.getRightChild() == null) 
					|| (lastNode.getLeftChild() == null && lastNode.getRightChild() != null)) {
				
				return _removeOneChild(lastNode);
				
			}
			
			else{
				
				this.origin = Origin.LEFT;
				this.curr = lastNode;
				this.increment();
				
				Comparable<T> swapper = lastNode.getData();
				
				lastNode.data = this.currentData();
				
				if(this.curr.isLeaf()) {
					
					_removeLeaf(this.curr);
				}
				
				else {
					
					_removeOneChild(this.curr);
				}
				
				return swapper;
			}
			
		}
		
		return null;
	}
	
	/**
	 * Helper method for removing a node 
	 * with only one child.
	 * 
	 * @return Object of removed node.
	 */
	private Comparable<T> _removeOneChild(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> node) {
		
		// Creates an object with reference of the predecessor of removed node. 
		// Furthermore it saves a reference on the root of the tree either in 
		// rson or in lson, to save if the removed node was left or right child.
		predecessorDummy = new BinTreeNode<Comparable<T>>();
		predecessorDummy.parent = node.parent;
		
		
		if(node.getLeftChild() != null) {
			
			node.lson.parent = node.parent;
			
			if(node.isLeftChild()) {
				
				predecessorDummy.lson = getRoot();
				node.parent.lson = node.lson;
			}
			else {
				
				predecessorDummy.rson = getRoot();
				node.parent.rson = node.lson;
			}
			
			node.parent = null;
			node.lson = null;
			
			return node.getData();
		}
		
		else {
			
			node.rson.parent = node.parent;
			
			if(node.isLeftChild()) {
				
				predecessorDummy.lson = getRoot();
				node.parent.lson = node.rson;
			}
			else{
				
				predecessorDummy.rson = getRoot();
				node.parent.rson = node.rson;
			}
			
			node.parent = null;
			node.rson = null;
			
			return node.getData();
		}	
	}
	
	/**
	 * Helper method for removing one node with 
	 * no children at all.
	 * 
	 * @return Object of removed node.
	 */
	private Comparable<T> _removeLeaf(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> node) {
		
		predecessorDummy = new BinTreeNode<Comparable<T>>();
		predecessorDummy.parent = node.parent;
		
		if(node.isLeftChild()) {
			predecessorDummy.lson = getRoot();
			node.parent.lson = null;
		}
		else{
			predecessorDummy.rson = getRoot();
			node.parent.rson = null;
		}
		
		node.parent = null;
		
		return node.getData();
		
	}
	
	

	@Override
	public int maxSearch() {
		
		return super.getHeight();
	}

	@Override
	/**
	 * This method examines if the traversal pointer
	 * points on the last element of the SearchTree.
	 * 
	 * @return true, if curr points at the most right 
	 * node of the tree; false, otherwise.
	 */
	public boolean isAtEnd() {
		
		if(this.isEmpty())
			return true;
		
		return this.curr.equals(_theMostRight());
	}
	
	/**
	 * This method determines the most right node 
	 * in the current SeachTree.
	 * 
	 * @return the most right node
	 */
	private BinTreeNode<Comparable<T>> _theMostRight() {
		
		BinTreeNode<Comparable<T>> iterator = this.getRoot();
			
			while(iterator.getRightChild() != null)
				iterator = iterator.getRightChild();
			
			return iterator;
	}

	@Override
	/**
	 * Advances the super class method in order to 
	 * handle an empty tree.
	 */
	public void reset() {
		
		if(this.isEmpty()){}
			
		else
			super.reset();
	}
	
	
	/**
	 * In this method the right rotation is employed on node v. In 
	 * this case the root is located on the right side of its subtree.
	 * 
	 * @param v the root of the subtree to be rotated.
	 * 
	 * @return aux the new root of the subtree
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> rotRight(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> aux = v.lson;
		
		if(aux.getRightChild() == null) {
			
			v.lson = null;
			
			aux.parent = v.parent;
			
			if(v.isLeftChild())
				aux.parent.lson = aux;
			else
				aux.parent.rson = aux;
			
			aux.rson = v;
			v.parent = aux;
			
		}
		
		else {
			
			v.lson = aux.rson;
			aux.rson.parent = v;
			
			aux.parent = v.parent;
			
			if(v.isLeftChild())
				aux.parent.lson = aux;
			else
				aux.parent.rson = aux;
			
			v.parent = aux;
			aux.rson = v;
		}
		
		return aux;
	}
	
	/**
	 * In this method the left rotation is employed on node v. In 
	 * this case the root is located on the left side of its subtree.
	 * 
	 * @param v the root of the subtree to be rotated.
	 * 
	 * @return aux the new root of the subtree.
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> rotLeft(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		
		BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> aux = v.rson;
		
		if(aux.getLeftChild() == null) {
			
			v.rson = null;
			
			aux.parent = v.parent;
			
			if(v.isLeftChild())
				aux.parent.lson = aux;
			else
				aux.parent.rson = aux;
			
			aux.lson = v;
			v.parent = aux;
			
		}
		
		else {
			
			v.rson = aux.lson;
			aux.lson.parent = v;
			
			aux.parent = v.parent;
			
			if(v.isLeftChild())
				aux.parent.lson = aux;
			else
				aux.parent.rson = aux;
			
			aux.lson = v;
			v.parent = aux;	
		}
		
		return aux;
	}
	
	/**
	 * In this method the double right rotation is employed on node v.
	 * This means first to pass the leftson of v to the left rotation-method and
	 * after that to pass v itself to the right rotation method.
	 * 
	 * @param v the root of the subtree to be rotated.
	 * 
	 * @return the new root of the rotated subtree.
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> dRotRight(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		rotLeft(v.lson);
			
		return rotRight(v);
	}
	
	/**
	 * In this method the double left rotation is employed on node v.
	 * This means first to pass the rightson of v to the right rotation-method and
	 * after that to pass v itself to the left rotation method.
	 * 
	 * @param v the root of the subtree to be rotated.
	 * 
	 * @return v the new root of the rotated subtree.
	 */
	public BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> dRotLeft(BinTree<Comparable<T>>.BinTreeNode<Comparable<T>> v) {
		
		rotRight(v.rson);
			
		return rotLeft(v);
	}
}

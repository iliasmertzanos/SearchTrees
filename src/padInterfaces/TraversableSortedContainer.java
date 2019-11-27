package padInterfaces;


/**
 * interface for sorted dynamic data structures with iterator-like
 * functionality
 */
public interface TraversableSortedContainer<T>
{
    /**
     * @param obj object whose key is searched
     * @return object with same key as obj, null otherwise
     */
    public Comparable<T> contains ( Comparable<T> obj ) ;


    /**
     * insert object if no other object with same key is present
     *
     * @param obj object to be inserted
     * @return the object itself if it is the first one with its key,
     *         otherwise the found object with same key
     */
    public Comparable<T> insert ( Comparable<T> obj ) ;


    /**
     * remove object with same key from if present
     *
     * @param obj object whose key is searched
     * @return removed object if any was found, null otherwise
     */
    public Comparable<T> remove ( Comparable<T> obj ) ;

    
    /**
     * Reset traversal pointer. Right after calling reset,
     * currentData is expected to return the object with the lowest
     * key in the container.
     *
     */
    public void reset(); // Exists in BinTreeNode + New implementation if the tree is empty.


    /**
     * Advance traversal pointer to the element with the next greater
     * key.
     *
     */
    public void increment(); // Exists in BinTreeNode


    /**
     * Check if traversal pointer has arrived at the last element of
     * the container (which is also expected to be the one with the
     * greatest key).
     *
     * @return does the traversal pointer point to the last (greatest) element?
     */
    public boolean isAtEnd(); // Exists in BinTreeNode + New Implementation in SearchTree because requirements are different


    /**
     * Provides access to the object the traversal pointer currently
     * points to.
     *
     * @return the object the traversal pointer currently points to
     */
    public Comparable<T> currentData(); // Exists in BinTreeNode
    

    /**
     * @return current number of entries in container
     */
    public int getSize ( ) ; // Exists in BinTreeNode


    /**
     * @return current maximum effort for a search
     */
    public int maxSearch ( ) ;


    /**
     * @return is container currently empty?
     */
    public boolean isEmpty ( ) ; // Exists in BinTreeNode


}

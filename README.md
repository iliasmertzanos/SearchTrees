# SearchTrees

## Implementation of a AVL and basic search tree algorithm

The java code can be tested using the main method of the class 'SearchTreeTest' in the src folder .

Please read the usage of test class:

- usage: SearchTreeTest `[-[a|b][v][d]]`  `<numElements>` `<access>`
- Insert `<numElements>` elements in search tree, access them <access> times
    and then remove them (all in randomized order).
    <numElements> : number of elements to insert into tree; must be <= 100000
    <access>      : number of requests for a tree element before it is removed
- options:
    - `a` : use AVLTree (default)
    - `b` : use SearchTree
    - `d` : debug mode; after each insert/remove, search tree property is checked
    by traversal; if an AVLTree is used, balance property is checked too;

    Additionally, maximum size and height of tree are calculated;
    CAUTION: may be equally slow with option a and b for large instances
    - `v` : verbose mode; search tree is output after each insert/remove

More information about avl trees are to be found here:

https://en.wikipedia.org/wiki/AVL_tree

and for search trees here:

https://en.wikipedia.org/wiki/Search_tree

For specific information see the source code comments.

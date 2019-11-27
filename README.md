# SearchTrees

## Implementation of a AVL tree algorithm

The java code can be tested over the main method of the class SearchTreeTest.

Please read the usage of test class:

- usage: SearchTreeTest [-[a|b][v][d]] <numElements> <access>") ;
- Insert <numElements> elements in search tree, access them <access> times");
    and then remove them (all in randomized order)." ) ;
    <numElements> : number of elements to insert into tree; must be <= 100000");
    <access>      : number of requests for a tree element before it is removed" ) ;
- options:
-- a : use AVLTree (default)
-- b : use SearchTree
-- d : debug mode; after each insert/remove, search tree property is checked
by traversal; if an AVLTree is used, balance property is checked too;

Additionally, maximum size and height of tree are calculated;
CAUTION: may be equally slow with option a and b for large instances
v : verbose mode; search tree is output after each insert/remove

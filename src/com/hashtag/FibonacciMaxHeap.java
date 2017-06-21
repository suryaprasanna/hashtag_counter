package com.hashtag;

/**
 * Created by Surya on 6/21/2017.
 */
public class FibonacciMaxHeap {
    /**
     * root node contains the max element of the heap.
     * n contains the number of elements in the heap.
     * root is null if and only if n is 0.
     */
    private Node root;
    private int n;

    /**
     * FibonacciMaxHeap constructor creates the heap with n = 0 and root = null;
     */
    public FibonacciMaxHeap() {
        n = 0;
    }

    /**
     * insert method takes tag and value as parameters and initializes a node with default values.
     * @param tag   tag or name of the node
     * @param value value of the node
     *
     * @return insertedNode returns the object of the newly inserted node.
     *
     */
    public Node insert(String tag, int value) {
        Node node = new Node(tag, value);
        return insertNode(node, false);
    }

    /**
     * insertNode method takes initialized node and isPresent as parameters.
     * This method is used in following cases.
     * 1. While inserting a new node into the heap.
     * 2. While doing increase key operation after freeing the node from the heap and to again insert back.
     *
     * If isPresent is false then only the method increments n.
     * And if root is null then the new node will becomes the root.
     * If the new node's value is greater than the root's value then we update the root to newly created node.
     * @param node
     * @param isPresent
     *
     * @return node     returns the object of the inserted node.
     *
     */
    public Node insertNode(Node node, boolean isPresent) {
        if (!isPresent) n = n + 1;
        if (null == root) {
            root = node;
            return node;
        }
        node.setChildCut(false);
        node.setRightNode(root.getRightNode());
        node.setLeftNode(root);
        root.getRightNode().setLeftNode(node);
        root.setRightNode(node);
        if (root.getValue() < node.getValue()) root = node;
        return node;
    }

    /**
     * Variables c and rootNodesCount are used, so that to iterate the root elements
     * rootNodesCount initially iterated so that it contains the count of elements in the upper layer,
     * This method gets called after the removeMax method is executed on the heap,
     * this method does the pairwise combine of the nodes in the upper layer,
     * so that no two nodes have same degree.
     */
    private int c = 0, rootNodesCount = 0;
    private void pairwiseCombine() {
        if (null == root) return;

        //create nodes array and initilize it to null
        // Nodes [] nodes -> this array is used to keep track of the node degrees
        Node[] nodes = new Node[n+1];
        for (int i = 0; i <= n; i++) {
            nodes[i] = null;
        }

        rootNodesCount = 0;
        c = 0;

        // node is created to iterate the root level nodes and
        // their by initialize rootNodesCount with the count.
        Node node = root;
        while (true) {
            rootNodesCount++;
            if (node.getRightNode() == root) {
                break;
            }
            node = node.getRightNode();
        }

        // this loop runs until the number of elements in the root level i.e. rootNodesCount
        // is equal to the number of not null elements in the Node [] array i.e. c.
        // checkDegreeAndCombine method takes care of the combining
        node = root;
        while (rootNodesCount != c) {
            Node nextNode = node.getRightNode();
            checkDegreeAndCombine(nodes, node);
            if (node.getRightNode() == root) break;
            node = nextNode;
        }
    }

    /**
     * This method before inserting the node into Node [],
     * calls isNewDegree method to check whether already a node with same degree exists or not.
     * If duplicate degree exists then it calls combine method to join the nodes with same degree recursively
     * and their by update the c and rootNodesCount values.
     * @param nodes
     * @param node
     */
    private void checkDegreeAndCombine(Node[] nodes, Node node) {
        int currNodeDegree = node.getDegree();
        if (isNewDegree(nodes, node)) {
            nodes[currNodeDegree] = node;
            c = c + 1;
        } else {
            Node newNode = combine(nodes[currNodeDegree], node);
            rootNodesCount--;
            nodes[currNodeDegree] = null;
            c = c - 1;
            checkDegreeAndCombine(nodes, newNode);
        }
    }

    /**
     * It find whether the given node's degree is already present in the Node [] array or not
     * and their by returns boolean value accordingly.
     * @param nodes
     * @param node
     * @return
     */
    private boolean isNewDegree(Node[] nodes, Node node) {
        Node prevNode = nodes[node.getDegree()];
        return (null == prevNode || prevNode == node);
    }

    /**
     * checkAndDegreeCombine method calls this method to combine two nodes of equal degree
     * and this method combines two nodes by comparing node values
     * and make the smaller valued node child of the other.
     * @param a
     * @param b
     * @return -> resultant node
     */
    private Node combine(Node a, Node b) {

        // updating the higher values node to be parent, and the other to be child
        Node parentNode = a, childNode = b;
        if (a.getValue() < b.getValue()) {
            parentNode = b;
            childNode = a;
        }

        //freeing the node from the heap
        nodeFree(childNode);

        //attaching the child node to the parent and returns the parent node.
        // if their is already a child to the parent then it updated the node accordingly.
        Node prevChildNode = parentNode.getChildNode();
        if (null != prevChildNode) {
            Node prevChildLeftNode = prevChildNode.getLeftNode();
            childNode.setLeftNode(prevChildLeftNode);
            prevChildNode.setLeftNode(childNode);
            prevChildLeftNode.setRightNode(childNode);
            childNode.setRightNode(prevChildNode);
        }
        parentNode.setChildNode(childNode);
        childNode.setParentNode(parentNode);
        parentNode.setDegree(parentNode.getDegree() + 1);
        if (childNode == root) root = parentNode;
        return parentNode;
    }

    /**
     * This is a generic method, which frees the node out of the heap by properly updating the node right, left
     * and parent nodes. The node's children are not affected by this method, so they remain in intact
     * with this node.
     * increaseKey calls this method to free the node and insert it at the root level,
     * similarly cascadeCut and combine methods also calls this method.
     * @param node  -> node that needs to be freed.
     */
    private void nodeFree(Node node) {
        Node leftNode = node.getLeftNode();
        Node rightNode = node.getRightNode();
        Node parentNode = node.getParentNode();

        //while removing we need to adjust right and left node only in the following case.
        // 1. If the node to remove does not have any siblings.
        if (node != node.getRightNode()) {
            leftNode.setRightNode(rightNode);
            rightNode.setLeftNode(leftNode);
        }
        node.setRightNode(node);
        node.setLeftNode(node);

        //if parent exists for the node, which needs to be freed.
        //if required we should properly update the childnode values of the parent
        if (null != parentNode) {
            if (parentNode.getChildNode() == node) {
                if (rightNode != node) {
                    parentNode.setChildNode(rightNode);
                } else {
                    parentNode.setChildNode(null);
                }
            }
            node.setParentNode(null);

            // decrease the degree of the parent
            int parentDegree = parentNode.getDegree();
            parentNode.setDegree(parentDegree - 1);
        }
    }

    /**
     * This method is called aftet the increase key operation on a node.
     * It checks isChildCut value method and their by decides whether the node's child is previously removed or not.
     * If previously it's child is removed then it updates the childCut to false and removes the node and
     * attaches at the root level. After that it recursively update the does same for it ancestors
     * until it meets a node with childCut value false.
     *
     * @param node                  Node that needs to be cascade cut.
     * @param isFirstCascadeCut     If this true then the method frees the node and attaches at root level.
     */
    private void cascadeCut(Node node, boolean isFirstCascadeCut) {

        if (!isFirstCascadeCut && !node.isChildCut()) return;
        Node parentNode = node.getParentNode();
        nodeFree(node);
        insertNode(node, true);

        // if child of the parent is already removed before
        // then use cascade cut to remove itself from the tree.
        if (null != parentNode ) {
            if (parentNode.isChildCut()) {
                cascadeCut(parentNode, false);
            } else {
                parentNode.setChildCut(true);
            }
        }
    }

    /**
     * This method is used to remove the max node that is present in the heap and return.
     * To do this, the method removes its children one by one and reinserts back into the heap.
     * After this it calls pairwise combine to combine the nodes with equal degrees.
     * their by it updates the root node to the max node in the heap.
     * @return  returns the max element in the heap structure.
     */
    public Node removeMax() {
        if (null == root) {
            return null;
        }
        Node node = root;
        Node rootRightNode = null;
        if (node != node.getRightNode()) {
            rootRightNode = root.getRightNode();
        }
        Node childNode = node.getChildNode();

        //freeing the root node from the heap structure.
        nodeFree(node);
        node.setChildNode(null);
        node.setDegree(0);
        root = rootRightNode;

        // if there exists atleast one child to the root.
        if (null != childNode) {

            //set parentNode to false for all children of root
            Node x = childNode;
            Node t = x;
            while (x != x.getRightNode()) {
                t = x.getRightNode();
                x.setParentNode(null);
                nodeFree(x);
                insertNode(x, true);
                x = t;
            }
            x.setParentNode(null);
            nodeFree(x);
            insertNode(x, true);
        }

        // this method combines the element in the root level which has the same degree.
        pairwiseCombine();

        //Below while loop is used to find the max node and assign it to root.
        Node node1 = root.getRightNode();
        Node tempRoot = root;
        while (root != node1) {
            if (node1.getValue() > tempRoot.getValue() ) {
                tempRoot = node1;
            }
            node1 = node1.getRightNode();
        }
        root = tempRoot;
        n = n - 1;
        return node;
    }

    /**
     * This method is used to increase the value of the node.
     * If the value is greater than the parent node then it calls cascadeCut
     * to recursively remove the parent nodes whose childCut value is true.
     * @param node
     * @param value
     */
    public void increaseKey(Node node, int value) {
        int prevValue = node.getValue();
        node.setValue(prevValue + value);

        // if root is the node whose values is incremented no further operations.
        if (node == root) {
            return;
        }
        Node parentNode = node.getParentNode();
        if (null != parentNode) {
            if (node.getValue() <= parentNode.getValue()) {
                return;
            }
            // if node value is greater than the parent then the node is eligible for cascadeCut operation.
            cascadeCut(node, true);
        } else {
            nodeFree(node);
            insertNode(node, true);
        }
    }
}

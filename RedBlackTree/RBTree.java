package RedBlackTree;
import java.util.ArrayList;
public class RBTree<E extends Comparable<E>> extends BST<E>{
    public RBTree(){}

    public RBTree(E[] elements){super(elements);}

    @Override
    protected RBTreeNode<E> createNewNode(E e){return new RBTreeNode<E>(e);}

    @Override
    public boolean insert(E e){
        boolean successful = super.insert(e);
        if(!successful) return false;
        else ensureRBTree(e);
        return true;
    }
    /* This function ensures that the tree is a proper Red-Black tree */
    private void ensureRBTree(E e){
        ArrayList<TreeNode<E>> path = path(e);
        // Index to the current node in the path //
        int i = path.size() - 1;
        // x is the last node in the path. Contains element e
        RBTreeNode<E> x = (RBTreeNode) (path.get(i));

        // u is the parent of x, if it exists.
        RBTreeNode<E> u = (x == root) ? null:(RBTreeNode) (path.get(i-1));

        x.setRed();

        if(x == root) x.setBlack();
        else if(u.isRed()) fixDoubleRed(x, u, path, i);
    }

    private void fixDoubleRed(RBTreeNode<E> x, RBTreeNode<E> u, ArrayList<TreeNode<E>> path, int i){

        // w = GrandParent of x
        RBTreeNode<E> w = (RBTreeNode<E>) (path.get(i - 2));
        RBTreeNode<E> parentOfW = (w == root) ? null : (RBTreeNode<E>) path.get(i - 3);

        // Get u's sibling named v
        RBTreeNode<E> v = (w.left == u) ? (RBTreeNode<E>) (w.right) : (RBTreeNode<E>) (w.left);

        if(v == null || v.isBlack()){
            // Case 1: u's sibling v is black
            if(w.left == u && u.left == x){
                //Case 1.1: x < u < w : restructure and recolor nodes
                restructureRecolor(x, u, w, w, parentOfW);
                w.left = u.right;
                u.right = w;
            }else if(w.left == u && u.right == x){
                //Case 1.2: u < x < w : restructure and recolor nodes
                restructureRecolor(u, x, w, w, parentOfW);
                u.right = x.left;
                w.left = x.right;
                x.left = u;
                x.right = w;
            }else if (w.right == u && u.right == x){
                //Case 1.3: w < u < x : restructure and recolor nodes
                restructureRecolor(w, u, x, w, parentOfW);
                w.right = u.left;
                u.left = w;
            }else{
                //Case 1.4: w < x < u : restructure and recolor nodes
                restructureRecolor(w, x, u, w, parentOfW);
                w.right = x.left;
                u.left = x.right;
                x.left = w;
                x.right = u;
            }// Case 1.x cases
        }//End Case 1
        else{
            // Case 2: u's sibling v is red
            w.setRed();
            x.setRed();
            ((RBTreeNode<E>)(w.left)).setBlack();
            ((RBTreeNode<E>)(w.right)).setBlack();

            if(w == root) w.setBlack();
            else if(((RBTreeNode<E>)parentOfW).isRed()){
                x = w;
                u = (RBTreeNode<E>) parentOfW;
                fixDoubleRed(x,u,path, i - 2);
            }
        }//End Case 2
    }
    /* Connect b with parentOfW and recolor a, b, c, for a < b < c */
    private void restructureRecolor(RBTreeNode<E> a, RBTreeNode<E> b, RBTreeNode<E> c, RBTreeNode<E> w, RBTreeNode<E> parentOfW){
        if(parentOfW == null) root = b;
        else if(parentOfW.left == w) parentOfW.left = b;
        else parentOfW.right = b;
        b.setBlack();// b becomes the root in the subtree
        a.setRed();// a becomes the left child of b
        c.setRed();// c becomes the right child of b
    }
    @Override
    public boolean delete(E e){
        TreeNode<E> current = root;
        while(current != null){
            if(e.compareTo(current.element) < 0) current = current.left;
            else if(e.compareTo(current.element) > 0) current = current.right;
            else break;
        }
        if(current == null) return false;
        ArrayList<TreeNode<E>> path = new ArrayList<>();
        // Current Node is an internal node
        if(current.left != null && current.right != null){
            //Locate the right most node in the left subtree of current
            TreeNode<E> rightMost = current.left;
            while(rightMost.right != null)
                rightMost = rightMost.right;

            path = path(rightMost.element); //Get path before replacement
            current.element = rightMost.element;
        }else path = path(e);
        deleteLastNodeInPath(path);
        size--;
        return true;
    }
    /* Delete the last node from the path*/
    public void deleteLastNodeInPath(ArrayList<TreeNode<E>> path){
        int i = path.size() - 1;
        // x is the last node in the path
        RBTreeNode<E> x  = (RBTreeNode<E>) (path.get(i));
        RBTreeNode<E> parentOfX = (x == root) ? null : (RBTreeNode<E>) (path.get(i-1));
        RBTreeNode<E> grandParentOfX = (parentOfX == null || parentOfX == root) ? null : (RBTreeNode<E>) (path.get(i-2));
        RBTreeNode<E> childOfX = (x.left == null) ? (RBTreeNode<E>) (x.right) : (RBTreeNode<E>) (x.left);

        // Delete node x. Connect childOfX with parentOfX
        connectNewParent(parentOfX, x, childOfX);

        //Recolor the nodes and fix double black if needed
        if( childOfX == root || x.isRed()) return;
        else if( childOfX != null && childOfX.isRed()) childOfX.setBlack();
        else fixDoubleBlack(grandParentOfX, parentOfX, childOfX, path, i);
    }
    private void fixDoubleBlack(RBTreeNode<E> grandParent, RBTreeNode<E> parent, RBTreeNode<E> db, ArrayList<TreeNode<E>> path, int i){
        // Obtain y, y1, y2
        RBTreeNode<E> y = (parent.right == db) ? (RBTreeNode<E>)(parent.left) : (RBTreeNode<E>)(parent.right);
        RBTreeNode<E> y1 = (RBTreeNode<E>)(y.left);
        RBTreeNode<E> y2 = (RBTreeNode<E>)(y.right);

        if(y.isBlack() && y1 != null && y1.isRed()){
            if(parent.right == db){
                // Case 1.1: y is a left black siblink and y1 is red
                connectNewParent(grandParent, parent, y);
                recolor(parent, y, y1);
                // adjust child links
                parent.left = y.left;
                y.right = parent;
            }else{
                // Case 1.3: y is a right black sibling and y1 is red
                connectNewParent(grandParent, parent, y1);
                recolor(parent, y1, y);

                // adjust child links
                parent.right = y1.left;
                y.left = y1.right;
                y1.left = parent;
                y1.right = y;
            }
        }
        else if(y.isBlack() && y2 != null && y2.isRed()) {
            if (parent.right == db) {
                // Case 1.2: y is a left black siblinkg and y2 is red
                connectNewParent(grandParent, parent, y2);
                recolor(parent, y2, y);

                //adjust child links
                y.right = y2.left;
                parent.left = y2.right;
                y2.left = y;
                y2.right = parent;
            } else {
                // Case 1.4: y is a right black sibling and y2 is red
                connectNewParent(grandParent, parent, y);
                recolor(parent, y, y2);

                //adjust child links
                y.left = parent;
                parent.right = y1;
            }
        }
        else if(y.isBlack()){
            // Case 2: y is black and y's children are black or null
                y.setRed();
                if(parent.isRed()) parent.setBlack();
                else if(parent != root){
                    // Propagate double black to the parent node
                    // Fix new appearance of double black recursively
                    db = parent;
                    parent = grandParent;
                    grandParent = (i >= 3) ? (RBTreeNode<E>)(path.get(i - 3)) : null;
                    fixDoubleBlack(grandParent, parent, db, path, i - 1);
                }
            }
        else{
            // y is red
            if(parent.right == db){
                // Case 3.1: y is a left red child of parent
                parent.left = y2;
                y.right = parent;
            }else{
                // Case 3.2: y is a right red child of parent
                parent.right = y.left;
                y.left = parent;
            }
            parent.setRed();
            y.setBlack();
            connectNewParent(grandParent, parent, y);
            fixDoubleBlack(y, parent, db, path, i - 1);
        }
    }
    /* Recolor Parent, newParent, and c*/
    private void recolor(RBTreeNode<E> parent, RBTreeNode<E> newParent, RBTreeNode<E> c){
        if(parent.isRed()) newParent.setRed();
        else newParent.setBlack();

        // c and parent become the children of newParent; set the black
        parent.setBlack();
        c.setBlack();
    }
    /* Connect newParent with grandParent*/
    private void connectNewParent(RBTreeNode<E> grandParent, RBTreeNode<E> parent, RBTreeNode<E> newParent){
        if(parent == root){
            root = newParent;
            if(root != null) newParent.setBlack();
        }
        else if ( grandParent.left == parent) grandParent.left = newParent;
        else grandParent.right = newParent;
    }
    @Override
    protected void preOrder(TreeNode<E> root){
        if(root == null) return;
        System.out.println(root.element + (((RBTreeNode<E>)root).isRed() ? " (red) " : " (black) "));
        preOrder(root.left);
        preOrder(root.right);
    }

    protected static class RBTreeNode<E extends Comparable<E>> extends BST.TreeNode<E>{

        private boolean red = true;
        public RBTreeNode(E data) {
            super(data);
        }
        public boolean isRed(){return red;}
        public boolean isBlack(){return !red;}
        public void setBlack(){red = false;}
        public void setRed(){red = true;}
        int blackHeight;
    }

    //Print Method Stuff
    public String treeToString(RBTreeNode<E> subtreeRoot) {
        if (subtreeRoot == null) return "(empty tree)";

        // First convert the tree to an array of line strings
        String[] lines = treeToLines(subtreeRoot);

        // Combine all lines into 1 string
        String treeString = lines[0];
        for (int i = 1; i < lines.length; i++)
            treeString += ("\n" + lines[i]);

        return treeString;
    }

    private String[] treeToLines(RBTreeNode<E> subtreeRoot) {
        if (subtreeRoot == null) return new String[0];


        // Make a string with the subtreeRoot's key enclosed in brackets
        String color = (subtreeRoot.isRed()) ? "Red" : "Black";
        String rootString = "[" + subtreeRoot.element + "](" + color + ")";
        int rootStrLen = rootString.length();

        // Case 1: subtreeRoot is a leaf
        if (subtreeRoot.left == null && subtreeRoot.right == null) {
            String[] oneLine = new String[1];
            oneLine[0] = rootString;
            return oneLine;
        }

        // Recursively make line strings for each child
        String[] leftLines = treeToLines((RBTreeNode<E>) subtreeRoot.left);
        String[] rightLines = treeToLines((RBTreeNode<E>) subtreeRoot.right);

        int lineCount = Math.max(leftLines.length, rightLines.length);
        String[] allLines = new String[lineCount + 2];

        // Case 2: subtreeRoot has no left child
        if (subtreeRoot.left == null) {
            // Create the first 2 lines, not yet indented
            allLines[0] = rootString;
            allLines[1] = getSpaces(rootStrLen) + "\\";

            // Find where the right child starts
            int rightChildIndent = rightLines[0].indexOf('[');

            // Goal: Indent lines appropriately so that the parent's right branch
            // character ('\') matches up with the right child's '['.

            if (rightChildIndent <= rootStrLen) {
                // Indent all lines below
                indentLines(rightLines, rootStrLen - rightChildIndent);
            }
            else {
                // Indent first 2 lines
                String indent = getSpaces(rightChildIndent - rootStrLen);
                allLines[0] = indent + allLines[0];
                allLines[1] = indent + allLines[1];
            }

            // Copy rightLines into allLines starting at index 2
            System.arraycopy(rightLines, 0, allLines, 2, rightLines.length);

            return allLines;
        }

        // Case 3: subtreeRoot has no right child
        if (subtreeRoot.right == null) {
            // Goal: Indent lines appropriately so that the parent's left branch
            // character ('/') matches up with the left child's ']'.

            // Create the first 2 lines
            String indent = getSpaces(leftLines[0].indexOf(']'));
            allLines[0] = indent + " " + rootString;
            allLines[1] = indent + "/";

            // Copy leftLines into allLines starting at index 2
            System.arraycopy(leftLines, 0, allLines, 2, leftLines.length);

            return allLines;
        }

        // Case 4: subtreeRoot has both a left and right child

        // The goal is to have the two child nodes as close to the parent as
        // possible without overlap on any level.

        // Compute absolute indentation, in number of spaces, needed for right lines
        int indentNeeded = 0;
        if (rightLines.length > 0) {
            // Indent should at least get the immediate right child to be to the
            // right of the root
            indentNeeded = Math.max(0,
                    leftLines[0].length() + rootString.length() - rightLines[0].indexOf('['));
        }
        for (int i = 0; i < leftLines.length && i < rightLines.length; i += 2) {
            // Lines with branches are skipped, so the line of interest has only
            // nodes. The difference between where the left line ends and the
            // right line begins should be at least 3 spaces for clarity.
            int leftEnd = leftLines[i].lastIndexOf(']');
            int rightBegin = rightLines[i].indexOf('[');

            int forThisLine = leftLines[i].length() + 3 - rightBegin;
            indentNeeded = Math.max(indentNeeded, forThisLine);
        }

        // Build final lines in allLines starting at index 2
        String absoluteIndent = getSpaces(indentNeeded);
        for (int i = 0; i < leftLines.length || i < rightLines.length; i++) {
            // If no right line, just take the left
            if (i >= rightLines.length) {
                allLines[2 + i] = leftLines[i];
            }
            else {
                String left = "";
                if (i < leftLines.length) {
                    left = leftLines[i];
                }
                String right = absoluteIndent + rightLines[i];
                allLines[2 + i] = left + right.substring(left.length());
            }
        }

        // The first 2 lines remain. allLines[2] has the proper string for the
        // 2 child nodes, and thus can be used to create branches in allLines[1].
        int leftIndex = allLines[2].indexOf(']');
        int rightIndex = allLines[2].lastIndexOf('[');
        allLines[1] = getSpaces(leftIndex) + "/" +
                getSpaces(rightIndex - leftIndex - 1) + "\\";

        // The space between leftIndex and rightIndex is the space that
        // subtreeRoot's string should occupy. If rootString is too short, put
        // underscores on the sides.
        rootStrLen = rightIndex - leftIndex - 1;
        if (rootString.length() < rootStrLen) {
            int difference = rootStrLen - rootString.length();
            String underscores = getRepeated('_', difference / 2);
            if (difference % 2 == 0) {
                rootString = underscores + rootString + underscores;
            }
            else {
                rootString = underscores + rootString + underscores + "_";
            }
        }
        allLines[0] = getSpaces(leftIndex + 1) + rootString;

        return allLines;
    }

    private String getRepeated(char toRepeat, int numberOfTimes) {
        if (numberOfTimes <= 0) return "";

        char[] chars = new char[numberOfTimes];
        for (int i = 0; i < numberOfTimes; i++)
            chars[i] = toRepeat;

        return new String(chars);
    }

    private String getSpaces(int numberOfSpaces) {return getRepeated(' ', numberOfSpaces);}

    private void indentLines(String[] lines, int numberOfSpaces) {
        if (numberOfSpaces <= 0) return;

        // Prepend indentation to each line
        String indent = getSpaces(numberOfSpaces);
        for (int i = 0; i < lines.length; i++)
            lines[i] = indent + lines[i];

    }
}

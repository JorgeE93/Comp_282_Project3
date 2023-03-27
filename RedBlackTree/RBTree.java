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

        if(x == root) u.setBlack();
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
}

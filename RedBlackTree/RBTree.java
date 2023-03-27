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

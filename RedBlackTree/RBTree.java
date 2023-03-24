package RedBlackTree;
import java.util.ArrayList;
public class RBTree<E extends Comparable<E>> extends BST<E>{
    public RBTree(){
    }

    public RBTree(E[] elements){super(elements);}

    @Override
    protected RBTreeNode<E> createNewNode(E e){return new RBTree<E>(e);}

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
    }
}

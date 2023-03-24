package RedBlackTree;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;

public class BST<E> implements Tree<E> {

    protected TreeNode<E> root;
    protected int size = 0;
    protected Comparator<E> c;

    /** Create default BST with a natural order comparator **/
    public BST(){
        this.c = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
    }
    /** Create a BST with a specified Comparator **/
    public BST(Comparator<E> c){
        this.c = c;
    }
    /** Create a BST from an array of objects **/
    public BST(E[] objects){
        this.c = (e1, e2) -> ((Comparable<E>)e1).compareTo(e2);
        for(int i = 0; i < objects.length; i++)
            add(objects[i]);
    }

    @Override
    public boolean search(E data){
        TreeNode<E> current = root;
        while(current != null){
            if(c.compare(data, current.element) < 0) current = current.left;
            else if(c.compare(data, current.element) > 0) current = current.right;
            else return true;
        }
        return false;
    }
    @Override
    public boolean insert(E data){
        if(root == null) root = createNewNode(data);
        else {
            TreeNode<E> parent = null;
            TreeNode<E> current = root;
            while (current != null) {
                if (c.compare(data, current.element) < 0) {
                    parent = current;
                    current = current.left;
                } else if (c.compare(data, current.element) > 0) {
                    parent = current;
                    current = current.right;
                } else return false;
            }//end while loop
            if (c.compare(data, parent.element) < 0)
                parent.left = createNewNode(data);
            else
                parent.right = createNewNode(data);
        }
        size++;
        return true;
    }
    protected TreeNode<E> createNewNode(E data){ return new TreeNode<>(data);}

    @Override
    public void inOrder(){inOrder(root);}
    protected void inOrder(TreeNode<E> root){
        if(root == null) return;
        inOrder(root.left);
        System.out.print(root.element + " ");
        inOrder(root.right);
    }
    @Override
    public void postOrder(){postOrder(root);}
    protected void postOrder(TreeNode<E> root){
        if(root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.element + " ");
    }
    public void preOrder(){preOrder(root);}
    protected void preOrder(TreeNode<E> root){
        if(root == null) return;
        System.out.print(root.element + " ");
        preOrder(root.left);
        preOrder(root.right);
    }
    /** This inner class is static, because it does not access
     *  any instance members defined in its outer class
     *  This can also prevent multiple BST instantces to access
     *  this inner class **/
    public static class TreeNode<E> {
        protected E element;
        protected TreeNode<E> left;
        protected TreeNode<E> right;
        public TreeNode(E data){element = data;}
    }

    @Override
    public int getSize(){return size;}
    public TreeNode<E> getRoot(){return root;}

    /** Returns a path from the root leading to the specified element **/
    public ArrayList<TreeNode<E>> path(E data){
        ArrayList<TreeNode<E>> list = new ArrayList<>();
        TreeNode<E> current = root;
        while(current != null){
            list.add(current);
            if(c.compare(data, current.element) < 0) current = current.left;
            else if(c.compare(data, current.element) > 0) current = current.right;
            else break;
        }//End While loop
        return list;
    }

    /** Delete an element from the Binary Tree. Return True if deleted
     *  Return False if the element is not in the tree. **/
    public boolean delete(E data){
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while(current != null){
            if(c.compare(data, current.element) < 0){
                parent = current;
                current = current.left;
            }else if(c.compare(data, current.element) > 0){
                parent = current;
                current = current.right;
            }else break;
        }//End While loop
        if(current == null) return false;

        /** This will be Overriden in AVLTree **/
        // Case 1: current has no left child
        if(current.left == null){
            // Connect the parent with the right child of the current node
            // If the Parent is NULL then we must be in the root node.
            if(parent == null) root = current.right;
            else{
                // If less than Parent element, its a left child
                if(c.compare(data, parent.element) < 0) parent.left = current.right;
                else parent.right = current.right;
            }
        }//End Case 1
        // Case 2: Current node has left child. Locate the right most node,
        // in the left subtree of the current node and also its parent
        else{
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;
            while(rightMost != null){
                parentOfRightMost = rightMost;
                rightMost = rightMost.right;
            }// End While loop
            // Replace the element in current by the element in the rightMost
            current.element = rightMost.element;

            //Eliminate rightMost node
            if(parentOfRightMost.right == rightMost) parentOfRightMost.right = rightMost.left;
            else parentOfRightMost.left = rightMost.left;
        }// End Case 2
        size--;
        return true;
    }

    @Override
    public Iterator<E> iterator(){return new InorderIterator();}
    private class InorderIterator implements Iterator<E>{
        private ArrayList<E> list = new ArrayList<>();
        private int current = 0;
        public InorderIterator(){inOrder();}
        private void inOrder(){inOrder(root);}
        private void inOrder(TreeNode<E> root){
            if(root == null) return;
            inOrder(root.left);
            list.add(root.element);
            inOrder(root.right);
        }
        @Override
        public boolean hasNext(){
            if(current < list.size())return true;

            return false;
        }
        @Override
        public E next(){return list.get(current++);}
        @Override
        public void remove(){
            if(current == 0) throw new IllegalStateException();
            delete(list.get(--current));
            list.clear();
            inOrder();
        }
    }//End Inner Class

    @Override
    public void clear(){
        root = null;
        size = 0;
    }

}

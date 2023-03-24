package RedBlackTree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Tree<E> extends Collection<E> {

    public boolean search(E e);
    public boolean insert(E e);
    public boolean delete(E e);
    public int getSize();
    public default void inOrder(){}
    public default void postOrder(){}
    public default void preOrder(){}
    @Override
    public default boolean isEmpty(){return size()==0;}
    @Override
    public default boolean contains(Object e){return search((E)e);}
    @Override
    public default boolean add(E e){return insert(e);}
    @Override
    public default boolean remove(Object e){return delete((E)e);}
    @Override
    public default int size(){return getSize();}
    @Override
    public default boolean containsAll(Collection<?> c){
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }
    @Override
    public default boolean addAll(Collection<? extends E> c){
        for(Object e: c)
            add((E)e);
        return true;
    }
    @Override
    public default boolean removeAll(Collection<?> c){
        for(Object e: c)
            remove(e);
        return true;
    }
    @Override
    public default boolean retainAll (Collection<?> c){
        for(Object e: c)
            if(!contains(e)) remove(e);
        return true;
    }
    @Override
    public default Object[] toArray(){
        List<E> list = new ArrayList<E>(size());
        for (E e : this)
            list.add(e);
        return list.toArray();
    }

    @Override
    public default <T> T[] toArray(T[] array){
        List<E> list = new ArrayList<E>(size());
        for (E e : this)
            list.add(e);
        return list.toArray(array);
    }

}
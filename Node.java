
import java.io.Serializable;
public class Node<E> implements Serializable{
    private E data;
    private Node<E> next, prev;
    public Node(E data){
        this.data = data;
        next = null;
        prev = null;
    }
    public E get(){
        return data;
    }
    public void set(E e){
        data = e;
    }
    public Node<E> next(){
        return next;
    }
    public Node<E> prev(){
        return prev;
    }
    public String toString() {
        return data.toString();
    }
    public void setNext(Node<E> newNext){
        next = newNext;
    }
    public void setPrev(Node<E> newPrev){
        prev = newPrev;
    }
}
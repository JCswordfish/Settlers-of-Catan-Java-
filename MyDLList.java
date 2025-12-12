
import java.io.Serializable;
public class MyDLList<E> implements Serializable{
    //Instance Variables
    private Node<E> head;
    private Node<E> tail;
    private int size;
    //Constructor
    public MyDLList() {
        head = new Node<E>(null); 
        tail = new Node<E>(null);
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }  
    //Methods
    public boolean add(E e) {
        size++;
        Node<E> newNode = new Node<E>(e);
        newNode.setPrev(tail.prev());
        tail.prev().setNext(newNode);
        newNode.setNext(tail);
        tail.setPrev(newNode);
        return true;
    } 
    public void add(int index,E e) { 
        size++;
        Node<E> newNode = new Node<E>(e);
        Node<E> before = getNode(index).prev(); 
        Node<E> after = before.next();  
        before.setNext(newNode);
        newNode.setPrev(before);
        after.setPrev(newNode); 
        newNode.setNext(after);

    } 
    public E get(int index) {
        return getNode(index).get();
    }

    public E remove(int index) {
        Node<E> tempNode = getNode(index);
        Node<E> before = tempNode.prev();
        Node<E> after = tempNode.next();
        before.setNext(after); 
        after.setPrev(before);
        size--;
        return (E)(tempNode.get());
        
    } 

    public boolean remove(E e){
        Node<E> tempNode = head.next();
        while(tempNode.get() != null){
            if((tempNode.get().toString()).equals(e.toString())){
                Node<E> before = tempNode.prev();
                Node<E> after = tempNode.next();
                before.setNext(after); 
                after.setPrev(before);
                size--;
                return true;
            }
            tempNode = tempNode.next();
        }
        return false;
    }

    public int size() {
        return size;
    }
    public E set(int index,E e) {
        Node<E> tempNode = getNode(index);
        E tempObj = (E)(tempNode.get());
        tempNode.set(e);
        return tempObj; 
    }
    public String toString() {
        Node<E> tempNode = head.next();
        String result = "";
        while (tempNode.get()!=null) {
            result += tempNode.get() + " ";
            tempNode = tempNode.next();
        }
        return result;
    } 
    private Node<E> getNode(int index) {
        Node<E> tempNode;
        if(index <= size/2){
            tempNode = head.next(); 
            // do we not need to do index-1?
            for (int i=0; i<index; i++) {
                tempNode = tempNode.next();
            }
        } else {
            tempNode = tail.prev(); // 
            for (int i=size-1; i > index; i--) { // if last 
                tempNode = tempNode.prev();
            }
        }
        return tempNode; 
    } 
}




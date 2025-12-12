
import java.io.Serializable;

public class MyArrayList<E> implements Serializable{
    private Object[] list;
    private int size;
    public MyArrayList(){
        list = new Object[0];
        size = 0;
    }


    //Methods
    public boolean add(E e) {
        size += 1;
        Object[] list2 = new Object[size];
        for(int i = 0; i < size-1; i++){
            list2[i] = list[i];
        }
        list2[size-1] = e;
        list = list2;
        return true;
    } 
    public boolean add(int index, E e) {
        size += 1;
        Object[] list2 = new Object[size];
        int stagger = 0;
        for(int i = 0; i < size-1; i++){
            if(i == index){
                stagger = 1;
            }
            list2[i+stagger] = list[i];
        }
        list2[index] = e;
        list = list2;
        return true;
    } 
    public E get(int index){
        return (E)list[index];
    }
    public String toString() {
        String words="";
        for(int i = 0; i < size; i++){
            words +=list[i] + "\n";
        }
        return words;
    }
    public int size() {
        return size;
    }
    public void set(int i, E e) {
        list[i] = e;
    }
    public E remove(int a) {
        size -= 1;
        Object[] list2 = new Object[size];
        for(int i = 0; i < size; i++){
            if (i<a) {
                list2[i] = list[i];
            }
            if (i>=a) {
                list2[i] = list[i+1];
            }
        }
        Object removed = list[a];
        list = list2;
        return (E)removed;
    }
    public E remove(E e) {
        Object removed = null;
        for(int i = 0; i < size; i++){
            if (list[i].equals(e)) {
                removed = remove(i);
                break;
            }
        } 
        return (E)removed; 
    } 
    public boolean contains(E e){
        for(int i = 0; i < size; i++){
            if (list[i].equals(e)) {
                return true;
            }
        } 
        return false;
    }

}
package e.pmart.project;

import java.util.ArrayList;

public class MyArrayList<E> extends ArrayList<E> {
    public MyArrayList() {
        super();
    }

    public E getLast() {
        return get(size()-1);
    }

    public void setLast(E object) {
        set(size()-1, object);
    }

    public void removeLast() {
        remove(size()-1);
    }
    
    public String toText() {
        if (getLast().getClass().equals("".getClass())) {
            String a = "";
            for (int i = 0; i < size(); i++) {
                a += (String) get(i);
            }
            return a;
        } else throw new RuntimeException("method work only with strings!");

    }
}
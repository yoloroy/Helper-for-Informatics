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
    
    public String toText(int num_syst) {
        if (getLast().getClass().equals("".getClass())) {
            // unchecked cast checked above
            /*if(this.clone() instanceof MyArrayList) {
                MyArrayList<String> fixed_this = (MyArrayList) this.clone();
            }*/
            MyArrayList<String> fixed_this = (MyArrayList<String>) this.clone();
            while (fixed_this.contains(".")) {
                try {
                    fixed_this.set(fixed_this.indexOf(".") - 1,
                                  get(fixed_this.indexOf(".") - 1) +
                            "." + get(fixed_this.indexOf(".") + 1));
                    fixed_this.remove(fixed_this.indexOf(".") + 1);
                    fixed_this.remove(fixed_this.indexOf("."));
                } catch (NumberFormatException nfe) {
                    // pass
                } catch (IndexOutOfBoundsException eoobe) {
                    fixed_this.set(fixed_this.indexOf(".") - 1,
                            get(fixed_this.indexOf(".") - 1)+"");
                    fixed_this.remove(fixed_this.indexOf("."));
                }
            }

            String a = "";
            for (int i = 0; i < fixed_this.size(); i++) {
                try {
                    a += ToNumSystem.run(Double.parseDouble(fixed_this.get(i)),
                                         num_syst);
                } catch (NumberFormatException nfe) {
                    a += fixed_this.get(i);
                }
            }
            return a;
        } else throw new RuntimeException("method work only with strings!");
    }

    public String toText() {
        try {
            if (getLast().getClass().equals("".getClass())) {
                String a = "";
                for (E i : this) {
                    a += i;
                }
                return a;
            } else throw new RuntimeException("method work only with strings!");
        } catch (ArrayIndexOutOfBoundsException e) { return ""; }
    }
}

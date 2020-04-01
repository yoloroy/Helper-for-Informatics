package e.pmart.project.solvers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Resh13 {
    private static Formulas formulas = new Formulas();
    private ArrayList<String> path = new ArrayList<>();
    private String solution;
    private Map<String, Double> given_val = new HashMap<>();
    private String find;
    public Double answer = Double.NaN;

    public void main(double units) {
        solution = "Решение:";

        try {
            answer = main_func(given_val, find) / units;
        } catch (ArithmeticException e) {
            answer = Double.NaN;
        }
    }

    private double main_func(Map<String, Double> given_val, String find) {
        Set<String> given = given_val.keySet();
        if (given.contains(find))
            return given_val.get(find);
        boolean rez;

        for (String i : formulas.getKeys(find)) {
            if (!(path.contains(i))) {
                rez = SetFuncs.containsAll(SetFuncs.intersection(given, strToSetStr(i)), strToSetStr(i)) &&  // if (set(list(i)) & given) == set(list(i)):
                        SetFuncs.containsAll(strToSetStr(i), SetFuncs.intersection(given, strToSetStr(i)));
                if (rez) {
                    this.solution += "\n" + find + " = " + formulas.getDesc(find, i);

                    String pub = formulas.getDesc(find, i);
                    for (String v : given) {
                        if (i.contains(v))
                            pub = pub.replace(v, StripInt(given_val.get(v)));
                    }
                    this.solution += "\n" + find + " = " + pub;
                    this.solution += "\n" + find + " = " + StripInt(formulas.exec(find, i, given_val));

                    return formulas.exec(find, i, given_val);
                }
                for (char j : i.toCharArray()) {
                    if (!(given.contains(String.valueOf(j)))) {
                        path.add(i);
                        double helper_value;
                        try {
                            helper_value = main_func(given_val, String.valueOf(j));
                        } catch (ArithmeticException e) {
                            break;
                        }
                        //Log.i(TAG, "main_func: " + String.valueOf(j));
                        given_val.put(String.valueOf(j), helper_value);
                        given = given_val.keySet();
                    }
                }
                rez = SetFuncs.containsAll(SetFuncs.intersection(given, strToSetStr(i)), strToSetStr(i)) &&
                        SetFuncs.containsAll(strToSetStr(i), SetFuncs.intersection(given, strToSetStr(i)));
                if (rez) {
                    this.solution += "\n" + find + " = " + formulas.getDesc(find, i);

                    String pub = formulas.getDesc(find, i);
                    for (String v : given) {
                        if (i.contains(v))
                            pub = pub.replace(v, StripInt(given_val.get(v)));
                    }
                    this.solution += "\n" + find + " = " + pub;
                    this.solution += "\n" + find + " = " + StripInt(formulas.exec(find, i, given_val));

                    return formulas.exec(find, i, given_val);
                }
            }
        }
        throw new ArithmeticException();
    }

    private HashSet<String> strToSetStr(String str) {
        HashSet<String> hvi = new HashSet<String>();
        for (char g : str.toCharArray())
            hvi.add(String.valueOf(g));
        return hvi;
    }

    public static String StripInt(double num) {
        String striped_num;
        if (num == (int)num) {
            striped_num = String.valueOf((int)num);
        } else {
            striped_num = String.valueOf(num);
        }

        return striped_num;
    }

    public String getSolution() { return solution; }

    public void setValues(Map<String, Double> given_val_, String find_) {
        //Log.i(TAG, "setValues");
        given_val = given_val_;
        find = find_;
    }
}

class SetFuncs {
    static Set<String> intersection (Set<String>set1, Set<String>set2) {
        HashSet<String> d = new HashSet<>(set1);
        d.retainAll(set2);
        return d;
    }
    @NonNull
    static Boolean containsAll (Set<String>set1, Set<String>set2) {
        for (String i : set2) {
            if (!(set1.contains(i)))
                return false;
        }
        return true;
    }
}


class Formulas {
    String getDesc (String var, String path) {
        switch (var) {
            case "i":
                switch (path) {
                    case "N":
                        return "[log2(N)]";
                    case "KI":
                        return "I / K" ;
                }
            case "I":
                switch (path) {
                    case "Ki":
                        return "K * i";
                }
            case "K":
                switch (path) {
                    case "Ii":
                        return "I / i";
                }
            case "N":
                switch (path) {
                    case "i":
                        return "2^i";
                }
        }
        return "None";
    }

    String getName (String var, String path) {
        switch (var) {
            case "i":
                switch (path) {
                    case "N":  // [log2(N)]
                        return "i1";
                    case "KI":  // I / K
                        return "I" ;
                }
            case "I":
                switch (path) {
                    case "Ki":  // K * i
                        return "I";
                }
            case "K":
                switch (path) {
                    case "Ii":  // I / i
                        return "I";
                }
            case "N":
                switch (path) {
                    case "i":  // 2^i
                        return "i1";
                }
        }
        return "None";
    }

    Double exec (String var, String path, Map<String,Double> given_val) {
        switch (var) {
            case "i":
                switch (path) {
                    case "N":
                        return Math.ceil(Math.log10(given_val.get("N")) / Math.log10(2.));
                    case "KI":
                        return given_val.get("I") / given_val.get("K");
                }
            case "I":
                switch (path) {
                    case "Ki":
                        return given_val.get("K") * given_val.get("i");
                }
            case "K":
                switch (path) {
                    case "Ii":
                        return given_val.get("I") / given_val.get("i");
                }
            case "N":
                switch (path) {
                    case "i":
                        return Math.pow(2.0, given_val.get("i"));
                }
        }
        return -1.;
    }

    Set<String> getKeys (String var) {
        HashSet<String> ret = new HashSet<String>();
        switch (var) {
            case "i":
                ret.add("N");
                ret.add("KI");
                return ret;
            case "I":
                ret.add("Ki");
                return ret;
            case "K":
                ret.add("Ii");
                return ret;
            case "N":
                ret.add("i");
                return ret;
        }
        return ret;
    }
}
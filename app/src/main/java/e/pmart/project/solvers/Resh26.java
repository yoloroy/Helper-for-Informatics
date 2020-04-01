package e.pmart.project.solvers;


import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resh26 {
    private String[] steps;
    private String[] end;


    public Map<String, Object> call(Object[] start_pos,
                             String[] end,
                             String[] steps) {
        this.end = end;
        this.steps = steps;

        return gen_tree(start_pos);
    }


    private Map<String, Object> gen_tree(Object[] arr, int phase) {
        Map<String, Object> temp = new HashMap<>();
        List<Map<String, Object>> deep_trees = new ArrayList<>();

        for (Object[] e: enum_evaluate(arr)) {
            deep_trees.add(gen_tree(e, 1 - phase));
        }

        if (deep_trees.size() == 0) {
            temp.put(toPoint(arr), phase);
            return temp;
        }
        temp.put(toPoint(arr), new HashMap<String, Object>());
        for (Map<String, Object> i: deep_trees) {
            ((Map) temp.get(toPoint(arr))).putAll(i);
        }
        return temp;
    }
    private Map<String, Object> gen_tree(Object[] arr) {
        return gen_tree(arr, 0);
    }

    private List<Object[]> enum_evaluate(Object[] arr) {
        List<Object[]> temp = new ArrayList<>();
        Object[] temp_arr = arr.clone();
        Object[] temp_temp_arr;
        List<Object> arr_list = Arrays.asList(arr);

        for (String i: steps) {
            for (String e: end) {
                if (EndResh26.run((Integer[]) arr, e))
                    return new ArrayList<>();
            }
            if (i.contains("*") && arr_list.contains(0))
                continue;

            temp_arr = StepResh26.run((Integer[]) arr, i);
            if (i.contains("ONE")) {
                for (int j = 0; j < arr.length; j++) {
                    temp_temp_arr = arr.clone();
                    temp_temp_arr[j] = temp_arr[j];
                    temp.add(temp_temp_arr);
                }
            } else
                temp.add(temp_arr);
        }
        return temp;
    }

    public String calculate_winner(Map<String, Object> tree, String[] players) {
        if (tree_logic_and(tree))
            return players[0];
        return players[1];
    }

    private Boolean tree_logic_and(Map<String, Object> tree) {
        List<Boolean> temp = new ArrayList<>();
        for (String i: tree.keySet()) {
            try {
                temp.add(tree_logic_or((Map<String, Object>) tree.get(i)));
            } catch (ClassCastException cce) {
                temp.add(tree_logic_or((Integer) tree.get(i)));
            }
        }
        return all(temp);
    }
    private Boolean tree_logic_and(Integer arg) {
        return arg == 1;
    }

    private Boolean tree_logic_or(Map<String, Object> tree) {
        List<Boolean> temp = new ArrayList<>();
        for (String i: tree.keySet()) {
            try {
                temp.add(tree_logic_and((Map<String, Object>) tree.get(i)));
            } catch (ClassCastException cce) {
                temp.add(tree_logic_and((Integer) tree.get(i)));
            }

        }
        return any(temp);
    }
    private Boolean tree_logic_or(Integer arg) {
        return arg == 1;
    }

    private static Boolean all(Collection<Boolean> bools) {
        for (Boolean bool: bools) {
            if (!bool)
                return false;
        }
        return true;
    }
    private static Boolean any(Collection<Boolean> bools) {
        for (Boolean bool: bools) {
            if (bool)
                return true;
        }
        return false;
    }

    private static String toPoint(Object[] arr) {
        String temp = "(";
        for (Object i: arr) {
            temp += i.toString() + ", ";
        }
        return temp.substring(0, temp.length()-2) + ")";
    }
}

class StepResh26 {
    static Integer[] run(Integer[] arr, String expression) {
        Integer[] temp_arr = arr.clone();
        Function f = new Function("stepReshTS(x) = " +
                expression.replace("ONE", "x")
                        .replace("ALL", "x"));
        Expression e = new Expression(f);

        for (int i = 0; i < arr.length; i++) {
            e.setExpressionString("stepReshTS("+temp_arr[i]+")");
            temp_arr[i] = (int) e.calculate();
        }
        return temp_arr;
    }
}

class EndResh26 {
    static Boolean run(Integer[] arr, String expression) {
        if (expression.contains("ANY")) {
            for (Integer item: arr) {
                if (new Expression(expression
                        .replace("ANY", String.valueOf(item)))
                        .calculate() == 1)
                    return true;
            }
            return false;
        } else if (expression.contains("ALL")) {
            for (Integer item: arr) {
                if (new Expression(expression
                        .replace("ALL", String.valueOf(item)))
                        .calculate() != 1)
                    return false;
            }
            return true;
        } else if (expression.contains("SUM")) {
            int sum = 0;
            for (Integer item: arr) {
                sum += item;
            }
            return new Expression(expression
                    .replace("SUM", String.valueOf(sum)))
                    .calculate() == 1;
        }

        return false;
    }
}

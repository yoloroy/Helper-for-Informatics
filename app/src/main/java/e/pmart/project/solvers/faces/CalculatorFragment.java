package e.pmart.project.solvers.faces;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.mariuszgromada.math.mxparser.Expression;

import e.pmart.project.ExtraCalcFuncs;
import e.pmart.project.MyArrayList;
import e.pmart.project.R;
import e.pmart.project.ToNumSystem;
import e.pmart.project.solvers.Resh13;


public class CalculatorFragment extends Fragment {
    View root_view;
    MyArrayList<String> calc_text = new MyArrayList<>();


    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_calculator, container, false);

        Integer[] calc_num_systems = {16, 10, 8, 2};
        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setAdapter(new ArrayAdapter<Integer>(getContext(),
                        R.layout.my_simple_dropdown_spinner_item,
                        calc_num_systems));

        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setSelection(1);

        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        calc_onClickInstantEvaluate(view);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        return root_view;
    }

    @Override
    public void onStart() {
        calc_text.add("0");
        for (int i = 1; i < ((GridLayout) root_view.findViewById(R.id.calcButtons)).getChildCount(); i++)
            ((GridLayout) root_view.findViewById(R.id.calcButtons)).getChildAt(i).setOnClickListener(this::calc_onClickButton);
        root_view.findViewById(R.id.calc_evaluate).setOnClickListener(this::calc_onClickButton);

        super.onStart();
    }

    public void calc_onClickEvaluate(View view) {
        Expression e = new Expression(new ExtraCalcFuncs().getExtraCalcFuncs());

        MyArrayList<String> calc_text_copy = (MyArrayList<String>) calc_text.clone();

        int div_i;
        while (calc_text_copy.contains(" div ")) {
            div_i = calc_text_copy.indexOf(" div ");
            try {
                calc_text_copy.set(div_i-1, String.valueOf(Math.floor(
                        Double.valueOf(calc_text_copy.get(div_i-1)) /
                                Double.valueOf(calc_text_copy.get(div_i+1)))));
                calc_text_copy.remove(div_i); calc_text_copy.remove(div_i);
            } catch (Exception ex) {
                break;
            }
        }

        e.setExpressionString(calc_text_copy.toText()
                .replace("mod", "#")
                .replace("log", "my_log")
                .replace("not", "bnot")
                .replace("and", "@&")
                .replace("or", "@|")
                .replace("0(", "0*(")
                .replace("1(", "1*(")
                .replace("2(", "2*(")
                .replace("3(", "3*(")
                .replace("4(", "4*(")
                .replace("5(", "5*(")
                .replace("6(", "6*(")
                .replace("7(", "7*(")
                .replace("8(", "8*(")
                .replace("9(", "9*(")
                .replace("my_log2*", "my_log2"));

        ((TextView) root_view.findViewById(R.id.calc_enter))
                .setText(ToNumSystem.run(e.calculate(), 10));
        calc_text.clear();
        calc_text.add(ToNumSystem.run(e.calculate(), 10));
    }

    public void calc_onClickInstantEvaluate(View view) {
        Expression e = new Expression(new ExtraCalcFuncs().getExtraCalcFuncs());

        MyArrayList<String> calc_text_copy = (MyArrayList<String>) calc_text.clone();

        int div_i;
        while (calc_text_copy.contains(" div ")) {
            div_i = calc_text_copy.indexOf(" div ");
            try {
                calc_text_copy.set(div_i-1, String.valueOf(Math.floor(
                        Double.valueOf(calc_text_copy.get(div_i-1)) /
                                Double.valueOf(calc_text_copy.get(div_i+1)))));
                calc_text_copy.remove(div_i); calc_text_copy.remove(div_i);
            } catch (Exception ex) {
                break;
            }
        }

        e.setExpressionString(calc_text_copy.toText()
                .replace("mod", "#")
                .replace("log", "my_log")
                .replace("not", "bnot")
                .replace("and", "@&")
                .replace("or", "@|")
                .replace("0(", "0*(")
                .replace("1(", "1*(")
                .replace("2(", "2*(")
                .replace("3(", "3*(")
                .replace("4(", "4*(")
                .replace("5(", "5*(")
                .replace("6(", "6*(")
                .replace("7(", "7*(")
                .replace("8(", "8*(")
                .replace("9(", "9*(")
                .replace("my_log2*", "my_log2"));

        if ((int)((Spinner) root_view.findViewById(R.id.calc_num_system_spinner)).getSelectedItem() != 10)
            ((TextView) root_view.findViewById(R.id.calc_preview))
                    .setText(calc_text_copy.toText((int)((Spinner) root_view.findViewById(R.id.calc_num_system_spinner)).getSelectedItem()));
        else
            ((TextView) root_view.findViewById(R.id.calc_preview)).setText("");

        if (!Resh13.StripInt(e.calculate()).equals("NaN"))
            ((TextView) root_view.findViewById(R.id.calc_answer))
                    .setText(" = " + ToNumSystem.run(e.calculate(),
                            (int)((Spinner) root_view.findViewById(R.id.calc_num_system_spinner)).getSelectedItem()));
    }

    public void calc_onClickButton(View view) {
        String text = (String) ((TextView) root_view.findViewById(R.id.calc_enter)).getText();
        switch (view.getId()) {
            case (R.id.calc_evaluate):
            case (R.id.calc_eval):
                calc_onClickEvaluate(view);
                return;
            case (R.id.calc_backspace):
                if ("0123456789".contains(String.valueOf(calc_text.getLast().charAt(0)))) {
                    if (calc_text.getLast().length() > 2 ||
                            !(calc_text.getLast().length() == 2
                                    == calc_text.getLast().startsWith("-"))) {
                        calc_text.setLast(calc_text.getLast()
                                .substring(0, calc_text.getLast().length() - 1));
                    } else {
                        calc_text.removeLast();
                    }
                } else {
                    calc_text.removeLast();
                }
                if (calc_text.size() == 0)
                    calc_text.add("0");
                break;
            case (R.id.calc_clear):
                calc_text.clear();
                calc_text.add("0");
                break;
            default:
                if ("0123456789".contains(((Button) view).getText())) {
                    if (calc_text.getLast().equals("0") && calc_text.size() == 1) {
                        calc_text.setLast((String) ((Button) view).getText());
                    } else if ("0123456789".contains(String.valueOf(calc_text.getLast().charAt(0))) ||
                            (calc_text.getLast().charAt(0) == '-' &&
                                    "0123456789".contains(String.valueOf(calc_text.getLast().charAt(1))))) {
                        calc_text.setLast(calc_text.getLast() + ((Button) view).getText());
                    } else {
                        if (calc_text.size() == 2) {  // for assert
                            if (calc_text.getLast().equals(" - ") && calc_text.get(0).equals("0")) {
                                calc_text.clear();
                                calc_text.add("-" + ((Button) view).getText());
                            } else
                                calc_text.add((String) ((Button) view).getText());
                        } else
                            calc_text.add((String) ((Button) view).getText());
                    }
                } else {
                    if (((String) ((Button) view).getText()).contains("log2"))
                        calc_text.add(((String) ((Button) view).getText())
                                .replace("log2", "log2("));
                    else
                        calc_text.add(((String) ((Button) view).getText())
                                .replace("not", "not(")
                                .replace("log", "log(")
                                .replace("!", "! "));
                }
        }
        ((TextView) root_view.findViewById(R.id.calc_enter))
                .setText(calc_text.toText());

        calc_onClickInstantEvaluate(view);
    }
}

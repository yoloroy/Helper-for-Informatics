package e.pmart.project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class Resh13Fragment extends Fragment {
    View root_view;

    FieldOfСondition variable1;
    FieldOfСondition variable2;
    FieldOfСondition variable3;
    FieldOfСondition variable4;

    TextView textView;
    ScrollView scrollView;
    QuadButton run;

    String[] variableData;  // variants of variables in task

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_resh13, container, false);

        textView = root_view.findViewById(R.id.resh13_answer);
        scrollView = root_view.findViewById(R.id.scrollView2);
        run = root_view.findViewById(R.id.run13);

        variableData = createData();
        createSpinnersEnvironment();

        return root_view;
    }

    @Override
    public void onStart() {
        getView().findViewById(R.id.run13).setOnClickListener(this::onClickStart);

        super.onStart();
    }

    // helper functions
    private String[] createData() {
        String[] data = new String[5];

        data[0] = " ";
        data[1] = "I";
        data[2] = "i";
        data[3] = "N";
        data[4] = "K";

        return data;
    }
    private void createSpinnersEnvironment() {
        variable1 = new FieldOfСondition(root_view.findViewById(R.id.spinner1),
                root_view.findViewById(R.id.spinner_measure_units1),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText1));

        variable2 = new FieldOfСondition(root_view.findViewById(R.id.spinner2),
                root_view.findViewById(R.id.spinner_measure_units2),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText2));

        variable3 = new FieldOfСondition(root_view.findViewById(R.id.spinner3),
                root_view.findViewById(R.id.spinner_measure_units3),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText3));

        variable4 = new FieldOfСondition(root_view.findViewById(R.id.spinner4),
                root_view.findViewById(R.id.spinner_measure_units4),
                getContext(), variableData, android.R.layout.simple_spinner_item);
    }

    public void onClickStart(View view) {
        if (textView.getVisibility() == View.GONE) {
            Map<String, Double> given_val = new HashMap<>();

            if (!variable1.getSelectedItem().toString().equals(" "))
                given_val.put(variable1.getSelectedItem().toString(),
                        variable1.getValue());
            if (!variable2.getSelectedItem().toString().equals(" "))
                given_val.put(variable2.getSelectedItem().toString(),
                        variable2.getValue());
            if (!variable3.getSelectedItem().toString().equals(" "))
                given_val.put(variable3.getSelectedItem().toString(),
                        variable3.getValue());

            MyProgram zee = new MyProgram();
            zee.setValues(given_val, variable4.getSelectedItem().toString());
            zee.main(variable4.getUnits());
            String y = zee.getSolution();

            if (zee.answer.isNaN())
                textView.setText(y + "\nОтвет не может быть получен из-за неверных входных данных");
            else
                textView.setText(y +
                        "\n\nОтвет: " +
                        MyProgram.StripInt(zee.answer / variable4.getUnits()));
            textView.setVisibility(View.VISIBLE);

            run.setRotation(180.0f);
        }
        else {
            textView.setVisibility(View.GONE);

            run.setRotation(0.0f);
        }
    }
}
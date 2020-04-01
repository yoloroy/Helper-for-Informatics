package e.pmart.project.solvers.faces;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import e.pmart.project.FieldOfCondition;
import e.pmart.project.QuadButton;
import e.pmart.project.R;
import e.pmart.project.solvers.Resh13;


public class Resh13Fragment extends Fragment {
    View root_view;

    FieldOfCondition variable1;
    FieldOfCondition variable2;
    FieldOfCondition variable3;
    FieldOfCondition variable4;

    TextView textView;
    ScrollView scrollView;
    QuadButton run;

    String[] variableData;  // variants of variables in task

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        variable1 = new FieldOfCondition(root_view.findViewById(R.id.spinner1),
                root_view.findViewById(R.id.spinner_measure_units1),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText1));

        variable2 = new FieldOfCondition(root_view.findViewById(R.id.spinner2),
                root_view.findViewById(R.id.spinner_measure_units2),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText2));

        variable3 = new FieldOfCondition(root_view.findViewById(R.id.spinner3),
                root_view.findViewById(R.id.spinner_measure_units3),
                getContext(), variableData, android.R.layout.simple_spinner_item,
                root_view.findViewById(R.id.editText3));

        variable4 = new FieldOfCondition(root_view.findViewById(R.id.spinner4),
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

            Resh13 zee = new Resh13();
            zee.setValues(given_val, variable4.getSelectedItem().toString());
            zee.main(variable4.getUnits());
            String y = zee.getSolution();

            if (zee.answer.isNaN())
                textView.setText(String.format("%s\nОтвет не может быть получен из-за неверных входных данных", y));
            else
                textView.setText(String.format(
                        "%s\n\nОтвет: %s", y,
                        Resh13.StripInt(zee.answer / variable4.getUnits())));
            textView.setVisibility(View.VISIBLE);

            run.setRotation(180.0f);
        }
        else {
            textView.setVisibility(View.GONE);

            run.setRotation(0.0f);
        }
    }
}
package e.pmart.project;


import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.HashMap;


class FieldOfСondition {
    @SuppressWarnings("FieldCanBeLocal")
    private Spinner spinner;
    private Spinner unitsSpinner;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> unitsAdapter;
    @SuppressWarnings("FieldCanBeLocal")
    private EditText field;
    @SuppressWarnings("FieldCanBeLocal")
    private Spannable[] data_plus_description;
    @SuppressWarnings("FieldCanBeLocal")
    private String[] data;
    @SuppressWarnings("FieldCanBeLocal")
    private int simple_spinner_item;
    private HashMap<String, Double[]> unitsData;  // variants of units of measurement in the received data
    private HashMap<String, String[]> unitsDataNames;

    FieldOfСondition(View spinnerID, View unitsSpinnerID, Context context, String[] data_,
                     int simple_spinner_item_) {
        simple_spinner_item = simple_spinner_item_;
        data = data_;
        unitsData = createUnitsData();
        unitsDataNames = createUnitsNamesData();

        unitsSpinner = (Spinner) unitsSpinnerID;
        unitsAdapter = new ArrayAdapter<>(context, simple_spinner_item, unitsDataNames.get(" "));
        unitsSpinner.setAdapter(unitsAdapter);

        spinner = (Spinner) spinnerID;
        adapter = new ArrayAdapter<>(context, simple_spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitsAdapter = new ArrayAdapter<>(unitsAdapter.getContext(), simple_spinner_item,
                        unitsDataNames.get(data[i]));
                unitsSpinner.setAdapter(unitsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    FieldOfСondition(View spinnerID, View unitsSpinnerID, Context context, String[] data_,
                     int simple_spinner_item_, View field_) {
        simple_spinner_item = simple_spinner_item_;
        data = data_;
        unitsData = createUnitsData();
        unitsDataNames = createUnitsNamesData();

        unitsSpinner = (Spinner) unitsSpinnerID;
        unitsAdapter = new ArrayAdapter<>(context, simple_spinner_item, unitsDataNames.get(" "));
        unitsSpinner.setAdapter(unitsAdapter);

        spinner = (Spinner) spinnerID;
        adapter = new ArrayAdapter<>(context, simple_spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitsAdapter = new ArrayAdapter<>(unitsAdapter.getContext(), simple_spinner_item,
                        unitsDataNames.get(data[i]));
                unitsSpinner.setAdapter(unitsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        field = (EditText) field_;
    }
    private HashMap<String, Double[]> createUnitsData() {
        HashMap<String, Double[]> data = new HashMap<>();

        data.put(" ", new Double[]{1.0});
        data.put("i", new Double[]{1.0});
        data.put("N", new Double[]{1.0});
        data.put("K", new Double[]{1.0});
        //                         бит  Байт КБ          МБ                 ГБ
        data.put("I", new Double[]{1.0, 8.0, 1024.0*8.0, 1024.0*1024.0*8.0, 1024.0*1024.0*1024.0*8.0});

        return data;
    }
    private HashMap<String, String[]> createUnitsNamesData() {
        HashMap<String, String[]> data = new HashMap<>();

        data.put(" ", new String[]{" "});
        data.put("i", new String[]{" "});
        data.put("N", new String[]{" "});
        data.put("K", new String[]{" "});
        data.put("I", new String[]{"бит", "байт", "КБ", "МБ", "ГБ"});

        return data;
    }
    public int getId() {
        return spinner.getId();
    }
    Editable getText() {
        return field.getText();
    }
    Double getUnits() {
        return unitsData.get(data[spinner.getSelectedItemPosition()])[unitsSpinner.getSelectedItemPosition()];
    }
    Double getValue() {
        Expression e = new Expression();

        e.setExpressionString(getText().toString());

        return e.calculate() * getUnits();
    }
    Object getSelectedItem() {
        return spinner.getSelectedItem();
    }
    void setSelectedItem(int selectedItem) {
        spinner.setSelection(selectedItem);
    }
    void setValue(String text) {
        field.setText(text);
    }
}

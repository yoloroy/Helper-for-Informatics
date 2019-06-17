package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Resh13Activity extends AppCompatActivity {
    public static final String NAME = EvalActivity.NAME;

    FieldOfСondition variable1;
    FieldOfСondition variable2;
    FieldOfСondition variable3;
    FieldOfСondition variable4;

    TextView textView;
    ScrollView scrollView;
    ImageButton run;

    String[] variableData;  // variants of variables in task

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resh13);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_eval);
        //fragment.setSelection(NAME);

        textView = findViewById(R.id.DecisionWindow);
        scrollView = findViewById(R.id.scrollView2);
        run = findViewById(R.id.run);

        variableData = createData();
        createSpinnersEnvironment();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eval13_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent Act;
        switch(id){
            case R.id.restart :
                variable1.setSelectedItem(0);
                variable1.setValue("");
                variable2.setSelectedItem(0);
                variable2.setValue("");
                variable3.setSelectedItem(0);
                variable3.setValue("");
                variable4.setSelectedItem(0);
                break;
            case R.id.info :
                Act = new Intent(getApplicationContext(), EvalInfoIVActivity.class);
                startActivity(Act);
                break;
            case R.id.calc :
                Act = new Intent(getApplicationContext(), CalcActivity.class);
                startActivity(Act);
        }
        return super.onOptionsItemSelected(item);
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
        variable1 = new FieldOfСondition(findViewById(R.id.spinner1), findViewById(R.id.spinner_measure_units1),
                this, variableData, android.R.layout.simple_spinner_item, findViewById(R.id.editText1));

        variable2 = new FieldOfСondition(findViewById(R.id.spinner2), findViewById(R.id.spinner_measure_units2),
                this, variableData, android.R.layout.simple_spinner_item, findViewById(R.id.editText2));

        variable3 = new FieldOfСondition(findViewById(R.id.spinner3), findViewById(R.id.spinner_measure_units3),
                this, variableData, android.R.layout.simple_spinner_item, findViewById(R.id.editText3));

        variable4 = new FieldOfСondition(findViewById(R.id.spinner4), findViewById(R.id.spinner_measure_units4),
                this, variableData, android.R.layout.simple_spinner_item);
    }

    // onClick events
    // Down menu "onClick"s
    public void onClickMain (View view) {
        if (NAME != MainActivity1.NAME) {
            Intent Act = new Intent(getApplicationContext(), MainActivity1.class);
            startActivity(Act);
        }
    }
    public void onClickGener (View view) {
        if (NAME != GenerActivity.NAME) {
            Intent Act = new Intent(getApplicationContext(), GenerActivity.class);
            startActivity(Act);
        }
    }
    public void onClickEval (View view) {
        if (NAME != EvalActivity.NAME) {
            Intent Act = new Intent(getApplicationContext(), EvalActivity.class);
            startActivity(Act);
        }
    }
    public void onClickGuide(View view) {
        if (NAME != GuideActivity.NAME) {
            Intent Act = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(Act);
        }
    }
    public void onClickOther(View view) {
        if (NAME != OtherActivity.NAME) {
            Intent Act = new Intent(getApplicationContext(), OtherActivity.class);
            startActivity(Act);
        }
    }

    public void onClickStart(View view) {
        if (scrollView.getVisibility() == View.VISIBLE) {
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

            Log.d("STATE", y);
            textView.setText(y);
            textView.setVisibility(View.VISIBLE);
            Log.d("STATE", String.valueOf(textView.getVisibility() == View.VISIBLE));

            scrollView.setVisibility(View.INVISIBLE);

            run.setRotation(180.0f);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);

            run.setRotation(0.0f);
        }
    }
}

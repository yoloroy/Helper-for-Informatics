package e.pmart.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalcActivity1 extends AppCompatActivity {
    public static final String NAME = EvalActivity.NAME;
    private static final String[] EVENTS = {};
    Boolean start = true;
    TextView display;

    private Menu menu;
    Boolean colorAccent = true;

    @SuppressWarnings("FieldCanBeLocal")
    private String lastCommand = "=";
    private Double result = 0.0d;
    private Double data = 0.0d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc1);

        display = findViewById(R.id.textView12);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_eval);
        //fragment.setSelection(NAME);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calc_bar, menu);
        this.menu = menu;
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent Act;
        switch(id){
            case R.id.resh13 :
                Act = new Intent(getApplicationContext(), Resh13Activity.class);
                startActivity(Act);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setColors() {
        if (colorAccent) {
            menu.getItem(0).setIcon(R.drawable.ic_wb_sunny_yellow_24dp);

            findViewById(R.id.calc_plus).setBackground(getDrawable(R.color.colorCalcMain));
            findViewById(R.id.calc_minus).setBackground(getDrawable(R.color.colorCalcMain));
            findViewById(R.id.calc_dividion).setBackground(getDrawable(R.color.colorCalcMain));
            findViewById(R.id.calc_multiply).setBackground(getDrawable(R.color.colorCalcMain));
            findViewById(R.id.calc_delall).setBackground(getDrawable(R.color.colorCalcMain));
            findViewById(R.id.calc_exec).setBackground(getDrawable(R.color.colorCalcMain));

            findViewById(R.id.calc_Mplus).setBackground(getDrawable(R.color.colorCalcData));
            findViewById(R.id.calc_MR).setBackground(getDrawable(R.color.colorCalcData));

            findViewById(R.id.calc_step).setBackground(getDrawable(R.color.colorCalcMath));
            findViewById(R.id.calc_root).setBackground(getDrawable(R.color.colorCalcMath));
            findViewById(R.id.calc_log).setBackground(getDrawable(R.color.colorCalcMath));
            findViewById(R.id.calc_log2).setBackground(getDrawable(R.color.colorCalcMath));
            findViewById(R.id.calc_log10).setBackground(getDrawable(R.color.colorCalcMath));

            findViewById(R.id.calc_and).setBackground(getDrawable(R.color.colorCalcInf));
            findViewById(R.id.calc_or).setBackground(getDrawable(R.color.colorCalcInf));
            findViewById(R.id.calc_not).setBackground(getDrawable(R.color.colorCalcInf));
            findViewById(R.id.calc_toIndex).setBackground(getDrawable(R.color.colorCalcInf));
            findViewById(R.id.calc_mod).setBackground(getDrawable(R.color.colorCalcInf));

            
            ((Button) findViewById(R.id.calc_plus)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_minus)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_dividion)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_multiply)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_delall)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_exec)).setTextColor(Color.WHITE);

            ((Button) findViewById(R.id.calc_Mplus)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_MR)).setTextColor(Color.WHITE);

            ((Button) findViewById(R.id.calc_step)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_root)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_log)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_log2)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_log10)).setTextColor(Color.WHITE);

            ((Button) findViewById(R.id.calc_and)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_or)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_not)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_toIndex)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.calc_mod)).setTextColor(Color.WHITE);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_wb_sunny_black_24dp);

            findViewById(R.id.calc_plus).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_minus).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_dividion).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_multiply).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_delall).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_exec).setBackground(getDrawable(R.color.colorCalcNums));

            findViewById(R.id.calc_Mplus).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_MR).setBackground(getDrawable(R.color.colorCalcNums));

            findViewById(R.id.calc_step).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_root).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_log).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_log2).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_log10).setBackground(getDrawable(R.color.colorCalcNums));

            findViewById(R.id.calc_and).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_or).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_not).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_toIndex).setBackground(getDrawable(R.color.colorCalcNums));
            findViewById(R.id.calc_mod).setBackground(getDrawable(R.color.colorCalcNums));


            ((Button) findViewById(R.id.calc_plus)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_minus)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_dividion)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_multiply)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_delall)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_exec)).setTextColor(Color.BLACK);

            ((Button) findViewById(R.id.calc_Mplus)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_MR)).setTextColor(Color.BLACK);

            ((Button) findViewById(R.id.calc_step)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_root)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_log)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_log2)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_log10)).setTextColor(Color.BLACK);

            ((Button) findViewById(R.id.calc_and)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_or)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_not)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_toIndex)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.calc_mod)).setTextColor(Color.BLACK);
        } 
    }

    public void calcInsert(View view) {
        String input = (String) ((Button) view).getText();
        if (start) {
            display.setText("0");
            start = false;
        }
        if (String.valueOf(display.getText().charAt(display.getText().length()-1)).equals(".") &&
                input.equals(".")) {
            display.setText(display.getText().subSequence(0,display.getText().length()-1));
        }
        else {
            if (display.getText().equals("0") && !input.equals("."))
                display.setText("");
            display.setText(display.getText() + input);
        }
        Double current = Double.parseDouble((String) display.getText());
        if (lastCommand.equals("=")) {
            result = current;
        }
    }
    public void calcAction(View view) {
        String action = (String) ((Button) view).getText();
        Double current = Double.parseDouble((String) display.getText());
        switch (action)
        {
            case "+/-": current = 0 - current; break;
            case "CE": current = 0.0d; break;
            case "MR": current = data; break;
            case "M+": data += current; break;
            case "=":
                calcCommand(view); break;
            case "ln": current = Math.log(current); break;
            case "log2": current = Math.log(current)/Math.log(2); break;
            case "log10": current = Math.log10(current); break;
            case "not": current = logicNot(current); break;
        }
        if (!start)  result = current;
        display.setText(MyProgram.StripInt(current));
    }
    public void calcCommand(View view) {
        String command = (String) ((Button) view).getText();

        if (!start)
            calculate(Double.parseDouble((String) display.getText()));
        lastCommand = command;
        start = true;
    }

    public void calculate(double x) {
        switch (lastCommand)
        {
            case "+": result +=x; break;
            case "-": result -=x; break;
            case "*": result *=x; break;
            case "/": result /=x; break;
            case "x^y": result = Math.pow(result, x); break;
            case "y√x": result = Math.pow(result, 1/x); break;
            case "and": result = (double) (result.intValue() & (int) x); break;
            case "or": result = (double) (result.intValue() | (int) x); break;
            case "X₁₀ => Xy": display.setText(Integer.toString(result.intValue(), (int)x)); return;
            case "mod": result %=x; break;
        }
        display.setText(MyProgram.StripInt(result));
    }
    public double logicNot(double x) {
        String num = Integer.toBinaryString((int) x);
        num = num.replace('0', '2');
        num = num.replace('1', '0');
        num = num.replace('2', '1');
        return (double) (Integer.parseInt(num, 2));
    }

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
}

package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EvalActivity extends AppCompatActivity {
    public static final String NAME = "EvalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval);

        Intent Act = new Intent(getApplicationContext(), CalcActivity.class);
        startActivity(Act);
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

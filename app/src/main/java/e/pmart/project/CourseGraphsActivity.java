package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

public class CourseGraphsActivity extends AppCompatActivity {
    int phase = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_graphs);
    }

    private void toPhase(int n) {
        phase = n;
        switch (phase) {
            case 1:
                findViewById(R.id.phase1).setVisibility(View.VISIBLE);
                findViewById(R.id.phase2).setVisibility(View.INVISIBLE);
                break;
            case 2:
                findViewById(R.id.phase1).setVisibility(View.INVISIBLE);
                findViewById(R.id.phase2).setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClickCheck(View view) {
        RadioGroup rg = findViewById(R.id.radio_group_graphs2);
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.radioButton4:
                Intent Act = new Intent(getApplicationContext(), MainActivity1.class);
                startActivity(Act);
                return;
            default:
                rg.clearCheck();
                toPhase(1);
        }
    }
    public void onToPhase2(View view) {
        toPhase(2);
    }
}

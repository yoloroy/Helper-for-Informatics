package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class OtherActivity extends AppCompatActivity {
    public static final String NAME = "OtherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_other);
        //fragment.setSelection(NAME);
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
        if (NAME != Resh13Activity.NAME) {
            Intent Act = new Intent(getApplicationContext(), Resh13Activity.class);
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

    public void onClickAbout(View view) {
        Intent Act = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(Act);
    }
}

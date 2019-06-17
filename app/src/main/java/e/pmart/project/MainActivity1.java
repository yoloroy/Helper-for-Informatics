package e.pmart.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity1 extends AppCompatActivity {
    public static final String NAME = "MainActivity1";

    public static final String APP_TEMP = "temp";
    public static final String APP_TEMP_OPENED = "opened";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_main);
        //fragment.setSelection(NAME);

        mSettings = getSharedPreferences(APP_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        if (mSettings.contains(APP_TEMP_OPENED)) {
            if (!mSettings.getBoolean(APP_TEMP_OPENED, true)) {
                showHello();
            }
        } else {
            showHello();
        }
        editor.putBoolean(APP_TEMP_OPENED, true);
        editor.apply();
    }

    public void showHello() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
        builder.setTitle("Добро пожаловать в \"Помощник по информатике\"!")
                .setMessage("Мы вам поможем справиться с заданиями по информатике и легче усвоить материал.")
                .setCancelable(false)
                .setNegativeButton("Спасибо",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClickCourse(View view) {
        switch (view.getId()) {
            case R.id.course_graphs :
                Intent Act = new Intent(getApplicationContext(), CourseGraphsActivity.class);
                startActivity(Act);
        }
    }
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
}

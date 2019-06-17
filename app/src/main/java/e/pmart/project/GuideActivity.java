package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GuideActivity extends AppCompatActivity {
    public static final String NAME = "GuideActivity";

    ListView themesView;
    String[] themesList;
    ArrayAdapter<String> themesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_guide);
        //fragment.setSelection(NAME);

        themesView = findViewById(R.id.themesList);
        themesList = getResources().getStringArray(R.array.inf_themes);
        themesAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, themesList);
        themesView.setAdapter(themesAdapter);

        themesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent Act = new Intent(getApplicationContext(), GuidePageActivity.class);
                Act.putExtra("theme", getResources().getStringArray(R.array.inf_names)[i]);
                startActivity(Act);
            }
        });
    }

    // onClick events
    // Down menu "onClick"s
    public void onClickMain (View view) {
        if (NAME != MainActivity1.NAME) {
            Intent Act = new Intent(getApplicationContext(), MainActivity1.class);
            startActivity(Act);
        }
    }
    public void onClickGener(View view) {
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

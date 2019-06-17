package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GenerActivity extends AppCompatActivity {
    public static final String NAME = "GenerActivity";

    ListView tasksView;
    String[] tasksList;
    ArrayAdapter<String> tasksAdapter;
    TaskViewFragment task_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gener);

        GotoFragment fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_gener);

        tasksView = findViewById(R.id.tasksList);
        tasksList = getResources().getStringArray(R.array.inf_ege_tasks);
        tasksAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, tasksList);
        tasksView.setAdapter(tasksAdapter);

        tasksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                try {
                    findViewById(R.id.tasksList).setVisibility(View.INVISIBLE);
                    findViewById(R.id.task_layout).setVisibility(View.VISIBLE);
                    task_view = (TaskViewFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.task_);
                    task_view.setTask(i+1);
                }
                catch (NullPointerException e) {  // заглушка
                    Intent Act = new Intent(getApplicationContext(), MainActivity1.class);
                    startActivity(Act);
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                findViewById(R.id.tasksList).setVisibility(View.VISIBLE);
                findViewById(R.id.task_layout).setVisibility(View.INVISIBLE);
                task_view.toNULL();
                task_view = (TaskViewFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.task_);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickCheckAnswer(View view) {
        task_view.onClickCheckAnswer(view);
    }}

    //

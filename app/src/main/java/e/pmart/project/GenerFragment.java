package e.pmart.project;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class GenerFragment extends Fragment {
    View root_view;

    MainActivity mainActivity;

    ListView tasksView;
    String[] tasksList;
    ArrayAdapter<String> tasksAdapter;

    ActionBar supportActionBar;
    FragmentManager supportFragmentManager;


    public GenerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_gener, container, false);
        return root_view;
    }

    @Override
    public void onStart() {
        createTasks();

        super.onStart();
    }

    private void createTasks() {
        tasksView = getView().findViewById(R.id.tasksList);
        tasksList = getResources().getStringArray(R.array.inf_ege_tasks);
        tasksAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, tasksList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View temp = super.getView(position, convertView, parent);

                if (getCount() == 1)
                    temp.setBackground(getResources().getDrawable(R.drawable.rect_corners_s_16));
                else if (position == 0)
                    temp.setBackground(getResources().getDrawable(R.drawable.rect_corners_u_16));
                else if (position == getCount()-1)
                    temp.setBackground(getResources().getDrawable(R.drawable.rect_corners_d_16));
                else
                    temp.setBackground(getResources().getDrawable(R.drawable.rect));

                return temp;
            }
        };
        tasksView.setAdapter(tasksAdapter);

        tasksView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ActionBar actionBar = supportActionBar;
                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                ((TaskViewFragment)
                        (mainActivity.mode_fragments.get("gener_task").get(0))).setRootView(root_view);
                ((TaskViewFragment)
                        (mainActivity.mode_fragments.get("gener_task").get(0))).setTask(i+1);
                mainActivity.changeMode("gener_task");
            }
        });
    }



    public GenerFragment setSupportActionBar(ActionBar supportActionBar) {
        this.supportActionBar = supportActionBar;
        return this;
    }
    public GenerFragment setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
        return this;
    }
    public GenerFragment setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        return this;
    }
}

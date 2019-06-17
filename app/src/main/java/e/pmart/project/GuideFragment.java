package e.pmart.project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {
    View root_view;

    MainActivity mainActivity;

    ListView themesView;
    String[] themesList;
    ArrayAdapter<String> themesAdapter;

    int curr;
    int num;
    String[] tasks;

    ActionBar supportActionBar;
    FragmentManager supportFragmentManager;


    public GuideFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_gener, container, false);
        createList();
        return root_view;
    }

    private void createList() {
        themesView = root_view.findViewById(R.id.inf_themes);
        themesList = getResources().getStringArray(R.array.inf_themes);
        themesAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, themesList);
        themesView.setAdapter(themesAdapter);



        themesView.setOnItemClickListener(
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


    public GuideFragment setSupportActionBar(ActionBar supportActionBar) {
        this.supportActionBar = supportActionBar;
        return this;
    }
    public GuideFragment setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
        return this;
    }
    public GuideFragment setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        return this;
    }
}

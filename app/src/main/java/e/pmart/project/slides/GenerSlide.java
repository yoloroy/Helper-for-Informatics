package e.pmart.project.slides;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import e.pmart.project.DatabaseHelper;
import e.pmart.project.FactoryEducationFragment;
import e.pmart.project.MainActivity;
import e.pmart.project.R;
import e.pmart.project.TaskViewFragment;
import e.pmart.project.TestEndFragment;


public class GenerSlide extends Fragment {
    View root_view;

    MainActivity mainActivity;

    ListView tasksView;
    String[] tasksList;
    ArrayAdapter<String> tasksAdapter;

    ActionBar supportActionBar;
    FragmentManager supportFragmentManager;


    public GenerSlide() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.slide_gener, container, false);
        return root_view;
    }

    @Override
    public void onStart() {
        createTasks();

        super.onStart();
    }

    private void createTasks() {
        tasksView = getView().findViewById(R.id.tasksList);
        tasksView.setVisibility(View.VISIBLE);
        getView().findViewById(R.id.task_view).setVisibility(View.GONE);
        tasksList = getResources().getStringArray(R.array.inf_ege_tasks);
        tasksAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, tasksList) {
            @Override
            public int getViewTypeCount() {
                return 3;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0)
                    return 0;
                if (position+1 == getCount())
                    return 2;
                return 1;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);

                switch (getItemViewType(position)) {
                    case 0:
                        Log.i("getView", view.getText().toString());
                        view.setBackground(getResources().getDrawable(R.drawable.gener_hat));
                        view.setTextColor(getResources().getColor(R.color.colorPrimary));
                        view.setTextSize(17);
                        view.setTypeface(null, Typeface.BOLD);
                        break;
                    case 1:
                        view.setBackground(getResources().getDrawable(R.drawable.rect));
                        break;
                    case 2:
                        view.setBackground(getResources().getDrawable(R.drawable.rect_corners_d_16));
                }

                return view;
            }
        };
        tasksView.setAdapter(tasksAdapter);

        tasksView.setOnItemClickListener((adapterView, view, position, l) -> {
            if (position > 0) onTaskChosen(position);
            else onVariantChosen();
        });
    }

    private void onTaskChosen(int position) {
        getActivity().getMenuInflater().inflate(R.menu.gener_bar, ((MainActivity) getActivity()).menu);
        getView().findViewById(R.id.task_view).setVisibility(View.VISIBLE);
        tasksView.setVisibility(View.GONE);
        ((TaskViewFragment)
                getChildFragmentManager().findFragmentById(R.id.task_fragment))
                .setRootView(root_view);
        ((TaskViewFragment)
                getChildFragmentManager().findFragmentById(R.id.task_fragment))
                .setTask(position);
        ((TaskViewFragment)
                getChildFragmentManager().findFragmentById(R.id.task_fragment))
                .startTaskSet();
    }

    private void onVariantChosen() {
        ArrayList<Fragment> fragments = new ArrayList();
        ArrayList<String> actionBarNames = new ArrayList();

        Random random = new Random();
        DatabaseHelper mDBHelper = new DatabaseHelper(getContext());
        SQLiteDatabase mDb = getCheckedDb(mDBHelper);

        Cursor cursor;
        int num;

        for (int i = 1; i <= 23; i++) {
            cursor = mDb.rawQuery("SELECT task, answer FROM tasks WHERE number="+i, null);
            cursor.moveToFirst();

            num = random.nextInt(cursor.getCount() / 2);
            cursor.moveToPosition(num*2);

            int finalI = i;
            fragments.add(new FactoryEducationFragment()
                    .setTextWatcher(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.length() == 0) {
                                ((TestEndFragment) fragments.get(fragments.size() - 1))
                                        .delAnswered(finalI);
                            } else {
                                ((TestEndFragment) fragments.get(fragments.size() - 1))
                                        .addAnswered(finalI);
                            }
                        }
            })
                    .newWebview(cursor
                            .getString(0)
                            .replace("\"width:650px\"", "\"width:94%\""))
                    .newEnter(cursor.getString(1)));
            actionBarNames.add("Задание "+i);

            cursor.close();
        }

        fragments.add(new TestEndFragment());
        actionBarNames.add("Оканчание");

        ((MainActivity) getActivity()).mode_fragments.put("test", fragments);
        ((MainActivity) getActivity()).actionBarNames.put("test", actionBarNames);

        ((MainActivity) getActivity()).changeMode("test");
    }

    private SQLiteDatabase getCheckedDb(DatabaseHelper DBHelper) {
        try {
            DBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        return DBHelper.getWritableDatabase();
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public GenerSlide setSupportActionBar(ActionBar supportActionBar) {
        this.supportActionBar = supportActionBar;
        return this;
    }
    public GenerSlide setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
        return this;
    }
    public GenerSlide setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        return this;
    }
}

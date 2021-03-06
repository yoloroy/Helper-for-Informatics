package e.pmart.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class TaskViewFragment extends Fragment {
    View rootView;
    int curr;
    String[] tasks = new String[] {"", ""};

    FloatingActionButton fab;
    BottomSheetBehavior bottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =
                inflater.inflate(R.layout.task_view, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        fab = rootView.findViewById(R.id.fab_task_view);
        fab.setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (!((EditText) rootView.findViewById(R.id.task_answer)).getText().toString().equals(""))
                onClickCheckAnswer(view);
        });
        View llBottomSheet = rootView.findViewById(R.id.bottom_sheet);

        // настройка поведения нижнего экрана
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        ((EditText) rootView.findViewById(R.id.task_answer)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                    fab.setScaleX(0.7f); fab.setScaleY(0.7f);
                    fab.animate().scaleX(1).scaleY(1).setDuration(200).start();
                } else if (s.length() == 0) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                    fab.setScaleX(0.7f); fab.setScaleY(0.7f);
                    fab.animate().scaleX(1).scaleY(1).setDuration(200).start();
                }
            }
        });

        //startTaskSet();

        super.onStart();
    }

    public void setTask(int i) {
        curr = i;
    }
    public void startTaskSet() {
        Random random = new Random();

        DatabaseHelper mDBHelper = new DatabaseHelper(rootView.getContext());
        SQLiteDatabase mDb = getCheckedDb(mDBHelper);

        Cursor cursor = mDb.rawQuery("SELECT task, answer FROM tasks WHERE number="+curr, null);
        cursor.moveToFirst();

        int num = random.nextInt(cursor.getCount() / 2);
        cursor.moveToPosition(num*2);
        tasks = new String[]{cursor.getString(0), cursor.getString(1)};
        cursor.close();

        ((TextView) rootView.findViewById(R.id.task_theme)).setText("Задание " + curr);
        WebView task_text = rootView.findViewById(R.id.task_text);
        task_text.loadDataWithBaseURL(null, tasks[0].replace("\"width:650px\"", "\"width:94%\""),
                "text/html", "en_US", null);

        task_text.getSettings().setBuiltInZoomControls(true);
        task_text.getSettings().setMinimumFontSize(16);
    }

    private SQLiteDatabase getCheckedDb(DatabaseHelper DBHelper) {
        try {
            DBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        return DBHelper.getWritableDatabase();
    }

    public void onClickCheckAnswer(View view) {
        SharedPreferences sp = getActivity().getSharedPreferences("solved_tasks", Context.MODE_PRIVATE);
        sp.edit().putInt("num", sp.getInt("num", 0)+1).apply();

        if (tasks[1].equals(((EditText) rootView.findViewById(R.id.task_answer)).getText().toString())) {
            rootView.findViewById(R.id.fab_task_view).getBackground().setTint(getResources().getColor(R.color.colorAppGreen));
            ((EditText) rootView.findViewById(R.id.task_answer)).setText("");
            ((EditText) rootView.findViewById(R.id.task_blackovik)).setText("");
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            sp.edit().putInt(
                    "solved_"+curr,
                    sp.getInt("solved_"+curr, 0) + 1)
                    .apply();

            startTaskSet();
        } else {
            rootView.findViewById(R.id.fab_task_view).getBackground().setTint(getResources().getColor(R.color.colorAppFalse));
        }
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
}

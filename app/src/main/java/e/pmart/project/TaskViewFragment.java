package e.pmart.project;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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
    int num;
    String[] tasks = new String[] {"We have't tasks for this number yet, *answer is \"\"*", ""};

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    FloatingActionButton fab;
    BottomSheetBehavior bottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =
                inflater.inflate(R.layout.task_view, container, false);

        fab = rootView.findViewById(R.id.fab_task_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (!((EditText) rootView.findViewById(R.id.task_answer)).getText().toString().equals(""))
                    onClickCheckAnswer(view);
            }
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

        ((EditText) rootView.findViewById(R.id.task_answer)).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int len = ((EditText) rootView.findViewById(R.id.task_answer)).getText().toString().length();
                if (len == 1) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                    fab.setScaleX(0.7f); fab.setScaleY(0.7f);
                    fab.animate().scaleX(1).scaleY(1).setDuration(200).start();
                } else if (len == 0) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                    fab.setScaleX(0.7f); fab.setScaleY(0.7f);
                    fab.animate().scaleX(1).scaleY(1).setDuration(200).start();
                }
                return false;
            }
        });

        startTaskSet();
        return rootView;
    }

    public void setTask(int i) {
        curr = i;
    }
    public void startTaskSet() {
        mDBHelper = new DatabaseHelper(rootView.getContext());

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor cursor;
        Random random = new Random();

        cursor = mDb.rawQuery("SELECT task, answer FROM tasks WHERE number="+curr, null);
        cursor.moveToFirst();

        num = random.nextInt(cursor.getCount() / 2);
        cursor.moveToPosition(num*2);
        tasks = new String[]{cursor.getString(0), cursor.getString(1)};
        cursor.close();

        ((TextView) rootView.findViewById(R.id.task_theme)).setText("№ " + curr);
        WebView task_text = rootView.findViewById(R.id.task_text);
        task_text.loadDataWithBaseURL(null, tasks[0].replaceFirst("\"width:650px\"", "\"width:94%\""),
                "text/html", "en_US", null);
        //task_text.setBackgroundColor(Color.TRANSPARENT);
        task_text.getSettings().setBuiltInZoomControls(true);
        task_text.getSettings().setMinimumFontSize(16);

        //(rootView.findViewById(R.id.task_answer))
        //        .setMinimumWidth(  (int)((EditText) rootView.findViewById(R.id.task_answer))
        //                .getTextSize() * tasks[1].length());
    }

    public void onClickCheckAnswer(View view) {
        if (tasks[1].equals(((EditText) rootView.findViewById(R.id.task_answer)).getText().toString())) {
            rootView.findViewById(R.id.fab_task_view).getBackground().setTint(getResources().getColor(R.color.colorAppGreen));
            ((EditText) rootView.findViewById(R.id.task_answer)).setText("");
            ((EditText) rootView.findViewById(R.id.task_blackovik)).setText("");
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            //setTask(curr);
            startTaskSet();
        }
        else
            rootView.findViewById(R.id.fab_task_view).getBackground().setTint(getResources().getColor(R.color.colorAppFalse));
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
}

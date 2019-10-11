package e.pmart.project;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class TaskViewFragment extends Fragment {
    View rootView;
    int curr;
    int num;
    String[] tasks = new String[]{"We have't tasks for this number yet, *answer is \"\"*", ""};

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =
                inflater.inflate(R.layout.task_view, container, false);
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
        if (curr == 1 || curr == 5 || curr == 6) {
            String product = "";

            cursor = mDb.rawQuery("SELECT task, answer FROM tasks WHERE number="+curr, null);
            cursor.moveToFirst();

            num = random.nextInt(cursor.getCount() / 2);
            cursor.moveToPosition(num*2);
            tasks = new String[]{cursor.getString(0), cursor.getString(1)};
            cursor.close();

        } else {
            tasks = new String[]{"We have't tasks for this number yet, *answer is \"\"*", ""};
            num = 0;
        }


        Log.i("fak", "setTask: "+rootView.getContext());
        ((TextView) rootView.findViewById(R.id.task_theme)).setText("№ " + curr);
        ((TextView) rootView.findViewById(R.id.task_text)).setText(tasks[0]);
        (rootView.findViewById(R.id.task_answer))
                .setMinimumWidth(  (int)((EditText) rootView.findViewById(R.id.task_answer))
                        .getTextSize() * tasks[1].length());
    }

    public void onClickCheckAnswer(View view) {
        if (tasks[1].equals(((EditText) rootView.findViewById(R.id.task_answer)).getText().toString())) {
            rootView.findViewById(R.id.task_check).getBackground().setTint(getResources().getColor(R.color.colorAppTrue));
            ((EditText) rootView.findViewById(R.id.task_answer)).setText("");
            //setTask(curr);
            startTaskSet();
        }
        else
            rootView.findViewById(R.id.task_check).getBackground().setTint(getResources().getColor(R.color.colorAppFalse));
    }
    public void toNULL() {
        ((TextView) rootView.findViewById(R.id.task_theme)).setText("Theme");
        ((TextView) rootView.findViewById(R.id.task_text)).setText("Task");
        rootView.findViewById(R.id.task_check).getBackground().setTint(getResources().getColor(R.color.colorAppGreen));
        ((EditText) rootView.findViewById(R.id.task_answer)).setText("");
    }
    public void setRootView(View rootView) {
        this.rootView = rootView;
    }
}

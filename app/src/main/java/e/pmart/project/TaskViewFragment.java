package e.pmart.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class TaskViewFragment extends Fragment {
    View rootView;
    int curr;
    int num;
    String[] tasks = new String[]{"We have't tasks for this number yet, *answer is \"\"*", ""};

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
        switch(i) {
            case 13 :
                tasks = rootView.getResources().getStringArray(R.array.task_group_13);
                break;
            default :
                tasks = new String[]{"We have't tasks for this number yet, *answer is \"\"*", ""};
        }


        //            (                                          )
    }
    public void startTaskSet() {
        Random random = new Random();
        num = random.nextInt(tasks.length / 2);
        ((TextView) rootView.findViewById(R.id.task_theme)).setText("№ " + curr);
        ((TextView) rootView.findViewById(R.id.task_text)).setText(tasks[num*2]);
        (rootView.findViewById(R.id.task_answer))
                .setMinimumWidth(  (int)((EditText) rootView.findViewById(R.id.task_answer))
                        .getTextSize() * tasks[num*2+1].length());
    }

    public void onClickCheckAnswer(View view) {
        if (tasks[num*2+1].equals(((EditText) rootView.findViewById(R.id.task_answer)).getText().toString())) {
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

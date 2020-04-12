package e.pmart.project;


import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FactoryEducationFragment extends Fragment {
    final String[] TASKS = {"image", "text", "title", "choice", "enter", "text_list", "view", "webview", "title_s", "enter_s", "text_s"};
    float SCALE;

    List<List> task_list = new ArrayList<>();
    String answer = null;
    String user_answer = "";
    boolean enable_continue = true;

    EditText enter;
    View root_view;

    TextWatcher textWatcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_factory_education, container, false);
        SCALE = getContext().getResources().getDisplayMetrics().density;
        evaluateTasks();
        if (enable_continue)
            root_view.findViewById(R.id.button5).setVisibility(View.VISIBLE);
        else
            root_view.findViewById(R.id.button5).setVisibility(View.INVISIBLE);
        return root_view;
    }

    public FactoryEducationFragment setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;

        return this;
    }

    public FactoryEducationFragment newImage(int image_id) {
        return newTask("image", image_id);
    }

    public FactoryEducationFragment newText(int text_id) {
        return newTask("text", text_id);
    }

    public FactoryEducationFragment newText(String text) {
        return newTask("text_s", text);
    }

    public FactoryEducationFragment newTitle(int text_id) {
        return newTask("title", text_id);
    }

    public FactoryEducationFragment newTitle(String  text) {
        return newTask("title_s", text);
    }

    public FactoryEducationFragment newChoice(int choice_array_id) {
        return newTask("choice", choice_array_id);
    }

    public FactoryEducationFragment newEnter(int answer_id) {
        return newTask("enter", answer_id);
    }

    public FactoryEducationFragment newEnter(String answer) {
        return newTask("enter_s", answer);
    }

    public FactoryEducationFragment newTextList(int text_array_id) {
        return newTask("text_list", text_array_id);
    }

    public FactoryEducationFragment newWebview(String data) {
        return newTask("webview", data);
    }

    public FactoryEducationFragment newView(View view) {
        return newTask("view", view);
    }

    public FactoryEducationFragment enableContinue(boolean a) {
        enable_continue = a;
        return this;
    }

    public FactoryEducationFragment newTask(String name, Object obj) {
        List<Object> temp_list = new ArrayList<>();
        temp_list.add(name);
        temp_list.add(obj);  // object or id for object
        task_list.add(temp_list);

        return this;
    }

    public FactoryEducationFragment delTask(int position) {
        task_list.remove(position);

        return this;
    }

    public void evaluateTasks() {
        for (int i = 0; i < task_list.size(); i++) {
            switch (Arrays.asList(TASKS).indexOf(task_list.get(i).get(0))) {
                case 0:
                    addImage(task_list.get(i));
                    break;
                case 1:
                    addText(task_list.get(i));
                    break;
                case 2:
                    addTitle(task_list.get(i));
                    break;
                case 3:
                    addChoice(task_list.get(i));
                    break;
                case 4:
                    addEnter(task_list.get(i));
                    break;
                case 5:
                    addTextList(task_list.get(i));
                    break;
                case 6:
                    addView(task_list.get(i));
                    break;
                case 7:
                    addWebview(task_list.get(i));
                    break;
                case 8:
                    addTitleS(task_list.get(i));
                    break;
                case 9:
                    addEnterS(task_list.get(i));
                    break;
                case 10:
                    addTextS(task_list.get(i));
                    break;
            }
        }
    }

    public FactoryEducationFragment addImage(List task) {
        QuadImage imageView = new QuadImage(getContext());
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), (int) task.get(1)));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        float scale = getResources().getDisplayMetrics().density;
        imageViewLayoutParams.setMargins(
                (int) (64 * scale + 0.5f),
                (int) (8 * scale + 0.5f),
                (int) (64 * scale + 0.5f),
                (int) (8 * scale + 0.5f));

        imageView.setLayoutParams(imageViewLayoutParams);
        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(imageView);
        return this;
    }

    public FactoryEducationFragment addText(List task) {
        TextView textView = new TextView(getContext());
        textView.setText((int)task.get(1));

        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.setMargins(0,8,0,8);
        textView.setLayoutParams(imageViewLayoutParams);

        TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationText, R.styleable.StandartText);
        textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
        ta.recycle();

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(textView);
        return this;
    }

    public FactoryEducationFragment addTextS(List task) {
        TextView textView = new TextView(getContext());
        textView.setText((String) task.get(1));

        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.setMargins(0,8,0,8);
        textView.setLayoutParams(imageViewLayoutParams);

        TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationText, R.styleable.StandartText);
        textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
        ta.recycle();

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(textView);
        return this;
    }

    public FactoryEducationFragment addTitle(List task) {
        TextView textView = new TextView(getContext());
        textView.setText((int)task.get(1));

        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.setMargins(
                0,
                (int) (16*getContext().getResources().getDisplayMetrics().density+0.5),
                0,
                (int) (8/getContext().getResources().getDisplayMetrics().density+0.5));
        textView.setLayoutParams(imageViewLayoutParams);

        TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationTitle, R.styleable.StandartText);
        textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
        textView.setTextSize(24);
        ta.recycle();

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(textView);
        return this;
    }

    public FactoryEducationFragment addTitleS(List task) {
        TextView textView = new TextView(getContext());
        textView.setText((String) task.get(1));

        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.setMargins(
                0,
                (int) (16*getContext().getResources().getDisplayMetrics().density+0.5),
                0,
                (int) (8/getContext().getResources().getDisplayMetrics().density+0.5));
        textView.setLayoutParams(imageViewLayoutParams);

        TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationTitle, R.styleable.StandartText);
        textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
        textView.setTextSize(24);
        ta.recycle();

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(textView);
        return this;
    }

    public FactoryEducationFragment addChoice(List task) {
        RadioGroup radioGroup = new RadioGroup(getContext());

        String[] answers = getResources().getStringArray((int) task.get(1));
        answer = answers[answers.length - 1];
        List<String> temp = Arrays.asList(answers);
        Collections.shuffle(temp);
        answers = (String[]) temp.toArray();

        RadioButton radioButton;
        for (String choice: answers) {
            radioButton = new RadioButton(getContext());
            radioButton.setText(choice);
            if (choice.equals(user_answer))
                radioButton.setPressed(true);

            radioGroup.addView(radioButton);
            //(RadioButton) radioGroup.getChildAt(radioGroup.getChildCount()-1)
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                user_answer = (String)
                        ((RadioButton) root_view.findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText();
            }
        });

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(radioGroup);
        return this;
    }

    public FactoryEducationFragment addEnter(List task) {
        enter = new EditText(getContext());

        if (textWatcher != null) {
            enter.addTextChangedListener(textWatcher);
        }

        answer = getString((int) task.get(1));
        enter.setText(user_answer);

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(enter);
        return this;
    }

    public FactoryEducationFragment addEnterS(List task) {
        enter = new EditText(getContext());

        if (textWatcher != null) {
            enter.addTextChangedListener(textWatcher);
        }

        answer = (String) task.get(1);
        enter.setText(user_answer);

        ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(enter);
        return this;
    }

    public FactoryEducationFragment addTextList(List task) {
        String[] text_list = getResources().getStringArray((int) task.get(1));

        for (String i : text_list) {
            TextView textView = new TextView(getContext());
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(17);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(
                    pxToDp(16), pxToDp(8), 0, pxToDp(8)
            );
            textView.setLayoutParams(layoutParams);
            textView.setText(i);

            ((LinearLayout) root_view.findViewById(R.id.factory_base)).addView(textView);
        }
        return this;
    }

    public FactoryEducationFragment addView(List task) {
        ((LinearLayout) root_view.findViewById(R.id.factory_base))
                .addView((View) task.get(1));
        return this;
    }

    public FactoryEducationFragment addWebview(List task) {
        WebView webView = new WebView(getContext());

        webView.loadDataWithBaseURL(null,
                ((String) task.get(1)).replace("\"width:650px\"", "\"width:94%\""),
                "text/html", "en_US", null);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setMinimumFontSize(16);

        ((LinearLayout) root_view.findViewById(R.id.factory_base))
                .addView(webView);
        return this;
    }

    public FactoryEducationFragment delView(int position) {
        ((LinearLayout) root_view.findViewById(R.id.factory_base)).removeViewAt(position);
        return this;
    }

    public Boolean checkAnswer() {
        if (enter != null) {
            return answer.equals(enter.getText().toString());
        }
        return answer == null || answer.equals(user_answer);
    }

    public void save() {
        try {
            Log.i("-----------------------", "save: " + enter.getText());
            user_answer = "" + enter.getText();
        } catch (NullPointerException e) {
            if (!user_answer.equals(""))
                Log.i("-----------------------", "save: " + user_answer);
        }
    }

    public int pxToDp(int px) {
        return (int) (px * SCALE + 0.5f);
    }
}

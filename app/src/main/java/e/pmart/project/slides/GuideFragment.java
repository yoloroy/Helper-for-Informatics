package e.pmart.project.slides;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;

import e.pmart.project.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onStart() {
        SharedPreferences tasks = getActivity().getSharedPreferences("solved_tasks", Context.MODE_PRIVATE);
        ((TextView) getView().findViewById(R.id.tasks_solved)).setText(String.valueOf(tasks.getInt("num", 0)));
        StringBuilder solved_tasks = new StringBuilder();

        for (int i = 1; i <= 23; i++) {
            if (tasks.getInt("solved_"+i, 0) == 0)
                continue;

            TextView textView = new TextView(getContext());
            textView.setText(String.format("№ %d - %d", i, tasks.getInt("solved_"+i, 0)));
            solved_tasks.append(String.format("№ %d - %d", i, tasks.getInt("solved_" + i, 0))).append("\n");

            LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageViewLayoutParams.setMargins(0,8,0,8);
            textView.setLayoutParams(imageViewLayoutParams);

            TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationText, R.styleable.StandartText);
            textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
            ta.recycle();

            ((LinearLayout) getView().findViewById(R.id.tasks_info)).addView(textView);
        }

        SharedPreferences variants = getActivity().getSharedPreferences("variants", Context.MODE_PRIVATE);
        ((TextView) getView().findViewById(R.id.variants_solved)).setText(String.valueOf(variants.getStringSet("variants_info", new HashSet<>()).size()));
        StringBuilder solved_variants = new StringBuilder();

        for (String i: variants.getStringSet("variants_info", new HashSet<>())) {
            TextView textView = new TextView(getContext());
            textView.setText(i);
            solved_variants.append(i);

            LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageViewLayoutParams.setMargins(0,8,0,8);
            textView.setLayoutParams(imageViewLayoutParams);

            TypedArray ta = getContext().obtainStyledAttributes(R.style.EducationText, R.styleable.StandartText);
            textView.setTextColor(ta.getColor(R.styleable.StandartText_android_textColor, -1));
            ta.recycle();

            ((LinearLayout) getView().findViewById(R.id.variants_info)).addView(textView);
        }

        getView().findViewById(R.id.share_stats)
                .setOnClickListener((View view) -> {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String body = "" +
                            "Инфа на 5\n" +
                            "Статистика ученика\n" +
                            "Всего решено номеров: "+tasks.getInt("num", 0)+"\n" +
                            solved_tasks.toString()+"\n" +
                            "Всего решено вариантов: "+variants.getStringSet("variants_info", new HashSet<>()).size()+"\n" +
                            solved_variants.toString();
                    intent.putExtra(Intent.EXTRA_TEXT,body);
                    startActivity(Intent.createChooser(intent, "Share Using"));
                });

        super.onStart();
    }
}

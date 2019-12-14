package e.pmart.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    GotoFragment goto_fragment;
    Menu menu;

    FragmentStatePagerAdapter slides;
    String mode = "main";
    Map<String, List<Fragment>> mode_fragments = new HashMap<>();
    Map<String, List<String>> actionBarNames = new HashMap<>();

    ViewPager pager;

    Animation animAlpha;

    MyArrayList<String> calc_text = new MyArrayList<>();

    public static final String APP_TEMP = "temp";
    public static final String APP_TEMP_OPENED = "opened";
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        goto_fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_main);

        create_fragment_unions();

        pager = findViewById(R.id.view_pager_main);
        slides = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(slides);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageSelected(position);

                if (positionOffset > 0.5) {
                    setTitleFromModeNames(position+1);
                    goto_fragment.setSelection(position+1);
                }
                else {
                    setTitleFromModeNames(position);
                    goto_fragment.setSelection(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setTitleFromModeNames(position);
                goto_fragment.setSelection(position);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
                }

                if (mode.equals("main")) {
                    if (pager.getCurrentItem() == 2) {
                        if (menu != null) {
                            menu.clear();
                            getMenuInflater().inflate(R.menu.calc_bar, menu);
                        }
                    } else {
                        if (menu != null) {
                            menu.clear();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toMain();
        pager.setCurrentItem(2);
        setTitleFromModeNames(2);

        calc_text.add("0");

        mSettings = getSharedPreferences(APP_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        if (mSettings.contains(APP_TEMP_OPENED)) {
            if (!mSettings.getBoolean(APP_TEMP_OPENED, true)) {
                showHello();
            }
        } else {
            showHello();
        }

        editor.putBoolean(APP_TEMP_OPENED, true);
        editor.apply();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public void showHello() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добро пожаловать в \"Помощник по информатике\"!")
                .setMessage("Мы вам поможем справиться с заданиями по информатике и легче усвоить материал.")
                .setCancelable(false)
                .setNegativeButton("Спасибо",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //  main part
    public void onClickGoto (View view) {
        LinearLayout goto_layout = findViewById(R.id.goto_layout);

        for (int i = 0; i < goto_layout.getChildCount(); i++) {
            if (goto_layout.getChildAt(i).equals(view)) {
                pager.setCurrentItem(i);
                goto_fragment.setSelection(i);
            }
        }
    }
    public void onClickNext(View view) {
        if (mode.startsWith("course")) {
            if (!((FactoryEducationFragment)
                    ((MyFragmentPagerAdapter) pager.getAdapter())
                            .getItem(pager.getCurrentItem())).onClickNext()) {

                ((FactoryEducationFragment)
                        ((MyFragmentPagerAdapter) pager.getAdapter())
                                .getItem(pager.getCurrentItem())).save();

                pager.setCurrentItem(pager.getCurrentItem() - 1);
                return;
            }
            if (pager.getCurrentItem() == (pager.getAdapter().getCount() - 1)) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(mode, true);
                editor.apply();

                toMain();
                pager.setCurrentItem(1);
                return;
            }
        }

        pager.setCurrentItem(pager.getCurrentItem()+1);
    }
    public void changeMode(View view) {
        mode_fragments.remove(mode);
        mode_fragments
                .put(mode, ((MyFragmentPagerAdapter) pager.getAdapter())
                .getFragments());

        ActionBar actionBar = getSupportActionBar();
        switch (view.getId()) {
            /*case R.id.course_graphs:
                mode = "course_graphs";

                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;*/
            case R.id.goto_about:
                mode = "about";

                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;
        }
        toInnerPages();
    }
    public void changeMode(String mode) {
        mode_fragments.remove(this.mode);
        mode_fragments
                .put(this.mode, ((MyFragmentPagerAdapter) pager.getAdapter())
                        .getFragments());
        ActionBar actionBar = getSupportActionBar();
        switch (mode) {
            case "course_graphs":
                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;
            case "gener_task":
                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;
        }
        this.mode = mode;
        toInnerPages();
    }
    public void toMain() {
        mode = "main";
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable actionBar_back = getDrawable(R.drawable.action_bar_background);
            actionBar.setBackgroundDrawable(actionBar_back);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        pager.setBackground(getDrawable(R.color.colorBackground));

        findViewById(R.id.down_menu_main).setVisibility(View.VISIBLE);

        ((MyFragmentPagerAdapter) pager.getAdapter()).setList(mode_fragments.get(mode));
    }
    public void toInnerPages() {
        findViewById(R.id.down_menu_main).setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            Drawable actionBar_back = getDrawable(R.color.colorPrimary);
            getSupportActionBar().setBackgroundDrawable(actionBar_back);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pager.setBackground(getDrawable(R.drawable.rect_corners_u));
        //pager.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        ((MyFragmentPagerAdapter) pager.getAdapter()).setList(mode_fragments.get(mode));
        pager.setCurrentItem(0);
    }
    public void create_fragment_unions() {
        /*     main     */
        mode_fragments.put("main", new ArrayList<Fragment>());
        actionBarNames.put("main", new ArrayList<String>());

        mode_fragments.get("main").add(new GuideFragment()
                .setSupportActionBar(getSupportActionBar())
                .setSupportFragmentManager(getSupportFragmentManager())
                .setMainActivity(this));
        actionBarNames.get("main").add("Справочник");
        mode_fragments.get("main").add(new EducationFragment());
        actionBarNames.get("main").add("Курсы");
        mode_fragments.get("main").add(new EvalFragment());
        actionBarNames.get("main").add("Калькулятор");
        mode_fragments.get("main").add(new GenerFragment()
                .setSupportActionBar(getSupportActionBar())
                .setSupportFragmentManager(getSupportFragmentManager())
                .setMainActivity(this));
        actionBarNames.get("main").add("Задачник");
        mode_fragments.get("main").add(new AboutFragment());
        actionBarNames.get("main").add("О программе");
        //mode_fragments.get("main").add(new OtherFragment());
        //actionBarNames.get("main").add("Дополнительно");

        /*     courses     */
        upload_courses();

        /*     task     */
        mode_fragments.put("gener_task", new ArrayList<Fragment>());
        actionBarNames.put("gener_task", new ArrayList<String>());

        mode_fragments.get("gener_task").add(new TaskViewFragment());
        actionBarNames.get("gener_task").add("Задача");

        /*     guide page     */
        mode_fragments.put("guide_inner", new ArrayList<Fragment>());
        actionBarNames.put("guide_inner", new ArrayList<String>());

        /*     about     */
        mode_fragments.put("about", new ArrayList<Fragment>());
        actionBarNames.put("about", new ArrayList<String>());

        mode_fragments.get("about").add(new AboutFragment());
        actionBarNames.get("about").add("О программе");
    }
    private void upload_courses() {
        // if you update courses - go to educationFragment too

        mode_fragments.put("course_graphs", new ArrayList<Fragment>());
        actionBarNames.put("course_graphs", new ArrayList<String>());

        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newTitle(R.string.graphs_1)
                .newText(R.string.graphs_2)
                .newImage(R.mipmap.graph_visualisation));
        actionBarNames.get("course_graphs").add("Графы");
        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newText(R.string.graphs_task1)
                .newChoice(R.array.choice_graphs_task1));
        actionBarNames.get("course_graphs").add("Графы");
    }

    public void setTitleFromModeNames(int position) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(actionBarNames.get(mode).get(position));
        }
    }


    @Override
    public void onBackPressed() {
        switch (mode) {
            case "main":
                super.onBackPressed();
                break;
            case "course_graphs":
                ((FactoryEducationFragment)
                        ((MyFragmentPagerAdapter) pager.getAdapter())
                                .getItem(pager.getCurrentItem())).save();
                if (pager.getCurrentItem() == 0) {
                    mode_fragments.remove(mode);
                    mode_fragments
                            .put(mode, ((MyFragmentPagerAdapter) pager.getAdapter())
                                    .getFragments());
                    toMain();
                    pager.setCurrentItem(1);
                    return;
                }
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                break;
            case "gener_task":
                if (((EditText) findViewById(R.id.task_answer))
                        .getText().toString().equals("")) {
                    toMain();
                    pager.setCurrentItem(3);
                }
                else {
                    ((EditText) findViewById(R.id.task_answer)).setText("");
                }
                break;
            case "guide_inner":
                toMain();
                pager.setCurrentItem(0);
                break;
            case "about":
                toMain();
                pager.setCurrentItem(4);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (mode) {
            case "main":
                switch(item.getItemId()) {
                    case R.id.calc:
                        mode_fragments.get("main").set(2, new CalculatorFragment());
                        actionBarNames.get("main").set(2, "Калькулятор");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh13:
                        mode_fragments.get("main").set(2, new Resh13Fragment());
                        actionBarNames.get("main").set(2, "Решатор");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh26:
                        mode_fragments.get("main").set(2, new Resh26Fragment());
                        actionBarNames.get("main").set(2, "Решатор");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                }
                return super.onOptionsItemSelected(item);
            case "course_graphs":
                switch (item.getItemId()) {
                    case android.R.id.home:
                        onBackPressed();
                        return true;
            }
            case "gener_task":
                switch (item.getItemId()) {
                    case android.R.id.home:
                        toMain();
                        pager.setCurrentItem(3);
                        return true;
                }
            case "guide_inner":
                switch (item.getItemId()) {
                    case android.R.id.home:
                        onBackPressed();
                        return true;
                }
            case "about":
                switch (item.getItemId()) {
                    case android.R.id.home:
                        onBackPressed();
                        return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickCheckAnswer(View view) {
        ((TaskViewFragment)
            ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).onClickCheckAnswer(view);

    }

    // calculator
    public void calc_onClickEvaluate(View view) {
        CharSequence answer = ((TextView) findViewById(R.id.calc_answer)).getText();
        calc_text.clear();
        calc_text.add((String) answer.subSequence(3, answer.length()));
        ((TextView) findViewById(R.id.calc_enter))
                .setText(calc_text.toText());
    }
    public void calc_onClickInstantEvaluate(View view) {
        Expression e = new Expression(new ExtraCalcFuncs().getExtraCalcFuncs());
        
        MyArrayList<String> calc_text_copy = (MyArrayList<String>) calc_text.clone();

        int div_i;
        while (calc_text_copy.contains(" div ")) {
            div_i = calc_text_copy.indexOf(" div ");
            try {
                calc_text_copy.set(div_i-1, String.valueOf(Math.floor(
                        Double.valueOf(calc_text_copy.get(div_i-1)) /
                        Double.valueOf(calc_text_copy.get(div_i+1)))));
                calc_text_copy.remove(div_i); calc_text_copy.remove(div_i);
            } catch (Exception ex) {
                break;
            }
        }

        e.setExpressionString(calc_text_copy.toText()
                .replace("mod", "#")
                .replace("log", "my_log")
                .replace("not", "bnot")
                .replace("and", "@&")
                .replace("or", "@|")
                .replace("0(", "0*(")
                .replace("1(", "1*(")
                .replace("2(", "2*(")
                .replace("3(", "3*(")
                .replace("4(", "4*(")
                .replace("5(", "5*(")
                .replace("6(", "6*(")
                .replace("7(", "7*(")
                .replace("8(", "8*(")
                .replace("9(", "9*(")
                .replace("my_log2*", "my_log2"));

        if ((int)((Spinner) findViewById(R.id.calc_num_system_spinner)).getSelectedItem() != 10)
            ((TextView) findViewById(R.id.calc_preview))
                    .setText(calc_text_copy.toText((int)((Spinner) findViewById(R.id.calc_num_system_spinner)).getSelectedItem()));
        else
            ((TextView) findViewById(R.id.calc_preview)).setText("");

        if (!MyProgram.StripInt(e.calculate()).equals("NaN"))
            ((TextView) findViewById(R.id.calc_answer))
                    .setText(" = " + ToNumSystem.run(e.calculate(),
                                    (int)((Spinner) findViewById(R.id.calc_num_system_spinner)).getSelectedItem()));
    }
    public void calc_onClickButton(View view) {
        String text = (String) ((TextView) findViewById(R.id.calc_enter)).getText();
        switch (view.getId()) {
            case (R.id.calc_eval):
                calc_onClickEvaluate(view);
                return;
            case (R.id.calc_backspace):
                if ("0123456789".contains(String.valueOf(calc_text.getLast().charAt(0)))) {
                    if (calc_text.getLast().length() > 2 ||
                            !(calc_text.getLast().length() == 2
                                    == calc_text.getLast().startsWith("-"))) {
                        calc_text.setLast(calc_text.getLast()
                                .substring(0, calc_text.getLast().length() - 1));
                    } else {
                        calc_text.removeLast();
                    }
                } else {
                    calc_text.removeLast();
                }
                if (calc_text.size() == 0)
                    calc_text.add("0");
                break;
            case (R.id.calc_clear):
                calc_text.clear();
                calc_text.add("0");
                break;
            default:
                if ("0123456789".contains(((Button) view).getText())) {
                    if (calc_text.getLast().equals("0") && calc_text.size() == 1) {
                        calc_text.setLast((String) ((Button) view).getText());
                    } else if ("0123456789".contains(String.valueOf(calc_text.getLast().charAt(0))) ||
                            (calc_text.getLast().charAt(0) == '-' &&
                                    "0123456789".contains(String.valueOf(calc_text.getLast().charAt(1))))) {
                        calc_text.setLast(calc_text.getLast() + ((Button) view).getText());
                    } else {
                        if (calc_text.size() == 2) {  // for assert
                            if (calc_text.getLast().equals(" - ") && calc_text.get(0).equals("0")) {
                                calc_text.clear();
                                calc_text.add("-" + ((Button) view).getText());
                            } else
                                calc_text.add((String) ((Button) view).getText());
                        } else
                            calc_text.add((String) ((Button) view).getText());
                    }
                } else {
                    if (((String) ((Button) view).getText()).contains("log2"))
                        calc_text.add(((String) ((Button) view).getText())
                                .replace("log2", "log2("));
                    else
                        calc_text.add(((String) ((Button) view).getText())
                                .replace("not", "not(")
                                .replace("log", "log(")
                                .replace("!", "! "));
                }
        }
        ((TextView) findViewById(R.id.calc_enter))
                .setText(calc_text.toText());

        calc_onClickInstantEvaluate(view);
    }


    // 13
    public void onClickStart(View view) {
        ((Resh13Fragment) ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).onClickStart(view);
    }

    // 26
    public void onClickRun26(View view) {
        ((Resh26Fragment) ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).onClickRun26(view);
    }
    public void onClickBack26(View view) {
        ((Resh26Fragment) ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).onClickBack26(view);
    }
    public void corrEdits(View view) {
        ((Resh26Fragment) ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).corrEdits(view);
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments = new ArrayList<>();

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if ((0 <= position) && (position < getCount())) {
                return fragments.get(position);
            }
            return fragments.get(0);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }

        public List<Fragment> getFragments() {
            return fragments;
        }
        private void addFragment(Fragment fragment) {
            fragments.add(fragment);
            notifyDataSetChanged();
        }
        private void setList(List<Fragment> fragments) {
            this.fragments = fragments;
            notifyDataSetChanged();
        }
        private void clearList() {
            fragments = new ArrayList<>();
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }
}
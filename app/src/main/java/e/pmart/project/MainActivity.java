package e.pmart.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goto_fragment = (GotoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.down_menu_main);

        create_fragment_unions();

        pager = findViewById(R.id.view_pager_main);
        slides = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(slides);

        toMain();

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    //  main part
    public void onClickBackToOld(View view) {
        Intent Act = new Intent(getApplicationContext(), MainActivity1.class);
        startActivity(Act);
    }
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
                toMain();
                pager.setCurrentItem(0);
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
            case R.id.course_graphs:
                mode = "course_graphs";

                if (actionBar != null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                break;
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
    public void create_fragment_unions() {
        /*     main     */
        mode_fragments.put("main", new ArrayList<Fragment>());
        actionBarNames.put("main", new ArrayList<String>());

        mode_fragments.get("main").add(new EducationFragment());
        actionBarNames.get("main").add("Курсы");
        mode_fragments.get("main").add(new CalculatorFragment());
        actionBarNames.get("main").add("Решатор");
            //mode_fragments.get("main").add(new EvalFragment());
        mode_fragments.get("main").add(new GenerFragment()
                .setSupportActionBar(getSupportActionBar())
                .setSupportFragmentManager(getSupportFragmentManager())
                .setMainActivity(this));
        actionBarNames.get("main").add("Задачник");
        mode_fragments.get("main").add(new GuideFragment()
                .setSupportActionBar(getSupportActionBar())
                .setSupportFragmentManager(getSupportFragmentManager())
                .setMainActivity(this));
        actionBarNames.get("main").add("Справочник");
        mode_fragments.get("main").add(new OtherFragment());
        actionBarNames.get("main").add("Дополнительно");

        /*     course_graphs     */
        mode_fragments.put("course_graphs", new ArrayList<Fragment>());
        actionBarNames.put("course_graphs", new ArrayList<String>());

        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newTitle(R.string.graphs_1)
                .newText(R.string.graphs_2));
        actionBarNames.get("course_graphs").add("Графы");
        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newText(R.string.graphs_task1)
                .newChoice(R.array.choice_graphs_task1));
        actionBarNames.get("course_graphs").add("Графы");

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
    public void toMain() {
        mode = "main";
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        findViewById(R.id.down_menu_main).setVisibility(View.VISIBLE);

        ((MyFragmentPagerAdapter) pager.getAdapter()).setList(mode_fragments.get(mode));

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTitleFromModeNames(position);

                goto_fragment.setSelection(position);
            }

            @Override
            public void onPageSelected(int position) {
                goto_fragment.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void toInnerPages() {
        findViewById(R.id.down_menu_main).setVisibility(View.GONE);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageSelected(position);
            }

            @Override
            public void onPageSelected(int position) {
                setTitleFromModeNames(position);
                goto_fragment.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ((MyFragmentPagerAdapter) pager.getAdapter()).setList(mode_fragments.get(mode));

        pager.clearOnPageChangeListeners();
    }

    public void setTitleFromModeNames(int position) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle((String) actionBarNames.get(mode).get(position));
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
                    pager.setCurrentItem(0);
                }
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                break;
            case "gener_task":
                if (((EditText) findViewById(R.id.task_answer))
                        .getText().toString().equals("")) {
                    toMain();
                    pager.setCurrentItem(2);
                }
                else {
                    ((EditText) findViewById(R.id.task_answer)).setText("");
                }
                break;
            case "guide_inner":
                toMain();
                pager.setCurrentItem(3);
                break;
            case "about":
                toMain();
                pager.setCurrentItem(4);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (mode) {
            case "main":
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
                        pager.setCurrentItem(2);
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

    public void onClickCheckAnswer(View view) {
        ((TaskViewFragment)
            ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).onClickCheckAnswer(view);
    }

    public void calc_onClickEvaluate(View view) {
        CharSequence answer = ((TextView) findViewById(R.id.calc_answer)).getText();
        ((TextView) findViewById(R.id.calc_enter))
                .setText(answer.subSequence(3, answer.length()));
    }

    public void calc_onClickInstantEvaluate(View view) {
        Expression e = new Expression(new ExtraCalcFuncs().getExtraCalcFuncs());

        e.setExpressionString(((TextView) findViewById(R.id.calc_enter)).getText().toString()
                .replace("²√", "square_root")
                .replace("³√", "volume_root")
                .replace("0(", "0*(")
                .replace("1(", "1*(")
                .replace("2(", "2*(")
                .replace("3(", "3*(")
                .replace("4(", "4*(")
                .replace("5(", "5*(")
                .replace("6(", "6*(")
                .replace("7(", "7*(")
                .replace("8(", "8*(")
                .replace("9(", "9*("));

        if (!MyProgram.StripInt(e.calculate()).equals("NaN"))
            ((TextView) findViewById(R.id.calc_answer))
                    .setText(" = " + MyProgram.StripInt(e.calculate()));
    }

    public void calc_onClickButton(View view) {
        String text = (String) ((TextView) findViewById(R.id.calc_enter)).getText();
        switch (view.getId()) {
            case (R.id.calc_eval):
                calc_onClickEvaluate(view);
                return;
            case (R.id.calc_backspace):
                if (text.length() > 1 && !(text.length() == 2 == text.startsWith("-")))
                    ((TextView) findViewById(R.id.calc_enter))
                            .setText(text.substring(0, text.length() - 1));
                else
                    ((TextView) findViewById(R.id.calc_enter))
                            .setText("0");
                break;
            case (R.id.calc_clear):
                ((TextView) findViewById(R.id.calc_enter))
                        .setText(0);
                break;
            default:
                if ("0123456789".contains(((Button) view).getText()) && text.equals("0")) {
                    ((TextView) findViewById(R.id.calc_enter))
                            .setText(((Button) view).getText());
                } else {
                    ((TextView) findViewById(R.id.calc_enter))
                            .setText(text + ((Button) view).getText());
                }
        }
        calc_onClickInstantEvaluate(view);
    }
}
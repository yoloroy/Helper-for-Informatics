package e.pmart.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.LinearLayout;

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
                    if (mode.equals("main")) goto_fragment.setSelection(position+1);
                }
                else {
                    setTitleFromModeNames(position);
                    if (mode.equals("main")) goto_fragment.setSelection(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setTitleFromModeNames(position);
                if (mode.equals("main")) goto_fragment.setSelection(position);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
                }

                switch (mode) {
                    case "main":
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
                        break;
                    case "gener_task":
                        if (menu != null) {
                            menu.clear();
                            getMenuInflater().inflate(R.menu.task_view_bar, menu);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toMain();
        pager.setCurrentItem(2);
        setTitleFromModeNames(2);

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
        menu.clear();
        getMenuInflater().inflate(R.menu.calc_bar, menu);
        return true;
    }

    public void showHello() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добро пожаловать в \"Инфу на 5\"!")
                .setMessage("Мы вам поможем справиться с заданиями по информатике и легче усвоить материал.")
                .setCancelable(false)
                .setNegativeButton("Спасибо",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        //alert.setFeatureDrawable(Color.WHITE, getDrawable(R.drawable.rect_corners_s));
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
            case "course_la":
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
        mode_fragments.put("course_graphs", new ArrayList<>());
        actionBarNames.put("course_graphs", new ArrayList<>());

        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newTitle(R.string.graphs_1)
                .newText(R.string.graphs_2)
                .newImage(R.mipmap.graph_visualisation));
        actionBarNames.get("course_graphs").add("Графы");
        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newText(R.string.graphs_task1)
                .newChoice(R.array.choice_graphs_task1));
        actionBarNames.get("course_graphs").add("Графы");
        /* algebra of logic */
        mode_fragments.put("course_la", new ArrayList<>());
        actionBarNames.put("course_la", new ArrayList<>());

        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_1)
                .newText(R.string.course_la_2)
                .newTitle(R.string.course_la_3)
                .newText(R.string.course_la_4));
        actionBarNames.get("course_la").add("Алгебра логики");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_5)
                .newTextList(R.array.course_la_signs));
        actionBarNames.get("course_la").add("Алгебра логики");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_6)
                .newText(R.string.course_la_7)
                .newText(R.string.course_la_8)
                .newTextList(R.array.course_la_9));
        actionBarNames.get("course_la").add("Основные символы");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_10)
                .newText(R.string.course_la_11)
                .newText(R.string.course_la_12)
                .newTextList(R.array.course_la_13));
        actionBarNames.get("course_la").add("Основные символы");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_14)
                .newText(R.string.course_la_15)
                .newText(R.string.course_la_16)
                .newTitle(R.string.course_la_17)
                .newTextList(R.array.course_la_18));
        actionBarNames.get("course_la").add("Основные символы");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_adv_title_1)
                .newText(R.string.course_la_adv_text_1)
                .newTextList(R.array.course_la_adv_list_1)
                .newTitle(R.string.course_la_adv_title_2)
                .newTextList(R.array.course_la_adv_list_2));
        actionBarNames.get("course_la").add("Расширенные символы");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_adv_title_3)
                .newText(R.string.course_la_adv_text_2)
                .newTextList(R.array.course_la_adv_list_3)
                .newText(R.string.course_la_adv_text_3)
                .newTitle(R.string.course_la_adv_title_4)
                .newTextList(R.array.course_la_adv_list_4));
        actionBarNames.get("course_la").add("Расширенные символы");
        mode_fragments.get("course_la").add(new FactoryEducationFragment()
                .newTitle(R.string.course_la_adv_title_5)
                .newText(R.string.course_la_adv_text_4)
                .newTextList(R.array.course_la_adv_list_5)
                .newTitle(R.string.course_la_adv_title_6)
                .newTextList(R.array.course_la_adv_list_6));
        actionBarNames.get("course_la").add("Расширенные символы");
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
            case "course_la":
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
                //if (((EditText) findViewById(R.id.task_answer))
                //        .getText().toString().equals("")) {
                    toMain();
                    pager.setCurrentItem(3);
                //}
                //else {
                //    ((EditText) findViewById(R.id.task_answer)).setText("");
                //}
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
                    case R.id.resh1:
                        mode_fragments.get("main").set(2, new Resh1Fragment());
                        actionBarNames.get("main").set(2, "Решатор №1");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh2:
                        mode_fragments.get("main").set(2, new Resh2Fragment());
                        actionBarNames.get("main").set(2, "Решатор №2");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh3:
                        mode_fragments.get("main").set(2, new Resh3Fragment());
                        actionBarNames.get("main").set(2, "Решатор №3");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh13:
                        mode_fragments.get("main").set(2, new Resh13Fragment());
                        actionBarNames.get("main").set(2, "Решатор №13");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh15:
                        mode_fragments.get("main").set(2, new Resh15Fragment());
                        actionBarNames.get("main").set(2, "Решатор №15");
                        toMain();
                        pager.setCurrentItem(2);
                        break;
                    case R.id.resh26:
                        mode_fragments.get("main").set(2, new Resh26Fragment());
                        actionBarNames.get("main").set(2, "Решатор №26");
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
            case "course_la":
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
                    case R.id.sdamgia_link:
                        Intent open_link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://inf-ege.sdamgia.ru/"));
                        startActivity(open_link);
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

    // 26
    public void corrEdits(View view) {
        ((Resh26Fragment) ((MyFragmentPagerAdapter) pager.getAdapter())
                .getItem(pager.getCurrentItem())).corrEdits(view);
    }
}
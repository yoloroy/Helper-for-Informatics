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
import android.widget.EditText;
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

    ViewPager pager;


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

        switch (view.getId()) {
            case R.id.course_graphs:
                mode = "course_graphs";

                ActionBar actionBar = getSupportActionBar();
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

        mode_fragments.get("main").add(new EducationFragment());
        mode_fragments.get("main").add(new EvalFragment());
        mode_fragments.get("main").add(new GenerFragment()
                .setSupportActionBar(getSupportActionBar())
                .setSupportFragmentManager(getSupportFragmentManager())
                .setMainActivity(this));

        //mode_fragments.get("main").add(new GuideFragment());
        //mode_fragments.get("main").add(new OtherFragment());

        /*     course_graphs     */
        mode_fragments.put("course_graphs", new ArrayList<Fragment>());
        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
        //        .newTitle(R.string.graphs_1)
        //        .newText(R.string.graphs_2)
                .newTextList(R.array.main));
        mode_fragments.get("course_graphs").add(new FactoryEducationFragment()
                .newText(R.string.graphs_task1)
                .newChoice(R.array.choice_graphs_task1));

        /*     task     */
        mode_fragments.put("gener_task", new ArrayList<Fragment>());
        mode_fragments.get("gener_task").add(new TaskViewFragment());
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

        ((MyFragmentPagerAdapter) pager.getAdapter()).setList(mode_fragments.get(mode));

        pager.clearOnPageChangeListeners();
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
}
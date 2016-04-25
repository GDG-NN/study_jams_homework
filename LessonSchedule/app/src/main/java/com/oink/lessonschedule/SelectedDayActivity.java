package com.oink.lessonschedule;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectedDayActivity extends AppCompatActivity {

    private JSONObject jsonDayObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            jsonDayObject = new JSONObject(intent.getStringExtra("jsonDay"));
            toolbar.setTitle(jsonDayObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        try {
            setupViewPager(viewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) throws JSONException {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        Fragment evenFragment = new ScheduleFragment();
        Fragment oddFragment = new ScheduleFragment();

        Bundle evenBundle = new Bundle();
        evenBundle.putString("json", jsonDayObject.getJSONArray("lessonsEven").toString());
        evenFragment.setArguments(evenBundle);

        Bundle oddBundle = new Bundle();
        oddBundle.putString("json", jsonDayObject.getJSONArray("lessonsOdd").toString());
        oddFragment.setArguments(oddBundle);

        adapter.addFragment(evenFragment, "Четная");
        adapter.addFragment(oddFragment, "Нечетная");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

package com.example.ljb.jbapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button uploadBtn, recordBtn, record2Btn, upload2Btn;
    public static final int RECORD_MENU = 100;
    public static final int RECORD2_MENU = 101;
    public static final int UPLOAD_MENU = 102;
    public static final int UPLOAD2_MENU = 103;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabTextColors(Color.RED, Color.RED);
        tabLayout.setBackgroundColor(Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

    }



}
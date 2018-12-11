package com.example.ljb.jbapp.ChatTabFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ljb.jbapp.ChatTabFrag.TabFragment1;
import com.example.ljb.jbapp.ChatTabFrag.TabFragment2;
import com.example.ljb.jbapp.ChatTabFrag.TabFragment3;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    private String[] tabTitles = new String[]{"녹음", "스트리밍", "결과"};

    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

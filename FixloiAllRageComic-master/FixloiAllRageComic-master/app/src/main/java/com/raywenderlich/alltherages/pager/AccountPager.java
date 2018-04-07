package com.raywenderlich.alltherages.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



/**
 * Created by ToanTV on 3/26/2017.
 */

public class AccountPager extends FragmentStatePagerAdapter{
    int tabCount;

    public AccountPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return null;
            case 1:
                return null;
            case 2:
                return null;
            case 3:
                return null;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

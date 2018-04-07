package com.raywenderlich.alltherages.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.raywenderlich.alltherages.fragments.RageComicConsultsFragment;
import com.raywenderlich.alltherages.fragments.RageComicListFragment;
import com.raywenderlich.alltherages.fragments.RageComicServicesFragment;


public class Pager extends FragmentStatePagerAdapter {
    int tabCount;
    //RageComicListFragment rageComicListFragment;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new RageComicListFragment();
            case 1:
                return new RageComicConsultsFragment();
            case 2:
                return new RageComicServicesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

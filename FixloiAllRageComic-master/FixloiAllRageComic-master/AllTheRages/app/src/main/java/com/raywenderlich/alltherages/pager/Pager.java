package com.raywenderlich.alltherages.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.raywenderlich.alltherages.RageComicConsultFragment;
import com.raywenderlich.alltherages.RageComicProductFragment;
import com.raywenderlich.alltherages.RageComicServicesFragment;

/**
 * Created by laptopTCC on 6/16/2017.
 */



public class Pager extends FragmentStatePagerAdapter {
    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new RageComicProductFragment();
            case 1:
                return new RageComicServicesFragment();
            case 2:
                return new RageComicConsultFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}

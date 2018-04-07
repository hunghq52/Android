package com.raywenderlich.alltherages.eventbus;

import android.support.v4.app.FragmentManager;

/**
 * Created by laptopTCC on 6/16/2017.
 */

public class ChangeRageComicListFragment {
    FragmentManager fragmentManager;

    public ChangeRageComicListFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}

package com.raywenderlich.alltherages.utils;

import android.support.v4.app.Fragment;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public interface FragmentListener{
    void onChangeFragment(Fragment frag, boolean addToBackStack);
}

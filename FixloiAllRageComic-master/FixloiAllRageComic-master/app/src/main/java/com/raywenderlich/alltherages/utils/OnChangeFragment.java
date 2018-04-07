package com.raywenderlich.alltherages.utils;

import android.support.v4.app.Fragment;

import com.raywenderlich.alltherages.R;

/**
 * Created by laptopTCC on 5/28/2017.
 */

public abstract class OnChangeFragment {
    public void onChangeFragment(Fragment originFragment, Fragment changeFragment, boolean addToBackStack){
        if(addToBackStack){
            originFragment.getFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, changeFragment, "ChangeFragment")
                    .addToBackStack(null)
                    .commit();
        } else {
            originFragment.getFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, changeFragment, "ChangeFragment")
                    .commit();
        }
    }
}

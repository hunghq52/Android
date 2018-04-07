package com.raywenderlich.alltherages.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raywenderlich.alltherages.R;

/**
 * Created by laptopTCC on 6/6/2017.
 */

public class ScheduleFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_rage_comic_details,
                container,
                false);
        //setupUI(view);
        return view;
    }
}

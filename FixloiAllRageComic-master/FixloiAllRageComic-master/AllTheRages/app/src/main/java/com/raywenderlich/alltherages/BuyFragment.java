package com.raywenderlich.alltherages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by laptopTCC on 6/6/2017.
 */

public class BuyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_rage_comic_details,
                container,
                false);
        setHasOptionsMenu(true);
        //setupUI(view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem sv_search = menu.findItem(R.id.sv_search);
        MenuItem action_cart = menu.findItem(R.id.action_cart);
        sv_search.setVisible(false);
        action_cart.setVisible(false);
    }
}

package com.raywenderlich.alltherages;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raywenderlich.alltherages.pager.Pager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RageComicListFragment extends Fragment implements TabLayout.OnTabSelectedListener {
  private static String TAG = RageComicListFragment.class.toString();
  @BindView(R.id.tl_tab)
  TabLayout tabLayout;
  @BindView(R.id.pager)
  ViewPager viewPager;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_rage_comic_list, container, false);
    setupUI(view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  private void setupUI(View view) {
    ButterKnife.bind(this,view);

    tabLayout.setupWithViewPager(viewPager);
    //create pager adapter
    //Creating our pager adapter
    Pager adapter = new Pager(this.getChildFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(this);
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {

  }

  public void onChangeFragement(){
    getFragmentManager().beginTransaction()
            .addToBackStack(null)
            .commit();
  }

}

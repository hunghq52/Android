package com.raywenderlich.alltherages.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.activities.RageActivity;
import com.raywenderlich.alltherages.activities.ShipActivity;
import com.raywenderlich.alltherages.adapter.RageComicAdapterAustralia;
import com.raywenderlich.alltherages.adapter.RageComicAdapterFrance;
import com.raywenderlich.alltherages.adapter.RageComicAdapterGermany;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.database.model.SingleOrder;
import com.raywenderlich.alltherages.eventbus.RageComicBuy;
import com.raywenderlich.alltherages.eventbus.RageComicReturn;
import com.raywenderlich.alltherages.utils.FragmentListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RageComicListFragment extends Fragment{
  private static String TAG = RageComicListFragment.class.toString();
  @BindView(R.id.recycler_view_france)
  RecyclerView recyclerViewFrance;
  @BindView(R.id.recycler_view_germany)
  RecyclerView recyclerViewGermany;
  @BindView(R.id.recycler_view_australia)
  RecyclerView recyclerViewAustralia;
  @BindView(R.id.swipeRefreshLayout)
  PullRefreshLayout layout;
  Dialog dialog_buy;
  private FragmentListener fragmentListener;
  private Boolean onPause = true;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_rage_comic_list, container, false);
    EventBus.getDefault().register(this);
    setupUI(view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    fragmentListener = (FragmentListener) context;
  }

  @Override
  public void onDestroyView(){
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  private void setupUI(View view) {
    ButterKnife.bind(this,view);
    RageComicAdapterFrance rageComicAdapterFrance = new RageComicAdapterFrance();
    RageComicAdapterGermany rageComicAdapterGermany = new RageComicAdapterGermany();
    RageComicAdapterAustralia rageComicAdapterAustralia = new RageComicAdapterAustralia();
    if (recyclerViewFrance == null){
      Log.d(TAG,String.format("recyclerViewFrance is null"));
    }
    recyclerViewFrance.setAdapter(rageComicAdapterFrance);
    //recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    recyclerViewFrance.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayout.HORIZONTAL, false));
    recyclerViewGermany.setAdapter(rageComicAdapterGermany);
    recyclerViewGermany.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayout.HORIZONTAL, false));
    recyclerViewAustralia.setAdapter(rageComicAdapterAustralia);
    recyclerViewAustralia.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayout.HORIZONTAL, false));
  }

  //chuyen toi rageActivity
  @Subscribe
  public void ChangeActivity(RageComicReturn rage){
    RageActivity.rageComic = rage.getRageComic();
    Intent i = new Intent(getActivity(), RageActivity.class);
    startActivity(i);
    /*RageComicDetailsFragment rageComicDetailsFragment = new RageComicDetailsFragment();
    rageComicDetailsFragment.setRageComic(rageComicReturn.getRageComic());
    getFragmentManager().beginTransaction()
                        .replace(R.id.fl_main, rageComicDetailsFragment)
                        .addToBackStack(null)
                        .commit();*/
  }



  @Override
  public void onPause(){
      super.onPause();
  }

  //chuyen luu don hang vao DBContext va chuyen toi ShipActitivy
  @Subscribe
  public void getSingleOrder(final RageComicBuy rageComicBuy){
    //1. Lấy Được RageComic
    RageComic rageComic = rageComicBuy.getRageComic();
    //2. Tạo đơn hàng mặc định với số lượng là 1
    SingleOrder singleOrder = new SingleOrder(rageComic,1);
    //3. Check xem co rageComic trong order chua
    if(DBContext.instance.findRageComicSingleOrder(rageComicBuy.getRageComic())){
      Toast.makeText(this.getContext(), "Đã tồn tại trong giỏ hàng", Toast.LENGTH_SHORT).show();
    } else {
      //4. Lưu vào DBContext
      DBContext.instance.addOrUpdate(singleOrder);
      //5. Chuyển shipActivity
      if(DBContext.instance.getAllSingleOrder() != null){
        changeShipActivity();
      }
    }
  }

  private void changeShipActivity() {
      Intent i = new Intent(getContext(), ShipActivity.class);
      startActivity(i);
      /*ShipFragment shipFragment = new ShipFragment();
      getFragmentManager().beginTransaction()
              .replace(R.id.fl_main, shipFragment)
              .addToBackStack(null)
              .commit();*/
  }
}

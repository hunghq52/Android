package com.raywenderlich.alltherages;


import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class RageComicProductFragment extends Fragment {
    public static String TAG = RageComicProductFragment.class.toString();
    @BindView(R.id.recycler_view_france)
    RecyclerView recyclerViewFrance;
    @BindView(R.id.recycler_view_germany)
    RecyclerView recyclerViewGermany;
    @BindView(R.id.recycler_view_australia)
    RecyclerView recyclerViewAustralia;
    private FragmentListener fragmentListener;
    private Boolean onPause = true;

    public RageComicProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rage_comic_product, container, false);
        EventBus.getDefault().register(this);
        setupUI(view);
        return view;
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

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        fragmentListener = (FragmentListener) context;
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onChangeShipFragment(final RageComicBuy rageComicBuy){
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
            //5. Chuyển shipFragment
            onChangeShipFragment();
        }
    }

    @Subscribe
    public void onChangeRageDetailFragment(RageComicReturn rageComicReturn){
        RageComicDetailsFragment rageComicDetailsFragment = new RageComicDetailsFragment();
        rageComicDetailsFragment.setRageComic(rageComicReturn.getRageComic());
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_main, rageComicDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void onChangeShipFragment() {
        ShipFragment shipFragment = new ShipFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_main, shipFragment)
                .addToBackStack(null)
                .commit();
    }

}

package com.raywenderlich.alltherages.fragments;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.adapter.RageNotShipAdapter;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.model.SingleOrder;
import com.raywenderlich.alltherages.eventbus.SendRequestEvent;
import com.raywenderlich.alltherages.eventbus.TypeRequestEvent;
import com.raywenderlich.alltherages.utils.SharedPref;
import com.raywenderlich.alltherages.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipFragment extends Fragment {
    public static String TAG = ShipFragment.class.toString();
    @BindView(R.id.rv_not_ship_food)
    RecyclerView rvNotShipFood;
    RageNotShipAdapter rageNotShipAdapter;
    @BindView(R.id.tv_payment)
    TextView tvPayment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ship, container, false);
        setupUI(view);
        //loadDataForAdapter();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupUI(View view) {
        ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        tvPayment.setText(String.format("%s %s", Utils.getPrice(payment()),"VND"));
        Log.d(TAG,String.format("payment: %s",payment()));
        //rageNotShipAdapter.notifyDataSetChanged();
        rageNotShipAdapter = new RageNotShipAdapter(getActivity());
        if (rvNotShipFood == null){
            Log.d(TAG,String.format("recyclerShipFood is null"));
        }
        rvNotShipFood.setAdapter(rageNotShipAdapter);
        rvNotShipFood.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    /*private void loadDataForAdapter(){
        List<SingleOrder> list = DBContext.instance.getAllSingleOrder();
        rageNotShipAdapter.changeData(list);

    }*/

    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void deleteListener(final SendRequestEvent event){

        if(event.getTypeRequestEvent() == TypeRequestEvent.DELETE){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
            dialog.setTitle("Xóa " + event.getSingleOrder().getRageComic().getName() + " ra khỏi giỏ hàng");
            dialog.setMessage("Bạn chắc chắn chứ?");
            dialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int count  = SharedPref.instance.getCount();
                    count -= event.getSingleOrder().getCount();
                    SharedPref.instance.putCount(count);
                    DBContext.instance.deleteSingleOrder(event.getSingleOrder());
                    rageNotShipAdapter.notifyDataSetChanged();

                    EventBus.getDefault().post(new SendRequestEvent(event.getSingleOrder(),
                            TypeRequestEvent.CHANGE_BT_ADDTOCART));
                    EventBus.getDefault().post(new SendRequestEvent(TypeRequestEvent.CHANGE_PAYMENT));
                }
            });
            dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }
        if(DBContext.instance.getAllSingleOrder().size() == 0){
            emptyBill();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe
    public void setPayment(SendRequestEvent event) {
        if (event.getTypeRequestEvent() == TypeRequestEvent.CHANGE_PAYMENT) {
            Log.d(TAG, "setPayment: hi");
            tvPayment.setText(String.format("%s %s", Utils.getPrice(payment()),"VND"));
            SharedPref.instance.putTotalSpend(payment());
            rageNotShipAdapter.notifyDataSetChanged();
        } else if (event.getTypeRequestEvent() == TypeRequestEvent.QUANTITY_ERROR) {
            Log.d(TAG, "QUANTITY_ERROR: hi");
            Toast.makeText(getContext(),"Số lượng chỉ từ 1-10",Toast.LENGTH_SHORT).show();
        } else if(event.getTypeRequestEvent() == TypeRequestEvent.HIDE_INPUT){
            /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(event.getWindowToken(), 0);*/
        }
    }



    private float payment() {
        float payment = 0;
        for (SingleOrder s: DBContext.instance.getAllSingleOrder()){
            int sl = s.getCount();
            float price = s.getRageComic().getNew_price();
            payment += sl*price;
        }
        return payment;
    }

    private void emptyBill() {
        //Show hình ảnh của emptyBill lên
    }

}

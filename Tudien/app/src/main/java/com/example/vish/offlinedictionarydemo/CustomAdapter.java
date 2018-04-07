package com.example.vish.offlinedictionarydemo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DictObjectModel> dataSet;
    boolean check = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView word, meaning;

        RelativeLayout expandable;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.expandable = (RelativeLayout) itemView.findViewById(R.id.expandableLayout);
            this.word = (TextView) itemView.findViewById(R.id.wordtext);
            this.meaning = (TextView) itemView.findViewById(R.id.meaningtext);

        }
    }

    public CustomAdapter(ArrayList<DictObjectModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = !check;
                Log.d("aaa", "onClick: ");

                if (myViewHolder.expandable.getVisibility() == View.VISIBLE) {
                    myViewHolder.expandable.setVisibility(View.GONE);

                    myViewHolder.expandable.animate()
                            .alpha(0.0f)
                            .setDuration(1000);


                    //  myViewHolder.schedule.setVisibility(View.VISIBLE);

                } else {
                    myViewHolder.expandable.setVisibility(View.VISIBLE);
                    myViewHolder.expandable.animate()
                            .alpha(1.0f)
                            .setDuration(1000);


                }
            }
        });

        return myViewHolder;
    }

    @Override
    //vẽ từ tại vị trí position
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView word1 = holder.word;
        TextView meaning1 = holder.meaning;

        word1.setText(dataSet.get(listPosition).getWord());
        meaning1.setText(dataSet.get(listPosition).getMeaning());

    }

    @Override
    //lấy số lượng từ
    public int getItemCount() {
        return dataSet.size();
    }
}
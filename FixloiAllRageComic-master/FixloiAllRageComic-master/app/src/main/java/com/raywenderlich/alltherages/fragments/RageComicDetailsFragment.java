package com.raywenderlich.alltherages.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.model.RageComic;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RageComicDetailsFragment extends Fragment {
  private static String TAG = RageComicDetailsFragment.class.toString();
  @BindView(R.id.tv_name_detail)
  TextView name;
  @BindView(R.id.im_comic_detail)
  ImageView imageView;
  @BindView(R.id.tv_description)
  TextView description;
  @BindView(R.id.bt_buy)
  Button bt_buy;
  Dialog dialog_buy;

  public RageComic rageComic;

  public void setRageComic(RageComic rageComic) {
    this.rageComic = rageComic;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
    View view = layoutInflater.inflate(R.layout.fragment_rage_comic_details,
            container,
            false);
    dialog_buy = new Dialog(this.getContext());
    dialog_buy.setContentView(R.layout.dialog_buy);
    dialog_buy.setTitle("BUY RAGE");
    setupUI(view);
    return view;
  }

  private void setupUI(View view) {
    ButterKnife.bind(this,view);
    String mName = rageComic.getName();
    name.setText(mName);
    Log.d(TAG,String.format("Url comic:%s",rageComic.getUrl()));
    Picasso.with(getContext()).load(rageComic.getUrl()).into(imageView);
    String mDescription = rageComic.getDescription();
    description.setText(mDescription);

    bt_buy.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          dialog_buy.show();
      }
    });
  }
}

package com.raywenderlich.alltherages.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.raywenderlich.alltherages.R;
import com.raywenderlich.alltherages.database.DBContext;
import com.raywenderlich.alltherages.database.RequestServerManager;
import com.raywenderlich.alltherages.fragments.BuyFragment;
import com.raywenderlich.alltherages.fragments.ChatFragment;
import com.raywenderlich.alltherages.fragments.CustomerFragment;
import com.raywenderlich.alltherages.fragments.ScheduleFragment;
import com.raywenderlich.alltherages.pager.Pager;
import com.raywenderlich.alltherages.utils.CircleTransform;
import com.raywenderlich.alltherages.utils.FragmentListener;
import com.raywenderlich.alltherages.utils.SharedPref;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener,FragmentListener, SearchView.OnQueryTextListener{
  public static String TAG = MainActivity.class.toString();
  ActionBarDrawerToggle toggle;
  FrameLayout fl_cart;
  FrameLayout circle;
  TextView countTextView;
  int count;
  TabLayout tabLayout;
  ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //Pager.loadData();
    tabLayout = (TabLayout) findViewById(R.id.tl_tab);
    viewPager = (ViewPager) findViewById(R.id.pager);
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setItemIconTintList(null);
    navigationView.setNavigationItemSelectedListener(this);
    View header = navigationView.getHeaderView(0);
    CircleImageView civ_user = (CircleImageView) header.findViewById(R.id.civ_user);
    TextView tv_username = (TextView) header.findViewById(R.id.tv_username);
    TextView tv_email_facebook = (TextView) header.findViewById(R.id.tv_email_facebook);
    TextView tv_mobile = (TextView) header.findViewById(R.id.tv_mobile);

    //Lấy nội dung bắn từ LoginActivity sang
    Intent i = getIntent();
    Log.d(TAG,String.format("Name: %s; User: %s; Url_avatar: %s; Facebook: %s; Email: %s; Phone: %s",i.getStringExtra("name"),i.getStringExtra("user"),
            i.getStringExtra("url_avatar"), i.getStringExtra("facebook"), i.getStringExtra("email"),i.getStringExtra("phone_number")));
    tv_username.setText(i.getStringExtra("name"));

    if(i.getStringExtra("url_avatar") != null){
      Picasso.with(this).load(i.getStringExtra("url_avatar"))
              .transform(new CircleTransform()).into(civ_user);
    }

    tv_email_facebook.setText(i.getStringExtra("email"));
    tv_mobile.setText(i.getStringExtra("phone_number"));

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    //
    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
      @Override
      public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
          // set toggle icon arrow
          toggle.setDrawerIndicatorEnabled(false);
          toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
          toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
          });
        } else {
          toggle.setDrawerIndicatorEnabled(true);
          toggle.setToolbarNavigationClickListener(null);
        }
      }
    });

    //Pager.loadData();
    //Xet loadData xong hay chua:
    //hien thi loadingFragment
    loadData();
    Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(this);
  }

  public void loadData(){
    RequestServerManager.instance.getRageFromServer();
    RequestServerManager.instance.setRageListener(new RequestServerManager.GetRageListener() {
      @Override
      public void onGetAllRage(boolean ok) {
        if(ok){
          //đã lấy được toàn bộ data từ server và lưu vào DBContext
          //changeFragment
        }
        else {
          //chưa load data thành công, lấy trên DBContext
          DBContext.instance.getAllRageComic();
        }
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    getMenuInflater().inflate(R.menu.cart_menu,menu);
    final MenuItem searchItem = menu.findItem(R.id.sv_search);
    final MenuItem shipItem = menu.findItem(R.id.action_cart);

    return true;
  }

  public boolean onOptionsItemSelected(MenuItem menuItem){
    switch (menuItem.getItemId()) {
      case R.id.action_cart: {
        Log.d(TAG, "onOptionsItemSelected: ");
        //onChangeFragment(new ShipFragment(), true);
        Intent i = new Intent(this,ShipActivity.class);
        startActivity(i);
        break;
      }
      case R.id.sv_search: {
        Log.d(TAG, "onSearchViewItemSelected: ");
        //do Searchview
        break;
      }
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu){
    //Do something
    //Nếu có đơn hàng đang chờ ship thì hiển thị thông báo
    final MenuItem alertMenuItem = menu.findItem(R.id.action_cart);
    FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
    fl_cart = (FrameLayout) rootView.findViewById(R.id.fl_cart);
    circle = (FrameLayout) rootView.findViewById(R.id.fl_noti);
    countTextView = (TextView) rootView.findViewById(R.id.tv_count);
    count = SharedPref.instance.getSharedPreferences().getInt("COUNT",0);
    if (0 < count){
      countTextView.setText(String.valueOf(count));
      circle.setVisibility(View.VISIBLE);
    } else {
      circle.setVisibility(View.GONE);
    }
    rootView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onOptionsItemSelected(alertMenuItem);
      }
    });
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  protected void onRestart() {
    //Khi start thì lại cho circle nhìn thấy hoặc không nhìn thấy (phụ thuộc vào số lượng.
    count = SharedPref.instance.getSharedPreferences().getInt("COUNT", 0);
    if (0 < count) {
      countTextView.setText(String.valueOf(count));
      circle.setVisibility(View.VISIBLE);
    } else {
      circle.setVisibility(View.GONE);
    }
    super.onRestart();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_person) {
        onChangeFragment(new CustomerFragment(), true);
    } else if (id == R.id.nav_schedule) {
        onChangeFragment(new ScheduleFragment(), true);
    } else if (id == R.id.nav_chat) {
        onChangeFragment(new ChatFragment(), true);
    } else if (id == R.id.nav_phonecall) {
        onChangeFragment(new BuyFragment(), true);
    } else if (id == R.id.nav_favorite) {
        onChangeFragment(new ScheduleFragment(), true);
    } else if (id == R.id.nav_signout){
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        SharedPref.instance.cleanAll();
        startActivity(intent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  @Override
  public void onChangeFragment(Fragment fragment, boolean addToBackStack) {
    if(addToBackStack){
      getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fl_main, fragment,"rageComicDetails")
              .addToBackStack(null)
              .commitAllowingStateLoss();
    } else {
      getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fl_main, fragment,"rageComicDetails")
              .commitAllowingStateLoss();
    }
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
      return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
      return false;
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
}

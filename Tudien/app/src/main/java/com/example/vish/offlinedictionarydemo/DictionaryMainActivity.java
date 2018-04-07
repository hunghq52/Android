package com.example.vish.offlinedictionarydemo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hung.
 */
public class DictionaryMainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static ArrayList<DictObjectModel> data;
    DatabaseHelper db;
    SQLiteDatabase sd;
    ArrayList<String> wordcombimelist;
    ArrayList<String> meancombimelist;
    LinkedHashMap<String, String> namelist;
    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mn_add) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        final LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        builder.setTitle("Thêm từ");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_add_write_24dp);
        Dialog dialog = builder.create();
        final Dialog finalDialog = dialog;
        final View view=inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("word",((EditText)view.findViewById(R.id.et_word)).getText().toString() );
                                insertValues.put("definition",((EditText)view.findViewById(R.id.et_description)).getText().toString() );
                                long a=sd.insert("Dictionary1", null, insertValues);
                                Log.d("cc", "onClick: "+a);
                                fetchData();
                            }
                        }
                )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finalDialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        db = new DatabaseHelper(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Tìm kiếm");
        searchView.setQueryRefinementEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DictObjectModel>();
        fetchData();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                newText = newText.toLowerCase();

                final ArrayList<DictObjectModel> filteredList = new ArrayList<DictObjectModel>();

                for (int i = 0; i < wordcombimelist.size(); i++) {

                    final String text = wordcombimelist.get(i).toLowerCase();
                    if (text.equals(newText) || newText.isEmpty()) {

                        filteredList.add(new DictObjectModel(wordcombimelist.get(i), meancombimelist.get(i)));
                    }
                }

                adapter = new CustomAdapter(filteredList);
                recyclerView.setAdapter(adapter);


                return true;
            }
        });


    }

    public void fetchData() {
        db = new DatabaseHelper(this);
        try {

            db.createDataBase();
            db.openDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }


        namelist = new LinkedHashMap<>();
        int ii;
         sd = db.getReadableDatabase();
        Cursor cursor = sd.query("Dictionary1", null, null, null, null, null, null);
        ii = cursor.getColumnIndex("word");
        wordcombimelist = new ArrayList<String>();
        meancombimelist = new ArrayList<String>();
        while (cursor.moveToNext()) {
            namelist.put(cursor.getString(ii), cursor.getString(cursor.getColumnIndex("definition")));
        }
        Iterator entries = namelist.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            wordcombimelist.add(String.valueOf(thisEntry.getKey()));
            meancombimelist.add("- " + String.valueOf(thisEntry.getValue()));
        }

        for (int i = 0; i < wordcombimelist.size(); i++) {
            data.add(new DictObjectModel(wordcombimelist.get(i), meancombimelist.get(i)));
        }
//        DictObjectModel m=data.get(111557);
//        Log.d("ccc", "fetchData: "+m.getWord());
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
    }
}

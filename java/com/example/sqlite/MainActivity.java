package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sqlite.adapter.temanadapter;
import com.example.sqlite.database.DBController;
import com.example.sqlite.database.teman;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private temanadapter adapter;
    private ArrayList<teman> temanArrayList;
    DBController controler = new DBController(this);

    String id,nama,telpon;
    private FloatingActionButton fab;
    private Object temanadapter;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url_select = "http://10.0.2.2/umyTI/bacateman.php";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_TELEPON = "telpon";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingBtn);
        basisdata();
        adapter = new temanadapter(temanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter((RecyclerView.Adapter) temanadapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), temanbaru.class);
                startActivity(intent);
            }
        });

    }

    public  void basisdata(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jArr = new JsonArrayRequest(url_select,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                Log.d(TAG, response.toString());
                //Parsing json
                for (int i=0; i<response.length();i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        teman item = new teman();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setTelpon(obj.getString(TAG_TELEPON));
                        temanArrayList.add(item);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"gagal",Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jArr);

    }
}


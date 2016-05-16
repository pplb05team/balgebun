package pplb05.balgebun.costumer.Tab;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Adapter.CounterAdapter;
import pplb05.balgebun.costumer.Entity.CounterEntity;
import pplb05.balgebun.R;
import pplb05.balgebun.costumer.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class Counter extends Fragment {
    ArrayList<CounterEntity> counters;
    private RequestQueue queue;
    private  CounterAdapter counterAdapter;

    public Counter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("Fragmet", "Counter");
        counters = new ArrayList<>();
        getCounterList();
        //downloadFile("http://aaa.esy.es/coba_wahid/login.php");

        View v = inflater.inflate(R.layout.fragment_counter, container, false);
        super.onCreate(savedInstanceState);
        GridView gridView = (GridView) v.findViewById(R.id.counterGridViewLayout);
        counterAdapter = new CounterAdapter(counters, getActivity());
        gridView.setAdapter(counterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent nextScreen = new Intent(getActivity(), MenuActivity.class);

                Bitmap _bitmap = counters.get(position).getBitmap(); // your bitmap
                if(_bitmap != null) {
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
                    nextScreen.putExtra("image", _bs.toByteArray());
                }
                nextScreen.putExtra("username", counters.get(position).getUsername());
                nextScreen.putExtra("countername", counters.get(position).getCounterName());
                startActivity(nextScreen);

            }
        });
        // Inflate the layout for this fragment
        return v;
    }


    public void getCounterList(){
        String url = "http://aaa.esy.es/coba_wahid/login.php";
        final StringRequest stringChess = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               //Log.d("RESPONSE", "Get counter Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray counter = new JSONArray(temp);
                        for(int i = 0; i < counter.length(); i++){
                            JSONObject acounter = new JSONObject(counter.get(i).toString());
                            counters.add(new CounterEntity(0, acounter.getString("nama_counter"), acounter.getString("username")));
                        }
                        counterAdapter.notifyDataSetChanged();
                        //Log.d("ABCD", "ABCD");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringChess);
    }

}

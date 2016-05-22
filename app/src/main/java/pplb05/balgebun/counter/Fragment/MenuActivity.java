package pplb05.balgebun.counter.Fragment;

//https://www.youtube.com/watch?v=ZEEYYvVwJGY

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.counter.Adapter.PesananPenjualAdapter;
import pplb05.balgebun.counter.Entity.PesananPenjual;

//import com.example.febriyolaanastasia.balgebun.R;

/**
 * @author dananarief
 * Kelas ini merupakan kelas fragmen yang akan menamipilkan list pesanan pada suatu counter
 */

public class MenuActivity extends Fragment {
    private ArrayList<PesananPenjual> order;
    private PesananPenjualAdapter pesananAdapter;
    private RequestQueue queue;
    private String username;
    Spinner spinnerku;
    ArrayAdapter<CharSequence> adapterSpinner;
    private ImageView _imv;
    private TextView counterAntrian;

    public MenuActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_pesanan, container, false);
        super.onCreate(savedInstanceState);
        Log.d("apakah loading 43", "load");

        SharedPreferences settings = getActivity().getSharedPreferences("BalgebunLogin", Context.MODE_PRIVATE);
        username = settings.getString("username", "");

        TextView counterUsernameText = (TextView)v.findViewById(R.id.counter_name_id);
        counterUsernameText.setText(username);

        counterAntrian = (TextView) v.findViewById(R.id.antrian_id);
        getAntrian();

        Button refreshButton = (Button)v.findViewById(R.id.refresh_pesanan_penjual);
        _imv = (ImageView) v.findViewById(R.id.counter_image_id);
        getImage();
        //membuat array berisi pesanan
        order = new ArrayList<>();

        //ambil data dari database untuk ditampilkan
        getPesananList();

        pesananAdapter = new PesananPenjualAdapter(order,getActivity(),MenuActivity.this);
        GridView fieldMenu = (GridView)v.findViewById(R.id.menu_field);
        fieldMenu.setAdapter(pesananAdapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPesananList();
                Log.d("Refresh", "Refreshing");
            }
        });

        return v;
    }

    //Method untuk mendapatkan data yang dibutuhkan untuk ditampilkan pada list pesanan
    public void getPesananList() {
        //order.clear();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPesananPenjual.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Get Pemesanan Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("try", "try");
                    if (!error) {
                        order.clear();

                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);

                        for (int i = 0; i < menuTemp.length(); i++) {
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            if (!jsonMenu.getString("status").equals("selesai")) {
                                order.add(new PesananPenjual(
                                        jsonMenu.getString("nama_menu"),
                                        jsonMenu.getString("username_pembeli"),
                                        Integer.parseInt(jsonMenu.getString("jumlah")),
                                        jsonMenu.getString("status")
                                        , i, Integer.parseInt(jsonMenu.getString("id")))
                                );

                            }
                        }

                        pesananAdapter.notifyDataSetChanged();

                    } else {
                        //kalo database kosong
                        order.clear();
                        pesananAdapter.notifyDataSetChanged();
                        Log.d("A", "A");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                Log.d("hai", "hai");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }

        };

        queue.add(stringChess);
    }

    private void getImage() {
        String fileUrl = AppConfig.URL_IMG  + username + ".jpg";
        ImageRequest imgReqCtr = new ImageRequest(fileUrl, new Response.Listener<Bitmap>() {

            /**
             * Drae thw image to the imageviw
             * @param response
             */
            @Override
            public void onResponse(Bitmap response) {
                _imv.setImageBitmap(response);
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Use VolleySingelton
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(imgReqCtr);

    }

    public void getAntrian() {
        //order.clear();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getAntrianPenjual.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Get Antrian Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("try", "try");
                    if (!error) {

                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);

                        Log.d("cekAntrian1","cekAntrian1");

                        for (int i = 0; i < menuTemp.length(); i++) {
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            counterAntrian.setText(jsonMenu.getString("banyak_antrian"));
                            Log.d("cekAntrian2","cekAntrian2");
                        }

                        //pesananAdapter.notifyDataSetChanged();

                    } else {
                        //kalo database kosong
                        //order.clear();
                        //pesananAdapter.notifyDataSetChanged();
                        Log.d("Antrian error", "Antrian error");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("antrian_exception","antrian_exception");
                }
                Log.d("antrianhai", "antrianhai");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }

        };

        queue.add(stringChess);
    }
}


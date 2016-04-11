package pplb05.balgebun.costumer.Tab;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.costumer.Adapter.PesananAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    private ArrayList<Menu> foods = new ArrayList<>();
    private PesananAdapter pesananAdapter;
    private TextView total;
    private Pemesanan pesan = new Pemesanan ();
    private RequestQueue queue;
    String username;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getContext().getSharedPreferences("BalgebunLogin", Context.MODE_PRIVATE);
        username = settings.getString("username", "");

        getPesanan();

        pesananAdapter = new PesananAdapter(foods,getActivity());
        GridView fieldMenu = (GridView)v.findViewById(R.id.pesanan_field);
        fieldMenu.setAdapter(pesananAdapter);


        Button refresh = (Button)v.findViewById(R.id.refresh_history);

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPesanan();
                Log.d("Refresh", "Refreshing");
            }
        });

        return  v;
    }

    public void getPesanan() {
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPesanan.php";
        final StringRequest stringResp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    //"user":[{"nama_menu":"Soto ati ampela + nasi","id_menu":"13","jumlah":"2","status":"dimasak"}
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);
                        foods.clear();
                        for(int i = 0; i < menuTemp.length(); i++){
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            if(jsonMenu.getString("status").equals("selesai")) {
                                foods.add(new Menu(
                                                i,
                                                Integer.parseInt(jsonMenu.getString("id_menu")),
                                                jsonMenu.getString("nama_menu"),
                                                Integer.parseInt(jsonMenu.getString("jumlah")),
                                                jsonMenu.getString("status"),
                                                jsonMenu.getString("nama_counter"))
                                );
                            }
                        }
                        pesananAdapter.notifyDataSetChanged();
                        Log.d("ABCD", "ABCD");
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
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }

        };
        queue.add(stringResp);
    }


}

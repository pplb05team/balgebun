package pplb05.balgebun.counter.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
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
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.counter.Adapter.RiwayatPesananPenjualAdapter;
import pplb05.balgebun.counter.Entity.RiwayatPesananPenjual;
import pplb05.balgebun.helper.SessionManager;

/**
 * @author dananarief
 * Kelas ini merupakan kelas activity yang mengatur tampilan riwayat pesanan
 */
public class RiwayatActivity extends Fragment {
    private ArrayList<RiwayatPesananPenjual> riwayatPesanan;
    private RiwayatPesananPenjualAdapter riwayatAdapter;
    private String username;
    private SessionManager session;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_riwayat_antrian_penjual, container, false);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_riwayat_antrian_penjual);

        session = new SessionManager(getActivity());
        username = session.getUsername();

        TextView counterUsernameText = (TextView) v.findViewById(R.id.counter_name_id_riwayat);
        counterUsernameText.setText(username);

        riwayatPesanan = new ArrayList<RiwayatPesananPenjual>();

        getRiwayatList();

        riwayatAdapter = new RiwayatPesananPenjualAdapter(riwayatPesanan,getActivity(),RiwayatActivity.this);
        GridView riwayatList = (GridView) v.findViewById(R.id.riwayat_field);
        riwayatList.setAdapter(riwayatAdapter);

        riwayatAdapter.notifyDataSetChanged();
        return v;
    }


    public void getRiwayatList(){
        String url = "http://aaa.esy.es/coba_wahid2/getRiwayatPesananPenjual.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Get Pemesanan Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("try", "try");
                    if (!error) {
                        riwayatPesanan.clear();
                        Log.d("if", "" + error);
                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);
                        Log.d("panjang", "" + menuTemp.length());
                        for (int i = 0; i < menuTemp.length(); i++) {
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());

                                riwayatPesanan.add(new RiwayatPesananPenjual(
                                        jsonMenu.getString("username_pembeli"),
                                        jsonMenu.getString("nama_menu"),
                                        Integer.parseInt(jsonMenu.getString("jumlah")),
                                        Integer.parseInt(jsonMenu.getString("id"))
                                        ,jsonMenu.getString("waktu")
                                        , i)
                                );
                                Log.d("i=", "" + i);
                                Log.d("menu", jsonMenu.getString("nama_menu") + "with id " + jsonMenu.getString("id"));

                        }
                        riwayatAdapter.notifyDataSetChanged();

                    } else {
                        //kalo database kosong
                        riwayatPesanan.clear();
                        riwayatAdapter.notifyDataSetChanged();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringChess);
    }
}

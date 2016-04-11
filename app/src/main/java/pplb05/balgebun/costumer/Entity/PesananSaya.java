package pplb05.balgebun.costumer.Entity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import pplb05.balgebun.costumer.Adapter.PesananAdapter;
import pplb05.balgebun.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PesananSaya extends AppCompatActivity {

    private ArrayList<Menu> foods = new ArrayList<>();
    private PesananAdapter pesananAdapter;
    private TextView total;
    private Pemesanan pesan = new Pemesanan ();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_saya);
        
        getPesanan();

        pesananAdapter = new PesananAdapter(foods,this);
        GridView fieldMenu = (GridView)findViewById(R.id.pesanan_field);
        fieldMenu.setAdapter(pesananAdapter);
        
        
    }

    public void getPesanan() {
        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPesanan.php";
        final StringRequest stringResp = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Menu Response: " + response.toString());
                try {
                    //"user":[{"nama_menu":"Soto ati ampela + nasi","id_menu":"13","jumlah":"2","status":"dimasak"}
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);
                        for(int i = 0; i < menuTemp.length(); i++){
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            foods.add(new Menu(
                                            i,
                                            Integer.parseInt(jsonMenu.getString("id_menu")),
                                            jsonMenu.getString("nama_menu"),
                                            Integer.parseInt(jsonMenu.getString("jumlah")),
                                            jsonMenu.getString("status"),
                                            jsonMenu.getString("nama_counter"))
                            );
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
        });
        queue.add(stringResp);
    }
}

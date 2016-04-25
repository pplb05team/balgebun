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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Adapter.PesananAdapter;

/**
 * @author febriyola anastasia
 * This class is used to show all pesanan, its status, jumlah, and the counter
 */
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

        //get all of the buyer's order from database
        getPesanan();

        pesananAdapter = new PesananAdapter(foods,this);
        GridView fieldMenu = (GridView)findViewById(R.id.pesanan_field);
        fieldMenu.setAdapter(pesananAdapter);
    }

    /**
     * this method will create object Menu from every Menu the buyer's ordered
     */
    public void getPesanan() {
        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPesanan.php";
        final StringRequest stringResp = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Pesanan Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);
                        for(int i = 0; i < menuTemp.length(); i++){
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            //create object Menu with its id, nama, jumlah, status, nama counter
                            foods.add(new Menu(
                                            i,
                                            Integer.parseInt(jsonMenu.getString("id_order")),
                                            Integer.parseInt(jsonMenu.getString("id_menu")),
                                            jsonMenu.getString("nama_menu"),
                                            Integer.parseInt(jsonMenu.getString("jumlah")),
                                            jsonMenu.getString("status"),
                                            jsonMenu.getString("nama_counter"))
                            );
                        }
                        pesananAdapter.notifyDataSetChanged();
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

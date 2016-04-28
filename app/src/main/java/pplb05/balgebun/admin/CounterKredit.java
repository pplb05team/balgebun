package pplb05.balgebun.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
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
import pplb05.balgebun.admin.Adapter.BayarPemasukanAdapter;
import pplb05.balgebun.costumer.Entity.CounterEntity;

/**
 * @author febriyola anastasia
 * This class is used to check and pay counter's income
 */
public class CounterKredit extends AppCompatActivity {

    private EditText namaCounter;
    private TextView jumlah,nama;
    private ArrayList<CounterEntity> counters;
    private RequestQueue queue;
    private BayarPemasukanAdapter counterKreditAdapter;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_kredit);

        counters = new ArrayList<>();


        getCounterList();

        counterKreditAdapter = new BayarPemasukanAdapter(counters, this);
        GridView fieldBayar = (GridView)findViewById(R.id.kredit_counter);
        fieldBayar.setAdapter(counterKreditAdapter);

    }

    public void getCounterList(){
        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/login.php";
        final StringRequest stringChess = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray counter = new JSONArray(temp);
                        for(int i = 0; i < counter.length(); i++){
                            Log.d("ADD ARRAY", " i > " + i);
                            JSONObject acounter = new JSONObject(counter.get(i).toString());
                            counters.add(new CounterEntity(i, acounter.getString("nama_counter"), acounter.getString("username"),
                                    Integer.parseInt(acounter.getString("pemasukan"))));
                        }

                        counterKreditAdapter.notifyDataSetChanged();

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

        queue.add(stringChess);



    }
}

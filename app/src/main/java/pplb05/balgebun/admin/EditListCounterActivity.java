package pplb05.balgebun.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

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
import pplb05.balgebun.admin.Adapter.EditCounterAdapter;
import pplb05.balgebun.admin.Entity.EditCounterEntity;

/**
 * Created by Rahmi Julianasari on 25/04/2016.
 * Kelas ini sebagai kelas activity untuk menampilkan semua counter yang akan di edit
 */
public class EditListCounterActivity extends AppCompatActivity{

    //inisialisasi
    private ArrayList<EditCounterEntity> counters = new ArrayList<>();
    private EditCounterAdapter counterAdapter;
    private RequestQueue queue;
    Context context;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_listcounter);

        //get all counters
        if(counters.isEmpty())
            getCounterList();

        //showing the counters in xml
        GridView gridView = (GridView) findViewById(R.id.list_counter);
        counterAdapter = new EditCounterAdapter(counters, this);
        gridView.setAdapter(counterAdapter);


    }
    public void registerCounter(View view) {
        Intent i = new Intent(this, RegisterCounter.class);
        startActivity(i);
    }

    /*
     * Method ini untuk memanggil fungsi gatCounterList pada API
     * fungsi tersebut akan menampilkan semua counter pada database
     */
    public void getCounterList(){
        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getCounter.php";
        final StringRequest stringChess = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("RESPONSE", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("Test","Test");
                        String temp = jObj.getString("user");
                        JSONArray counterTemp = new JSONArray(temp);
                        for(int i = 0; i < counterTemp.length(); i++){
                            JSONObject counter = new JSONObject(counterTemp.get(i).toString());

                            //membuat object counter
                            counters.add(new EditCounterEntity(
                                    counter.getString("username"),
                                    counter.getString("nama_counter"),
                                    Integer.parseInt(counter.getString("pemasukan"))
                            ));
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

        queue.add(stringChess);
    }


}

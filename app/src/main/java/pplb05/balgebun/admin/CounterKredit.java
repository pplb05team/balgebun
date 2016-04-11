package pplb05.balgebun.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;

/**
 * @author febriyola anastasia
 * This class is used to check and pay counter's income
 */
public class CounterKredit extends AppCompatActivity {

    private EditText namaCounter;
    private TextView jumlah,nama;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_kredit);

        //init var
        namaCounter  = (EditText)findViewById(R.id.nama_counter);
        nama  = (TextView)findViewById(R.id.nama_counter_id);
        jumlah  = (TextView)findViewById(R.id.pemasukan_id);
    }

    /**
     * This method is used to pay the income
     */
    public void bayar(View view) {
        final String username = namaCounter.getText().toString();

        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/bayar.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("RESPONSE", "BAYAR Response: " + response.toString());
                //set jumlah text to 0
                jumlah.setText("Rp. 0,00");
                //show toast
                Toast toast = Toast.makeText(getApplicationContext(), "Berhasil membayar", Toast.LENGTH_SHORT);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to bayar url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        queue.add(stringReq);
    }

    /**
     * This method is used to check counter's income
     */
    public void cekKredit(View view) {

        final String username = namaCounter.getText().toString();

        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPemasukanCounter.php";
        final StringRequest stringReqKredit = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray temp2 = new JSONArray(temp);
                        Log.d("RESPONSE", temp);
                        JSONObject jsonPemasukan = new JSONObject(temp2.get(0).toString());
                        String tempNama = jsonPemasukan.getString("nama_counter");
                        int tempPemasukan = Integer.parseInt(jsonPemasukan.getString("pemasukan"));

                        nama.setText(tempNama);

                        int temp3 = tempPemasukan;
                        int ribuan = temp3/1000;
                        temp3 = tempPemasukan - ribuan*1000;
                        if(temp3 == 0)
                            jumlah.setText("Rp. " + ribuan +".000,00");
                        else
                            jumlah.setText("Rp. " +ribuan +"." + temp3 +",00");

                    } else {
                        //if username invalid, show toast username salah
                        Toast toast = Toast.makeText(getApplicationContext(), "Username salah", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to getPemasukanCounter url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        queue.add(stringReqKredit);

    }
}

package pplb05.balgebun.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PembeliKredit extends AppCompatActivity {
    private EditText namaCounter, tambahKredit;
    private TextView jumlah,nama;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembeli_kredit);

        namaCounter  = (EditText)findViewById(R.id.nama_pembeli);
        nama  = (TextView)findViewById(R.id.nama_pembeli);
        jumlah  = (TextView)findViewById(R.id.kredit);
        tambahKredit  = (EditText)findViewById(R.id.tambah_id);
    }

    public void tambah(View view) {
        final String username = namaCounter.getText().toString();
        final String tambah = tambahKredit.getText().toString();

        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/tambahKreditPembeli.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("RESPONSE", "BAYAR Response: " + response.toString());
                //int total = Integer.parseInt((String) jumlah.getText()) + Integer.parseInt(tambah);
                //jumlah.setText("Rp. 0,00");
                setKredit();
                Toast toast = Toast.makeText(getApplicationContext(), "Berhasil menambah", Toast.LENGTH_SHORT);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to order url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("jumlahUang", tambah);
                return params;
            }
        };

        queue.add(stringChess);
    }

    public void cekKredit(View view) {

        setKredit();

    }

    public void setKredit(){
        final String username = namaCounter.getText().toString();

        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPemasukanPembeli.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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
                        int tempPemasukan = Integer.parseInt(jsonPemasukan.getString("kredit"));


                        int temp3 = tempPemasukan;
                        int ribuan = temp3/1000;
                        temp3 = tempPemasukan - ribuan*1000;
                        if(temp3 == 0)
                            jumlah.setText("Rp. " + ribuan +".000,00");
                        else
                            jumlah.setText("Rp. " +ribuan +"." + temp3 +",00");

                        Log.d("ABCD", "ABCD");
                    } else {
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
                // Posting params to order url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        queue.add(stringChess);
    }
}

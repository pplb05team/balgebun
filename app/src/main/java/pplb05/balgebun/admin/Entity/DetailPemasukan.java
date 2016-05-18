package pplb05.balgebun.admin.Entity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.CounterKredit;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.counter.Adapter.RiwayatPesananPenjualAdapter;
import pplb05.balgebun.counter.Entity.RiwayatPesananPenjual;

/**
 * Created by febriyolaanastasia on 05/12/16.
 * Class ini digunakan untuk melihat detail pemasukan dari suatu counter
 */
public class DetailPemasukan extends AppCompatActivity implements View.OnClickListener {

    private RiwayatPesananPenjualAdapter riwayatAdapter;
    private TextView total, namaCounterTxt, usernameTxt;
    private ArrayList<RiwayatPesananPenjual> riwayatPesanan;
    private String counterName;
    private String counterUsername;
    private ImageView _imv;
    private int totalInt;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemasukan);

        total = (TextView) findViewById(R.id.total_view);
        namaCounterTxt = (TextView) findViewById(R.id.nama_counter);
        usernameTxt = (TextView) findViewById(R.id.counter_username);
        _imv = (ImageView)findViewById(R.id.counter_image_id_struk);




        //get object & variable from previous activity using parcelable
        Intent intent = getIntent();
        Pemesanan pesan = intent.getExtras().getParcelable("pemesan");
        counterName = intent.getExtras().getString("counterName");
        counterUsername = intent.getExtras().getString("counterUsername");
        totalInt = intent.getExtras().getInt("counterPemasukan");

        namaCounterTxt.setText(counterName);
        usernameTxt.setText(counterUsername);

        getImage();

        System.out.println("PEMASUKAN = " + totalInt);

        //set total pada text
        int ribuan = totalInt/1000;
        int sisa = totalInt-ribuan*1000;
        if(sisa == 0){
            total.setText("Rp. " + ribuan + ".000,00");
        }else{
            total.setText("Rp. " + ribuan + "." + sisa + ",00");
        }

        //button pesan
        Button next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(this);

        riwayatPesanan = new ArrayList<RiwayatPesananPenjual>();

        getRiwayatList();

        //adapter untuk meload menu yang akan dikonfirmasi
        riwayatAdapter = new RiwayatPesananPenjualAdapter(riwayatPesanan, this.getApplicationContext());
        GridView fieldMenu = (GridView)findViewById(R.id.menu_field);
        fieldMenu.setAdapter(riwayatAdapter);

    }

    /**
     * Method ini akan menstore pesanan di database
     */
    public void onClick(View v) {

        //show alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPemasukan.this);
        alertDialogBuilder.setTitle("Bayar Pemasukan Counter");
        alertDialogBuilder.setMessage("Apakah Anda ingin membayar counter " + counterName + "?");
        AlertDialog.Builder builder = alertDialogBuilder.setPositiveButton("Bayar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) { //if pilih bayar, then bayar counter tsb
                        bayar(counterUsername, counterName);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //do nothing if cancel
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void bayar(String user, final String nama ) {

        final String username = user;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/bayar.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("RESPONSE", "BAYAR Response: " + response.toString());

                Toast toast = Toast.makeText(getApplicationContext(), "Berhasil membayar " + nama, Toast.LENGTH_SHORT);
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

        Intent i = new Intent(this, CounterKredit.class);
        startActivity(i);
        finish();
    }

    public void getRiwayatList(){
        final String username=counterUsername;
        queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid2/getRiwayatPesananPenjual.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Get Riwayat Response: " + response.toString());
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
        queue.add(stringReq);
    }

    /**
     * Get image (bitmap) from hosting and draw it on ImageView of the counter
     *
     */
    private void getImage() {
        String fileUrl = AppConfig.URL_IMG  + counterUsername + ".jpg";
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
        VolleySingleton.getInstance(this).addToRequestQueue(imgReqCtr);

    }
}

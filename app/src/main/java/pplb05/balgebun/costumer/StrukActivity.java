package pplb05.balgebun.costumer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.costumer.Entity.CounterEntity;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Adapter.StrukAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StrukActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = StrukActivity.class.getSimpleName();
    private StrukAdapter strukAdapter;
    private TextView total;
    private ArrayList<Menu> foods = new ArrayList<>();
    private ProgressDialog pDialog;
    private int id_struk;
    private String buyerUsername;
    private String counterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struk);
        Intent intent = getIntent();

        Pemesanan pesan = intent.getExtras().getParcelable("pemesan");
        counterName = intent.getExtras().getString("counterName");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setIdStruk();
        ArrayList<Menu> foodsTemp = pesan.getPesanan();

        int i = 0;

        for(Menu makanan : foodsTemp){
            foods.add(new Menu(
                            i++, makanan.getId(), makanan.getNamaMenu(), makanan.getHarga(), makanan.getSatus(), makanan.getJumlah()
                    ));
        }

        total = (TextView) findViewById(R.id.total_view);

        // Get uername of buyer
        SharedPreferences settings = getSharedPreferences("BalgebunLogin", Context.MODE_PRIVATE);
        buyerUsername = settings.getString("username", "");

        TextView buyerUsernameText = (TextView)findViewById(R.id.pembeli_id);
        buyerUsernameText.setText(buyerUsername);
        TextView counterNameText = (TextView)findViewById(R.id.counter_id);
        counterNameText.setText(counterName);

        int tot = pesan.getTotal();
        int ribuan = tot/1000;
        int sisa = tot-ribuan*1000;
        if(sisa == 0){
            total.setText("Rp. " + ribuan + ".000,00");
        }else{
            total.setText("Rp. " + ribuan + "." + sisa + ",00");
        }

        strukAdapter = new StrukAdapter(foods, pesan,this);
        GridView fieldMenu = (GridView)findViewById(R.id.menu_field);
        fieldMenu.setAdapter(strukAdapter);

        Button next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(this);

    }

    public void onBackPressed() {
        Intent i = new Intent(this, MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        //String name = inpurUserName.getText().toString().trim();
/*
if (isset($_POST['username']) && isset($_POST['id_menu']) && isset($_POST['jumlah']) && isset($_POST['harga_total'])) {
 */
        if(foods.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Anda belum memesan apapun", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        for(Menu makanan : foods){
            //masih hardcode nama pemesan
            int id_menu = makanan.getId();
            int jumlah = makanan.getJumlah();
            int harga_total = jumlah * makanan.getHarga();
            int struk = id_struk+1;


            order(buyerUsername, struk, id_menu, jumlah, harga_total);

        }

        Intent i = new Intent(this, BuyerActivity.class);
        startActivity(i);
        finish();
    }

    private void order(final String username_pembeli, final int id_struk_final, final int id_menu, final int jumlah, final int harga_total) {

        // Tag used to cancel the request
        String tag_string_order = "req_order";

        pDialog.setMessage("Memesan ...");
        showDialog();

        //get id order set


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon pesanan: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Berhasil memesan!", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Ordering Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to order url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username_pembeli", username_pembeli);
                params.put("id_struk", ""+ id_struk_final);
                params.put("id_menu", ""+id_menu);
                params.put("jumlah", ""+jumlah);
                params.put("harga_total", ""+harga_total);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_order);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void setIdStruk(){
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getIdStruk.php";
        final StringRequest stringResp = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "iD Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray strukTemp = new JSONArray(temp);

                        for(int i = 0; i < strukTemp.length(); i++){
                            JSONObject jsonMenu = new JSONObject(strukTemp.get(i).toString());
                            id_struk = Integer.parseInt(jsonMenu.getString("id_struk"));
                        }


                        //JSONObject jsonMenu = new JSONObject(strukTemp.toString());


                        //menuAdapter.notifyDataSetChanged();
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

    void downloadImage(final ImageView imageView, String fileUrl, final CounterEntity counter) {

        if(counter.getBitmap() != null) {
            imageView.setImageBitmap(counter.getBitmap());
            //Log.d("Cache", "Success");
        } else {
            AsyncTask<String, Object, String> task = new AsyncTask<String, Object, String>() {
                Bitmap bmImg;
                @Override
                protected String doInBackground(String... params) {
                    URL myFileUrl = null;
                    try {
                        myFileUrl = new URL(params[0]);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl
                                .openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();

                        bmImg = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    return null;
                }

                protected void onPostExecute(String unused) {
                    imageView.setImageBitmap(bmImg);
                    counter.setBitmap(bmImg);
                }
            };
            task.execute(fileUrl);
        }
    }
}

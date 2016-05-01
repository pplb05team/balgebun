package pplb05.balgebun.costumer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Adapter.StrukAdapter;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;

/**
 * @author febriyola anastasia
 * Class ini digunakan untuk mengkonfirmasi pembelian.
 * Class ini akan menampilkan menu-menu yang sudah dipilih, jumlahnya, dan total harga yang harus dibayar
 */
public class StrukActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = StrukActivity.class.getSimpleName();
    private StrukAdapter strukAdapter;
    private TextView total, saldo, buyerUsernameText, counterNameText;
    private ArrayList<Menu> foods = new ArrayList<>();
    private ProgressDialog pDialog;
    private int id_struk;
    private String buyerUsername;
    private String counterName;
    private String counterUsername;
    private ImageView _imv;
    private int saldoInt, totalInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struk);

        total = (TextView) findViewById(R.id.total_view);
        saldo = (TextView) findViewById(R.id.saldo_id);

        //get object & variable from previous activity using parcelable
        Intent intent = getIntent();
        Pemesanan pesan = intent.getExtras().getParcelable("pemesan");
        counterName = intent.getExtras().getString("counterName");
        counterUsername = intent.getExtras().getString("counterUsername");

        System.out.println("counter username" + counterUsername);
        System.out.println("counter name" + counterName);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //initialize id_struk
        setIdStruk();

        ArrayList<Menu> foodsTemp = pesan.getPesanan();

        int i = 0;

        //create new object Menu for each selected menu with new position = index
        for(Menu makanan : foodsTemp){
            foods.add(new Menu(
                    i++,
                    makanan.getId(),
                    makanan.getNamaMenu(),
                    makanan.getHarga(),
                    makanan.getSatus(),
                    makanan.getJumlah()
            ));
        }

        // Get username of buyer
        SharedPreferences settings = getSharedPreferences("BalgebunLogin", Context.MODE_PRIVATE);
        buyerUsername = settings.getString("username", "");
        setKredit(); //set kredit untuk buyer tsb

        buyerUsernameText = (TextView)findViewById(R.id.pembeli_id);
        buyerUsernameText.setText(buyerUsername);
        counterNameText = (TextView)findViewById(R.id.counter_id);
        counterNameText.setText(counterName);
        _imv = (ImageView)findViewById(R.id.counter_image_id_struk);


        getImage();

        //set total pada text
        totalInt = pesan.getTotal();
        int ribuan = totalInt/1000;
        int sisa = totalInt-ribuan*1000;
        if(sisa == 0){
            total.setText("Rp. " + ribuan + ".000,00");
        }else{
            total.setText("Rp. " + ribuan + "." + sisa + ",00");
        }

        //adapter untuk meload menu yang akan dikonfirmasi
        strukAdapter = new StrukAdapter(foods, pesan,this);
        GridView fieldMenu = (GridView)findViewById(R.id.menu_field);
        fieldMenu.setAdapter(strukAdapter);

        //button pesan
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
    /**
     * Method ini akan menstore pesanan di database
     */
    public void onClick(View v) {

        if(saldoInt < totalInt){
            Snackbar.make(v, "Saldo tidak mencukupi", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        //for each makanan, store di database
        for(Menu makanan : foods){
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

    /**
     * Method ini akan menstore pesanan ke database sesuai dengan param input
     * @param username_pembeli  username pembeli
     * @param id_struk_final    untuk 1 pesanan, akan memiliki id struk yang sama walau menu makanan berbeda-beda
     * @param id_menu           tiap menu diambil id nya
     * @param jumlah            jumlah makanan dari tiap menu yang dipesan
     * @param harga_total       harga total dari jumlah*harga untuk tiap menu
     */
    private void order(final String username_pembeli, final int id_struk_final, final int id_menu, final int jumlah, final int harga_total) {

        String tag_string_order = "req_order";

        pDialog.setMessage("Memesan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon pesanan: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = (jObj.getBoolean("error"));
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Berhasil memesan!", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in ordering. Get the error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "Tidak berhasil memesan", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    System.out.println("MASUK JSON EXCEPTION");
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

    /**
     * This method will initialize id_struk
     * id_struk = get last id_struk from database
     * sekali memesan = 1 id struk, walau jenis menu yang dipesan > 1
     */
    public void setIdStruk(){
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getIdStruk.php";
        final StringRequest stringResp = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "ID Struk Response: " + response.toString());
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

    public void setKredit(){
        final String username = buyerUsername;

        System.out.println("masuk set kredit untuk username = " + username);

        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getPemasukanPembeli.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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
                        saldoInt = Integer.parseInt(jsonPemasukan.getString("kredit"));

                        System.out.println("SALDO = "+saldoInt);

                        int temp3 = saldoInt;
                        int ribuan = temp3/1000;
                        temp3 = saldoInt - ribuan*1000;
                        if(temp3 == 0)
                            saldo.setText("Rp. " + ribuan +".000,00");
                        else
                            saldo.setText("Rp. " +ribuan +"." + temp3 +",00");

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error get saldo", Toast.LENGTH_SHORT);
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
                // Posting params to getPemasukanPembeli url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };
        queue.add(stringReq);
    }
}

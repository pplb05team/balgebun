package pplb05.balgebun.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.counter.EditMenu;

/**
 * Created by Rahmi Julianasari on 26/04/2016.
 * Kelas ini adalah kelas activity untuk menambahkan menu
 * user dapat melakukan tambah menu atau meng-cancel activity tersebut
 */
public class TambahMenuActivity extends AppCompatActivity {
    private EditText namaInput, hargaInput;
    private Button tambahButton,batalButton;
    private String unameCounter,nameCounter,namaMenu,hargaMenu;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_tambah_menu);

        //Receiving the Data
        Intent i = getIntent();
        unameCounter = i.getStringExtra("counterUsername");
        nameCounter = i.getStringExtra("counterName");

        //set variable
        namaInput = (EditText)findViewById(R.id.edit_nama);
        hargaInput = (EditText)findViewById(R.id.edit_harga);
        tambahButton = (Button)findViewById(R.id.button_tambah);
        batalButton = (Button)findViewById(R.id.button_batal);

        //jika button tambah menu di-click maka menu yang ditambahkan akan dimasukan ke data base
        //user harud mengiri semua field untuk tambah menu
        tambahButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                namaMenu = namaInput.getText().toString().trim();
                hargaMenu = hargaInput.getText().toString().trim();

                if (!namaMenu.isEmpty() && !hargaMenu.isEmpty()) {
                    // login user
                    tambahMenu();

                } else {//error jika ada field yang belum diisi

                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        //jika button batal di-click maka user akan dialihkan ke EditCounterActivity
        batalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });

    }

    /**
     * Method ini untuk mengalihkan user ke activity selanjutnya
     * next activity tergantung user login (admin/counter)
     */
    private void nextActivity(){
        SharedPreferences settings = getSharedPreferences("BalgebunLogin", Context.MODE_PRIVATE);
        String role = settings.getString("role", "");
        if(role.equals("2")){
            toCounter();
        }
        else{
            toAdmin();
        }
    }

    /**
     * Method untuk mengalihkan user(admin) ke EditCounterActivity
     */
    private void toAdmin(){
        Intent intent= new Intent(TambahMenuActivity.this, EditCounterActivity.class);
        intent.putExtra("counterUsername", unameCounter);
        intent.putExtra("counterName", nameCounter);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TambahMenuActivity.this.startActivity(intent);
    }

    /**
     * Method untuk mengalihkan user(counter) ke EditMenu
     */
    private void toCounter(){
        Intent intent= new Intent(TambahMenuActivity.this, EditMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TambahMenuActivity.this.startActivity(intent);
    }

    /**
     * Method untuk memanggil fungsi tambahMenu di API
     * fungsi tersebut akan menambahkan menu baru pada database
     */
    private void tambahMenu(){

        String url = "http://aaa.esy.es/coba_wahid/addMenu.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("addmenu","addmenu");
                        Toast.makeText(getApplicationContext(), "Menu berhasil ditambahkan", Toast.LENGTH_LONG).show();
                        nextActivity();
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

            //show error message
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_menu", TambahMenuActivity.this.namaMenu);
                params.put("harga", TambahMenuActivity.this.hargaMenu);
                params.put("username_penjual", TambahMenuActivity.this.unameCounter);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "req_queue");

    }

}

package pplb05.balgebun.admin;

import android.content.Intent;
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

/**
 * Created by Rahmi Julianasari on 26/04/2016.
 */
public class TambahMenuActivity extends AppCompatActivity {
    private EditText namaInput, hargaInput;
    private Button tambahButton,batalButton;
    private String unameCounter,namaMenu,hargaMenu;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_tambah_menu);

        Intent i = getIntent();
        unameCounter = i.getStringExtra("counterUsername");

        namaInput = (EditText)findViewById(R.id.edit_nama);
        hargaInput = (EditText)findViewById(R.id.edit_harga);
        tambahButton = (Button)findViewById(R.id.button_tambah);
        batalButton = (Button)findViewById(R.id.button_batal);

        tambahButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                namaMenu = namaInput.getText().toString().trim();
                hargaMenu = hargaInput.getText().toString().trim();

                if (!namaMenu.isEmpty() && !hargaMenu.isEmpty()) {
                    // login user
                    tambahMenu();

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        batalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(TambahMenuActivity.this, EditCounterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            }
        });

    }

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
                        Intent i = new Intent(TambahMenuActivity.this, EditCounterActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                        finish();

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

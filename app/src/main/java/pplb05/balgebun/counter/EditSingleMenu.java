package pplb05.balgebun.counter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.EditCounterActivity;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.helper.SessionManager;

/**
 * @author FebriyolaAnastasia
 * This class is used to edit & delete a single menu
 * edit: name & price
 */
public class EditSingleMenu extends AppCompatActivity {

    private EditText namaMenu, hargaMenu;
    private String namaString, hargaString, idString, usernameCounter, nameCounter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_menu);

        namaMenu  = (EditText)findViewById(R.id.nama_menu);
        hargaMenu  = (EditText)findViewById(R.id.harga_menu);

        Intent i = getIntent();
        // Receiving the Data
        idString = i.getStringExtra("id_menu");
        namaString = i.getStringExtra("nama_menu");
        hargaString = i.getStringExtra("harga_menu");
        usernameCounter = i.getStringExtra("counterUsername");
        nameCounter = i.getStringExtra("counterName");


        //set text
        namaMenu.setText(namaString);
        hargaMenu.setText(hargaString);

        Button button= (Button) findViewById(R.id.save_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNama = namaMenu.getText().toString();
                String newHarga = hargaMenu.getText().toString();

                if(newNama.length()==0 || newHarga.length() == 0){
                    Toast.makeText(getApplicationContext(),
                            "Invalid input", Toast.LENGTH_LONG).show();
                }else{
                    update(newNama, newHarga);
                }
            }
        });
    }

    /**
     * This method is used to identify which activity should start next
     */
    private void nextActivity(){
       session = new SessionManager(getApplicationContext());
        String role = session.getRole();
        if(role.equals("2")){
            toCounter();
        }
        else{
            toAdmin();
        }
    }

    /**
     * Next activity if role is admin
     */
    private void toAdmin(){
        Intent intent= new Intent(getApplicationContext(), EditCounterActivity.class);
        intent.putExtra("counterUsername", usernameCounter);
        intent.putExtra("counterName", nameCounter);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    /**
     * Next activity if role is counter
     */
    private void toCounter(){
        Intent intent= new Intent(getApplicationContext(), EditMenu.class);
        intent.putExtra("counterUsername", usernameCounter);
        intent.putExtra("counterName", nameCounter);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    /**
     * Update menu
     * @param nama_menu     new menu's name
     * @param harga         new price
     */
    private void update(final String nama_menu, final String harga) {
        String url = "http://aaa.esy.es/coba_wahid/updateMenu.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast toast = Toast.makeText(getApplicationContext(), "Berhasil merubah data", Toast.LENGTH_LONG);
                toast.show();
                nextActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to update menu url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_menu", idString);
                params.put("nama_menu", nama_menu);
                params.put("harga", harga);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringReq);
    }
}

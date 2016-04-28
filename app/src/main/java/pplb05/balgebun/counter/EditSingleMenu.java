package pplb05.balgebun.counter;

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

public class EditSingleMenu extends AppCompatActivity {

    private EditText namaMenu, hargaMenu;
    private String namaString, hargaString, idString;

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

    private void update(final String nama_menu, final String harga) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/updateMenu.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Toast toast = Toast.makeText(getApplicationContext(), "Berhasil merubah data", Toast.LENGTH_LONG);
                toast.show();
                Intent intent= new Intent(getApplicationContext(), EditMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

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
                params.put("id_menu", idString);
                params.put("nama_menu", nama_menu);
                params.put("harga", harga);
                return params;
            }
        };
        queue.add(stringReq);
    }
}

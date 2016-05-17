package pplb05.balgebun.costumer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import pplb05.balgebun.helper.SessionManager;

/**
 * Created by Haris Dwi Prakoso on 5/1/2016.
 */
public class MelihatKreditPembeli extends AppCompatActivity {
    private String tempUsername;
    private int tempSaldo;
    private TextView uname;
    private TextView saldo;
    private Button faqButton;
    private RequestQueue queue;
    private SessionManager session;

    public MelihatKreditPembeli(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melihat_kredit_pembeli);
        uname = (TextView) findViewById(R.id.unameView);
        saldo = (TextView) findViewById(R.id.textView_nominal);
        faqButton = (Button) findViewById(R.id.faqbutton);

        // Get username of buyer
        session = new SessionManager(getApplicationContext());
        tempUsername = session.getUsername();
        uname.setText(tempUsername);
        // Get buyer credit
        setKredit();
        // Goto FAQ page on clicking FAQ button
        faqButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(MelihatKreditPembeli.this, FAQPage.class);
                startActivity(intent);
            }
        });
    }

    public void setKredit(){
        final String username = tempUsername;

        System.out.println("masuk set kredit untuk username = " + username);

        queue = Volley.newRequestQueue(this.getApplicationContext());
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
                        tempSaldo = Integer.parseInt(jsonPemasukan.getString("kredit"));

                        System.out.println("SALDO = "+tempSaldo);

                        int temp3 = tempSaldo;
                        int ribuan = temp3/1000;
                        temp3 = tempSaldo - ribuan*1000;
                        if(temp3 == 0)
                            saldo.setText(ribuan +".000,00");
                        else
                            saldo.setText(ribuan +"." + temp3 +",00");

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

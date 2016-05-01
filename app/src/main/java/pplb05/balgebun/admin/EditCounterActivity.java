package pplb05.balgebun.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.Adapter.EditMenuAdapter;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.Menu;

/**
 * Created by Rahmi Julianasari on 26/04/2016.
 * This class is used for showing a specific counter will be edited
 */
public class EditCounterActivity extends AppCompatActivity {
    //Initialization
    private TextView namaCounter, usernameCounter;
    private String counterUsername, counterName;
    private ArrayList<Menu> foods = new ArrayList<>();
    private EditMenuAdapter menuAdapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_edit_counter);

        //init var
        namaCounter = (TextView)findViewById(R.id.nama_counter);
        usernameCounter = (TextView)findViewById(R.id.usernameC);

        //Receiving the Data
        Intent i = getIntent();
        counterUsername = i.getStringExtra("counterUsername");
        counterName = i.getStringExtra("counterName");

        //set text
        namaCounter.setText(counterName);
        usernameCounter.setText(counterUsername);

        //call the function
        getMenuList();

        //show the list of menu in grid view
        menuAdapter = new EditMenuAdapter(this,foods,counterUsername,counterName);
        GridView fieldMenu = (GridView)findViewById(R.id.menuList);
        fieldMenu.setAdapter(menuAdapter);

    }

    /*
     * Once button Tambah Menu is pressed, method tambahmenuActivity will be called
     * This method will start next activity for the admin to add new menu
     */
    public void tambahMenuActivity(View view){
        Intent i = new Intent(this, TambahMenuActivity.class);
        i.putExtra("counterUsername", counterUsername);
        i.putExtra("counterName", counterName);
        this.startActivity(i);
    }

    public void getMenuList(){
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
        String url = "http://aaa.esy.es/coba_wahid/getMenu.php";
        final StringRequest stringResp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Menu Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String temp = jObj.getString("user");
                        JSONArray menuTemp = new JSONArray(temp);
                        for(int i = 0; i < menuTemp.length(); i++){
                            JSONObject jsonMenu = new JSONObject(menuTemp.get(i).toString());
                            //create object Menu
                            foods.add(new Menu(
                                    i,
                                    Integer.parseInt(jsonMenu.getString("id_menu")),
                                    jsonMenu.getString("nama_menu"),
                                    Integer.parseInt(jsonMenu.getString("harga")),
                                    jsonMenu.getString("status"))
                            );
                        }
                        menuAdapter.notifyDataSetChanged();
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameter counterUsername to getMenu url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", counterUsername);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringResp);

    }

}

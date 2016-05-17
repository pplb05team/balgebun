package pplb05.balgebun.counter;

import android.content.Context;
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
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.counter.Adapter.MenuListAdapter;
import pplb05.balgebun.helper.SessionManager;

/**
 * @author febriyola anastasia
 * Main class for edit menu
 */
public class EditMenu extends AppCompatActivity {

    private String usernameCounter, namaCounter, role;
    private ArrayList<Menu> foods = new ArrayList<>();
    private MenuListAdapter menuAdapter;
    private ImageView _imv;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        session = new SessionManager(getApplicationContext());
        role = session.getRole();

        if(role.equals("3")){//admin side
            Intent i = getIntent();
            namaCounter =i.getStringExtra("counterName");
            usernameCounter =i.getStringExtra("counterUsername");
        }else{//counter side
            usernameCounter = session.getUsername();
            namaCounter = session.getName();
        }

        System.out.println("username = " + usernameCounter + "nama = " + namaCounter);

        TextView nama = (TextView) findViewById(R.id.counter_name_id);;
        nama.setText(namaCounter);

        _imv = (ImageView)findViewById(R.id.counter_image_id_struk);
        getImage();

        getMenuList(usernameCounter);

        menuAdapter = new MenuListAdapter(foods,this,usernameCounter,namaCounter);
        GridView fieldMenu = (GridView)findViewById(R.id.menu_field);
        fieldMenu.setAdapter(menuAdapter);

        Button addButton = (Button) findViewById(R.id.tambah_menu);

        //if button is pressed, edit that menu
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), pplb05.balgebun.admin.TambahMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("counterUsername", usernameCounter);
                intent.putExtra("nameCounter", namaCounter);
                getApplicationContext().startActivity(intent);
            }
        });

    }

    /**
     * This method is used to get image of the counter
     */
    private void getImage() {
        String fileUrl = AppConfig.URL_IMG  + usernameCounter + ".jpg";
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

    /**
     * this method will get all the menu from database and create object Menu for each Menu
     * then put it to array 'foods'
     */
    public void getMenuList(final String counterUsername){
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

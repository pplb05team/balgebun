package pplb05.balgebun.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.EditProfileActivity;
import pplb05.balgebun.R;
import pplb05.balgebun.admin.Adapter.EditMenuAdapter;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.Menu;

/**
 * Created by Rahmi Julianasari on 26/04/2016.
 * Kelas ini sebagai kelas activity untuk edit counter
 */
public class EditCounterActivity extends AppCompatActivity {
    //Initialization
    private TextView namaCounter, usernameCounter;
    private String counterUsername, counterName, counterEmail;
    private ArrayList<Menu> foods = new ArrayList<>();
    private EditMenuAdapter menuAdapter;
    private ImageView image;
    private Bitmap myBitmap;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_edit_counter);

        //init var
        namaCounter = (TextView)findViewById(R.id.nama_counter);
        usernameCounter = (TextView)findViewById(R.id.usernameC);
        image = (ImageView) findViewById(R.id.imgProfile);

        counterEmail="";
        getEmail();
        System.out.println(counterEmail);

        //Receiving the Data
        Intent i = getIntent();
        counterUsername = i.getStringExtra("counterUsername");
        counterName = i.getStringExtra("counterName");

        //set text
        namaCounter.setText(counterName);
        usernameCounter.setText(counterUsername);

        //call the function
        getMenuList();
        getBitmapFromURL("http://aaa.esy.es/coba_wahid/img/counter/" +counterUsername+".png");

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

    public void editCounterActivity(View view){
        Intent i= new Intent(EditCounterActivity.this, EditProfileActivity.class);
        i.putExtra("counterUsername", counterUsername);
        i.putExtra("counterName", counterName);
        i.putExtra("counterEmail", counterEmail);
        startActivity(i);
        finish();
    }


    /*
     *Method ini untuk memanggil fungsi getMenuList pada API
     * fungsi tersebut akan menampilkan semua menu ypada suatu counter
     */
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

    //The method is used to get profile image of the counter from URL
    void getBitmapFromURL(String src){
        AsyncTask<String, Object, String> task = new AsyncTask<String, Object, String>() {
            @Override
            protected String doInBackground(String... params){
                URL url = null;
                try{
                    url = new URL(params[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String unused){
                image.setImageBitmap(myBitmap);
            }
        };
        task.execute(src);

    }

    public void getEmail(){
        String url = "http://aaa.esy.es/coba_wahid/getEmailCounter.php";
        final StringRequest stringResp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE2", "Email Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String email =jObj.getString("user");
                        counterEmail=email;

                    } else {
                        Toast toast = Toast.makeText(EditCounterActivity.this, "Error get email", Toast.LENGTH_SHORT);
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
                params.put("username", counterUsername);
                return params;
            }


        };
        VolleySingleton.getInstance(EditCounterActivity.this).addToRequestQueue(stringResp);

    }

}

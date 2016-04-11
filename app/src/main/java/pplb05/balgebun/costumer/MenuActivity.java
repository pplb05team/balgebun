
package pplb05.balgebun.costumer;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Adapter.MenuAdapter;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;

/**
 * @author febriyola anastasia
 * class menuActivity akan menampilkan menu terkait counter tersebut
 * user juga akan memilih menu di class ini
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Menu> foods = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private TextView total;
    private TextView counterNameText;
    private Pemesanan pesan = new Pemesanan ();
    private RequestQueue queue;
    private String counterUsername;
    private String counterName;
    ImageView _imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get counter data from previous activity using intent
        Intent i = getIntent();

        // Receiving the Data
        counterUsername = i.getStringExtra("username");
        counterName = i.getStringExtra("countername");

        //init variables
        total = (TextView) findViewById(R.id.total_view);
        Button next = (Button) findViewById(R.id.next_btn);
        counterNameText = (TextView) findViewById(R.id.counter_name_id);
        counterNameText.setText(counterUsername);
        _imv = (ImageView)findViewById(R.id.counter_image_id);

        //set counter image for each counter
        downloadImage(_imv, counterName);

        //if Arraylist foods == empty, get menu list.
        if(foods.isEmpty()){
            getMenuList();
        }

        //using adapter to show the menu
        menuAdapter = new MenuAdapter(foods, pesan,this);
        GridView fieldMenu = (GridView)findViewById(R.id.menu_field);
        fieldMenu.setAdapter(menuAdapter);


        //if there's change on the adapter (means user click + or - button
        //then update the total
        menuAdapter.setOnDataChangeListener(new MenuAdapter.OnDataChangeListener() {
            public void onDataChanged() {
                int totalTemp = pesan.getTotal();
                int ribuan = totalTemp / 1000;
                totalTemp = totalTemp - ribuan * 1000;
                if (totalTemp == 0) {
                    total.setText("Rp. " + ribuan + ".000,00");
                } else {
                    total.setText("Rp. " + ribuan + "." + totalTemp + ",00");
                }
            }
        });
        next.setOnClickListener(this);
    }


    /**
     * this method will get all the menu from database and create object Menu for each Menu
     * then put it to array 'foods'
     */
    public void getMenuList(){
        queue = Volley.newRequestQueue(this.getApplicationContext());
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
        queue.add(stringResp);
    }

    @Override
    /**
     * this method will move the activity to next activity
     * from MenuActivity to StrukActivity
     */
    public void onClick(View v) {
        Intent i = new Intent(this, StrukActivity.class);

        //send variables to next activity
        i.putExtra("pemesan", pesan);
        i.putExtra("counterName", counterName);
        startActivity(i);
    }

    /**
     * This method will show the image of the counter
     * @param imageView the ImageView used to show the image
     * @param fileUrl used to complete the url
     */
    void downloadImage(final ImageView imageView, String fileUrl) {
        fileUrl = "http://aaa.eys.es/coba_wahid/img/counter/"+fileUrl+".jpg";

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
                }
            };
            task.execute(fileUrl);

    }
}
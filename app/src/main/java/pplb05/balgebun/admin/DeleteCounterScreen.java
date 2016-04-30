package pplb05.balgebun.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;

/**
 * Created by Haris Dwi Prakoso on 4/30/2016.
 */
public class DeleteCounterScreen extends Activity {
    private static final String TAG = DeleteCounterScreen.class.getSimpleName();
    private Button confirm, cancel;
    private String username;
    private TextView toDelete;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user_screen);
        confirm = (Button) findViewById(R.id.yesButton);
        cancel = (Button) findViewById(R.id.noButton);
        toDelete = (TextView) findViewById(R.id.toDelete);
        Intent i = getIntent();
        username = i.getStringExtra("counterUsername");
        toDelete.setText(username);

        confirm.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                deleteUser(username);
                Intent intent = new Intent(DeleteCounterScreen.this, MainActivity.class);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    private void deleteUser(final String username){
        String tag_string_req = "req_delete";
        pDialog.setMessage("Deleting user...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_DELETE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "User successfully deleted", Toast.LENGTH_LONG).show();
                    } else {
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
                Log.e(TAG, "Delete Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to delete url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

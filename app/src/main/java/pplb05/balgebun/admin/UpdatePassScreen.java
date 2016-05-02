package pplb05.balgebun.admin;

import android.app.Activity;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.R;

/**
 * Created by Haris Dwi Prakoso on 4/30/2016.
 */
public class UpdatePassScreen extends Activity {
    private static final String TAG = UpdatePassScreen.class.getSimpleName();
    private Button updateButton;
    private EditText newPassword;
    private String username;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass_screen);

        newPassword = (EditText) findViewById(R.id.newPassword);
        updateButton = (Button) findViewById(R.id.btnUpdate);
        Intent i = getIntent();
        username = i.getStringExtra("counterUsername");
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String password = newPassword.getText().toString().trim();
                if(!password.isEmpty()){
                    UpdatePassword(username, password);
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Please enter the new password!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void UpdatePassword(final String toUpdate, final String newPass){
        String tag_string_req = "req_update";
        pDialog.setMessage("Updating password...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_UPDATE_PASSWORD_FROM_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Password successfully updated", Toast.LENGTH_LONG).show();
                        finish();
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
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to update url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", toUpdate);
                params.put("password", newPass);
                params.put("new_password", newPass);
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

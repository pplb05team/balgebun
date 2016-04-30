package pplb05.balgebun;

import android.app.Activity;
import android.os.Bundle;
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

import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.helper.SQLiteHandler;

/**
 * Created by Wahid Nur Rohman on 4/29/2016.
 */
public class EditProfileActivity extends Activity{
    private static SQLiteHandler db;
    private HashMap<String, String> user;
    private EditText editName;
    private EditText editEmail;
    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editRetypePassword;
    private Button saveProfileButton;
    private Button savePasswordButton;
    private String newName;
    private String newEmail;
    private String oldPassword;
    private String newPassword;
    private String retypePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        editName = (EditText)findViewById(R.id.edit_name);
        editEmail = (EditText)findViewById(R.id.edit_email);
        editOldPassword = (EditText)findViewById(R.id.edit_old_password);
        editNewPassword = (EditText)findViewById(R.id.edit_password);
        editRetypePassword = (EditText)findViewById(R.id.edit_retypePassword);
        saveProfileButton = (Button)findViewById(R.id.edit_profile_btn);
        savePasswordButton = (Button)findViewById(R.id.edit_paswd_btn);

        editName.setText(user.get("name"));
        editEmail.setText(user.get("email"));

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newName = editName.getText().toString();
                newEmail = editEmail.getText().toString();

                    if(!checkInput(newName, "") && !checkInput(newEmail, "")){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("old_username", user.get("user"));
                        params.put("new_username", newName);
                        params.put("new_email", newEmail);
                        sendRequest(AppConfig.URL_UPDATE_PROFILE, params);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Isian tidak boleh kosong", Toast.LENGTH_SHORT)
                                .show();
                    }

            }
        });

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oldPassword = editOldPassword.getText().toString();
                newPassword = editNewPassword.getText().toString();
                retypePassword = editRetypePassword.getText().toString();

                if(checkInput(newPassword, retypePassword)){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", newName);
                    params.put("password", newPassword);
                    sendRequest(AppConfig.URL_UPDATE_PASSWORD, params);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Password berbeda", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }

    public boolean checkInput(String pass, String retype){
        return pass.equals(retype);
    }

    public void sendRequest(String url, final Map<String, String> params){

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("respoinses:", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Sukses update profile Anda", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Gagal update profile Anda", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Koneksi error", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return params;
            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}

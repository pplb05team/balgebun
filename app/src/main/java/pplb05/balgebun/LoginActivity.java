package pplb05.balgebun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import pplb05.balgebun.admin.MainActivity;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.costumer.BuyerActivity;
import pplb05.balgebun.counter.PenjualActivity;
import pplb05.balgebun.helper.SQLiteHandler;
import pplb05.balgebun.helper.SessionManager;


/**
 * Created by Haris Dwi Prakoso on 3/27/2016.
 */
public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputUname;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUname = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            if (session.getRole().equals("1")){
                // GOTO customer main activity

                HashMap<String, String> user = new HashMap<String, String>();
                user = db.getUserDetails();
                Intent intent = new Intent(LoginActivity.this, BuyerActivity.class);
                intent.putExtra("username", user.get("name"));
                startActivity(intent);
                finish();
            }
            else if(session.getRole().equals("2")) {
                // GOTO counter main activity
                Intent intent = new Intent(LoginActivity.this, pplb05.balgebun.counter.PenjualActivity.class);
                startActivity(intent);
                finish();
            }else if(session.getRole().equals("3")) {
                // GOTO counter main activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String uname = inputUname.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!uname.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(uname, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function to verify login details in MySQL database will post parameters(tag, username,
     * password) to login url
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Now store the user in SQLite
                        JSONObject user = jObj.getJSONObject("user");
                        String username = user.getString("username");
                        String email = user.getString("email");

                        String role = Integer.toString(user.getInt("role"));
                        String name = "";

                        if(role.equals("2")){
                            name = user.getString("name");
                            System.out.println("NAMA USER = " + name);
                        }

                        // user successfully logged in
                        // Create login session
                        session.setLogin(true, name, role, username);

                        // Inserting row in users table
                        db.addUser(name, email, role);

                        // Launch main activity
                        if (role.equals("1")){
                            // GOTO customer main activity
                            Intent intent = new Intent(LoginActivity.this, BuyerActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        }
                        else if (role.equals("2")){
                            // GOTO counter main activity
                            Intent intent = new Intent(LoginActivity.this, PenjualActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (role.equals("3")){
                            //GOTO admin main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            //show error message
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
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

    public static SQLiteHandler getDB(){
        return db;
    }
}

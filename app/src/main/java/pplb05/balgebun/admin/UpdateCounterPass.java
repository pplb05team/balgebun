package pplb05.balgebun.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pplb05.balgebun.R;
/**
 * Created by Haris Dwi Prakoso on 4/26/2016.
 */
public class UpdateCounterPass extends Activity {
    private static final String TAG = UpdateCounterPass.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<String> users = new ArrayList<String>();
    private ArrayAdapter<String> userListAdapter;
    private String toUpdate, newPass;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_counter_pass);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        String tag_string_req = "req_listing";

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LIST_USERNAME_BY_ROLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "List Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error){
                        JSONArray jArr = jObj.getJSONArray("user");
                        for(int i = 0; i < jArr.length(); i++){
                            JSONObject item = jArr.getJSONObject(i);
                            String uname = item.getString("username");
                            users.add(uname);
                        }
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
                Log.e(TAG, "Listing Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("role", "2");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        userListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        ListView userListView = (ListView) findViewById(R.id.listView1);
        userListView.setAdapter(userListAdapter);

        AdapterView.OnItemClickListener itemClickedHandler = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toUpdate = userListAdapter.getItem(position);
                final AlertDialog.Builder warning = new AlertDialog.Builder(UpdateCounterPass.this);
                warning.setMessage("Input new password");
                final EditText input = new EditText(UpdateCounterPass.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                warning.setView(input);
                warning.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPass = input.getText().toString();
                        String tag_string_req2 = "req_update";
                        pDialog.setMessage("Updating password...");
                        showDialog();
                        StringRequest strReq2 = new StringRequest(Method.POST,
                                AppConfig.URL_UPDATE_PASSWORD, new Response.Listener<String>() {
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
                                return params;
                            }

                        };

                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(strReq2, tag_string_req2);
                    }
                });
                warning.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            }
        };
        userListView.setOnItemClickListener(itemClickedHandler);

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

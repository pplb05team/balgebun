package pplb05.balgebun.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.CounterEntity;
import pplb05.balgebun.helper.SessionManager;

/**
 * Created by NgocTri on 4/8/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";
    SessionManager session;

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        Log.d("Enter Reg", "Sukses");
        //SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE);//Define shared reference file name
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        session = new SessionManager(getApplicationContext());
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);

            //String oldToken = sharedPreferences.getString(TAG, "");//Return "" when error or key not exists
            String oldToken = session.getToken();
            Log.d("OLD_TOKEN",oldToken);
            Log.d("GCM_TOKEN",token);

            //Only request to save token when token is new

            if(token.equals(oldToken)){
                Log.w("GCMRegistrationService", "Old token");
                storeNewToken(oldToken);

            } else {
                Log.d("OLD vs NEW", token.equals(oldToken) + "");
                storeNewToken(token);
                //Save new token to shared reference
                //editor.putString(TAG, token);
                session.setToken(token);
                //editor.commit();

            }
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void storeNewToken(final String token){
        String url = "http://aaa.esy.es/coba_wahid/updateToken.php";
        final StringRequest stringChess = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE TOKEN", "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

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
        }){

            @Override
            protected Map<String, String> getParams() {
                //Log.d("USERNAME", session.getUsername());
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", session.getUsername());
                params.put("token", token);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringChess);
    }

    public String getStringResultFromService_POST(String serviceURL, Map<String, String> params) {
        HttpURLConnection cnn = null;
        String line = null;
        URL url;
        try{
            url = new URL(serviceURL);
        } catch (MalformedURLException e){
            throw  new IllegalArgumentException("URL invalid:"+serviceURL);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        //Construct the post body using the parameter
        while (iterator.hasNext()){
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if(iterator.hasNext()){
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString(); //format same to arg1=val1&arg2=val2
        Log.w("AccessService", "param:" + body);
        byte[]bytes = body.getBytes();
        try{
            cnn = (HttpURLConnection)url.openConnection();
            cnn.setDoOutput(true);
            cnn.setUseCaches(false);
            cnn.setFixedLengthStreamingMode(bytes.length);
            cnn.setRequestMethod("POST");
            cnn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //Post the request
            OutputStream outputStream = cnn.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();

            //Handle the response
            int status = cnn.getResponseCode();
            if(status!=200){
                throw  new IOException("Post fail with error code:" + status);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line+'\n');
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

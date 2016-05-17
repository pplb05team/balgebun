package pplb05.balgebun.costumer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.EditProfileActivity;
import pplb05.balgebun.LoginActivity;
import pplb05.balgebun.R;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Tab.TabFragment;
import pplb05.balgebun.gcm.GCMRegistrationIntentService;
import pplb05.balgebun.helper.SessionManager;
import pplb05.balgebun.tools.RoundedImageView;

/**
 * Created by Wahid Nur Rohman on 4/29/2016.
 *
 * Kelas untuk menampilkan halaman utama pembeli/customer
 */
public class BuyerActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private NavigationView navigationView;
    private TextView name;
    private SessionManager session;
    private RoundedImageView imageUser;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                    String token = intent.getStringExtra("token");
                    Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                    Toast.makeText(getApplicationContext(), "GCM registration ???!!!", Toast.LENGTH_LONG).show();
                }
            }
        };          //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            Log.d("Connection Result", "Sukses");
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("Connection Result", "Gagal");
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

        // session manager
        session = new SessionManager(getApplicationContext());

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                // User memilih logout
                if (menuItem.getItemId() == R.id.log_out_nav_draw) {
                    logoutUser();

                }

                // User memilih menu edit profile, akan diarahkan ke EditProfileActivity
                if (menuItem.getItemId() == R.id.settings_profile_id) {
                    Intent intent = new Intent(BuyerActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

                // User memilioh menu lihat credit
                if (menuItem.getItemId() == R.id.settings_credit_id) {
                    Intent intent = new Intent(BuyerActivity.this, MelihatKreditPembeli.class);
                    startActivity(intent);
                }

                return false;
            }

        });


        // Menambah header pada navigation drawer
        View header= LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        imageUser = (RoundedImageView)header.findViewById(R.id.imageViewNav);
        getImage();
        name = (TextView)header.findViewById(R.id.user_name_nav_draw);
        name.setText(session.getUsername());

        mNavigationView.addHeaderView(header);

        /*
         * Setup Drawer Toggle of the Toolbar
         */
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    /**
     * Method untuk melakukan funsgi logout
     */
    private void logoutUser() {
        session.setLogin(false);
        storeNewToken("");

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method untuk mendapatkan gambar user
     */
    private void getImage() {
        final String fileUrl = AppConfig.URL_IMG_CUSTOMER + session.getUsername() + ".png";

        ImageRequest imgReqCtr = new ImageRequest(fileUrl, new Response.Listener<Bitmap>() {

            /**
             * Drae thw image to the imageviw
             *
             * @param response
             */
            @Override
            public void onResponse(Bitmap response) {
                imageUser.setImageBitmap(response);
                Log.d("SUKSES IMAGE", fileUrl);
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GAGAL IMAGE", fileUrl);
                    }
                });
        // Use VolleySingelton
        VolleySingleton.getInstance(this).addToRequestQueue(imgReqCtr);
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
}

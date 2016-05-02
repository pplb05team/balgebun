package pplb05.balgebun.costumer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import pplb05.balgebun.EditProfileActivity;
import pplb05.balgebun.LoginActivity;
import pplb05.balgebun.R;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Tab.Counter;
import pplb05.balgebun.costumer.Tab.History;
import pplb05.balgebun.costumer.Tab.Order;
import pplb05.balgebun.costumer.Adapter.ViewPageAdapter;
import pplb05.balgebun.costumer.Tab.TabFragment;
import pplb05.balgebun.counter.MelihatKreditPenjual;
import pplb05.balgebun.helper.SQLiteHandler;
import pplb05.balgebun.helper.SessionManager;
import pplb05.balgebun.tools.RoundedImageView;

/**
 * Created by Wahid Nur Rohman on 4/29/2016.
 *
 * Kelas untuk menampilkan halaman utama pembeli/customer
 */
public class BuyerActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    NavigationView navigationView;
    TextView name;
    private SQLiteHandler db;
    private SessionManager session;
    private RoundedImageView imageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

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

    /**
     * Method untuk melakukan funsgi logout
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

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
}

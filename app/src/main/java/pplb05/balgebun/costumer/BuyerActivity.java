package pplb05.balgebun.costumer;

import android.content.Intent;
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

import pplb05.balgebun.EditProfileActivity;
import pplb05.balgebun.LoginActivity;
import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Tab.Counter;
import pplb05.balgebun.costumer.Tab.History;
import pplb05.balgebun.costumer.Tab.Order;
import pplb05.balgebun.costumer.Adapter.ViewPageAdapter;
import pplb05.balgebun.costumer.Tab.TabFragment;
import pplb05.balgebun.helper.SQLiteHandler;
import pplb05.balgebun.helper.SessionManager;

public class BuyerActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    NavigationView navigationView;
    TextView name;
    private SQLiteHandler db;
    private SessionManager session;

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

                if (menuItem.getItemId() == R.id.log_out_nav_draw) {
                    logoutUser();
                    // FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    // fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                }


                if (menuItem.getItemId() == R.id.settings_profile_id) {
                    //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.containerView,new EditProfileActivity()).commit();
                    Intent intent = new Intent(BuyerActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }

                return false;
            }

        });


        View header= LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        name = (TextView)header.findViewById(R.id.user_name_nav_draw);
        name.setText(session.getUsername());

        mNavigationView.addHeaderView(header);

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

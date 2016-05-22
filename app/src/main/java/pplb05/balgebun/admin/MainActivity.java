package pplb05.balgebun.admin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import pplb05.balgebun.EditProfileActivity;
import pplb05.balgebun.LoginActivity;
import pplb05.balgebun.R;
import pplb05.balgebun.costumer.MelihatKreditPembeli;
import pplb05.balgebun.helper.SessionManager;
import pplb05.balgebun.tools.RoundedImageView;

/**
 * @author febriyola anastasia, rahmi julianasari, haris dwi
 * This class will show 2 button: Counter & Pembeli
 * Counter -> pay the counter's income
 * Pembeli -> top up kredit buyer
 */
public class MainActivity extends AppCompatActivity {
    private SessionManager session;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RoundedImageView imageUser;
    private  TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);

        session = new SessionManager(getApplicationContext());

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_adm);
        mNavigationView = (NavigationView) findViewById(R.id.navdraw_adm) ;


        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                // User memilih logout
                if (menuItem.getItemId() == R.id.top_up_saldo) {
                    Intent intent = new Intent(MainActivity.this, PembeliKredit.class);
                    startActivity(intent);

                }

                // User memilih menu edit profile, akan diarahkan ke EditProfileActivity
                if (menuItem.getItemId() == R.id.pass_penjual) {
                    Intent intent = new Intent(MainActivity.this, UpdateCounterPass.class);
                    startActivity(intent);
                }

                // User memilioh menu lihat credit
                if (menuItem.getItemId() == R.id.byr_saldo_penjual) {
                    Intent intent = new Intent(MainActivity.this, CounterKredit.class);
                    startActivity(intent);
                }

                // User memilioh menu lihat credit
                if (menuItem.getItemId() == R.id.crud_penjual) {
                    Intent intent = new Intent(MainActivity.this, EditListCounterActivity.class);
                    startActivity(intent);
                }

                // User memilioh menu lihat credit
                if (menuItem.getItemId() == R.id.logout_adm) {
                    session.setLogin(false);

                    // Launching the login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }


                return false;
            }

        });


        // Menambah header pada navigation drawer
        View header= LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        imageUser = (RoundedImageView)header.findViewById(R.id.imageViewNav);

        String uri = "@drawable/ic_launcher";  // where myresource (without the extension) is the file
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);

        imageUser.setImageDrawable(res);
        name = (TextView)header.findViewById(R.id.user_name_nav_draw);
        name.setText("Admin");

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


    public void updateCounterPass(View view) {
        Intent i = new Intent(this, UpdateCounterPass.class);
        startActivity(i);
    }

    /**
     * Once button counter is pressed, method counterKredit will be called
     * This method will start next activity for the admin to check & pay counter's income
     */
    public void counterKredit(View view) {
        Intent i = new Intent(this, CounterKredit.class);
        startActivity(i);
    }

    /**
     * Once button pembeli is pressed, method pembeliKredit will be called
     * This method will start next activity to top up buyer's credit
     */
    public void pembeliKredit(View view) {
        Intent i = new Intent(this, PembeliKredit.class);
        startActivity(i);
    }

    /**
     * Once button is pressed, method editCounter will be called
     * This method will start next activity for showing list of the counters
     */
    public void editCounterActivity(View view){
        Intent i = new Intent(this, EditListCounterActivity.class);
        startActivity(i);
    }
}

package pplb05.balgebun.costumer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Tab.Counter;
import pplb05.balgebun.costumer.Tab.History;
import pplb05.balgebun.costumer.Tab.Order;
import pplb05.balgebun.costumer.Adapter.ViewPageAdapter;

public class BuyerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        Intent i = getIntent();
        // Receiving the Data
        String name = i.getStringExtra("username");

        toolbar = (Toolbar)findViewById(R.id.toolBar);
        //setSupportActionBar(toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Counter(), "Konter");
        viewPagerAdapter.addFragments(new Order(), "Pesanan");
        viewPagerAdapter.addFragments(new History(), "Riwayat");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

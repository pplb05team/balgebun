package pplb05.balgebun.counter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pplb05.balgebun.R;

/**
 *
 * @author
 */

public class PenjualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjual);
    }

    public void counterKredit(View view) {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    public void pembeliKredit(View view) {
        Intent i = new Intent(this, MelihatKreditPenjual.class);
        startActivity(i);
    }
}

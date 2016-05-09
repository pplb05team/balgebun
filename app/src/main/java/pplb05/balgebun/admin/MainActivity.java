package pplb05.balgebun.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pplb05.balgebun.R;

/**
 * @author febriyola anastasia, rahmi julianasari, haris dwi
 * This class will show 2 button: Counter & Pembeli
 * Counter -> pay the counter's income
 * Pembeli -> top up kredit buyer
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);
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

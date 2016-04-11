package pplb05.balgebun.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pplb05.balgebun.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_main);
    }

    public void counterKredit(View view) {
        Intent i = new Intent(this, CounterKredit.class);
        startActivity(i);
    }

    public void pembeliKredit(View view) {
        Intent i = new Intent(this, PembeliKredit.class);
        startActivity(i);
    }
}

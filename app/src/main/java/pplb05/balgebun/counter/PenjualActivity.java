package pplb05.balgebun.counter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import pplb05.balgebun.R;

public class PenjualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjual);


        Button edit_menu = (Button)findViewById(R.id.edit_menu);

        edit_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditMenu.class);
                startActivity(i);

            }
        });

        Button kredit = (Button)findViewById(R.id.kredit);

        kredit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), MelihatKreditPenjual.class);
                startActivity(i);

            }
        });

        Button pesanan = (Button)findViewById(R.id.pesanan);

        pesanan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);

            }
        });

    }
}

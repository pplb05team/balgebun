package pplb05.balgebun.admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.EditCounterActivity;
import pplb05.balgebun.admin.Entity.EditCounterEntity;

/**
 * Created by Rahmi Julianasari on 25/04/2016.
 * This class is used as adapter for showing a spesific counter
 */
public class EditCounterAdapter extends BaseAdapter {
    //initialization
    ArrayList<EditCounterEntity> counters=new ArrayList<>();
    private TextView counterName, username;
    private Button editButton;
    Context context;

    public EditCounterAdapter(ArrayList<EditCounterEntity> counters, Context context){
        this.counters = counters;
        this.context = context;
    }

    @Override
    public int getCount() {
        return counters.size();
    }

    @Override
    public Object getItem(int position) {
        return counters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final EditCounterEntity counter = counters.get(position);
        LayoutInflater l = LayoutInflater.from(context);
        final View v = l.inflate(R.layout.adm_counter_layout, parent, false);

        counterName = (TextView) v.findViewById(R.id.namaCounter);
        username = (TextView) v.findViewById(R.id.usernameCounter);
        editButton = (Button) v.findViewById(R.id.edit_button);


        counterName.setText(counter.getCounterName());
        username.setText(counter.getUsername());


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCounter = counters.get(position).getCounterName();
                String strUsername = counters.get(position).getUsername();

                Intent i = new Intent(v.getContext(),EditCounterActivity.class);
                i.putExtra("counterUsername", strUsername);
                i.putExtra("counterName", strCounter);

                v.getContext().startActivity(i);
            }
        });


        return v;
    }
}


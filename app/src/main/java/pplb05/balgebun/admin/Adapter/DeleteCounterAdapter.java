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
import pplb05.balgebun.admin.Entity.EditCounterEntity;

/**
 * Created by Haris Dwi Prakoso on 4/30/2016.
 */
public class DeleteCounterAdapter extends BaseAdapter {
    //initialization
    ArrayList<EditCounterEntity> counters=new ArrayList<>();
    private TextView counterName, username;
    private Button deleteButton;
    Context context;

    public DeleteCounterAdapter(ArrayList<EditCounterEntity> counters, Context context){
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final EditCounterEntity counter = counters.get(position);
        LayoutInflater l = LayoutInflater.from(context);
        final View v = l.inflate(R.layout.adm_counter_layout2, parent, false);

        counterName = (TextView) v.findViewById(R.id.namaCounter);
        username = (TextView) v.findViewById(R.id.usernameCounter);
        deleteButton = (Button) v.findViewById(R.id.delete_button);
        deleteButton.setTag(position);

        counterName.setText(counter.getCounterName());
        username.setText(counter.getUsername());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUsername = counters.get(position).getUsername();

                Intent i = new Intent(v.getContext(), pplb05.balgebun.admin.DeleteCounterScreen.class);
                i.putExtra("counterUsername", strUsername);

                v.getContext().startActivity(i);
            }
        });

        return v;
    }
}

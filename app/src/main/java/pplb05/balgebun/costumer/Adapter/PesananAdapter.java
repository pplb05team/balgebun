package pplb05.balgebun.costumer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Entity.Menu;

/**
 * @author febriyola anastasia
 * this class is used as adapter to show list of buyer's Pesanan
 */
public class PesananAdapter extends BaseAdapter {

    private ArrayList<Menu> food;
    private Context context;
    private TextView nama, status, keterangan, jumlah;

    public PesananAdapter(ArrayList<Menu> food, Context context) {
        this.food = food;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return food.get(position).getPosition();
    }

    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return food.get(position);
    }


    /**
     * This method will set view for each pesanan
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.pesanan_item, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        keterangan = (TextView)v.findViewById(R.id.keterangan_menu);
        status = (TextView)v.findViewById(R.id.status);
        jumlah = (TextView)v.findViewById(R.id.jumlah);

        //set text for each var
        nama.setText(food.get(position).getNamaMenu());
        jumlah.setText("jumlah: " + food.get(position).getJumlah());
        keterangan.setText("Counter: " + food.get(position).getNamaCounter());
        status.setText(food.get(position).getSatus());

        return v;
    }

}


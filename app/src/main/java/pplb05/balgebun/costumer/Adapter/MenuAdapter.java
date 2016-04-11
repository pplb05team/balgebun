package pplb05.balgebun.costumer.Adapter;

/**
 * Created by febriyolaanastasia on 3/23/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.R;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter{
    @Override
    public long getItemId(int position) {
        return food.get(position).getPosition();
    }

    private ArrayList<Menu> food;
    private Context context;
    private Pemesanan pesan;
    private TextView nama;
    private TextView jumlah;
    private TextView harga;
    private Button tombolTambah;
    private Button tombolKurang;

    //THE INTERFACE FOR CALLBACK
    public interface OnDataChangeListener{
        public void onDataChanged();
    }

    OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
    }

    public MenuAdapter(ArrayList<Menu> food, Pemesanan pesan,Context context) {
        this.food = food;
        this.context = context;
        this.pesan = pesan;
    }



    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return food.get(position);
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.menu_layout, parent, false);

        nama = (TextView)v.findViewById(R.id.nama_menu);
        jumlah = (TextView)v.findViewById(R.id.counter_menu);
        harga = (TextView)v.findViewById(R.id.harga_view);
        tombolTambah = (Button)v.findViewById(R.id.button_plus);
        tombolKurang = (Button)v.findViewById(R.id.button_minus);

        nama.setText(food.get(position).getNamaMenu());
        harga.setText(food.get(position).getHargaText()); //Get the text from your adapter for example
        jumlah.setText(""+food.get(position).getJumlah());


        tombolTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food.get(position).getJumlah() != 9) {
                    food.get(position).addJumlah();
                    pesan.addPesanan(food.get(position));
                    notifyDataSetChanged();
                    mOnDataChangeListener.onDataChanged();
                }
            }
        });

        tombolKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesan.removePesanan(food.get(position));
                food.get(position).minJumlah();
                notifyDataSetChanged();
                mOnDataChangeListener.onDataChanged();
            }
        });

        return v;
    }

}

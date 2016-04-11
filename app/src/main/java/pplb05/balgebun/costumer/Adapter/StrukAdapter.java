package pplb05.balgebun.costumer.Adapter;

/**
 * Created by febriyolaanastasia on 3/23/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;
import pplb05.balgebun.R;

import java.util.ArrayList;


public class StrukAdapter extends BaseAdapter{
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
    private TextView total;
    //private Button tombolTambah;
    //private Button tombolKurang;

    //THE INTERFACE FOR CALLBACK
    public interface OnDataChangeListener{
        public void onDataChanged();
    }

    OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
    }

    public StrukAdapter(ArrayList<Menu> food, Pemesanan pesan, Context context) {
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
        View v = l.inflate(R.layout.struk_menu, parent, false);

        nama = (TextView)v.findViewById(R.id.nama_menu);
        jumlah = (TextView)v.findViewById(R.id.jumlah);
        harga = (TextView)v.findViewById(R.id.harga_view);
        total = (TextView)v.findViewById(R.id.total_id);

        nama.setText(food.get(position).getNamaMenu());
        harga.setText(food.get(position).getHargaText());
        jumlah.setText("x " + food.get(position).getJumlah());

        int tot = food.get(position).getJumlah() * food.get(position).getHarga();
        int ribuan = tot/1000;
        int sisa = tot-ribuan*1000;
        if(sisa == 0){
            total.setText("Rp. " + ribuan + ".000,00");
        }else{
            total.setText("Rp. " + ribuan + "." + sisa + ",00");
        }
        return v;
    }

}

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
import pplb05.balgebun.costumer.Entity.Pemesanan;

/**
 * @author febriyola anastasia
 * This class is used as adapter for showing the Menu
 */
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
    OnDataChangeListener mOnDataChangeListener;

    //generator
    public StrukAdapter(ArrayList<Menu> food, Pemesanan pesan, Context context) {
        this.food = food;
        this.context = context;
        this.pesan = pesan;
    }

    public interface OnDataChangeListener{
        public void onDataChanged();
    }

    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
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
     *Set view for each selected menu
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.struk_menu, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        jumlah = (TextView)v.findViewById(R.id.jumlah);
        harga = (TextView)v.findViewById(R.id.harga_view);
        total = (TextView)v.findViewById(R.id.total_id);

        //set text to the vars
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

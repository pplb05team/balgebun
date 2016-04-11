package pplb05.balgebun.costumer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.costumer.Entity.Pemesanan;

/**
 * @author by febriyola anastasia
 * This class is used as adapter for showing menu of a spesific counter
 */
public class MenuAdapter extends BaseAdapter{

    private ArrayList<Menu> food;
    private Context context;
    private Pemesanan pesan;
    private TextView nama;
    private TextView jumlah;
    private TextView harga;
    private Button tombolTambah;
    private Button tombolKurang;
    OnDataChangeListener mOnDataChangeListener;


    public interface OnDataChangeListener{
        public void onDataChanged();
    }


    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
    }

    //Generator
    public MenuAdapter(ArrayList<Menu> food, Pemesanan pesan,Context context) {
        this.food = food;
        this.context = context;
        this.pesan = pesan;
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
     * Set view for each menu
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.menu_layout, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        jumlah = (TextView)v.findViewById(R.id.counter_menu);
        harga = (TextView)v.findViewById(R.id.harga_view);
        tombolTambah = (Button)v.findViewById(R.id.button_plus);
        tombolKurang = (Button)v.findViewById(R.id.button_minus);

        //set var
        nama.setText(food.get(position).getNamaMenu());
        harga.setText(food.get(position).getHargaText()); //Get the text from your adapter for example
        jumlah.setText(""+food.get(position).getJumlah());

        //if button tombolTambah is pressed, jumlah++ for the spesific object Menu
        tombolTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //max jumlah == 9
                if (food.get(position).getJumlah() != 9) {
                    food.get(position).addJumlah();
                    //add to pesanan
                    pesan.addPesanan(food.get(position));
                    notifyDataSetChanged();
                    mOnDataChangeListener.onDataChanged();
                }
            }
        });

        //if button tombolKurang is pressed, jumlah-- for the spesific object Menu
        tombolKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove from pesanan
                pesan.removePesanan(food.get(position));
                food.get(position).minJumlah();
                notifyDataSetChanged();
                mOnDataChangeListener.onDataChanged();
            }
        });

        return v;
    }

}

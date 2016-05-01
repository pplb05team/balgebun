package pplb05.balgebun.counter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.counter.Entity.RiwayatPesananPenjual;
import pplb05.balgebun.counter.Fragment.RiwayatActivity;

/**
 * Created by dananarief on 27-04-16.
 * Kelas ini merupakan adapter untuk menampilkan riwayat pesanan
 */
public class RiwayatPesananPenjualAdapter extends BaseAdapter{
    private ArrayList<RiwayatPesananPenjual> listRiwayat;
    private Context context;
    private TextView namaMakanan;
    private TextView namaPembeli;
    private TextView jumlahPesanan;
    private TextView waktu;
    RiwayatActivity frag;

    public RiwayatPesananPenjualAdapter(ArrayList<RiwayatPesananPenjual> listRiwayat, Context context, RiwayatActivity frag) {
        this.listRiwayat = listRiwayat;
        this.context = context;
        this.frag=frag;
    }


    @Override
    public int getCount() {
        return listRiwayat.size();
    }

    @Override
    public Object getItem(int position) {
        return listRiwayat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listRiwayat.get(position).getPosition();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //beberapa baris kode ini untuk mengatur tampilan sesuai layout xml
        LayoutInflater i = LayoutInflater.from(context);
        final View v = i.inflate(R.layout.penjual_riwayat_layout,parent,false);
        namaMakanan = (TextView) v.findViewById(R.id.nama_menu);
        namaPembeli = (TextView) v.findViewById(R.id.nama_pembeli);
        jumlahPesanan = (TextView) v.findViewById(R.id.jumlah_pesanan);
        waktu = (TextView) v.findViewById(R.id.tanggalPesanan);

        namaMakanan.setText(listRiwayat.get(position).getNamaMakanan());
        namaPembeli.setText(listRiwayat.get(position).getNamaPembeli());
        jumlahPesanan.setText(Integer.toString(listRiwayat.get(position).getJumlahPesanan()));
        waktu.setText(listRiwayat.get(position).getTanggal());


        return v;
    }
}

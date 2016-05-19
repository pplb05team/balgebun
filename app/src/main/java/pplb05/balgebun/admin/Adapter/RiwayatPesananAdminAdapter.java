package pplb05.balgebun.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.counter.Entity.RiwayatPesananPenjual;

/**
 * Created by febriyolaanastasia on 5/19/16.
 */
public class RiwayatPesananAdminAdapter extends BaseAdapter {

    private ArrayList<RiwayatPesananPenjual> listRiwayat;
    private Context context;
    private TextView namaMakanan;
    private TextView namaPembeli;
    private TextView total;
    private TextView waktu;

    public RiwayatPesananAdminAdapter(ArrayList<RiwayatPesananPenjual> listRiwayat, Context context) {
        this.listRiwayat = listRiwayat;
        this.context = context;
    }

    public int getCount() {
        return listRiwayat.size();
    }

    public Object getItem(int position) {
        return listRiwayat.get(position);
    }

    public long getItemId(int position) {
        return listRiwayat.get(position).getPosition();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //beberapa baris kode ini untuk mengatur tampilan sesuai layout xml
        LayoutInflater i = LayoutInflater.from(context);
        View v = i.inflate(R.layout.item_riwayat_penjual_bayar,parent,false);

        namaMakanan = (TextView) v.findViewById(R.id.nama_menu);
        namaPembeli = (TextView) v.findViewById(R.id.nama_pembeli);
        total = (TextView) v.findViewById(R.id.harga);
        waktu = (TextView) v.findViewById(R.id.tanggalPesanan);


        namaMakanan.setText(listRiwayat.get(position).getNamaMakanan());
        namaPembeli.setText("Pembeli: " + listRiwayat.get(position).getNamaPembeli()
                + " jumlah: "+ Integer.toString(listRiwayat.get(position).getJumlahPesanan()));
        waktu.setText(listRiwayat.get(position).getTanggal());

        int totalInt = listRiwayat.get(position).getHarga();
        int ribuan = totalInt/1000;
        int sisa = totalInt-ribuan*1000;
        if(sisa == 0){
            total.setText("Rp. " + ribuan + ".000,00");
        }else{
            total.setText("Rp. " + ribuan + "." + sisa + ",00");
        }
        return v;
    }
}

package pplb05.balgebun.costumer.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by febriyolaanastasia on 4/2/16.
 */
public class Pemesanan implements Parcelable{

    private ArrayList<Menu> pesanan;
    private int total;

    public Pemesanan() {
        total = 0;
        pesanan = new ArrayList<Menu>();

    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Menu> getPesanan() {
        return pesanan;
    }

    public void addPesanan(Menu makanan) {
        if(pesanan.indexOf(makanan) < 0) {
            pesanan.add(makanan);
        }
        total+=makanan.getHarga();
    }

    public void removePesanan(Menu makanan){
        if(pesanan.indexOf(makanan)>-1){
            if(makanan.getJumlah()==1){
                pesanan.remove(makanan);
            }
            total -= makanan.getHarga();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(pesanan);
        dest.writeInt(total);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Pemesanan createFromParcel(Parcel in) {
            return new Pemesanan(in);
        }

        public Pemesanan[] newArray(int size) {
            return new Pemesanan[size];
        }
    };

    // "De-parcel object
    public Pemesanan(Parcel in) {
        pesanan = in.readArrayList(Menu.class.getClassLoader());
        total = in.readInt();
    }
}

package pplb05.balgebun.costumer.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author febriyola anastasia
 * Class Pemesanan implements parcelable because it'll be sent to the next activity
 * This object Pemesanan will be created everytime the buyer order
 */
public class Pemesanan implements Parcelable{

    private ArrayList<Menu> pesanan;
    private int total;

    public Pemesanan() {
        total = 0;
        pesanan = new ArrayList<Menu>();

    }

    /**
     * This method add Menu to the arraylist
     */
    public void addPesanan(Menu makanan) {
        //if menu already in the arraylist, means the buyer add the "jumlah"
        //jumlah will be increased in the object Menu itself
        if(pesanan.indexOf(makanan) < 0) {
            pesanan.add(makanan);
        }
        //update total
        total+=makanan.getHarga();
    }

    /**
     * This method will remove Menu from arraylist
     */
    public void removePesanan(Menu makanan){
        //if menu is in the arraylist
        if(pesanan.indexOf(makanan)>-1){
            //if jumlah of the Menu == 0, then removeMakanan means jumlah--, so jumlah will be 0
            //if so, then remove makanan from the arraylist
            if(makanan.getJumlah()==1){
                pesanan.remove(makanan);
            }
            //update total
            total -= makanan.getHarga();
        }

    }

    /**
     * getter & setter for the instance var
     */
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Menu> getPesanan() {
        return pesanan;
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

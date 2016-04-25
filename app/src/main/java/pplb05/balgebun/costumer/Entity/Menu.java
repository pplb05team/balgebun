package pplb05.balgebun.costumer.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author febriyola anastasia
 * Class Menu to create object Menu
 * Implements parcelable because Menu will be sent to another activity
 */
public class Menu implements Parcelable {
    private int id, position, idOrder;
    private String namaMenu, hargaText;
    private String status;
    private int jumlah, harga;
    private String i;
    private String namaCounter;


    //generator for object Menu created in class StrukActivity
    public Menu(int position, int id, String namaMenu, int harga, String status) {
        this.position = position;
        this.id = id;
        this.namaMenu = namaMenu;
        jumlah = 0;
        this.harga = harga;
        this.status = status;

        int totalTemp = harga;
        int ribuan = totalTemp/1000;
        totalTemp = totalTemp - ribuan*1000;
        if(totalTemp==0){
            hargaText = "Rp. "+ribuan+".000,00";
        }else{
            hargaText = "Rp. "+ribuan+"."+totalTemp+",00";
        }
    }

    //generator for object Menu created in class PesananSaya
    public Menu(int i, int idOrder, int id_menu, String nama_menu, int jumlah, String status, String nama_counter) {
        this.idOrder = idOrder;
        position = i;
        this.id = id_menu;
        this.jumlah = jumlah;
        this.namaMenu = nama_menu;
        this.status = status;
        this.namaCounter = nama_counter;

    }

    //generator for object Menu created in class StrukActivity
    public Menu(int position, int id, String namaMenu, int harga, String status, int jumlah) {
        this.position = position;
        this.id = id;
        this.namaMenu = namaMenu;
        this.jumlah = jumlah;
        this.harga = harga;
        this.status = status;

        int totalTemp = harga;
        int ribuan = totalTemp/1000;
        totalTemp = totalTemp - ribuan*1000;
        if(totalTemp==0){
            hargaText = "Rp. "+ribuan+".000,00";
        }else{
            hargaText = "Rp. "+ribuan+"."+totalTemp+",00";
        }
    }

    /*
     * Setter and getter for every instance variables.
     */

    public int getId() {
        return id;
    }

    public int getIdOrder() {

        return idOrder;
    }

    public String getHargaText(){
        return hargaText;
    }

    public int getPosition() {
        return position;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public int getHarga() {
        return harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void addJumlah(){
        jumlah++;
    }

    public void changePosition(int i) {
        position = i;
    }

    public String getSatus() {
        return status;
    }

    public String getNamaCounter() { return namaCounter;}

    public void minJumlah(){
        if(jumlah !=0){
            jumlah--;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idOrder);
        dest.writeInt(position);
        dest.writeString(namaMenu);
        dest.writeString(hargaText);
        dest.writeString(status);
        dest.writeInt(jumlah);
        dest.writeInt(harga);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    // "De-parcel object
    public Menu(Parcel in) {
        id = in.readInt();
        idOrder=in.readInt();
        position = in.readInt();
        namaMenu = in.readString();
        hargaText = in.readString();
        status = in.readString();
        jumlah = in.readInt();
        harga = in.readInt();
    }
}

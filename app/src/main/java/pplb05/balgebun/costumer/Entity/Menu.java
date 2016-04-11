package pplb05.balgebun.costumer.Entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by febriyolaanastasia on 3/23/16.
 */
public class Menu implements Parcelable {
    private int id, position;
    private String namaMenu, hargaText;
    private String status;
    private int jumlah, harga;
    private String i;
    private String namaCounter;


    //id nama harga status
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
            hargaText = "Rp. "+ribuan+".000,00"; //Get the text from your adapter for example
        }else{
            hargaText = "Rp. "+ribuan+"."+totalTemp+",00"; //Get the text from your adapter for example
        }
    }

    public Menu(int i, int id_menu, String nama_menu, int jumlah, String status, String nama_counter) {
        position = i;
        this.id = id_menu;
        this.jumlah = jumlah;
        this.namaMenu = nama_menu;
        this.status = status;
        this.namaCounter = nama_counter;

    }

    public String getNamaCounter(){
        return namaCounter;
    }

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
            hargaText = "Rp. "+ribuan+".000,00"; //Get the text from your adapter for example
        }else{
            hargaText = "Rp. "+ribuan+"."+totalTemp+",00"; //Get the text from your adapter for example
        }
    }


    /*
     * Setter and getter for every instance variables.
     */

    public int getId() {
        return id;
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
    /*private int id, position;
    private String namaMenu, hargaText;
    private boolean status;
    private int jumlah, harga;
    */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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
        position = in.readInt();
        namaMenu = in.readString();
        hargaText = in.readString();
        status = in.readString();
        jumlah = in.readInt();
        harga = in.readInt();
    }


    public void changePosition(int i) {
        position = i;
    }

    public String getSatus() {
        return status;
    }
}

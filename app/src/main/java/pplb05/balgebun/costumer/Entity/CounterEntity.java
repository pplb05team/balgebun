package pplb05.balgebun.costumer.Entity;

import android.graphics.Bitmap;

import pplb05.balgebun.app.AppConfig;

/**
 * Created by Wahid Nur Rohman on 3/24/2016.
 *
 * Method untuk merepresentasikan objek counter
 */
public class CounterEntity {
    private long id;
    private int pemasukan;
    private String counterName;
    private String username;
    private String imageName;
    private Bitmap bitmap;

    /**
     * Constructor
     *
     * @param id
     * @param counterName
     * @param username
     */
    public CounterEntity(long id, String counterName, String username) {
        this.id = id;
        this.counterName = counterName;
        this.username = username;
        this.imageName = username;
    }

    /**
     * Contructor
     *
     * @param id
     * @param counterName
     * @param username
     * @param pemasukan
     */
    public CounterEntity(long id, String counterName, String username, int pemasukan) {
        this.id = id;
        this.counterName = counterName;
        this.username = username;
        this.imageName = username;
        this.pemasukan = pemasukan;
    }

    /*
     * Setter dan Getter di kelas CoutnerEntity
     */


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getImageURL() {
        return AppConfig.URL_IMG + imageName + ".png";
    }
    public String getImageName() {
        return imageName;
    }

    public void setImage(String image) {
        this.imageName = image;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public String toString(){
        return username + " " + counterName;
    }

    public int getPemasukan() {
        return pemasukan;
    }

    public void bayar(){
        pemasukan = 0;
    }
}

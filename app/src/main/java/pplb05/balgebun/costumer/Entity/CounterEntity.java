package pplb05.balgebun.costumer.Entity;

import android.graphics.Bitmap;

import pplb05.balgebun.app.AppConfig;

/**
 * Created by Wahid Nur Rohman on 3/24/2016.
 */
public class CounterEntity {
    private long id;
    private String counterName;
    private String username;
    private String imageName;
    private Bitmap bitmap;

    public CounterEntity(long id, String counterName, String username) {
        this.id = id;
        this.counterName = counterName;
        this.username = username;
        this.imageName = username;
    }

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
        return AppConfig.URL_IMG + imageName + ".jpg";
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
}

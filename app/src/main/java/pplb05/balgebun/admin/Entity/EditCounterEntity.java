package pplb05.balgebun.admin.Entity;

/**
 * Created by Rahmi Julianasari on 25/04/2016.
 * Class EditCounterEntity to create object Counter
 */
public class EditCounterEntity {

    //initialization
    private String username;
    private String counterName;
    private int pemasukan;

    //generator for object Counter created in EditCounterActivity
    public EditCounterEntity(String username, String counterName, int pemasukan){
        this.username = username;
        this.counterName = counterName;
        this.pemasukan = pemasukan;
    }

    //setters and getters
    public String getUsername(){
        return username;
    }
    public String getCounterName(){
        return counterName;
    }
    public String getPemasukan(){
        String strPemasukan;
        int ribuan = pemasukan/1000;
        int sisa = pemasukan-ribuan*1000;
        if(sisa == 0){
            strPemasukan= "Rp. " + ribuan + ".000,00";
        }else{
            strPemasukan= "Rp. " + ribuan + "." + sisa + ",00";
        }
        return strPemasukan;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setCounterName(String counterName){
        this.counterName = counterName;
    }
    public void setPemasukan(int pemasukan){
        this.pemasukan = pemasukan;
    }

}

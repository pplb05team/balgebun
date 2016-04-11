package pplb05.balgebun.counter.Entity;

/**
 * Created by dananarief on 02-04-16.
 */
public class PesananPenjual {
    private String namaMakanan;
    private String namaPembeli;
    private int jumlahPesanan;
    private String status;
    private long position;
    private int id;

    public PesananPenjual(String namaMakanan, String namaPembeli, int jumlahPesanan, String status, long position, int id) {
        this.namaMakanan = namaMakanan;
        this.namaPembeli = namaPembeli;
        this.jumlahPesanan = jumlahPesanan;
        this.status = status;
        this.position=position;
        this.id=id;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(int jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

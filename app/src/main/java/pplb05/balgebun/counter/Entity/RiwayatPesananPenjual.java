package pplb05.balgebun.counter.Entity;

/**
 * Created by dananarief on 27-04-16.
 * Kelas ini merupakan entity untuk dalam menampilkan RiwayatPesananPenjual
 */
public class RiwayatPesananPenjual {
    private String namaPembeli;
    private String namaMakanan;
    private int jumlahPesanan;
    private int id;
    private String tanggal;
    private int position;

    public RiwayatPesananPenjual(String namaPembeli, String namaMakanan, int jumlahPesanan, int id, String tanggal, int position) {
        this.namaPembeli=namaPembeli;
        this.namaMakanan=namaMakanan;
        this.jumlahPesanan=jumlahPesanan;
        this.id=id;
        this.tanggal=tanggal;
        this.position=position;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(int jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}

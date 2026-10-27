public class AlatPraktikum {
    private final String kode;
    private final String namaAlat;
    private int stok;
    private boolean layakPakai;

    public AlatPraktikum(String kode, String namaAlat, int stok, boolean layakPakai) {
        this.kode = kode;
        this.namaAlat = namaAlat;
        this.stok = stok;
        this.layakPakai = layakPakai;
    }

    // Getter Section
    public String getKode() { return kode; }
    public String getNama() { return namaAlat; }
    public int getStok() { return stok; }
    public boolean isLayakPakai() { return layakPakai; }

    // Setter Section
    public void setLayakPakai(boolean layakPakai) { this.layakPakai = layakPakai; }

    public boolean ambilStok(int jumlah) {
        if (!layakPakai || jumlah <= 0 || stok < jumlah) return false;
        stok -= jumlah;
        return true;
    }

    public void kembalikanStok(int jumlah) {
        if (jumlah > 0) stok += jumlah;
    }

    @Override
    public String toString() {
        return String.format("%-7s | %-22s | Stok: %-2d | %s",
                kode, namaAlat, stok, (layakPakai ? "Layak Pakai" : "RUSAK"));
    }
}


public class RiwayatPeminjaman implements TransaksiLab {
    // Ketentuan 2: Konstanta final 2 digit terakhir NIM (36 -> Rp36.000)
    public static final int TARIF_DENDA_RIBUAN = SistemInventarisLab.BASE_DENDA;

    private final String idPinjam;
    private final PenggunaLab peminjam;
    private final AlatPraktikum alat;
    private final int jumlah;
    private boolean sudahKembali;
    private double totalDenda;

    public RiwayatPeminjaman(String idPinjam, PenggunaLab peminjam, AlatPraktikum alat, int jumlah) {
        this.idPinjam = idPinjam;
        this.peminjam = peminjam;
        this.alat = alat;
        this.jumlah = jumlah;
        this.sudahKembali = false;
    }

    public String getIdPinjam() { return idPinjam; }
    public PenggunaLab getPeminjam() { return peminjam; }
    public AlatPraktikum getAlat() { return alat; }
    public boolean isSudahKembali() { return sudahKembali; }

    @Override
    public boolean pinjam() {
        if (jumlah > peminjam.getMaksKuotaPinjam()) return false;
        return alat.ambilStok(jumlah);
    }

    @Override
    public double kembalikan(boolean kondisiKelayakanAkhir, int hariTerlambat) {
        if (sudahKembali) return 0;

        alat.kembalikanStok(jumlah);
        sudahKembali = true;

        int faktorPenalti = Math.max(0, hariTerlambat) + (kondisiKelayakanAkhir ? 0 : 2);
        totalDenda = faktorPenalti * (TARIF_DENDA_RIBUAN * 1000.0);
        return totalDenda;
    }

    @Override
    public String getRingkasan() {
        String status = sudahKembali ? "Kembali" : "Dipinjam";
        return String.format("%-7s | %-18s | %-20s | Jml: %-2d | %-8s | Denda: Rp%,.0f",
                idPinjam, peminjam.getNama(), alat.getNama(), jumlah, status, totalDenda);
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemInventarisLab {
    // Ketentuan 2: Konstanta final 2 digit terakhir NIM (2503036 -> 36)
    public static final int BASE_DENDA  = 36;
    public static final int MAKS_PINJAM = 36;

    private final PetugasLab petugasAktif;               // Association (1..1)
    private final List<AlatPraktikum> daftarAlat;        // Aggregation (1..1..*)
    private final List<RiwayatPeminjaman> daftarRiwayat; // Composition (1..0..*)

    public SistemInventarisLab(PetugasLab petugasAktif) {
        this.petugasAktif = petugasAktif;
        this.daftarAlat = new ArrayList<>();
        this.daftarRiwayat = new ArrayList<>();

        daftarAlat.add(new AlatPraktikum("ALT-01", "Mikroskop Binokuler", 10, true));
        daftarAlat.add(new AlatPraktikum("ALT-02", "Osiloskop Digital", 6, true));
        daftarAlat.add(new AlatPraktikum("ALT-03", "Multimeter Presisi", 15, true));
        daftarAlat.add(new AlatPraktikum("ALT-04", "Power Supply DC", 4, false));
    }

    public PetugasLab getPetugasAktif() { return petugasAktif; }
    public List<AlatPraktikum> getDaftarAlat() { return daftarAlat; }
    public List<RiwayatPeminjaman> getDaftarRiwayat() { return daftarRiwayat; }

    public boolean tambahAlatBaru(String kode, String nama, int stok) {
        if (stok < 0 || stok > MAKS_PINJAM || cariAlat(kode) != null) return false;
        daftarAlat.add(new AlatPraktikum(kode.toUpperCase(), nama, stok, true));
        return true;
    }

    public AlatPraktikum cariAlat(String kode) {
        return daftarAlat.stream()
                .filter(a -> a.getKode().equalsIgnoreCase(kode))
                .findFirst().orElse(null);
    }

    public String catatPeminjaman(PenggunaLab peminjam, AlatPraktikum alat, int jumlah) {
        String idTrx = String.format("TRX-%03d", daftarRiwayat.size() + 1);
        RiwayatPeminjaman riwayat = new RiwayatPeminjaman(idTrx, peminjam, alat, jumlah);
        if (riwayat.pinjam()) {
            daftarRiwayat.add(riwayat);
            return idTrx;
        }
        return null;
    }

    public RiwayatPeminjaman cariTransaksiAktif(String idPinjam) {
        return daftarRiwayat.stream()
                .filter(r -> r.getIdPinjam().equalsIgnoreCase(idPinjam) && !r.isSudahKembali())
                .findFirst().orElse(null);
    }

    public List<RiwayatPeminjaman> filterRiwayat(String kataKunci) {
        String kw = kataKunci.toLowerCase();
        return daftarRiwayat.stream()
                .filter(r -> r.getRingkasan().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }
}

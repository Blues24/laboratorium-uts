import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Ketentuan 1: Nama Anda sebagai default petugas/laboran saat pertama dieksekusi di main
        PetugasLab petugasDefault = new PetugasLab("LAB-01", "Mohamad Luckman Afandi", "2503036");
        SistemInventarisLab sistem = new SistemInventarisLab(petugasDefault);

        jalankanMenuCLI(sistem);
    }

    private static void jalankanMenuCLI(SistemInventarisLab sistem) {
        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n=========================================================");
            System.out.println("   SISTEM INVENTARIS LABORATORIUM TERPADU (CLI)");
            System.out.println("   Petugas Aktif : " + sistem.getPetugasAktif().getNama());
            System.out.println("   Konstanta NIM : " + SistemInventarisLab.MAKS_PINJAM);
            System.out.println("=========================================================");
            System.out.println("[1] Lihat Daftar & Kondisi Alat");
            System.out.println("[2] Tambah / Inspeksi Kelayakan Alat (Petugas Lab)");
            System.out.println("[3] Peminjaman Alat (Mahasiswa)");
            System.out.println("[4] Pengembalian Alat & Cek Denda");
            System.out.println("[5] Cari Riwayat Peminjaman");
            System.out.println("[0] Keluar");

            switch (bacaTeks("Pilih menu (0-5): ")) {
                case "1" -> tampilkanAlat(sistem);
                case "2" -> menuKelolaAlat(sistem);
                case "3" -> menuPinjam(sistem);
                case "4" -> menuKembali(sistem);
                case "5" -> menuCariRiwayat(sistem);
                case "0" -> {
                    berjalan = false;
                    System.out.println("Aplikasi ditutup. Petugas: " + sistem.getPetugasAktif().getNama());
                }
                default -> System.out.println("[!] Pilihan menu tidak valid.");
            }
        }
    }

    private static void tampilkanAlat(SistemInventarisLab sistem) {
        System.out.println("\n--- DAFTAR ALAT PRAKTIKUM ---");
        sistem.getDaftarAlat().forEach(System.out::println);
    }

    private static void menuKelolaAlat(SistemInventarisLab sistem) {
        System.out.println("[1] Tambah Alat Baru | [2] Inspeksi Kondisi Alat");
        String aksi = bacaTeks("Pilih aksi (1/2): ");

        if (aksi.equals("1")) {
            String kode = bacaTeks("Kode Alat (misal ALT-05): ");
            String nama = bacaTeks("Nama Alat               : ");
            int stok = bacaAngka("Stok Awal               : ");
            if (sistem.tambahAlatBaru(kode, nama, stok)) {
                System.out.println("[✓] Alat baru berhasil ditambahkan.");
            } else {
                System.out.println("[!] Gagal: Kode sudah ada atau stok di luar batas (0-" + SistemInventarisLab.MAKS_PINJAM + ").");
            }
        } else if (aksi.equals("2")) {
            tampilkanAlat(sistem);
            AlatPraktikum alat = sistem.cariAlat(bacaTeks("Kode Alat yang diperiksa: "));
            if (alat == null) {
                System.out.println("[!] Alat tidak ditemukan.");
                return;
            }
            boolean layak = bacaTeks("Apakah alat layak pakai? (y/n): ").equalsIgnoreCase("y");
            sistem.getPetugasAktif().periksaKelayakan(alat, layak);
            System.out.println("[✓] Kondisi alat diperbarui oleh " + sistem.getPetugasAktif().getNama());
        }
    }

    private static void menuPinjam(SistemInventarisLab sistem) {
        tampilkanAlat(sistem);
        AlatPraktikum alat = sistem.cariAlat(bacaTeks("Kode Alat yang dipinjam: "));
        if (alat == null || !alat.isLayakPakai()) {
            System.out.println("[!] Alat tidak ditemukan atau sedang RUSAK.");
            return;
        }

        String nama = bacaTeks("Nama Mahasiswa : ");
        String nim = bacaTeks("NIM            : ");
        int jumlah = bacaAngka("Jumlah Unit    : ");

        PenggunaLab mhs = new Mahasiswa("MHS-" + nim, nama, nim);
        String idTrx = sistem.catatPeminjaman(mhs, alat, jumlah);

        if (idTrx != null) {
            System.out.println("[✓] Peminjaman berhasil! ID Transaksi: " + idTrx);
        } else {
            System.out.println("[!] Gagal: Stok kurang atau melebihi kuota mahasiswa (" + mhs.getMaksKuotaPinjam() + " unit).");
        }
    }

    private static void menuKembali(SistemInventarisLab sistem) {
        if (sistem.getDaftarRiwayat().isEmpty()) {
            System.out.println("[!] Belum ada riwayat peminjaman.");
            return;
        }
        sistem.getDaftarRiwayat().forEach(r -> System.out.println(r.getRingkasan()));

        RiwayatPeminjaman trx = sistem.cariTransaksiAktif(bacaTeks("Masukkan ID Transaksi (misal TRX-001): "));
        if (trx == null) {
            System.out.println("[!] Transaksi aktif tidak ditemukan.");
            return;
        }

        boolean layak = bacaTeks("Kondisi alat masih layak? (y/n): ").equalsIgnoreCase("y");
        int terlambat = bacaAngka("Hari keterlambatan (0 jika tepat waktu): ");

        sistem.getPetugasAktif().periksaKelayakan(trx.getAlat(), layak);
        double denda = trx.kembalikan(layak, terlambat);
        System.out.printf("[✓] Pengembalian selesai. Total Denda: Rp%,.0f%n", denda);
    }

    private static void menuCariRiwayat(SistemInventarisLab sistem) {
        String keyword = bacaTeks("Masukkan kata kunci pencarian (Enter untuk semua): ");
        List<RiwayatPeminjaman> hasil = sistem.filterRiwayat(keyword);
        if (hasil.isEmpty()) {
            System.out.println("[!] Data riwayat tidak ditemukan.");
        } else {
            hasil.forEach(r -> System.out.println(r.getRingkasan()));
        }
    }

    private static String bacaTeks(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int bacaAngka(String prompt) {
        try {
            return Integer.parseInt(bacaTeks(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
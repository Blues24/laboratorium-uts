public interface TransaksiLab {
  boolean pinjam();
  double kembalikan(boolean kondisiKelayakanAkhir, int hariTerlambat);
  String getRingkasan();
}
